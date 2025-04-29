package svenhjol.charmony.stone_chests.client.features.stone_chest_puzzles;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

import java.util.function.Supplier;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class StoneChestPuzzles extends SidedFeature {
    public final Registers registers;
    public final Supplier<Common> common;

    public StoneChestPuzzles(Mod mod) {
        super(mod);
        common = Common::new;
        registers = new Registers(this);
    }

    public static StoneChestPuzzles feature() {
        return Mod.getSidedFeature(StoneChestPuzzles.class);
    }
}
