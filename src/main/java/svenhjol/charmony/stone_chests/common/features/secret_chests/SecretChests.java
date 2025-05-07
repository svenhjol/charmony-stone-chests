package svenhjol.charmony.stone_chests.common.features.secret_chests;

import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    Adds stone chests hidden in certain structures, caves, the ocean floor and End islands.""")
public final class SecretChests extends SidedFeature {
    public final Registers registers;
    public final Providers providers;
    public final Handlers handlers;

    @Configurable(
        name = "Fortress chance",
        description = "Chance (out of 1000) of a secret chest being placed in a Nether Fortress corridor or bridge.",
        requireRestart = false
    )
    private static int fortressChance = 20;

    @Configurable(
        name = "End City chance",
        description = "Chance (out of 1000) of a secret chest being placed in an End City tower room or roof.",
        requireRestart = false
    )
    private static int endCityChance = 3;

    @Configurable(
        name = "Bastion chance",
        description = "Chance (out of 1000) of a secret chest being placed randomly in a Bastion Remnant.",
        requireRestart = false
    )
    private static int bastionChance = 5;

    public SecretChests(Mod mod) {
        super(mod);
        handlers = new Handlers(this);
        registers = new Registers(this);
        providers = new Providers(this);
    }

    public static SecretChests feature() {
        return Mod.getSidedFeature(SecretChests.class);
    }

    public int fortressChance() {
        return fortressChance;
    }

    public int endCityChance() {
        return endCityChance;
    }

    public int bastionChance() {
        return bastionChance;
    }
}
