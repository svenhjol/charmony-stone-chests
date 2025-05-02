package svenhjol.charmony.stone_chests.common.features.secret_chests;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    Adds stone chests hidden in certain structures, caves, the ocean floor and End islands.""")
public final class SecretChests extends SidedFeature {
    public final Registers registers;
    public final Providers providers;

    public SecretChests(Mod mod) {
        super(mod);
        registers = new Registers(this);
        providers = new Providers(this);
    }

    public static SecretChests feature() {
        return Mod.getSidedFeature(SecretChests.class);
    }
}
