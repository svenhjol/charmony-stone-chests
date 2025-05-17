package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import svenhjol.charmony.api.chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.api.secret_chests.SecretChestPuzzleMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;

import java.util.List;

public class GenericItemPuzzleProvider extends BaseItemPuzzleMenuProvider implements SecretChestPuzzleMenuProvider {
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
