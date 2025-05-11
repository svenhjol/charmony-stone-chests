package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.api.chest_puzzles.ItemPuzzleRequirementProvider;
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
            new ItemPuzzleRequirement(Tags.PUZZLE_CANDLES, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_GEMS, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_GLAZED_TERRACOTTA, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_INGOTS,  2),
            new ItemPuzzleRequirement(Tags.PUZZLE_LOGS,  2),
            new ItemPuzzleRequirement(Tags.PUZZLE_SHERDS, 1),
            new ItemPuzzleRequirement(Tags.PUZZLE_STAINED_GLASS, 2),
            new ItemPuzzleRequirement(Tags.PUZZLE_STONES,  3),
            new ItemPuzzleRequirement(Tags.PUZZLE_TERRACOTTA, 3),
            new ItemPuzzleRequirement(Tags.PUZZLE_WOOL, 2)
        );
    }
}
