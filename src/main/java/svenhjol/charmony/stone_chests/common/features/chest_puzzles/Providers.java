package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.EnchantedBookPuzzleMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.EnchantedItemPuzzleMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.GemPuzzleMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.SherdPuzzleMenuProvider;

public class Providers extends Setup<ChestPuzzles> {
    public Providers(ChestPuzzles feature) {
        super(feature);

        new EnchantedBookPuzzleMenuProvider();
        new EnchantedItemPuzzleMenuProvider();
        new GemPuzzleMenuProvider();
        new SherdPuzzleMenuProvider();
    }
}
