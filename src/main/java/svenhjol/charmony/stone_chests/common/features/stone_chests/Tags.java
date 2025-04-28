package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charmony.stone_chests.StoneChestsMod;

public final class Tags {
    public static final TagKey<Item> PUZZLE_REQUIREMENTS = TagKey.create(Registries.ITEM,
        StoneChestsMod.id("puzzle_requirements"));
}
