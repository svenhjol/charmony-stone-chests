package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.common.ContainerMenu;

public class UnlockedMenu extends ContainerMenu {
    private final Container container;
    private final ContainerData data;

    protected UnlockedMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(ChestBlockEntity.SLOTS), new SimpleContainerData(1));
    }

    protected UnlockedMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(StoneChests.feature().registers.unlockedMenu.get(), syncId, playerInventory, container);
        this.container = container;
        this.data = data;

        this.addDataSlots(data);
        this.addChestGrid(8, 18);
        this.addStandardInventorySlots(playerInventory, 8, 86);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var stack = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot.hasItem()) {
            var stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            if (index < container.getContainerSize()) {
                if (!moveItemStackTo(stackInSlot, container.getContainerSize(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stackInSlot, 0, container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public void addChestGrid(int x, int y) {
        var i = 0;
        for (int k = 0; k < ChestBlockEntity.ROWS; k++) {
            for (int l = 0; l < ChestBlockEntity.COLUMNS; l++) {
                var id = i++;
                var xx = x + (l * 18);
                var yy = y + (k * 18);
                this.addSlot(new Slot(container, id, xx, yy));
            }
        }
    }

    public StoneChestMaterial getMaterial() {
        var id = this.data.get(0);
        return StoneChestMaterial.byId(id);
    }

    public Container getContainer() {
        return container;
    }
}
