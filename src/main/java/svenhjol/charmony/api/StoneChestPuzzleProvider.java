package svenhjol.charmony.api;

import net.minecraft.world.inventory.AbstractContainerMenu;

public interface StoneChestPuzzleProvider {
    AbstractContainerMenu getMenuProvider(StoneChestPuzzleMenuData menuData);
}
