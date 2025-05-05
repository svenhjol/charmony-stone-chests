package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.*;

public class Providers extends Setup<ChestPuzzles> {
    public Providers(ChestPuzzles feature) {
        super(feature);

        new ClockPuzzleMenuProvider();
        new EnchantedBookPuzzleMenuProvider();
        new EnchantedItemPuzzleMenuProvider();
        new GemPuzzleMenuProvider();
        new MoonPuzzleMenuProvider();
        new SherdPuzzleMenuProvider();
    }
}
