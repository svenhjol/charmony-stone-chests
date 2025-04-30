package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
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
        super(feature().registers.itemPuzzleMenu.get(), syncId, playerInventory, container);
        this.container = container;
        this.data = data;
        this.access = access;

        // Populate the fake slots.
        if (items.size() != 3) {
            throw new RuntimeException("Must supply exactly three items to the puzzle menu");
        }

        this.addSlot(new ItemPuzzleSlot(container, 0, 26, 27)).set(items.get(0));
        this.addSlot(new ItemPuzzleSlot(container, 1, 80, 27)).set(items.get(1));
        this.addSlot(new ItemPuzzleSlot(container, 2, 134, 27)).set(items.get(2));

        this.addSlot(new Slot(container, 3, 26, 47));
        this.addSlot(new Slot(container, 4, 80, 47));
        this.addSlot(new Slot(container, 5, 134, 47));

        this.addStandardInventorySlots(playerInventory, 8, 113);
        this.addDataSlots(data);
    }

    public static Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData, List<ItemPuzzleRequirement> requirements) {
        var random = new Random(menuData.seed);
        var serverLevel = menuData.level;
        var syncId = menuData.syncId;
        var inventory = menuData.playerInventory;
        var data = menuData.data;
        var access = ContainerLevelAccess.create(serverLevel, menuData.pos);

        if (requirements.isEmpty()) {
            return Optional.empty();
        }

        Map<TagKey<Item>, List<Item>> cached = new WeakHashMap<>();

        List<ItemStack> puzzle = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            var requirement = requirements.get(random.nextInt(requirements.size()));

            var item = requirement.item();
            var count = requirement.minCount() + random.nextInt(requirement.maxCount() - requirement.minCount());

            if (!cached.containsKey(item)) {
                cached.put(item, new ArrayList<>(TagHelper.getValues(serverLevel.registryAccess().lookupOrThrow(Registries.ITEM), item)));
            } else {
                feature().log().debug("Using cached item list for " + item);
            }

            var items = new ArrayList<>(cached.get(item));
            Collections.shuffle(items, random);
            puzzle.add(new ItemStack(items.getFirst(), count));
        }

        return Optional.of(new ItemPuzzleMenu(syncId, inventory, new SimpleContainer(6), data, puzzle, access));
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

                    // Get the stored "unlocked" loot table from the chest and set it as the primary loot table.
                    // When the chest is next opened the loot will be generated.
                    var unlockedLootTable = chest.getUnlockedLootTable();
                    chest.setLootTable(unlockedLootTable);
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
    protected void clearContainer(Player player, Container container) {
        if (container instanceof ChestBlockEntity chest && chest.isLocked()) {
            chest.setLootTable(Tags.NO_LOOT);
            feature().handlers.doBreakBehavior(player, player.level(), chest.getBlockPos(), chest);
        }
        super.clearContainer(player, container);
    }

    @Override
    public boolean stillValid(Player player) {
        return !slots.isEmpty() && container.stillValid(player);
    }

    public StoneChestMaterial getMaterial() {
        var id = this.data.get(0);
        return StoneChestMaterial.byId(id);
    }

    private static StoneChestPuzzles feature() {
        return StoneChestPuzzles.feature();
    }
}
