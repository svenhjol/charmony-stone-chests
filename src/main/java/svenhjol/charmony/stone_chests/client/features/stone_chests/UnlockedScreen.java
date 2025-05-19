package svenhjol.charmony.stone_chests.client.features.stone_chests;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.api.tint_background.TintedGuiGraphics;
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
        ((TintedGuiGraphics)guiGraphics).tint(tinted).blit(RenderType::guiTextured, BACKGROUND, x, y, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        var material = getMenu().getMaterial();
        var color = material.getColor();
        var tinted = ColorHelper.tintForegroundColor(color);
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, tinted, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, tinted, false);
    }
}
