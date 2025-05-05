package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.core.client.TintedGuiGraphics;
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
        var bx = width / 2;
        var by = (height - imageHeight) / 2;
        var ox = bx - 8;
        var oy = by + 46;

        var time = getMenu().getTime();
        var image = getClockImage(time);

        ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blit(RenderType::guiTextured, image, ox, oy, 0.0f, 0.0f, 16, 16, 16, 16);
    }

    protected ResourceLocation getClockImage(int time) {
        String i = String.valueOf(time);
        if (time < 10) {
            i = "0" + i;
        }
        return ResourceLocation.withDefaultNamespace("textures/item/clock_" + i + ".png");
    }
}
