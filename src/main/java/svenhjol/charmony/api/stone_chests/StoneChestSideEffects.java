package svenhjol.charmony.api.stone_chests;

import net.minecraft.util.StringRepresentable;

public enum StoneChestSideEffects implements StringRepresentable {
    Nothing("nothing"),
    SpawnOverworldMonsters("spawn_overworld_monsters"),
    SpawnNetherMonsters("spawn_nether_monsters"),
    SpawnEndMonsters("spawn_end_monsters"),
    Explode("explode"),
    GiveBadEffect("give_bad_effect");

    private final String name;

    StoneChestSideEffects(String name) {
        this.name = name;
    }

    public static StoneChestSideEffects getOrDefault(String name) {
        StoneChestSideEffects sideEffect;
        try {
            sideEffect = StoneChestSideEffects.valueOf(name);
        } catch (IllegalArgumentException e) {
            sideEffect = Nothing;
        }
        return sideEffect;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
