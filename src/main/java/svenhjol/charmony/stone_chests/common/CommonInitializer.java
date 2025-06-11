package svenhjol.charmony.stone_chests.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.stone_chests.StoneChestsMod;
import svenhjol.charmony.stone_chests.common.features.stone_chests.StoneChests;

public final class CommonInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.common.CommonInitializer.init();

        // Prepare and run the mod.
        var mod = StoneChestsMod.instance();
        mod.addSidedFeature(StoneChests.class);
        mod.run(Side.Common);
    }
}
