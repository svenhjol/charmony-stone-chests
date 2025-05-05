package svenhjol.charmony.stone_chests.client.features.chest_puzzles;

import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;
import svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens.ItemPuzzleScreen;
import svenhjol.charmony.stone_chests.client.features.chest_puzzles.screens.MoonPuzzleScreen;

public class Registers extends Setup<ChestPuzzles> {
    public Registers(ChestPuzzles feature) {
        super(feature);
        var registry = ClientRegistry.forFeature(feature);
        var common = feature.common.get();

        registry.menuScreen(common.registers.moonPuzzleMenu.get(), MoonPuzzleScreen::new);

        for (var supplier : common.registers.itemPuzzleMenus.values()) {
            registry.menuScreen(supplier.get(), ItemPuzzleScreen::new);
        }
    }
}
