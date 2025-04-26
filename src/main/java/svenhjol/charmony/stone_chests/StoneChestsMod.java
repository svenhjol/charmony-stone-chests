package svenhjol.charmony.stone_chests;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.core.annotations.ModDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.enums.Side;

@ModDefinition(
    id = StoneChestsMod.ID,
    sides = {Side.Client, Side.Common},
    name = "Stone Chests",
    description = "Stone chests.")
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

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}