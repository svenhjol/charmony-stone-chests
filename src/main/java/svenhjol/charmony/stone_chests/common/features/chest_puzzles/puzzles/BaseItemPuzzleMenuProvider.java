package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.Util;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charmony.api.ItemPuzzleRequirement;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
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
