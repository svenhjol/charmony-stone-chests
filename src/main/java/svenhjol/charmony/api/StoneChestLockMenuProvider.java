package svenhjol.charmony.api;

import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;

/**
 * Add a custom menu that can be used when the player attempts to open a locked stone chest.
 */
public interface StoneChestLockMenuProvider {
    /**
     * ID of the custom menu.
     * This will be used as a map key for this provider.
     */
    String getMenuProviderId();

    /**
     * Custom menu to open when the chest is interacted with by the player.
     * Return an empty optional if something goes wrong in the processing of the custom menu.
     */
    Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData);
}
