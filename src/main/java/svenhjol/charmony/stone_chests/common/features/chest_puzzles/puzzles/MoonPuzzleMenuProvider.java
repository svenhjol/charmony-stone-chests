package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuData;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.MoonPuzzleMenu;

import java.util.Optional;

public class MoonPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "moon_puzzle";

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var serverLevel = menuData.level;
        var pos = menuData.pos;
        var random = menuData.random;
        var syncId = menuData.syncId;
        var inventory = menuData.playerInventory;
        var material = menuData.material;
        var access = ContainerLevelAccess.create(serverLevel,pos);

        // Generate a random moon phase
        var phase = random.nextInt(0, 8);

        return Optional.of(new MoonPuzzleMenu(syncId, inventory, new SimpleContainer(1), material, phase, access));
    }
}
