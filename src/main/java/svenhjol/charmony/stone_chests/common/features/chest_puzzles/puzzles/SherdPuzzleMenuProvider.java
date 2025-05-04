package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.DynamicItemPuzzleMenu;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Tags;

import java.util.List;
import java.util.Optional;

public class SherdPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "sherd_item_puzzle";

    public SherdPuzzleMenuProvider() {
        Api.registerProvider(this);
    }

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var requirements = List.of(
            new ItemPuzzleRequirement(Tags.PUZZLE_SHERDS, 1, 4)
        );

        return DynamicItemPuzzleMenu.getMenuProvider(menuData, requirements, menuData.difficultyAmplifier);
    }
}