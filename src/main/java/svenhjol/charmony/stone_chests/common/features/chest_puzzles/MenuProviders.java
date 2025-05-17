package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.secret_chests.SecretChestPuzzleMenuProvider;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.*;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.EnderItemPuzzleProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.GenericItemPuzzleProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.NetherItemPuzzleProvider;

import java.util.List;

public class MenuProviders extends Setup<ChestPuzzles> {
    public MenuProviders(ChestPuzzles feature) {
        super(feature);

        List<? extends SecretChestPuzzleMenuProvider> providers = List.of(
            new ClockPuzzleMenuProvider(),
            new EnchantedBookPuzzleMenuProvider(),
            new EnchantedItemPuzzleMenuProvider(),
            new GenericItemPuzzleProvider(),
            new MoonPuzzleMenuProvider(),

            // Specific lootTable puzzles
            new NetherItemPuzzleProvider(),
            new EnderItemPuzzleProvider()
        );

        for (var provider : providers) {
            Api.registerProvider(provider);
            feature.log().debug("Registered puzzle provider: " + provider.getClass().getSimpleName());
        }
    }
}
