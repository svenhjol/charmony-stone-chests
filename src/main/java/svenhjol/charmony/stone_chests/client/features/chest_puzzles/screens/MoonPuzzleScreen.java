package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.api.tint_background.TintedGuiGraphics;
import svenhjol.charmony.stone_chests.StoneChestsMod;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.MoonPuzzleMenu;

import java.util.HashMap;
import java.util.Map;

public class MoonPuzzleScreen extends BasePuzzleScreen<MoonPuzzleMenu> {
    public static final Map<Integer, ResourceLocation> MOON_PHASES = new HashMap<>();

    public MoonPuzzleScreen(MoonPuzzleMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, ticks, mouseX, mouseY);
        renderMoonPhase(guiGraphics);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        drawCenteredString(guiGraphics, font, getMoonTitle(), imageWidth / 2, 32, tintedFg, false);
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
