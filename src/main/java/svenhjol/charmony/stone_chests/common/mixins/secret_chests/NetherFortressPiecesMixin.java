package svenhjol.charmony.stone_chests.common.mixins.secret_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.stone_chests.common.features.secret_chests.SecretChests;

@Mixin(value = {
    NetherFortressPieces.CastleSmallCorridorPiece.class,
    NetherFortressPieces.BridgeStraight.class
})
public abstract class NetherFortressPiecesMixin extends StructurePiece {
    protected NetherFortressPiecesMixin(StructurePieceType structurePieceType, int i, BoundingBox boundingBox) {
        super(structurePieceType, i, boundingBox);
    }

    @Inject(
        method = "postProcess",
        at = @At("TAIL")
    )
    private void hookPostProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci) {
        SecretChests.feature().handlers.createNetherFortressChest(this, level, random);
    }
}
