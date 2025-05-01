package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import net.minecraft.util.StringRepresentable;

public enum BreakBehavior implements StringRepresentable {
    NOTHING("nothing"),
    SPAWN_OVERWORLD_MONSTER("spawn_overworld_monster"),
    EXPLODE("explode"),
    GIVE_BAD_EFFECT("give_bad_effect");

    private final String name;

    BreakBehavior(String name) {
        this.name = name;
    }

    public static BreakBehavior getOrDefault(String name) {
        BreakBehavior behavior;
        try {
            behavior = BreakBehavior.valueOf(name);
        } catch (IllegalArgumentException e) {
            behavior = NOTHING;
        }
        return behavior;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
