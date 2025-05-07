package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
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
        drawCenteredString(guiGraphics, font, Component.translatable("gui.charmony-stone-chests.clock.time"), imageWidth / 2, 32, tintedFg, false);
    }

    protected void renderClockTime(GuiGraphics guiGraphics) {
        var time = getMenu().getTime();
        var image = getClockImage(time);

        var pose = guiGraphics.pose();
        pose.pushPose();
        var x = -9; // This is scaled by pose.scale()
        var y = -42; // This is scaled by pose.scale()
        pose.translate(((float) width / 2), ((float)height / 2), 1.0f);
        pose.scale(1.24f, 1.24f, 1.24f);
        guiGraphics.blit(RenderType::guiTextured, image, x, y, 0.0f, 0.0f, 16, 16, 16, 16);
        pose.popPose();
    }

    protected ResourceLocation getClockImage(int time) {
        String i = String.valueOf(time);
        if (time < 10) {
            i = "0" + i;
        }
        return ResourceLocation.withDefaultNamespace("textures/item/clock_" + i + ".png");
    }
}
