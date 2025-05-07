package svenhjol.charmony.stone_chests.common.features.secret_chests;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public final class Tags {
    public static final TagKey<Block> FLOWER_RING_FOR_BEDROCK = TagKey.create(Registries.BLOCK,
        StoneChestsMod.id("flower_ring/bedrock"));

    public static final TagKey<Block> FLOWER_RING_FOR_DEEPSLATE = TagKey.create(Registries.BLOCK,
        StoneChestsMod.id("flower_ring/deepslate"));

    public static final TagKey<Block> FLOWER_RING_FOR_SURFACE = TagKey.create(Registries.BLOCK,
        StoneChestsMod.id("flower_ring/surface"));

    public static final TagKey<Block> GENERATES_BASTION_CHESTS = TagKey.create(Registries.BLOCK,
        StoneChestsMod.id("generates_bastion_chests"));

    public static final TagKey<Block> GENERATES_END_CITY_CHESTS = TagKey.create(Registries.BLOCK,
        StoneChestsMod.id("generates_end_city_chests"));

    public static final TagKey<Block> GENERATES_FLOWER_RINGS = TagKey.create(Registries.BLOCK,
        StoneChestsMod.id("generates_flower_rings"));

    public static final TagKey<Block> GENERATES_FORTRESS_CHESTS = TagKey.create(Registries.BLOCK,
        StoneChestsMod.id("generates_fortress_chests"));

    public static final ResourceKey<LootTable> LOOT_BOOKS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/books"));

    public static final ResourceKey<LootTable> LOOT_DIAMONDS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/diamonds"));

    public static final ResourceKey<LootTable> LOOT_EMERALDS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/emeralds"));

    public static final ResourceKey<LootTable> LOOT_END1 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/end1"));

    public static final ResourceKey<LootTable> LOOT_END2 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/end2"));

    public static final ResourceKey<LootTable> LOOT_ILLAGERS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/illagers"));

    public static final ResourceKey<LootTable> LOOT_GOLD = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/gold"));

    public static final ResourceKey<LootTable> LOOT_NETHER1 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/nether1"));

    public static final ResourceKey<LootTable> LOOT_NETHER2 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/nether2"));

    public static final ResourceKey<LootTable> LOOT_ORES = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/ores"));

    public static final ResourceKey<LootTable> LOOT_POTIONS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/potions"));

    public static final ResourceKey<LootTable> LOOT_TREASURE1 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/treasure1"));

    public static final ResourceKey<LootTable> LOOT_TREASURE2 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/treasure2"));
}
