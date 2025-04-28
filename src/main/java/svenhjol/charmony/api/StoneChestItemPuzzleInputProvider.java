package svenhjol.charmony.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;

/**
 * Tags and amounts for the "item puzzle" provided by the stone chest mod.
 * Use this to add more tags and amounts from custom mods to extend the items requested by the item puzzle.
 */
public interface StoneChestItemPuzzleInputProvider {
    Map<TagKey<Item>, Integer> getItemTagsAndAmounts();
}
