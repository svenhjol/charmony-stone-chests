package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.common.ContainerMenu;
import svenhjol.charmony.core.helpers.TagHelper;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

import java.util.*;

public class DynamicItemPuzzleMenu extends ContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final int numSlots;
    private final int numItems;

    public DynamicItemPuzzleMenu(int syncId, Inventory playerInventory, int slots) {
        this(syncId, playerInventory, new SimpleContainer(slots * 2), new SimpleContainerData(1), NonNullList.withSize(slots, ItemStack.EMPTY), ContainerLevelAccess.NULL);
    }

    public DynamicItemPuzzleMenu(int syncId, Inventory playerInventory, Container container, ContainerData data, List<ItemStack> items, ContainerLevelAccess access) {
        super(feature().registers.itemPuzzleMenus.get(items.size()).get(), syncId, playerInventory, container);
        this.container = container;
        this.data = data;
        this.access = access;
        this.numSlots = container.getContainerSize();
        this.numItems = items.size();

        if (numSlots != numItems * 2) {
            throw new RuntimeException("Number of slots must equal double the number of items in the puzzle menu");
        }

        for (var i = 0; i < numItems; i++) {
            var q = 160 / (numItems + 1);
            var x = (i + 1) * q;
            var ox = (1 + x);
            this.addSlot(new ItemPuzzleSlot(container, i, ox, 27)).set(items.get(i));
        }

        for (var i = 0; i < numItems; i++) {
            var q = 160 / (numItems + 1);
            var x = (i + 1) * q;
            var ox = (1 + x);
            this.addSlot(new Slot(container, i + numItems, ox, 47));
        }

        this.addStandardInventorySlots(playerInventory, 8, 113);
        this.addDataSlots(data);
    }

    public static Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData, List<ItemPuzzleRequirement> requirements, int slots) {
        var random = new Random(menuData.seed);
        var serverLevel = menuData.level;
        var syncId = menuData.syncId;
        var inventory = menuData.playerInventory;
        var data = menuData.data;
        var amplifier = menuData.difficultyAmplifier;
        var access = ContainerLevelAccess.create(serverLevel, menuData.pos);
        slots = Mth.clamp(slots, 1, Constants.MAX_ITEM_SLOTS);

        if (requirements.isEmpty()) {
            return Optional.empty();
        }

        Map<TagKey<Item>, List<Item>> cached = new WeakHashMap<>();
        List<ItemStack> puzzleItems = new ArrayList<>();

        for (int i = 0; i < slots; i++) {
            var requirement = requirements.get(random.nextInt(requirements.size()));

            var item = requirement.item();
            var count = requirement.minCount() + random.nextInt(requirement.maxCount() - requirement.minCount());
            var amplifiedCount = count * amplifier;

            if (!cached.containsKey(item)) {
                cached.put(item, new ArrayList<>(TagHelper.getValues(serverLevel.registryAccess().lookupOrThrow(Registries.ITEM), item)));
            } else {
                feature().log().debug("Using cached item list for " + item);
            }

            var items = new ArrayList<>(cached.get(item));
            Collections.shuffle(items, random);
            puzzleItems.add(new ItemStack(items.getFirst(), amplifiedCount));
        }

        return Optional.of(new DynamicItemPuzzleMenu(syncId, inventory, new SimpleContainer(slots * 2), data, puzzleItems, access));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int id) {
        var selected = ItemStack.EMPTY;
        var slot = slots.get(id);
        if (slot.hasItem()) {
            var slotItem = slot.getItem();
            selected = slotItem.copy();

            if (id >= numItems && id < numSlots) {
                // Move from the custom slots to the player inventory.
                if (!moveItemStackTo(selected, numSlots, numSlots + 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(selected, numItems, numSlots, false)) {
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

            for (var i = 0; i < numItems; i++) {
                requires.add(container.getItem(i));
            }
            for (var i = numItems; i < numSlots; i++) {
                supplied.add(container.getItem(i));
            }

            for (var i = 0; i < supplied.size(); i++) {
                var item = supplied.get(i);
                var req = requires.get(i);

                // TODO: handle enchantment checking
                if (ItemStack.isSameItem(item, req)) {
                    ++valid;
                }
            }

            if (level instanceof ServerLevel serverLevel && serverLevel.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                if (valid == numItems) {
                    // Success - consume the items
                    container.clearContent();

                    // Get the stored "unlocked" loot table from the chest and set it as the primary loot table.
                    // When the chest is next opened the loot will be generated.
                    var unlockedLootTable = chest.getUnlockedLootTable();
                    chest.setLootTable(unlockedLootTable);
                    chest.unlock();

                    player.openMenu(chest);
                } else {
                    // Fail - return the items to the player and run the break behavior
                    player.containerMenu.removed(player);
                    chest.unlock();
                    var result = feature().handlers.doBreakBehavior(player, player.level(), chest.getBlockPos(), chest);

                    if (!result) {
                        player.openMenu(chest);
                    }
                }
            }
        });
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        access.execute((level, pos) -> {
            var returnContainer = new SimpleContainer(numItems);
            for (var i = 0; i < numItems; i++) {
                returnContainer.setItem(i, container.getItem(i + numItems));
            }
            clearContainer(player, returnContainer);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return !slots.isEmpty() && container.stillValid(player);
    }

    public int getNumItems() {
        return numItems;
    }

    public StoneChestMaterial getMaterial() {
        var id = this.data.get(0);
        return StoneChestMaterial.byId(id);
    }

    private static ChestPuzzles feature() {
        return ChestPuzzles.feature();
    }
}
