package svenhjol.charmony.stone_chests.client.features.stone_chest_puzzles;

import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.Registers;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.StoneChestPuzzles;

public class Common {
    public final StoneChestPuzzles feature;
    public final Registers registers;

    public Common() {
        feature = StoneChestPuzzles.feature();
        registers = feature.registers;
    }
}
