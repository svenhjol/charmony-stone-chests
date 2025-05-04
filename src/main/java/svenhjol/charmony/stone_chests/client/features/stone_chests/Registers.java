package svenhjol.charmony.stone_chests.client.features.stone_chests;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;

public class Registers extends Setup<StoneChests> {
    public Registers(StoneChests feature) {
        super(feature);

        var registry = ClientRegistry.forFeature(feature);
        var common = feature.common.get();

        registry.blockEntityRenderer(common.registers.chestBlockEntity, () -> ChestBlockEntityRenderer::new);
        registry.menuScreen(common.registers.unlockedMenu.get(), UnlockedScreen::new);
    }

    @Override
    public Runnable boot() {
        return () -> {
            var registry = ClientRegistry.forFeature(feature());
            var common = feature().common.get();

            for (var item : common.registers.chestBlockItems.values()) {
                registry.itemTab(item.get(), CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.TRAPPED_CHEST);
            }
        };
    }
}
