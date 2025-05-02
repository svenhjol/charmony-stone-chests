package svenhjol.charmony.stone_chests.common.features.secret_chests;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import svenhjol.charmony.api.SecretChestDefinition;

import java.util.Optional;

public class SecretChestStructure extends Structure {
    public static final MapCodec<SecretChestStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SecretChestStructure.settingsCodec(instance),
        feature().registers.secretChestCodec.fieldOf(Registers.DEFINITION_ID)
            .forGetter(structure -> structure.definition))
        .apply(instance, SecretChestStructure::new));

    private final SecretChestDefinition definition;

    protected SecretChestStructure(StructureSettings settings, SecretChestDefinition definition) {
        super(settings);

        if (definition == null) {
            throw new RuntimeException("Missing secret stone chest definition");
        }

        this.definition = definition;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        var opt = findStart(context);
        if (opt.isEmpty()) {
            feature().log().warn("Could not find start position for secret chest");
            return Optional.empty();
        }

        var startPos = opt.get();
        return Optional.of(new GenerationStub(startPos,
            builder -> builder.addPiece(new SecretChestPiece(definition, startPos))));
    }

    private Optional<BlockPos> findStart(GenerationContext context) {
        var x = context.chunkPos().getMinBlockX();
        var z = context.chunkPos().getMinBlockZ();
        var depth = definition.depth();
        var base = 0;
        var min = depth.getFirst();
        var max = depth.getSecond();

        var column = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());

        for (var i = min; i < max; ++i) {
            var y = base + i;
            var state = column.getBlock(y);
            var above = column.getBlock(y + 1);
            if (state.isSolidRender() && above.isAir()) {
                // Found an air space with solid beneath, return early.
                return Optional.of(new BlockPos(x, y + 1, z));
            }
        }

        // Make a chest in something solid; we will move it in post process.
        var y = base + min + context.random().nextInt(max - min);
        return Optional.of(new BlockPos(x, y, z));
    }

    @Override
    public StructureType<?> type() {
        return feature().registers.structureType.get();
    }

    private static SecretChests feature() {
        return SecretChests.feature();
    }
}
