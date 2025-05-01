package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.TagHelper;
import svenhjol.charmony.core.helpers.WorldHelper;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

import java.util.ArrayList;
import java.util.Optional;

public class Handlers extends Setup<StoneChestPuzzles> {
    public static final int MOB_SPAWN_RANGE = 2;

    public Handlers(StoneChestPuzzles feature) {
        super(feature);
    }

    public Optional<AbstractContainerMenu> getMenuProvider(ServerLevel level, ChestBlockEntity chest, int syncId, Inventory inventory, ContainerData data) {
        var lockMenu = chest.lockMenu();
        var pos = chest.getBlockPos();

        var providers = feature().registers.lockMenuProviders;
        if (!providers.containsKey(lockMenu)) {
            // No lock menu provider, just unlock the chest.
            feature().log().debug("Provider not found, unlocking: " + lockMenu);
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
            menuData.data = data;
            menuData.seed = seed;
            menuData.difficultyAmplifier = chest.getDifficultyAmplifier();

            var menu = provider.getMenuProvider(menuData);
            if (menu.isPresent()) {
                return menu;
            } else {
                chest.unlock();
            }
        }

        return Optional.empty();
    }

    public void doBreakBehavior(Player player, Level level, BlockPos pos, ChestBlockEntity chest) {
        var amplifier = chest.getDifficultyAmplifier();
        switch (chest.getBreakBehavior()) {
            case SPAWN_OVERWORLD_MONSTER -> spawnOverworldMonster(player, level, pos, amplifier);
            case EXPLODE -> explosion(level, pos, amplifier);
            case GIVE_BAD_EFFECT -> badEffect(player, level, pos, amplifier);
        }
    }

    private void spawnOverworldMonster(Player player, Level level, BlockPos pos, double amplifier) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        var random = serverLevel.getRandom();
        var count = Math.round(2 * amplifier);

        var opt = randomMob(Tags.OVERWORLD_MOBS, random, serverLevel);
        if (opt.isEmpty()) {
            log().debug("overworld_mobs tag is empty, not spawning mobs");
            return;
        };
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

                if (!player.getAbilities().instabuild && !player.isSpectator()) {
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

    private void explosion(Level level, BlockPos pos, double amplifier) {
        var x = pos.getX() + 0.5d;
        var y = pos.getY() + 0.5d;
        var z = pos.getZ() + 0.5d;
        level.explode(null, x, y, z, Math.round(2 * amplifier), Level.ExplosionInteraction.BLOCK);
    }

    private void badEffect(Player player, Level level, BlockPos pos, double amplifier) {
        feature().log().debug("TODO: badEffect");
    }

    private Optional<EntityType<?>> randomMob(TagKey<EntityType<?>> tag, RandomSource random, ServerLevel level) {
        var mobs = new ArrayList<>(TagHelper.getValues(level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE), tag));
        if (mobs.isEmpty()) {
            return Optional.empty();
        }

        Util.shuffle(mobs, random);
        return Optional.of(mobs.getFirst());
    }
}
