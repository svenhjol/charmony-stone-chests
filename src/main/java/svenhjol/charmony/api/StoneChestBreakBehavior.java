package svenhjol.charmony.api;

import net.minecraft.util.StringRepresentable;

public enum StoneChestBreakBehavior implements StringRepresentable {
    NOTHING("nothing"),
    SPAWN_OVERWORLD_MONSTERS("spawn_overworld_monsters"),
    SPAWN_NETHER_MONSTERS("spawn_nether_monsters"),
    SPAWN_END_MONSTERS("spawn_end_monsters"),
    EXPLODE("explode"),
    GIVE_BAD_EFFECT("give_bad_effect");

    private final String name;

    StoneChestBreakBehavior(String name) {
        this.name = name;
    }

    public static StoneChestBreakBehavior getOrDefault(String name) {
        StoneChestBreakBehavior behavior;
        try {
            behavior = StoneChestBreakBehavior.valueOf(name);
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
