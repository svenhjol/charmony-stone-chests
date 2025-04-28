package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.common.ContainerMenu;

import java.util.ArrayList;
import java.util.List;

public class ItemPuzzleMenu extends ContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public ItemPuzzleMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory,
            new SimpleContainer(6), // 3x puzzle items + 3x supplied items
            new SimpleContainerData(1), // Chest material
            NonNullList.withSize(3, ItemStack.EMPTY), // Puzzle items
            ContainerLevelAccess.NULL);
    }

    public ItemPuzzleMenu(int syncId, Inventory playerInventory, Container container, ContainerData data, List<ItemStack> items, ContainerLevelAccess access) {
        super(StoneChests.feature().registers.itemPuzzleMenu.get(), syncId, playerInventory, container);
        this.container = container;
        this.data = data;
        this.access = access;

        // Populate the fake slots.
        if (items.size() != 3) {
            throw new RuntimeException("Must supply exactly three items to the puzzle menu");
        }

        this.addSlot(new PuzzleSlot(container, 0, 26, 27)).set(items.get(0));
        this.addSlot(new PuzzleSlot(container, 1, 80, 27)).set(items.get(1));
        this.addSlot(new PuzzleSlot(container, 2, 134, 27)).set(items.get(2));

        this.addSlot(new Slot(container, 3, 26, 47));
        this.addSlot(new Slot(container, 4, 80, 47));
        this.addSlot(new Slot(container, 5, 134, 47));

        this.addStandardInventorySlots(playerInventory, 8, 113);
        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int id) {
        var selected = ItemStack.EMPTY;
        var slot = slots.get(id);
        if (slot.hasItem()) {
            var slotItem = slot.getItem();
            selected = slotItem.copy();

            // Move from the custom slots to the player inventory.
            if (id >= 3 && id <= 5) {
                if (!moveItemStackTo(selected, 6, 42, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(selected, 3, 6, false)) {
                return ItemStack.EMPTY;
            }

            if (selected.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(player, selected);
        }

        return selected;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        access.execute((level, pos) -> {
            List<ItemStack> requires = new ArrayList<>();
            List<ItemStack> supplied = new ArrayList<>();
            int valid = 0;

            for (var i = 0; i < 3; i++) {
                requires.add(container.getItem(i));
            }
            for (var i = 3; i < 6; i++) {
                supplied.add(container.getItem(i));
            }

            for (var i = 0; i < supplied.size(); i++) {
                var item = supplied.get(i);
                var req = requires.get(i);

                if (ItemStack.isSameItem(item, req)) {
                    ++valid;
                }
            }

            if (level instanceof ServerLevel serverLevel && serverLevel.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                if (valid == 3) {
                    // Success - consume the items
                    container.clearContent();
                    chest.unlock();
                    player.openMenu(chest);
                } else {
                    // Fail - return the items to the player
                    player.containerMenu.removed(player);
                    serverLevel.destroyBlock(pos, true);
                }
            }
        });
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        access.execute((level, pos) -> {
            var returnContainer = new SimpleContainer(3);
            for (var i = 0; i < 3; i++) {
                returnContainer.setItem(i, container.getItem(i + 3));
            }
            clearContainer(player, returnContainer);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return !slots.isEmpty() && container.stillValid(player);
    }

    public StoneChestMaterial getMaterial() {
        var id = this.data.get(0);
        return StoneChestMaterial.byId(id);
    }
}
