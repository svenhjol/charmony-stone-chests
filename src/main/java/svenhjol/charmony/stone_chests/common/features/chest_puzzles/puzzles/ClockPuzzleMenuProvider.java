package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerLevelAccess;
import svenhjol.charmony.api.secret_chests.SecretChestPuzzleMenuData;
import svenhjol.charmony.api.secret_chests.SecretChestPuzzleMenuProvider;
import svenhjol.charmony.api.chest_puzzles.ChestPuzzleMenu;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ClockPuzzleMenu;

import java.util.Optional;

public class ClockPuzzleMenuProvider implements SecretChestPuzzleMenuProvider {
    public static final String ID = "clock_puzzle";

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<ChestPuzzleMenu> getMenuProvider(SecretChestPuzzleMenuData menuData) {
        var serverLevel = menuData.level;
        var pos = menuData.pos;
        var random = menuData.random;
        var syncId = menuData.syncId;
        var inventory = menuData.playerInventory;
        var material = menuData.material;
        var access = ContainerLevelAccess.create(serverLevel,pos);

        // Generate a random time
        var time = random.nextInt(0, 64);

        return Optional.of(new ClockPuzzleMenu(syncId, inventory, new SimpleContainer(1), material, time, access));
    }
}
