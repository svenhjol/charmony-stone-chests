package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ClockPuzzleMenu;

public class ClockPuzzleScreen extends BasePuzzleScreen<ClockPuzzleMenu> {
    public ClockPuzzleScreen(ClockPuzzleMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, ticks, mouseX, mouseY);
        renderClockTime(guiGraphics);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        drawCenteredString(guiGraphics, font, Component.translatable("gui.charmony.clock.time"), imageWidth / 2, 32, tintedFg.getArgbColor(), false);
    }

    protected void renderClockTime(GuiGraphics guiGraphics) {
        var time = getMenu().getTime();
        var image = getClockImage(time);

        var scale = 1.24f;
        var pose = guiGraphics.pose();
        pose.pushMatrix();
        pose.translate(((float) width / 2), ((float)height / 2));
        pose.scale(scale, scale);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, image, -8, -42, 0.0f, 0.0f, 16, 16, 16, 16);
        pose.popMatrix();
    }

    protected ResourceLocation getClockImage(int time) {
        String i = String.valueOf(time);
        if (time < 10) {
            i = "0" + i;
        }
        return ResourceLocation.withDefaultNamespace("textures/item/clock_" + i + ".png");
    }
}
