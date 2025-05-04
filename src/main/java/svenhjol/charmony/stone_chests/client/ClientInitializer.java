package svenhjol.charmony.stone_chests.client;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.stone_chests.StoneChestsMod;
import svenhjol.charmony.stone_chests.client.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.stone_chests.client.features.stone_chests.StoneChests;

public final class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.client.ClientInitializer.init();

        // Prepare and run the mod.
        var mod = StoneChestsMod.instance();
        mod.addSidedFeature(StoneChests.class);
        mod.addSidedFeature(ChestPuzzles.class);
        mod.run(Side.Client);
    }
}
