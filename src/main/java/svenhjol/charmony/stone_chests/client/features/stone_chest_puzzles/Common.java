package svenhjol.charmony.stone_chests.client.features.stone_chest_puzzles;

import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Registers;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;

public class Common {
    public final ChestPuzzles feature;
    public final Registers registers;

    public Common() {
        feature = ChestPuzzles.feature();
        registers = feature.registers;
    }
}
