package svenhjol.charmony.stone_chests.common.features.buried_stone_chests;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    Adds stone chests hidden in underground caves.""")
public final class BuriedStoneChests extends SidedFeature {
    public final Registers registers;
    public final Providers providers;

    public BuriedStoneChests(Mod mod) {
        super(mod);
        registers = new Registers(this);
        providers = new Providers(this);
    }

    public static BuriedStoneChests feature() {
        return Mod.getSidedFeature(BuriedStoneChests.class);
    }
}
