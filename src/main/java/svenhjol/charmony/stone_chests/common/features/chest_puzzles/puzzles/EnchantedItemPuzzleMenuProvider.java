package svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus.ItemMenuPuzzle;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.Tags;

import java.util.List;
import java.util.Optional;

public class EnchantedItemPuzzleMenuProvider implements StoneChestLockMenuProvider {
    public static final String ID = "enchanted_item_puzzle_menu_provider";

    public static final List<ResourceKey<Enchantment>> BLACKLIST = List.of(
        Enchantments.VANISHING_CURSE,
        Enchantments.BINDING_CURSE
    );

    public EnchantedItemPuzzleMenuProvider() {
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
        var enchantmentRegistry = menuData.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        var itemRegistry = menuData.level.registryAccess().lookupOrThrow(Registries.ITEM);
        var itemOpt = itemRegistry.getRandomElementOf(Tags.PUZZLE_ENCHANTABLE_ITEMS, random);
        if (itemOpt.isEmpty()) return Optional.empty();

        var level = 5 + (5 * amplifier);
        var stack = EnchantmentHelper.enchantItem(
            random,
            new ItemStack(itemOpt.get()),
            level,
            menuData.level.registryAccess(),
            enchantmentRegistry.get(Tags.PUZZLE_ENCHANTMENTS_FOR_ITEMS)
        );

        return ItemMenuPuzzle.getMenuProvider(menuData, List.of(stack));
    }
}
