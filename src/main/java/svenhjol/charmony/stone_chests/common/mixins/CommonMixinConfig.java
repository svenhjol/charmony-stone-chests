package svenhjol.charmony.stone_chests.common.mixins;

import svenhjol.charmony.core.base.MixinConfig;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public class CommonMixinConfig extends MixinConfig {
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
        return Side.Common;
    }
}
