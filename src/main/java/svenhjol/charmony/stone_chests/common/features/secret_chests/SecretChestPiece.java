package svenhjol.charmony.stone_chests.common.features.secret_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.Fluids;
import svenhjol.charmony.api.secret_chests.SecretChestsApi;
import svenhjol.charmony.api.secret_chests.SecretChestDefinition;
import svenhjol.charmony.api.secret_chests.SecretChestPlacement;
import svenhjol.charmony.core.base.Log;

public class SecretChestPiece extends StructurePiece {
    private SecretChestDefinition definition;

    public SecretChestPiece(SecretChestDefinition definition, BlockPos pos) {
        super(feature().registers.structurePiece.get(), 0, new BoundingBox(pos));
        this.definition = definition;
    }

    public SecretChestPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(feature().registers.structurePiece.get(), tag);
        tag.getString(Registers.DEFINITION_ID).ifPresent(t -> this.definition = feature().registers.secretChestDefinitions.get(t));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString(Registers.DEFINITION_ID, this.definition.getSerializedName());
    }

    @Override
    public void postProcess(
        WorldGenLevel level,
        StructureManager structureManager,
        ChunkGenerator chunkGenerator,
        RandomSource random,
        BoundingBox boundingBox,
        ChunkPos chunkPos,
        BlockPos pos
    ) {
        // Let the definition do world surface decoration.
        definition.decorateSurface(level, pos, random);

        var offsetPos = new BlockPos(boundingBox.minX(), pos.getY(), boundingBox.minZ());

        var state = level.getBlockState(offsetPos);
        var stateBelow = level.getBlockState(offsetPos.below());
        if (stateBelow.isSolidRender() && state.isAir()) {
            this.boundingBox = new BoundingBox(offsetPos);
            if (createChest(definition, level, this.boundingBox, random, offsetPos, false)) {
                log().debug("Placed chest using exact coordinates at " + pos);
            }
            return;
        }

        var fallbackYOffset = definition.fallbackYOffset();
        var minYOffset = fallbackYOffset.getFirst();
        var maxYOffset = fallbackYOffset.getSecond();
        var xzOffset = definition.fallbackXZOffset();

        if (definition.placement().equals(SecretChestPlacement.Buried)) {
            // Look for places to bury the chest in a range around the structure start pos.
            // It is considered a success if there is a solid block below and above the target pos.
            for (var y = minYOffset; y < maxYOffset; y++) {
                for (var x = -xzOffset; x < xzOffset; x++) {
                    for (var z = -xzOffset; z < xzOffset; z++) {
                        var tryPos = offsetPos.offset(x, y, z);
                        var tryStateAbove = level.getBlockState(tryPos.above());
                        var tryStateBelow = level.getBlockState(tryPos.below());

                        if (tryStateBelow.isSolidRender() && tryStateAbove.isSolidRender()) {
                            var fluidState = level.getFluidState(tryPos);
                            this.boundingBox = new BoundingBox(tryPos);
                            if (createChest(definition, level, this.boundingBox, random, tryPos, fluidState.is(Fluids.WATER))) {
                                log().debug("Placed chest using buried XZ offsets at " + pos);
                            }
                            return;
                        }
                    }
                }
            }

        } else {
            // For surface and cave placements, look for places to place the chest in a range around the structure start pos.
            // This is a success if the target block is air or water and there is a solid block beneath the target pos.
            for (var y = minYOffset; y < maxYOffset; y++) {
                for (var x = -xzOffset; x < xzOffset; x++) {
                    for (var z = -xzOffset; z < xzOffset; z++) {
                        var tryPos = offsetPos.offset(x, y, z);
                        var tryState = level.getBlockState(tryPos);
                        var tryStateBelow = level.getBlockState(tryPos.below());
                        var fluidState = level.getFluidState(tryPos);

                        var isAir = tryState.isAir();
                        var isNotSolidRender = !tryState.isSolidRender();
                        var isWater = fluidState.is(Fluids.WATER);

                        if (tryStateBelow.isSolidRender() && (isAir || isWater || isNotSolidRender)) {
                            this.boundingBox = new BoundingBox(tryPos);
                            if (createChest(definition, level, this.boundingBox, random, tryPos, isWater)) {
                                log().debug("Placed chest using surface/cave XZ offsets at " + pos);
                            }
                            return;
                        }
                    }
                }
            }

            // Let the definition do worldgen here.
            var generated = definition.generateSurface(level, pos.below(), random);
            if (generated) {
                if (createChest(definition, level, this.boundingBox, random, pos, false)) {
                    log().debug("Placed chest using generateSurface at " + pos);
                }
                return;
            }

            if (definition.canBeFullyBuried()) {
                // Force the chest into solid blocks.
                for (var y = 0; y < maxYOffset; y++) {
                    var tryPos = offsetPos.offset(0, y, 0);
                    if (stateBelow.isSolidRender()) {
                        this.boundingBox = new BoundingBox(tryPos);
                        if (createChest(definition, level, this.boundingBox, random, tryPos, false)) {
                            log().debug("Placed chest using canBeFullyBuried at " + pos);
                        }
                        return;
                    }
                }
            }
        }

        log().warn("Could not find valid position for " + definition.material().getSerializedName() + " chest at " + pos);
    }

    public static SecretChests feature() {
        return SecretChests.feature();
    }

    protected boolean createChest(
        SecretChestDefinition definition,
        ServerLevelAccessor level,
        BoundingBox box,
        RandomSource random,
        BlockPos pos,
        boolean waterlogged
    ) {
        if (!box.isInside(pos)) {
            log().debug("Bounding box is incorrect");
            return false;
        }
        return SecretChestsApi.instance().createChest(definition, level, random, pos, waterlogged, null);
    }

    protected Log log() {
        return feature().log();
    }
}
