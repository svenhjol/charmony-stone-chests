package svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.api.stone_chests.StoneChestMaterial;
import svenhjol.charmony.core.common.ContainerMenu;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.PuzzleMenu;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

public class MoonPuzzleMenu extends ContainerMenu implements PuzzleMenu {
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public MoonPuzzleMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(1), StoneChestMaterial.Stone, 0, ContainerLevelAccess.NULL);
    }

    public MoonPuzzleMenu(int id, Inventory playerInventory, Container container, StoneChestMaterial material, int phase, ContainerLevelAccess access) {
        super(feature().registers.moonPuzzleMenu.get(), id, playerInventory, container);
        this.access = access;
        this.data = new SimpleContainerData(2);
        this.data.set(0, material.getId());
        this.data.set(1, phase);

        this.addStandardInventorySlots(playerInventory, 8, 113);
        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        access.execute((level, pos) -> {
            if (level instanceof ServerLevel serverLevel && serverLevel.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                if (level.getMoonPhase() == getMoonPhase()) {
                    feature().handlers.doSuccessOpen(container, player, chest);
                } else {
                    feature().handlers.doFailOpen(player, chest);
                }
            }
        });
        return true;
    }

    public StoneChestMaterial getMaterial() {
        var id = this.data.get(0);
        return StoneChestMaterial.byId(id);
    }

    public int getMoonPhase() {
        return this.data.get(1);
    }

    private static ChestPuzzles feature() {
        return ChestPuzzles.feature();
    }
}
