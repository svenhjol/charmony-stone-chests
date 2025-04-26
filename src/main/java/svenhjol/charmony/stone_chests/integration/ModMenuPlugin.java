package svenhjol.charmony.stone_chests.integration;

import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.integration.BaseModMenuPlugin;
import svenhjol.charmony.stone_chests.StoneChestsMod;

@SuppressWarnings("unused")
public final class ModMenuPlugin extends BaseModMenuPlugin {
    @Override
    public Mod mod() {
        return StoneChestsMod.instance();
    }
}
