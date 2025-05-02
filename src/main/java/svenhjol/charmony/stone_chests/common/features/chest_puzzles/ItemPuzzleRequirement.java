package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public record ItemPuzzleRequirement(
    TagKey<Item> item,
    int minCount,
    int maxCount
) {
}
