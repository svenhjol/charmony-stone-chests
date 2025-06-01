package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.core.Charmony;

public final class Tags {
    public static final TagKey<Item> ENCHANTABLE_ITEMS = TagKey.create(Registries.ITEM,
        Charmony.id("enchantable_items"));

    public static final TagKey<Enchantment> ENCHANTMENTS_FOR_BOOKS = TagKey.create(Registries.ENCHANTMENT,
        Charmony.id("on_books"));

    public static final TagKey<Enchantment> ENCHANTMENTS_FOR_ITEMS = TagKey.create(Registries.ENCHANTMENT,
        Charmony.id("on_items"));

    public static final TagKey<EntityType<?>> END_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        Charmony.id("puzzle/end_monsters"));

    public static final ResourceKey<LootTable> LOOT_TRASH = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("chest/trash"));

    public static final TagKey<EntityType<?>> NETHER_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        Charmony.id("puzzle/nether_monsters"));

    public static final TagKey<EntityType<?>> OVERWORLD_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        Charmony.id("puzzle/overworld_monsters"));

    public static final ResourceKey<LootTable> PUZZLE_CANDLES = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/candles"));

    public static final ResourceKey<LootTable> PUZZLE_ENDER = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/ender"));

    public static final ResourceKey<LootTable> PUZZLE_GEMS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/gems"));

    public static final ResourceKey<LootTable> PUZZLE_GLAZED_TERRACOTTA = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/glazed_terracotta"));

    public static final ResourceKey<LootTable> PUZZLE_INGOTS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/ingots"));

    public static final ResourceKey<LootTable> PUZZLE_LOGS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/logs"));

    public static final ResourceKey<LootTable> PUZZLE_NETHER = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/nether"));

    public static final ResourceKey<LootTable> PUZZLE_SHERDS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/sherds"));

    public static final ResourceKey<LootTable> PUZZLE_STAINED_GLASS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/stained_glass"));

    public static final ResourceKey<LootTable> PUZZLE_STONES = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/stones"));

    public static final ResourceKey<LootTable> PUZZLE_TERRACOTTA = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/terracotta"));

    public static final ResourceKey<LootTable> PUZZLE_WOOL = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("puzzle/wool"));
}
