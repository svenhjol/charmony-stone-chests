package svenhjol.charmony.stone_chests.common.mixins.secret_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charmony.stone_chests.common.features.secret_chests.SecretChests;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin {
    @Unique
    private SecretChests feature;

    @Redirect(
        method = "placeInWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/ServerLevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            ordinal = 1
        )
    )
    public boolean hookPlaceInWorld(ServerLevelAccessor level, BlockPos pos, BlockState state, int i) {
        if (feature == null) {
            feature = SecretChests.feature(); // Simple cache.
        }
        var isAir = state.isAir();
        var belowState = level.getBlockState(pos.below());

        if (isAir) {
            if (belowState.is(Blocks.PURPUR_BLOCK) && feature.handlers.createEndCityChest(level, pos)) {
                return true;
            } else if (belowState.is(Blocks.POLISHED_BLACKSTONE_BRICKS) && feature.handlers.createBastionChest(level, pos)) {
                return true;
            }
        }
        return level.setBlock(pos, state, i);
    }
}
