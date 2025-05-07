package svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus;

import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import svenhjol.charmony.api.ItemPuzzleRequirement;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.common.ContainerMenu;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Constants;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ItemPuzzleSlot;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.PuzzleMenu;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

import java.util.*;

public class ItemPuzzleMenu extends ContainerMenu implements PuzzleMenu {
    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final int numSlots;
    private final int numItems;

    public ItemPuzzleMenu(int syncId, Inventory playerInventory, int slots) {
        this(syncId, playerInventory, new SimpleContainer(slots * 2), StoneChestMaterial.Stone, NonNullList.withSize(slots, ItemStack.EMPTY), ContainerLevelAccess.NULL);
    }

    public ItemPuzzleMenu(int syncId, Inventory playerInventory, Container container, StoneChestMaterial material, List<ItemStack> items, ContainerLevelAccess access) {
        super(feature().registers.itemPuzzleMenus.get(items.size()).get(), syncId, playerInventory, container);
        this.container = container;
        this.access = access;
        this.numSlots = container.getContainerSize();
        this.numItems = items.size();
        this.data = new SimpleContainerData(1);
        data.set(0, material.getId());

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

    public static Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData, ItemPuzzleRequirement requirement) {
        var serverLevel = menuData.level;
        var amplifier = menuData.difficultyAmplifier;
        var seed = menuData.seed;
        var slots = Mth.clamp(requirement.slots() * amplifier, 1, Constants.MAX_ITEM_SLOTS);
        List<ItemStack> puzzleItems = new ArrayList<>();


        for (int i = 0; i < slots; i++) {
            seed += (i * 15000L);
            var items = feature().handlers.getItemsFromLootTable(serverLevel, requirement.lootTable(), seed);
            // Bail out if there are no resolved items from the loot table.
            if (items.isEmpty()) return Optional.empty();

            var stack = items.getFirst();
            var count = stack.getCount();
            stack.setCount(Math.min(stack.getMaxStackSize(), count * amplifier));
            puzzleItems.add(stack);
        }

        return getMenuProvider(menuData, puzzleItems);
    }

    public static Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData, List<ItemStack> items) {
        var serverLevel = menuData.level;
        var syncId = menuData.syncId;
        var inventory = menuData.playerInventory;
        var material = menuData.material;
        var access = ContainerLevelAccess.create(serverLevel, menuData.pos);

        return Optional.of(new ItemPuzzleMenu(syncId, inventory, new SimpleContainer(items.size() * 2), material, items, access));
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
            int validItems = 0;

            for (var i = 0; i < numItems; i++) {
                requires.add(container.getItem(i));
            }
            for (var i = numItems; i < numSlots; i++) {
                supplied.add(container.getItem(i));
            }

            for (var i = 0; i < supplied.size(); i++) {
                var item = supplied.get(i);
                var req = requires.get(i);

                if (ItemStack.isSameItem(item, req)) {
                    ItemEnchantments reqEnchants;
                    ItemEnchantments itemEnchants;

                    if (req.is(Items.ENCHANTED_BOOK) && item.is(Items.ENCHANTED_BOOK)) {
                        // Books store their enchantments in a different component.
                        reqEnchants = req.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
                        itemEnchants = item.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
                    } else {
                        itemEnchants = item.getEnchantments();
                        reqEnchants = req.getEnchantments();
                    }

                    // Check enchantments match the requirements.
                    int validEnchants = 0;

                    Map<Holder<Enchantment>, Integer> reqMap = new HashMap<>();
                    for (var entry : reqEnchants.entrySet()) {
                        reqMap.put(entry.getKey(), entry.getIntValue());
                    }

                    for (var entry : itemEnchants.entrySet()) {
                        var lev = entry.getIntValue();
                        var enchant = entry.getKey();

                        if (!reqMap.containsKey(enchant)) continue;
                        if (reqMap.get(enchant) != lev) continue;
                        ++validEnchants;
                    }

                    if (validEnchants != reqMap.size()) continue;
                    ++validItems;
                }
            }

            if (level instanceof ServerLevel serverLevel && serverLevel.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                if (validItems == numItems) {
                    feature().handlers.doSuccessOpen(container, player, chest);
                } else {
                    feature().handlers.doFailOpen(player, chest);
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
