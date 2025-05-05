package svenhjol.charmony.stone_chests.common.features.secret_chests;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public final class Tags {
    public static final ResourceKey<LootTable> ORES = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/ores"));

    public static final ResourceKey<LootTable> TREASURE1 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/treasure1"));

    public static final ResourceKey<LootTable> TREASURE2 = ResourceKey.create(Registries.LOOT_TABLE,
        StoneChestsMod.id("chest/treasure2"));
}
