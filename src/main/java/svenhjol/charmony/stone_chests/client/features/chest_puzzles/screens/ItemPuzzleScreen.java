package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.core.client.SlotSprite;
import svenhjol.charmony.core.client.features.tint_background.TintedGuiGraphics;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemPuzzleMenu;

public class ItemPuzzleScreen extends BasePuzzleScreen<ItemPuzzleMenu> {
    private final int numSlots;

    public ItemPuzzleScreen(ItemPuzzleMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.numSlots = menu.getNumItems();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, ticks, mouseX, mouseY);
        renderDynamicSlots(guiGraphics);
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
            ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blitSprite(RenderPipelines.GUI_TEXTURED, texture.sprite(), ox, oy, texture.width(), texture.height());
        }
    }
}
