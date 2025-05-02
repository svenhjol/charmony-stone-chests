package svenhjol.charmony.stone_chests.common.features.secret_chests;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import svenhjol.charmony.api.SecretChestDefinition;
import svenhjol.charmony.api.SecretChestDefinitionProvider;
import svenhjol.charmony.core.Api;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

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
}
