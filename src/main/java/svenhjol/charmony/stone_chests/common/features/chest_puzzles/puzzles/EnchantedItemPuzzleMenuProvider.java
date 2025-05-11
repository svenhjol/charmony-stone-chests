package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuData;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Tags;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemPuzzleMenu;

import java.util.List;
import java.util.Optional;

public class EnchantedItemPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "enchanted_item_puzzle";

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var amplifier = menuData.difficultyAmplifier;
        var random = menuData.random;
        var enchantmentRegistry = menuData.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        var itemRegistry = menuData.level.registryAccess().lookupOrThrow(Registries.ITEM);
        var itemOpt = itemRegistry.getRandomElementOf(Tags.ENCHANTABLE_ITEMS, random);
        if (itemOpt.isEmpty()) return Optional.empty();

        var level = 5 + (5 * amplifier);
        var stack = EnchantmentHelper.enchantItem(
            random,
            new ItemStack(itemOpt.get()),
            level,
            menuData.level.registryAccess(),
            enchantmentRegistry.get(Tags.ENCHANTMENTS_FOR_ITEMS)
        );

        return ItemPuzzleMenu.getMenuProvider(menuData, List.of(stack));
    }
}
