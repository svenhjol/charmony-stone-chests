package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.StoneChestPuzzleMenuData;
import svenhjol.charmony.api.StoneChestPuzzleProvider;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.TagHelper;

import java.util.*;

public class PuzzleProviders extends Setup<StoneChests> implements StoneChestPuzzleProvider {
    public PuzzleProviders(StoneChests feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public AbstractContainerMenu getMenuProvider(StoneChestPuzzleMenuData menuData) {
        var random = new Random(menuData.seed);
        var serverLevel = menuData.level;
        var syncId = menuData.syncId;
        var inventory = menuData.playerInventory;
        var data = menuData.data;
        var access = ContainerLevelAccess.create(serverLevel, menuData.pos);

        var tags = Map.of(
            Tags.PUZZLE_MATERIALS, 2,
            Tags.PUZZLE_SHERDS, 4
        );
        var keys = new ArrayList<>(tags.keySet());
        Map<TagKey<Item>, List<Item>> cached = new WeakHashMap<>();

        List<ItemStack> puzzle = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            var key = keys.get(random.nextInt(keys.size()));
            var max = tags.get(key);

            if (!cached.containsKey(key)) {
                cached.put(key, new ArrayList<>(TagHelper.getValues(serverLevel.registryAccess().lookupOrThrow(Registries.ITEM), key)));
            }

            var items = new ArrayList<>(cached.get(key));
            Collections.shuffle(items, random);
            puzzle.add(new ItemStack(items.getFirst(), random.nextInt(max) + 1));
        }

        return new ItemPuzzleMenu(syncId, inventory, new SimpleContainer(6), data, puzzle, access);
    }
}
