package svenhjol.charmony.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;

/**
 * Get custom item tags that should be used when assembling a generic item puzzle.
 */
public interface ItemPuzzleTagProvider {
    List<TagKey<Item>> getItemPuzzleTags();
}
