package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.puzzles;

import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.ItemPuzzleMenu;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.StoneChestPuzzles;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.Tags;

import java.util.List;
import java.util.Optional;

public class GemPuzzleMenuProvider extends Setup<StoneChestPuzzles> implements StoneChestLockMenuProvider {
    public static final String ID = "gem_item_puzzle";

    public GemPuzzleMenuProvider(StoneChestPuzzles feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var requirements = List.of(
            new ItemPuzzleRequirement(Tags.PUZZLE_GEMS, 1, 3)
        );

        return ItemPuzzleMenu.getMenuProvider(menuData, requirements);
    }
}