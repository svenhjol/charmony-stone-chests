package svenhjol.charmony.stone_chests.common.features.secret_chests;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public final class Tags {
    public static final ResourceKey<LootTable> BOOKS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/books"));

    public static final ResourceKey<LootTable> DIAMONDS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/diamonds"));

    public static final ResourceKey<LootTable> EMERALDS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/emeralds"));

    public static final ResourceKey<LootTable> END1 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/end1"));

    public static final ResourceKey<LootTable> END2 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/end2"));

    public static final ResourceKey<LootTable> ILLAGERS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/illagers"));

    public static final ResourceKey<LootTable> GOLD = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/gold"));

    public static final ResourceKey<LootTable> NETHER1 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/nether1"));

    public static final ResourceKey<LootTable> NETHER2 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/nether2"));

    public static final ResourceKey<LootTable> ORES = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/ores"));

    public static final ResourceKey<LootTable> POTIONS = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/potions"));

    public static final ResourceKey<LootTable> TREASURE1 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/treasure1"));

    public static final ResourceKey<LootTable> TREASURE2 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/treasure2"));
}
