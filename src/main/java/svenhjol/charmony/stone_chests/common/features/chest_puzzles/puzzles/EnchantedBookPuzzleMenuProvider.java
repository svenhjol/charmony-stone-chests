package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuData;
import svenhjol.charmony.api.stone_chests.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Tags;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemPuzzleMenu;

import java.util.List;
import java.util.Optional;

public class EnchantedBookPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "enchanted_book_puzzle";

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var amplifier = menuData.difficultyAmplifier;
        var random = menuData.random;
        var registry = menuData.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var opt = registry.getRandomElementOf(Tags.ENCHANTMENTS_FOR_BOOKS, random);
        if (opt.isEmpty()) return Optional.empty();

        var holder = opt.get();
        var enchantment = holder.value();

        int level;
        if (enchantment.getMaxLevel() > 1) {
            level = Math.min(enchantment.getMaxLevel(), enchantment.getMinLevel() * amplifier);
        } else {
            level = 1;
        }
        var stack = EnchantmentHelper.createBook(new EnchantmentInstance(holder, level));

        return ItemPuzzleMenu.getMenuProvider(menuData, List.of(stack));
    }
}
