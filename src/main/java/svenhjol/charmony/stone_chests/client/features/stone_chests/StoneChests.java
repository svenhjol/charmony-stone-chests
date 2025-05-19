package svenhjol.charmony.stone_chests.client.features.stone_chests;

import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.api.core.Side;

import java.util.function.Supplier;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class StoneChests extends SidedFeature {
    public final Supplier<Common> common;
    public final Handlers handlers;
    public final Registers registers;
    public final ItemTidyingProviders itemTidyingProviders;

    public StoneChests(Mod mod) {
        super(mod);
        common = Common::new;
        handlers = new Handlers(this);
        registers = new Registers(this);
        itemTidyingProviders = new ItemTidyingProviders(this);
    }

    public static StoneChests feature() {
        return Mod.getSidedFeature(StoneChests.class);
    }
}
