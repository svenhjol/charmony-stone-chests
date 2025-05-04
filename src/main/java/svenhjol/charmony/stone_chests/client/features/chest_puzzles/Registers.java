package svenhjol.charmony.stone_chests.client.features.chest_puzzles;

import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;

public class Registers extends Setup<ChestPuzzles> {
    public Registers(ChestPuzzles feature) {
        super(feature);

        var registry = ClientRegistry.forFeature(feature);
        var common = feature.common.get();

        registry.menuScreen(common.registers.itemPuzzleMenu, () -> ItemPuzzleScreen::new);
    }
}
