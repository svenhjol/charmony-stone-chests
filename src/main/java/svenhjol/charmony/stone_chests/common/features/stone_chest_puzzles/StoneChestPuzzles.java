package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import svenhjol.charmony.core.annotations.Configurable;
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

    @Configurable(
        name = "Bad effect duration",
        description = "Minimum duration (in seconds) of any bad effects given to the player as a result of failing the puzzle."
    )
    private static int badEffectDuration = 60;

    @Configurable(
        name = "Number of mobs spawned",
        description = "Minimum number of mobs spawned as a result of failing the puzzle."
    )
    private static int numberOfMobsSpawned = 2;

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

    /**
     * Get duration in ticks.
     */
    public int badEffectDuration() {
        return badEffectDuration * 20;
    }

    public int numberOfMobsSpawned() {
        return numberOfMobsSpawned;
    }
}
