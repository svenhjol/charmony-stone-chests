package svenhjol.charmony.stone_chests.client.features.stone_chests;

import net.minecraft.client.gui.screens.Screen;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.tweaks.ItemTidyingWhitelistProvider;
import svenhjol.charmony.core.base.Setup;

import java.util.List;

public class ItemTidyingProviders extends Setup<StoneChests> implements
    ItemTidyingWhitelistProvider
{
    public ItemTidyingProviders(StoneChests feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public List<Class<? extends Screen>> getItemTidyingWhitelistedScreens() {
        return List.of(
            UnlockedScreen.class
        );
    }
}
