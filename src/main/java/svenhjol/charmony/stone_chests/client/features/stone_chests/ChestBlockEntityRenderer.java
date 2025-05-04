package svenhjol.charmony.stone_chests.client.features.stone_chests;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

public class ChestBlockEntityRenderer extends ChestRenderer<ChestBlockEntity> {
    public ChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, ChestModel chestModel, float f, int i, int j) {
        chestModel.setupAnim(f / 3); // Open the chest a crack
        chestModel.renderToBuffer(poseStack, vertexConsumer, i, j);
    }
}
