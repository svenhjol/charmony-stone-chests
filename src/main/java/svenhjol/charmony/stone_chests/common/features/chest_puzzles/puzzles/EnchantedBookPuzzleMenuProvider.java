package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemMenuPuzzle;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Tags;

import java.util.List;
import java.util.Optional;

public class EnchantedBookPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "enchanted_book_puzzle_menu_provider";

    public static final List<ResourceKey<Enchantment>> BLACKLIST = List.of(
        Enchantments.VANISHING_CURSE,
        Enchantments.BINDING_CURSE
    );

    public EnchantedBookPuzzleMenuProvider() {
        Api.registerProvider(this);
    }

    @Override
    public String getMenuProviderId() {
        return ID;
    }

    @Override
    public Optional<AbstractContainerMenu> getMenuProvider(StoneChestLockMenuData menuData) {
        var amplifier = menuData.difficultyAmplifier;
        var random = RandomSource.create(menuData.seed);
        var registry = menuData.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var opt = registry.getRandomElementOf(Tags.PUZZLE_ENCHANTMENTS_FOR_BOOKS, random);
        if (opt.isEmpty()) return Optional.empty();

        var holder = opt.get();
        var enchantment = holder.value();
        var min = Math.max(enchantment.getMinLevel(), amplifier);
        var max = Math.min(enchantment.getMaxLevel(), amplifier);
        var level = Mth.nextInt(random, min, max);
        var stack = EnchantmentHelper.createBook(new EnchantmentInstance(holder, level));

        return ItemMenuPuzzle.getMenuProvider(menuData, List.of(stack));
    }
}
