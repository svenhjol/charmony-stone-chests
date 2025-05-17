package svenhjol.charmony.api.chest_puzzles;

import svenhjol.charmony.api.stone_chests.StoneChestMaterial;

public interface ChestPuzzleMenu {
    StoneChestMaterial getMaterial();

    ChestPuzzleType puzzleType();
}
