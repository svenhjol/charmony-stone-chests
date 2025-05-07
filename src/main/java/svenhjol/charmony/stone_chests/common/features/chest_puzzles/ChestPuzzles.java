package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    Locked stone chests provide a puzzle to be solved before they can be unlocked.""")
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public final class ChestPuzzles extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final MenuProviders menuProviders;
    public final ItemRequirementProviders itemRequirementProviders;

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

    public ChestPuzzles(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
        menuProviders = new MenuProviders(this);
        itemRequirementProviders = new ItemRequirementProviders(this);
    }

    public static ChestPuzzles feature() {
        return Mod.getSidedFeature(ChestPuzzles.class);
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
