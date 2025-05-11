package svenhjol.charmony.stone_chests.common.features.secret_chests;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import svenhjol.charmony.api.secret_chests.SecretChestDefinition;
import svenhjol.charmony.stone_chests.common.features.stone_chests.StoneChests;

import java.util.List;
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
            return Optional.empty();
        }

        var startPos = opt.get();
        return Optional.of(new GenerationStub(startPos,
            builder -> builder.addPiece(new SecretChestPiece(definition, startPos))));
    }

    private Optional<BlockPos> findStart(GenerationContext context) {
        if (!feature().enabled() || !StoneChests.feature().enabled()) {
            return Optional.empty();
        }

        if (definition.chance() < context.random().nextDouble()) {
            return Optional.empty();
        }

        return switch (definition.placement()) {
            case Surface -> findSurfaceStart(context);
            case Cave -> findCaveStart(context);
            case Buried -> findBuriedStart(context);
            default -> Optional.empty();
        };
    }

    private Optional<BlockPos> findSurfaceStart(GenerationContext context) {
        var x = context.chunkPos().getMinBlockX();
        var z = context.chunkPos().getMinBlockZ();
        List<Heightmap.Types> heightMaps = List.of(Heightmap.Types.OCEAN_FLOOR_WG, Heightmap.Types.WORLD_SURFACE_WG);

        for (Heightmap.Types heightMap : heightMaps) {
            var offsets = definition.fallbackYOffset();

            var y = context.chunkGenerator().getFirstOccupiedHeight(x, z, heightMap, context.heightAccessor(), context.randomState());
            var column = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());

            int surface;
            for (surface = y + offsets.getFirst(); surface < y + offsets.getSecond(); ++surface) {
                var state = column.getBlock(y);
                var above = column.getBlock(y + 1);
                var stateIsValid = heightMap.isOpaque().test(state)
                    || state.is(Blocks.POWDER_SNOW);
                var aboveIsValid = !heightMap.isOpaque().test(above)
                    || above.is(Blocks.POWDER_SNOW);

                if (stateIsValid && aboveIsValid) {
                    return Optional.of(new BlockPos(x, surface, z));
                }
            }

            if (definition.strict()) {
                return Optional.empty();
            }
        }

        // Make a chest anywhere; we will try and move it in post process.
        var hmin = definition.height().getFirst();
        var hmax = definition.height().getSecond();
        var yOffset = hmin + context.random().nextInt(hmax - hmin);
        return Optional.of(new BlockPos(x, yOffset, z));
    }

    private Optional<BlockPos> findCaveStart(GenerationContext context) {
        var x = context.chunkPos().getMinBlockX();
        var z = context.chunkPos().getMinBlockZ();
        var depth = definition.height();
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

        if (definition.strict()) {
            return Optional.empty();
        }

        // Make a chest in something solid; we will try and move it in post process.
        var y = base + min + context.random().nextInt(max - min);
        return Optional.of(new BlockPos(x, y, z));
    }

    private Optional<BlockPos> findBuriedStart(GenerationContext context) {
        var x = context.chunkPos().getMinBlockX();
        var z = context.chunkPos().getMinBlockZ();
        var depth = definition.height();
        var base = 0;
        var min = depth.getFirst();
        var max = depth.getSecond();

        var column = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());

        for (var i = min; i < max; ++i) {
            var y = base + i;
            var below = column.getBlock(y - 1);
            var above = column.getBlock(y + 1);
            if (below.isSolidRender() && above.isSolidRender()) {
                // Found solid above and below, return early.
                return Optional.of(new BlockPos(x, y, z));
            }
        }

        if (definition.strict()) {
            return Optional.empty();
        }

        // Make a chest somewhere and we will try and move it in post process.
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
