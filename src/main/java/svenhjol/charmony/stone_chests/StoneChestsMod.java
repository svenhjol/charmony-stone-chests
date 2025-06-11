package svenhjol.charmony.stone_chests;

import svenhjol.charmony.api.core.ModDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;

@ModDefinition(
    id = StoneChestsMod.ID,
    sides = {Side.Client, Side.Common},
    name = "Stone Chests",
    description = """
        Adds craftable stone chests and secret chests found around the world.
        Secret chests can be looted after solving a puzzle.""")
public final class StoneChestsMod extends Mod {
    public static final String ID = "charmony-stone-chests";
    private static StoneChestsMod instance;

    private StoneChestsMod() {}

    public static StoneChestsMod instance() {
        if (instance == null) {
            instance = new StoneChestsMod();
        }
        return instance;
    }
}