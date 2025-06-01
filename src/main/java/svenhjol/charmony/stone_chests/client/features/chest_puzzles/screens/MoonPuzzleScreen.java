package svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.client.features.tint_background.TintedGuiGraphics;
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
        drawCenteredString(guiGraphics, font, getMoonTitle(), imageWidth / 2, 32, tintedFg.getArgbColor(), false);
    }

    protected void renderMoonPhase(GuiGraphics guiGraphics) {
        var bx = width / 2;
        var by = (height - imageHeight) / 2;
        var ox = bx - 8;
        var oy = by + 46;

        var phase = MOON_PHASES.get(getMenu().getMoonPhase());
        ((TintedGuiGraphics)guiGraphics).tint(tintedBg).blitSprite(RenderPipelines.GUI_TEXTURED, phase, ox, oy, 16, 16);
    }

    protected Component getMoonTitle() {
        Component moonTitle;
        var phase = getMenu().getMoonPhase();
        if (phase == 0) {
            moonTitle = Component.translatable("gui.charmony.moon.full");
        } else if (phase > 0 && phase < 4) {
            moonTitle = Component.translatable("gui.charmony.moon.waning");
        } else if (phase == 4) {
            moonTitle = Component.translatable("gui.charmony.moon.new");
        } else {
            moonTitle = Component.translatable("gui.charmony.moon.waxing");
        }
        return moonTitle;
    }

    static {
        MOON_PHASES.put(0, Charmony.id("moon/full"));
        MOON_PHASES.put(1, Charmony.id("moon/waning_gibbous"));
        MOON_PHASES.put(2, Charmony.id("moon/waning_half"));
        MOON_PHASES.put(3, Charmony.id("moon/waning_crescent"));
        MOON_PHASES.put(4, Charmony.id("moon/new"));
        MOON_PHASES.put(5, Charmony.id("moon/waxing_crescent"));
        MOON_PHASES.put(6, Charmony.id("moon/waxing_half"));
        MOON_PHASES.put(7, Charmony.id("moon/waxing_gibbous"));
    }
}
