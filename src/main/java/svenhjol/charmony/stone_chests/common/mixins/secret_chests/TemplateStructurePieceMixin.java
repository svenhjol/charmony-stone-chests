package svenhjol.charmony.stone_chests.common.mixins.secret_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.stone_chests.common.features.secret_chests.SecretChests;

@Mixin(TemplateStructurePiece.class)
public abstract class TemplateStructurePieceMixin {
    @Shadow @Final protected String templateName;

    @Inject(
        method = "postProcess",
        at = @At("HEAD")
    )
    public void hookPlaceInWorld(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfo ci) {
        SecretChests.feature().handlers.template = this.templateName;
    }
}
