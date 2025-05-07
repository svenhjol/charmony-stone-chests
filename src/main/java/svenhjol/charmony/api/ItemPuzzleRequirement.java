package svenhjol.charmony.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public record ItemPuzzleRequirement(
    TagKey<Item> item,
    int minCount,
    int maxCount,
    int slots
) {
}
