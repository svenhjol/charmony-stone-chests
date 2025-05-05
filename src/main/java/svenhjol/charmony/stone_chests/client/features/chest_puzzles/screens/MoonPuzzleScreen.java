package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.core.client.TintedGuiGraphics;
import svenhjol.charmony.core.helpers.ColorHelper;
import svenhjol.charmony.stone_chests.StoneChestsMod;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.MoonPuzzleMenu;

import java.util.HashMap;
import java.util.Map;

public class MoonPuzzleScreen extends AbstractContainerScreen<MoonPuzzleMenu> {
    public static final ResourceLocation BACKGROUND = StoneChestsMod.id("textures/gui/container/generic_puzzle.png");
    public static final Map<Integer, ResourceLocation> MOON_PHASES = new HashMap<>();

    protected ColorHelper.Color tintedBg;
    protected int tintedFg;

    public MoonPuzzleScreen(MoonPuzzleMenu menu, Inventory inventory, Component component) {
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
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        var x = (width - imageWidth) / 2;
        var y = (height - imageHeight) / 2;
        ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blit(RenderType::guiTextured, BACKGROUND, x, y, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256);
        renderMoonPhase(guiGraphics);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, tintedFg, false);

        // TODO: make a centered string helper
        var moonTitle = getMoonTitle();
        var moonText = moonTitle.getVisualOrderText();
        guiGraphics.drawString(font, moonText, (imageWidth / 2) - font.width(moonText) / 2, 32, tintedFg, false);

        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, tintedFg, false);
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

    protected void renderMoonPhase(GuiGraphics guiGraphics) {
        var bx = width / 2;
        var by = (height - imageHeight) / 2;
        var ox = bx - 8;
        var oy = by + 46;

        var phase = MOON_PHASES.get(getMenu().getMoonPhase());
        ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blitSprite(RenderType::guiTextured, phase, ox, oy, 16, 16);
    }

    protected Component getMoonTitle() {
        Component moonTitle;
        var phase = getMenu().getMoonPhase();
        if (phase == 0) {
            moonTitle = Component.translatable("gui.charmony-stone-chests.moon.full");
        } else if (phase > 0 && phase < 4) {
            moonTitle = Component.translatable("gui.charmony-stone-chests.moon.waning");
        } else if (phase == 4) {
            moonTitle = Component.translatable("gui.charmony-stone-chests.moon.new");
        } else {
            moonTitle = Component.translatable("gui.charmony-stone-chests.moon.waxing");
        }
        return moonTitle;
    }

    static {
        MOON_PHASES.put(0, StoneChestsMod.id("moon/full"));
        MOON_PHASES.put(1, StoneChestsMod.id("moon/waning_gibbous"));
        MOON_PHASES.put(2, StoneChestsMod.id("moon/waning_half"));
        MOON_PHASES.put(3, StoneChestsMod.id("moon/waning_crescent"));
        MOON_PHASES.put(4, StoneChestsMod.id("moon/new"));
        MOON_PHASES.put(5, StoneChestsMod.id("moon/waxing_crescent"));
        MOON_PHASES.put(6, StoneChestsMod.id("moon/waxing_half"));
        MOON_PHASES.put(7, StoneChestsMod.id("moon/waxing_gibbous"));
    }
}
