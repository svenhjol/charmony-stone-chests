package svenhjol.charmony.api;

import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;

/**
 * Add a custom menu that can be used when the player attempts to open a locked stone chest.
 */
public interface StoneChestLockProvider {
    Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData);
}
