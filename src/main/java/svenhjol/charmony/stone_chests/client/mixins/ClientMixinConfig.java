package svenhjol.charmony.stone_chests.client.mixins;

import svenhjol.charmony.core.base.MixinConfig;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public class ClientMixinConfig extends MixinConfig {
    @Override
    protected String modId() {
        return StoneChestsMod.ID;
    }

    @Override
    protected String modRoot() {
        return "svenhjol.charmony.stone_chests";
    }

    @Override
    protected Side side() {
        return Side.Client;
    }
}
