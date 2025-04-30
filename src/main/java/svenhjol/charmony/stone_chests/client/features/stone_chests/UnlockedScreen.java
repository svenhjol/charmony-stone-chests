package svenhjol.charmony.stone_chests.client.features.stone_chests;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.core.helpers.ColorHelper;
import svenhjol.charmony.stone_chests.StoneChestsMod;
import svenhjol.charmony.stone_chests.common.features.stone_chests.UnlockedMenu;

public class UnlockedScreen extends AbstractContainerScreen<UnlockedMenu> {
    public static final ResourceLocation BACKGROUND = StoneChestsMod.id("textures/gui/container/generic_27.png");

    public UnlockedScreen(UnlockedMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 168;
        this.inventoryLabelY += 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ticks) {
        super.render(guiGraphics, mouseX, mouseY, ticks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        var x = (width - imageWidth) / 2;
        var y = (height - imageHeight) / 2;

        var material = getMenu().getMaterial();
        var color = material.getColor();
        var tinted = ColorHelper.tintBackgroundColor(color);
        ColorHelper.tintTexture(guiGraphics, BACKGROUND, tinted, x, y, 0.0f, 0.0f, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        var material = getMenu().getMaterial();
        var color = material.getColor();
        var tinted = ColorHelper.tintForegroundColor(color);
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, tinted, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, tinted, false);
    }
}
