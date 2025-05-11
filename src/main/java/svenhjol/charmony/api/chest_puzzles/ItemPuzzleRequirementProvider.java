package svenhjol.charmony.api.chest_puzzles;

import java.util.List;

/**
 * Get custom requirements that should be used when assembling a generic lootTable puzzle.
 */
public interface ItemPuzzleRequirementProvider {
    List<ItemPuzzleRequirement> getItemPuzzleTags();
}
