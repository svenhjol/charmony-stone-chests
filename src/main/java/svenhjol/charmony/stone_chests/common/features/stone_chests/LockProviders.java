package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.StoneChestItemPuzzleInputProvider;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestLockProvider;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.TagHelper;

import java.util.*;

public class LockProviders extends Setup<StoneChests> implements StoneChestLockProvider, StoneChestItemPuzzleInputProvider {
    public LockProviders(StoneChests feature) {
        super(feature);
        Api.registerProvider(this);
    }

    /**
     * Provider for the "Item Puzzle" lock menu.
     */
    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var random = new Random(menuData.seed);
        var serverLevel = menuData.level;
        var syncId = menuData.syncId;
        var inventory = menuData.playerInventory;
        var data = menuData.data;
        var access = ContainerLevelAccess.create(serverLevel, menuData.pos);

        Map<TagKey<Item>, Integer> inputs = new HashMap<>();

        for (var itemPuzzleInputs : feature().registers.itemPuzzleInputs) {
            inputs.putAll(itemPuzzleInputs.getItemTagsAndAmounts());
        }

        if (inputs.isEmpty()) {
            return Optional.empty();
        }

        var keys = new ArrayList<>(inputs.keySet());
        Map<TagKey<Item>, List<Item>> cached = new WeakHashMap<>();

        List<ItemStack> puzzle = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            var key = keys.get(random.nextInt(keys.size()));
            var max = inputs.get(key);

            if (!cached.containsKey(key)) {
                cached.put(key, new ArrayList<>(TagHelper.getValues(serverLevel.registryAccess().lookupOrThrow(Registries.ITEM), key)));
            } else {
                feature().log().debug("Using cached item list for " + key);
            }

            var items = new ArrayList<>(cached.get(key));
            Collections.shuffle(items, random);
            puzzle.add(new ItemStack(items.getFirst(), random.nextInt(max) + 1));
        }

        return Optional.of(new ItemPuzzleMenu(syncId, inventory, new SimpleContainer(6), data, puzzle, access));
    }

    /**
     * Add some default item and amount requirements to the item puzzle.
     */
    @Override
    public Map<TagKey<Item>, Integer> getItemTagsAndAmounts() {
        return Map.of(
            Tags.PUZZLE_MATERIALS, 2,
            Tags.PUZZLE_SHERDS, 4
        );
    }
}
