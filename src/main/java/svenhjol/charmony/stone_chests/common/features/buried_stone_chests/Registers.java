package svenhjol.charmony.stone_chests.common.features.buried_stone_chests;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import svenhjol.charmony.api.BuriedStoneChestDefinition;
import svenhjol.charmony.api.BuriedStoneChestDefinitionProvider;
import svenhjol.charmony.core.Api;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<BuriedStoneChests> {
    public static final String STRUCTURE_ID = "buried_stone_chest";
    public static final String PIECE_ID = "buried_stone_chest_piece";
    public static final String DEFINITION_ID = "buried_stone_chest_definition";

    public final Supplier<StructureType<BuriedStoneChestStructure>> structureType;
    public final Supplier<StructurePieceType> structurePiece;
    public Codec<BuriedStoneChestDefinition> buriedChestCodec;
    public final Map<String, BuriedStoneChestDefinition> buriedChestDefinitions = new HashMap<>();

    public Registers(BuriedStoneChests feature) {
        super(feature);

        var registry = CommonRegistry.forFeature(feature);

        structureType = registry.structure(STRUCTURE_ID, () -> BuriedStoneChestStructure.CODEC);
        structurePiece = registry.structurePiece(PIECE_ID, () -> BuriedStoneChestPiece::new);

        // Consumer of buried chest definitions.
        Api.consume(BuriedStoneChestDefinitionProvider.class, provider -> {
            for (var definition : provider.getBuriedStoneChestDefinitions()) {
                this.buriedChestDefinitions.put(definition.name(), definition);
            }

            buriedChestCodec = StringRepresentable.fromValues(
                () -> buriedChestDefinitions.values().toArray(new BuriedStoneChestDefinition[0]));
        });
    }
}
