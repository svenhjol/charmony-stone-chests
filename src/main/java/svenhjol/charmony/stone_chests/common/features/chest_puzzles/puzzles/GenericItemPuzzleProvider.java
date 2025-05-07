package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import svenhjol.charmony.api.ItemPuzzleRequirement;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;

import java.util.List;

public class GenericItemPuzzleProvider extends BaseItemPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "item_puzzle";

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    protected List<ItemPuzzleRequirement> getRequirements() {
        return ChestPuzzles.feature().registers.itemPuzzleRequirements;
    }
}
