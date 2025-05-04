package svenhjol.charmony.stone_chests.client.features.chest_puzzles;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

import java.util.function.Supplier;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class ChestPuzzles extends SidedFeature {
    public final Registers registers;
    public final Supplier<Common> common;

    public ChestPuzzles(Mod mod) {
        super(mod);
        common = Common::new;
        registers = new Registers(this);
    }

    public static ChestPuzzles feature() {
        return Mod.getSidedFeature(ChestPuzzles.class);
    }
}
