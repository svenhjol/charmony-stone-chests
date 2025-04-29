package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.WorldHelper;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

import java.util.Optional;

public class Handlers extends Setup<StoneChestPuzzles> {
    public Handlers(StoneChestPuzzles feature) {
        super(feature);
    }

    public Optional<AbstractContainerMenu> getMenuProvider(ServerLevel level, ChestBlockEntity chest, int syncId, Inventory inventory, ContainerData data) {
        var lockMenu = chest.lockMenu();
        var pos = chest.getBlockPos();

        var providers = feature().registers.lockMenuProviders;
        if (!providers.containsKey(lockMenu)) {
            // No lock menu provider, just unlock the chest.
            feature().log().debug("Provider not found, unlocking: " + lockMenu);
            chest.unlock();
        }

        if (chest.isLocked()) {
            // Generate a seed based on this position.
            var seed = WorldHelper.seedFromBlockPos(pos);
            var provider = providers.get(lockMenu);
            var menuData = new StoneChestLockMenuData();

            menuData.syncId = syncId;
            menuData.playerInventory = inventory;
            menuData.level = level;
            menuData.pos = pos;
            menuData.data = data;
            menuData.seed = seed;

            var menu = provider.getMenuProvider(menuData);
            if (menu.isPresent()) {
                return menu;
            } else {
                chest.unlock();
            }
        }

        return Optional.empty();
    }
}
