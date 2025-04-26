package svenhjol.charmony.stone_chests.client.features.stone_chests;

import svenhjol.charmony.stone_chests.common.features.stone_chests.Registers;
import svenhjol.charmony.stone_chests.common.features.stone_chests.StoneChests;

public class Common {
    public final StoneChests feature;
    public final Registers registers;

    public Common() {
        feature = StoneChests.feature();
        registers = feature.registers;
    }
}
