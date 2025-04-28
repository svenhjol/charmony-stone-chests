package svenhjol.charmony.stone_chests.client.features.stone_chests;

import net.minecraft.client.renderer.blockentity.ChestRenderer;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;

public class Registers extends Setup<StoneChests> {
    public Registers(StoneChests feature) {
        super(feature);

        var registry = ClientRegistry.forFeature(feature);
        var common = feature.common.get();

        registry.blockEntityRenderer(common.registers.chestBlockEntity, () -> ChestRenderer::new);
        registry.menuScreen(common.registers.unlockedMenu, () -> UnlockedScreen::new);
        registry.menuScreen(common.registers.itemPuzzleMenu, () -> ItemPuzzleScreen::new);
    }
}
