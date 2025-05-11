package svenhjol.charmony.stone_chests.common.features.secret_chests;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import svenhjol.charmony.api.SecretChestApi;
import svenhjol.charmony.api.SecretChestDefinition;
import svenhjol.charmony.api.SecretChestDefinitionProvider;
import svenhjol.charmony.core.Api;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlock;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;
import svenhjol.charmony.stone_chests.common.features.stone_chests.StoneChests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<SecretChests> {
    public static final String STRUCTURE_ID = "secret_chest";
    public static final String PIECE_ID = "secret_chest_piece";
    public static final String DEFINITION_ID = "secret_chest_definition";

    public final Supplier<StructureType<SecretChestStructure>> structureType;
    public final Supplier<StructurePieceType> structurePiece;
    public Codec<SecretChestDefinition> secretChestCodec;
    public final Map<String, SecretChestDefinition> secretChestDefinitions = new HashMap<>();

    public Registers(SecretChests feature) {
        super(feature);

        var registry = CommonRegistry.forFeature(feature);

        structureType = registry.structure(STRUCTURE_ID, () -> SecretChestStructure.CODEC);
        structurePiece = registry.structurePiece(PIECE_ID, () -> SecretChestPiece::new);

        // Consumer of secret chest definitions.
        Api.consume(SecretChestDefinitionProvider.class, provider -> {
            for (var definition : provider.getSecretChestDefinitions()) {
                this.secretChestDefinitions.put(definition.name(), definition);
            }

            secretChestCodec = StringRepresentable.fromValues(
                () -> secretChestDefinitions.values().toArray(new SecretChestDefinition[0]));
        });
    }

    @Override
    public Runnable boot() {
        return () -> SecretChestApi.instance().setChestCreator((definition, level, random, pos, waterlogged) -> {
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
        });
    }
}
