package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemPuzzleMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "item_puzzle";

    public ItemPuzzleMenuProvider() {
        Api.registerProvider(this);
    }

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var tags = ChestPuzzles.feature().registers.itemPuzzleTags;
        if (tags.isEmpty()) return Optional.empty();

        List<ItemPuzzleRequirement> requirements = new ArrayList<>();

        for (var tag : tags) {
            requirements.add(new ItemPuzzleRequirement(tag, 1, 2));
        }

        return ItemPuzzleMenu.getMenuProvider(menuData, requirements, menuData.difficultyAmplifier);
    }
}
