package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.Util;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charmony.api.chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuData;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemPuzzleMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseItemPuzzleMenuProvider implements StoneChestLockMenuProvider {
    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var requirements = new ArrayList<>(getRequirements());
        if (requirements.isEmpty()) return Optional.empty();

        Util.shuffle(requirements, menuData.random);
        var requirement = requirements.getFirst();

        return ItemPuzzleMenu.getMenuProvider(menuData, requirement);
    }

    protected abstract List<ItemPuzzleRequirement> getRequirements();
}
