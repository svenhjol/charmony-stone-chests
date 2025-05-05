package svenhjol.charmony.stone_chests.common.features.secret_chests;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import svenhjol.charmony.api.SecretChestDefinition;
import svenhjol.charmony.api.SecretChestPlacement;
import svenhjol.charmony.core.base.Setup;
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
        if (random.nextInt(1000) < 12) {
            var definition = definitionForPlacement(SecretChestPlacement.Fortress, random).orElse(null);
            if (definition == null) return;
            var pos = piece.getWorldPos(random.nextBoolean() ? 1 : 3, 2, 2);
            createChest(definition, level, random, pos, false);
        }
    }

    public boolean createEndCityChest(ServerLevelAccessor level, BlockPos pos) {
        if (template != null && END_CITY_TEMPLATES.contains(template)) {
            if (level.getRandom().nextInt(1000) < 3) {
                var definition = definitionForPlacement(SecretChestPlacement.EndCity, level.getRandom()).orElse(null);
                if (definition == null) return false;
                return createChest(definition, level, level.getRandom(), pos, false);
            }
        }
        return false;
    }

    public boolean createBastionChest(ServerLevelAccessor level, BlockPos pos) {
        if (jigsawTemplate != null && jigsawTemplate.getPath().contains("bastion")) {
            if (level.getRandom().nextInt(1000) < 3) {
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
            String menu = "";

            if (!menus.isEmpty()) {
                // Add a random lock menu to the chest.
                Util.shuffle(menus, random);
                menu = menus.getFirst();
            }

            if (!menu.isEmpty() && ChestPuzzles.feature().registers.lockMenuProviders.containsKey(menu)) {
                chest.lock(menu);
                chest.setUnlockedLootTable(lootTable);
                chest.setDifficultyAmplifier(definition.difficultyAmplifier());

                if (!sideEffects.isEmpty()) {
                    // Add a random side-effect to the chest.
                    Util.shuffle(sideEffects, random);
                    chest.setSideEffect(sideEffects.getFirst());
                }
            }

            if (!chest.isLocked()) {
                chest.setLootTable(lootTable, random.nextLong());
                chest.setChanged();
            }
        }

        log().debug("Generated " + material.getSerializedName() + " chest at " + pos);
        return true;
    }

    public Optional<SecretChestDefinition> definitionForPlacement(SecretChestPlacement placement, RandomSource random) {
        var definitions = new ArrayList<>(feature().registers.secretChestDefinitions.values().stream()
            .filter(def -> def.placement() == placement).toList());

        if (definitions.isEmpty()) return Optional.empty();
        Util.shuffle(definitions, random);
        return Optional.of(definitions.getFirst());
    }
}
