package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import svenhjol.charmony.api.chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Tags;

import java.util.List;

public class NetherItemPuzzleProvider extends BaseItemPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "nether_item_puzzle";

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    protected List<ItemPuzzleRequirement> getRequirements() {
        return List.of(
            new ItemPuzzleRequirement(Tags.PUZZLE_NETHER, 2)
        );
    }
}
