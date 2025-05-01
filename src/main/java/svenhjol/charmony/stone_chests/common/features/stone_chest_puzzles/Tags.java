package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public final class Tags {
    public static final TagKey<Item> PUZZLE_GEMS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/gems"));

    public static final TagKey<Item> PUZZLE_SHERDS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle/sherds"));

    public static final ResourceKey<LootTable> NO_LOOT = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("puzzle/nothing"));

    public static final ResourceKey<LootTable> SIMPLE_LOOT = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("puzzle/simple"));

    public static final TagKey<EntityType<?>> OVERWORLD_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        StoneChestsMod.id("puzzle/overworld_monsters"));

    public static final TagKey<EntityType<?>> NETHER_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        StoneChestsMod.id("puzzle/nether_monsters"));

    public static final TagKey<EntityType<?>> END_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        StoneChestsMod.id("puzzle/end_monsters"));
}
