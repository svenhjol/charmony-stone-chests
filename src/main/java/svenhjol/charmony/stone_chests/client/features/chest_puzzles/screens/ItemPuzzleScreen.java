package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.core.client.SlotSprite;
import svenhjol.charmony.core.client.TintedGuiGraphics;
import svenhjol.charmony.core.helpers.ColorHelper;
import svenhjol.charmony.stone_chests.StoneChestsMod;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemMenuPuzzle;

public class ItemPuzzleScreen extends AbstractContainerScreen<ItemMenuPuzzle> {
    public static final ResourceLocation BACKGROUND = StoneChestsMod.id("textures/gui/container/generic_puzzle.png");

    private final int numSlots;
    protected ColorHelper.Color tintedBg;
    protected int tintedFg;

    public ItemPuzzleScreen(ItemMenuPuzzle menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 195;
        this.inventoryLabelY += 29;
        this.numSlots = menu.getNumItems();
    }

    @Override
    protected void init() {
        super.init();
        var buttonWidth = 126;
        var x = width / 2;
        var y = height / 2;

        var screenPosition = new ScreenPosition(x - (buttonWidth / 2), y - 25);
        var label = Component.translatable("gui.charmony-stone-chests.unlock");
        var builder = new Button.Builder(label, b -> {
            if (minecraft != null && minecraft.gameMode != null) {
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
                minecraft.setScreen(null);
            }
        });
        builder.pos(screenPosition.x(), screenPosition.y());
        builder.width(buttonWidth);
        this.addRenderableWidget(builder.build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ticks) {
        var material = getMenu().getMaterial();
        var color = material.getColor();
        this.tintedBg = ColorHelper.tintBackgroundColor(color);
        this.tintedFg = ColorHelper.tintForegroundColor(color);

        super.render(guiGraphics, mouseX, mouseY, ticks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        var x = (width - imageWidth) / 2;
        var y = (height - imageHeight) / 2;
        ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blit(RenderType::guiTextured, BACKGROUND, x, y, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256);
        renderDynamicSlots(guiGraphics);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, tintedFg, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, tintedFg, false);
    }

    protected void renderDynamicSlots(GuiGraphics guiGraphics) {
        var bx = (width - imageWidth) / 2;
        var by = (height - imageHeight) / 2;
        var oy = by + 46;

        for (var i = 0; i < numSlots; i++) {
            var q = 160 / (numSlots + 1);
            var x = (i + 1) * q;
            var ox = bx + x;

            var texture = SlotSprite.Slot;
            ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blitSprite(RenderType::guiTextured, texture.sprite(), ox, oy, texture.width(), texture.height());
        }
    }
}
