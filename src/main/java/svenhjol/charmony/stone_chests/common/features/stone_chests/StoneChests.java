package svenhjol.charmony.stone_chests.common.features.stone_chests;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    Chests made from various types of stone.""")
public final class StoneChests extends SidedFeature {
    public final Registers registers;

    public StoneChests(Mod mod) {
        super(mod);
        registers = new Registers(this);
    }

    public static StoneChests feature() {
        return Mod.getSidedFeature(StoneChests.class);
    }
}
