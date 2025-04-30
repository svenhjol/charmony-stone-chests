package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.puzzles.GemPuzzleMenuProvider;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.puzzles.SherdPuzzleMenuProvider;

@FeatureDefinition(side = Side.Common, description = """
    Locked stone chests provide a puzzle to be solved before they can be unlocked.""")
public final class StoneChestPuzzles extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;

    public final SherdPuzzleMenuProvider sherdPuzzle;
    public final GemPuzzleMenuProvider gemPuzzle;

    public StoneChestPuzzles(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);

        sherdPuzzle = new SherdPuzzleMenuProvider(this);
        gemPuzzle = new GemPuzzleMenuProvider(this);
    }

    public static StoneChestPuzzles feature() {
        return Mod.getSidedFeature(StoneChestPuzzles.class);
    }
}
