package svenhjol.charmony.stone_chests.common.features.secret_chests;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces;
import svenhjol.charmony.api.SecretChestDefinition;
import svenhjol.charmony.api.SecretChestPlacement;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.TagHelper;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlock;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;
import svenhjol.charmony.stone_chests.common.features.stone_chests.StoneChests;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Handlers extends Setup<SecretChests> {
    public @Nullable String template; // Used by End Cities
    public @Nullable ResourceLocation jigsawTemplate; // Used by Bastions

    public static final List<String> END_CITY_TEMPLATES = List.of(
        "third_roof", "bridge_piece", "base_floor"
    );

    public Handlers(SecretChests feature) {
        super(feature);
    }

    public void createNetherFortressChest(StructurePiece piece, WorldGenLevel level, RandomSource random) {
        if (random.nextInt(1000) < feature().fortressChance()) {
            var definition = definitionForPlacement(SecretChestPlacement.Fortress, random).orElse(null);
            if (definition == null) return;

            BlockPos pos;
            if (piece instanceof NetherFortressPieces.CastleSmallCorridorPiece) {
                pos = piece.getWorldPos(random.nextBoolean() ? 1 : 3, 2, 2);
            } else if (piece instanceof NetherFortressPieces.BridgeStraight) {
                pos = piece.getWorldPos(random.nextBoolean() ? 1 : 3, 5, 2);
            } else {
                return;
            }

            var stateBelow = level.getBlockState(pos.below());
            if (stateBelow.is(Tags.GENERATES_FORTRESS_CHESTS)) {
                createChest(definition, level, random, pos, false);
            }
        }
    }

    public boolean createEndCityChest(ServerLevelAccessor level, BlockPos pos, BlockState stateBelow) {
        if (template != null && END_CITY_TEMPLATES.contains(template) && stateBelow.is(Tags.GENERATES_END_CITY_CHESTS)) {
            if (level.getRandom().nextInt(1000) < feature().endCityChance()) {
                var definition = definitionForPlacement(SecretChestPlacement.EndCity, level.getRandom()).orElse(null);
                if (definition == null) return false;
                return createChest(definition, level, level.getRandom(), pos, false);
            }
        }
        return false;
    }

    public boolean createBastionChest(ServerLevelAccessor level, BlockPos pos, BlockState stateBelow) {
        if (jigsawTemplate != null && jigsawTemplate.getPath().contains("bastion") && stateBelow.is(Tags.GENERATES_BASTION_CHESTS)) {
            if (level.getRandom().nextInt(1000) < feature().bastionChance()) {
                var definition = definitionForPlacement(SecretChestPlacement.Bastion, level.getRandom()).orElse(null);
                if (definition == null) return false;
                return createChest(definition, level, level.getRandom(), pos, false);
            }
        }
        return false;
    }

    public boolean createChest(
        SecretChestDefinition definition,
        ServerLevelAccessor level,
        RandomSource random,
        BlockPos pos,
        boolean waterlogged
    ) {
        var material = definition.material();
        var block = StoneChests.feature().registers.chestBlocks.get(material).get();

        var lootTables = new ArrayList<>(definition.lootTables());
        var menus = new ArrayList<>(definition.lockMenus());
        var sideEffects = new ArrayList<>(definition.sideEffects());

        if (lootTables.isEmpty()) {
            log().debug("No loot tables for secret chest");
            return false;
        }

        Util.shuffle(lootTables, random);
        var lootTable = lootTables.getFirst();

        var state = StructurePiece.reorient(level, pos, block.defaultBlockState());
        if (waterlogged) {
            state = state.setValue(ChestBlock.WATERLOGGED, true);
        }

        level.setBlock(pos, state, 2);
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {

            // If the puzzles feature is enabled then add a puzzle to the chest entity.
            // Add the loot table to the "unlocked loot table" property so that
            // if the chest is broken it won't drop anything.
            if (Mod.getSidedFeature(ChestPuzzles.class).enabled()) {
                String menu = "";
                if (!menus.isEmpty()) {
                    // Add a random lock menu to the chest.
                    Util.shuffle(menus, random);
                    menu = menus.getFirst();
                }

                var providers = ChestPuzzles.feature().registers.lockMenuProviders;

                if (!menu.isEmpty() && providers.containsKey(menu)) {
                    chest.lock(menu);
                    chest.setUnlockedLootTable(lootTable);
                    chest.setDifficultyAmplifier(definition.difficultyAmplifier());

                    if (!sideEffects.isEmpty()) {
                        // Add a random side-effect to the chest.
                        Util.shuffle(sideEffects, random);
                        chest.setSideEffect(sideEffects.getFirst());
                    }
                } else {
                    log().warn("No provider matching menu: " + menu);
                }
            }

            // If the puzzles feature isn't enabled or has failed then the chest
            // will not be locked. Set the custom loot table directly.
            if (!chest.isLocked()) {
                chest.setLootTable(lootTable, random.nextLong());
                chest.setChanged();
            }
        }

        log().debug("Generated " + material.getSerializedName() + " chest at " + pos);
        return true;
    }

    public void createFlowerRing(WorldGenLevel level, BlockPos pos, RandomSource random, TagKey<Block> flowers) {
        if (!feature().flowerRings()) return;

        var values = new ArrayList<>(TagHelper.getValues(level.registryAccess().lookupOrThrow(Registries.BLOCK), flowers));
        if (values.isEmpty()) return;
        Util.shuffle(values, random);
        var flower = values.getFirst();

        var heightMap = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
        var radius = random.nextBoolean() ? 7 : 8;
        var heightTolerance = 5;

        for (int i = 0; i < 360; i += (28 + random.nextInt(5))) {
            var x = (int)(radius * Math.cos(i * Math.PI / 180));
            var z = (int)(radius * Math.sin(i * Math.PI / 180));

            for (int y = -heightTolerance; y < heightTolerance; y++) {
                var tryPos = heightMap.offset(x, y, z);
                var tryState = level.getBlockState(tryPos);
                var tryStateBelow = level.getBlockState(tryPos.below());

                if (tryStateBelow.is(Tags.GENERATES_FLOWER_RINGS) && (tryState.isAir() || tryState.canBeReplaced())) {
                    level.setBlock(tryPos, flower.defaultBlockState(), 3);
                }
            }
        }
    }

    public Optional<SecretChestDefinition> definitionForPlacement(SecretChestPlacement placement, RandomSource random) {
        var definitions = new ArrayList<>(feature().registers.secretChestDefinitions.values().stream()
            .filter(def -> def.placement() == placement).toList());

        if (definitions.isEmpty()) return Optional.empty();
        Util.shuffle(definitions, random);
        return Optional.of(definitions.getFirst());
    }
}
