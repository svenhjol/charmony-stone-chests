package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charmony.api.chest_puzzles.ChestPuzzleMenu;
import svenhjol.charmony.api.core.Color;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.client.features.tint_background.TintedGuiGraphics;
import svenhjol.charmony.core.helpers.ColorHelper;

public abstract class BasePuzzleScreen<T extends AbstractContainerMenu & ChestPuzzleMenu> extends AbstractContainerScreen<T> {
    public static final ResourceLocation BACKGROUND = Charmony.id("textures/gui/container/generic_puzzle.png");
    protected Color tintedBg;
    protected Color tintedFg;

    public BasePuzzleScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 195;
        this.inventoryLabelY += 29;
    }

    @Override
    protected void init() {
        super.init();
        var buttonWidth = 126;
        var x = width / 2;
        var y = height / 2;

        var screenPosition = new ScreenPosition(x - (buttonWidth / 2), y - 25);
        var label = Component.translatable("gui.charmony.unlock");
        var buttonBuilder = new Button.Builder(label, b -> {
            if (minecraft != null && minecraft.gameMode != null) {
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
                minecraft.setScreen(null);
            }
        });
        buttonBuilder.pos(screenPosition.x(), screenPosition.y());
        buttonBuilder.width(buttonWidth);
        this.addRenderableWidget(buttonBuilder.build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ticks) {
        var material = getMenu().getMaterial();
        var color = material.getColor();
        this.tintedBg = ColorHelper.getBackgroundColor(color);
        this.tintedFg = ColorHelper.getForegroundColor(color);

        super.render(guiGraphics, mouseX, mouseY, ticks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        var x = (width - imageWidth) / 2;
        var y = (height - imageHeight) / 2;
        ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blit(RenderPipelines.GUI_TEXTURED, background(), x, y, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, tintedFg.getArgbColor(), false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, tintedFg.getArgbColor(), false);
    }

    protected void drawCenteredString(GuiGraphics guiGraphics, Font font, Component string, int x, int y, int color, boolean shadow) {
        var text = string.getVisualOrderText();
        guiGraphics.drawString(font, text, x - font.width(text) / 2, y, color, shadow);
    }

    protected ResourceLocation background() {
        return BACKGROUND;
    }
}
