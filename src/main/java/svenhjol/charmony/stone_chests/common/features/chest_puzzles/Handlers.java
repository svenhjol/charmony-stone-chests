package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestSideEffects;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.TagHelper;
import svenhjol.charmony.core.helpers.WorldHelper;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Handlers extends Setup<ChestPuzzles> {
    public static final int MOB_SPAWN_RANGE = 2;
    public static final List<Holder<MobEffect>> BAD_EFFECTS;

    public Handlers(ChestPuzzles feature) {
        super(feature);
    }

    public Optional<AbstractContainerMenu> getMenuProvider(ServerLevel level, ChestBlockEntity chest, int syncId, Inventory inventory, StoneChestMaterial material) {
        var lockMenu = chest.lockMenu();
        var pos = chest.getBlockPos();

        var providers = feature().registers.lockMenuProviders;
        if (!providers.containsKey(lockMenu)) {
            // No lock menu provider, just unlock the chest.
            feature().log().warn("Provider " + lockMenu + " not found, unlocking chest");
            chest.unlock();
        }

        if (chest.isLocked()) {
            // Generate a seed based on this position.
            var seed = WorldHelper.seedFromBlockPos(pos);
            var provider = providers.get(lockMenu);
            var menuData = new StoneChestLockMenuData();

            menuData.syncId = syncId;
            menuData.playerInventory = inventory;
            menuData.level = level;
            menuData.pos = pos;
            menuData.material = material;
            menuData.seed = seed;
            menuData.random = RandomSource.create(seed);
            menuData.difficultyAmplifier = chest.getDifficultyAmplifier();

            var menu = provider.getMenuProvider(menuData);
            if (menu.isPresent()) {
                return menu;
            } else {
                feature().log().warn("Menu " + lockMenu + " not found, unlocking");
                chest.unlock();
            }
        }

        return Optional.empty();
    }

    public void doSuccessOpen(Container container, Player player, ChestBlockEntity chest) {
        // Consume any items held in the container.
        container.clearContent();

        // If it was a difficult challenge then give the player luck to modify the loot rolls.
        var difficultyAmplifier = chest.getDifficultyAmplifier();
        if (difficultyAmplifier > 1) {
            setPlayerLuck(player, difficultyAmplifier - 2);
        }

        // Get the stored "unlocked" loot table from the chest and set it as the primary loot table.
        // When the chest is next opened the loot will be generated.
        chest.setLootTable(chest.getUnlockedLootTable());
        chest.unlock();
        player.openMenu(chest);
    }

    public void doFailOpen(Player player, ChestBlockEntity chest) {
        // Let the container decide what to do with the container items.
        player.containerMenu.removed(player);

        // Give the player bad luck to make the treasure really crap.
        setPlayerBadLuck(player);

        // Unlock the chest and execute side-effects.
        chest.setLootTable(chest.getUnlockedLootTable());
        chest.unlock();
        var result = doSideEffects(player, player.level(), chest.getBlockPos(), chest);

        if (!result) {
            player.openMenu(chest);
        }
    }

    public boolean doSideEffects(Player player, Level level, BlockPos pos, ChestBlockEntity chest) {
        var amplifier = chest.getDifficultyAmplifier();
        var sideEffect = chest.getSideEffects();

        if (sideEffect == StoneChestSideEffects.Nothing) {
            return false;
        }

        switch (chest.getSideEffects()) {
            case SpawnOverworldMonsters -> spawnMonsters(Tags.OVERWORLD_MONSTERS, player, level, pos, amplifier);
            case SpawnNetherMonsters -> spawnMonsters(Tags.NETHER_MONSTERS, player, level, pos, amplifier);
            case SpawnEndMonsters -> spawnMonsters(Tags.END_MONSTERS, player, level, pos, amplifier);
            case Explode -> explode(level, pos, amplifier);
            case GiveBadEffect -> giveBadEffect(player, level, amplifier);
        }

        return true;
    }

    public void setPlayerLuck(Player player, int amplifier) {
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 30, amplifier));
    }

    public void setPlayerBadLuck(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 30, 2));
    }

    public void spawnMonsters(TagKey<EntityType<?>> tag, Player player, Level level, BlockPos pos, double amplifier) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        var random = serverLevel.getRandom();
        var count = Math.round(feature().numberOfMobsSpawned() * amplifier);

        var opt = randomMob(tag, random, serverLevel);
        if (opt.isEmpty()) {
            log().debug("mobs tag is empty, not spawning anything");
            return;
        }
        var mob = opt.get();

        for (var c = 0; c < count; c++) {
            Vec3 spawnVec = null;

            for (var tries = 0; tries < 10; tries++) {
                var tryVec = new Vec3(
                    pos.getX() + (random.nextDouble() - random.nextDouble()) * MOB_SPAWN_RANGE + 0.5d,
                    pos.getY() + random.nextInt(3) - 1,
                    pos.getZ() + (random.nextDouble() - random.nextDouble()) * MOB_SPAWN_RANGE + 0.5d
                );
                if (serverLevel.noCollision(mob.getSpawnAABB(tryVec.x(), tryVec.y(), tryVec.z()))) {
                    spawnVec = tryVec;
                    break;
                }
            }

            if (spawnVec == null) {
                log().debug("Could not spawn mob near pos " + pos);
                continue;
            }
            var spawnPos = BlockPos.containing(spawnVec);


            var entity = mob.create(serverLevel, EntitySpawnReason.STRUCTURE);
            if (entity == null) {
                log().debug("Could not create entity");
                continue;
            };

            entity.snapTo(spawnVec.x(), spawnVec.y(), spawnVec.z(), entity.getYRot(), entity.getXRot());

            if (entity instanceof Mob mobEntity) {
                mobEntity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.STRUCTURE, null);
                mobEntity.spawnAnim();

                if (isSurvivalMode(player)) {
                    mobEntity.setAggressive(true);
                    mobEntity.setTarget(player);
                }
            }

            if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
                log().debug("Could not add entity to level");
                continue;
            }

            serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, spawnPos);
            log().debug("Spawned entity " + entity + " at " + spawnPos);
        }
    }

    public void explode(Level level, BlockPos pos, double amplifier) {
        level.destroyBlock(pos, false);
        var x = pos.getX() + 0.5d;
        var y = pos.getY() + 0.5d;
        var z = pos.getZ() + 0.5d;
        level.explode(null, x, y, z, Math.round(2 * amplifier), Level.ExplosionInteraction.BLOCK);
    }

    public void giveBadEffect(Player player, Level level, double amplifier) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        var random = serverLevel.getRandom();
        var effect = BAD_EFFECTS.get(random.nextInt(BAD_EFFECTS.size()));

        var instance = new MobEffectInstance(effect, (int)Math.round(feature().badEffectDuration() * amplifier), Math.max(0, -1 + (int)Math.floor(amplifier)));
        if (isSurvivalMode(player)) {
            player.addEffect(instance);
        }
    }

    public Optional<EntityType<?>> randomMob(TagKey<EntityType<?>> tag, RandomSource random, ServerLevel level) {
        var mobs = new ArrayList<>(TagHelper.getValues(level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE), tag));
        if (mobs.isEmpty()) {
            return Optional.empty();
        }

        Util.shuffle(mobs, random);
        return Optional.of(mobs.getFirst());
    }

    public boolean isSurvivalMode(Player player) {
        return !player.getAbilities().instabuild && !player.isSpectator();
    }

    /**
     * @see PiglinAi getBarterResponseItems
     */
    public List<ItemStack> getItemsFromLootTable(ServerLevel level, ResourceKey<LootTable> loot, long seed) {
        var lootTable = level.getServer().reloadableRegistries().getLootTable(loot);
        return lootTable.getRandomItems(new LootParams.Builder(level).create(LootContextParamSets.EMPTY), seed);
    }

    static {
        BAD_EFFECTS = List.of(
            MobEffects.BLINDNESS,
            MobEffects.HUNGER,
            MobEffects.INFESTED,
            MobEffects.MINING_FATIGUE,
            MobEffects.OOZING,
            MobEffects.POISON,
            MobEffects.SLOWNESS
        );
    }
}
