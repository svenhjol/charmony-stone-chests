package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.ItemPuzzleRequirement;
import svenhjol.charmony.api.ItemPuzzleRequirementProvider;
import svenhjol.charmony.core.base.Setup;

import java.util.List;

public class ItemRequirementProviders extends Setup<ChestPuzzles> implements ItemPuzzleRequirementProvider {
    public ItemRequirementProviders(ChestPuzzles feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public List<ItemPuzzleRequirement> getItemPuzzleTags() {
        return List.of(
            new ItemPuzzleRequirement(Tags.PUZZLE_BUTTONS, 2, 4, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_CANDLES, 2, 4, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_GEMS, 1, 2, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_GLAZED_TERRACOTTA, 2, 4, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_INGOTS, 8, 16, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_LOGS, 8, 16, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_SHERDS, 1, 2, 1),
            new ItemPuzzleRequirement(Tags.PUZZLE_STONES, 8, 16, 3),
            new ItemPuzzleRequirement(Tags.PUZZLE_TERRACOTTA, 4, 8, 3),
            new ItemPuzzleRequirement(Tags.PUZZLE_WOOL, 2, 4, 2)
        );
    }
}
