package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public final class Tags {
    public static final TagKey<EntityType<?>> END_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        StoneChestsMod.id("puzzle/end_monsters"));

    public static final TagKey<EntityType<?>> NETHER_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        StoneChestsMod.id("puzzle/nether_monsters"));

    public static final TagKey<EntityType<?>> OVERWORLD_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        StoneChestsMod.id("puzzle/overworld_monsters"));

    public static final TagKey<Item> PUZZLE_BUTTONS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/buttons"));

    public static final TagKey<Item> PUZZLE_CANDLES = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/candles"));

    public static final TagKey<Item> PUZZLE_ENCHANTABLE_ITEMS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/enchantable_items"));

    public static final TagKey<Item> PUZZLE_ENDER = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/ender"));

    public static final TagKey<Item> PUZZLE_GEMS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/gems"));

    public static final TagKey<Item> PUZZLE_GLAZED_TERRACOTTA = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/glazed_terracotta"));

    public static final TagKey<Item> PUZZLE_INGOTS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/ingots"));

    public static final TagKey<Item> PUZZLE_LOGS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/logs"));

    public static final TagKey<Item> PUZZLE_NETHER = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/nether"));

    public static final TagKey<Item> PUZZLE_SHERDS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/sherds"));

    public static final TagKey<Item> PUZZLE_STONES = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/stones"));

    public static final TagKey<Item> PUZZLE_TERRACOTTA = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/terracotta"));

    public static final TagKey<Item> PUZZLE_WOOL = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/wool"));

    public static final TagKey<Enchantment> PUZZLE_ENCHANTMENTS_FOR_BOOKS = TagKey.create(Registries.ENCHANTMENT,
        StoneChestsMod.id("puzzle/on_books"));

    public static final TagKey<Enchantment> PUZZLE_ENCHANTMENTS_FOR_ITEMS = TagKey.create(Registries.ENCHANTMENT,
        StoneChestsMod.id("puzzle/on_items"));
}
