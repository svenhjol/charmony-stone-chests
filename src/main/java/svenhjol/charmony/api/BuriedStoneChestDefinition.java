package svenhjol.charmony.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.BreakBehavior;

import java.util.List;

public interface BuriedStoneChestDefinition extends StringRepresentable {
    /**
     * Name of this definition.
     * This will be used as a map key for this definition.
     */
    String name();

    /**
     * The chest material to use when placing the chest.
     */
    StoneChestMaterial material();

    /**
     * Min and max depth that the chest can be buried.
     */
    Pair<Integer, Integer> depth();

    /**
     * Loot tables to use for the chest contents when unlocked.
     * If more than one is specified, one is randomly chosen from the list.
     */
    List<ResourceKey<LootTable>> lootTables();

    /**
     * Behavior when the chest is broken.
     * If more than one is specified, one is randomly chosen from the list.
     */
    default List<BreakBehavior> breakBehaviors() {
        return List.of(BreakBehavior.NOTHING);
    }

    /**
     * IDs of the lock menus to use.
     * If empty the lock menu will not be used and the chest will not be locked.
     */
    default List<String> lockMenus() {
        return List.of();
    }

    /**
     * Get the definition name. Used when serializing the definition.
     */
    default String getSerializedName() {
        return name();
    }

    /**
     * If true, the chest will be buried in solid rock if a valid air/water space can't be found.
     * Note that if false the chest might not be able to be placed at the structure coordinates.
     * This failure does not impact gameplay and will show a message in the debug menu.
     */
    default boolean canBeFullyBuried() {
        return true;
    }

    /**
     * The min and max offset that will be used when trying to place a chest if an airspace
     * can't be found at the structure coordinates. The higher this range, the greater the amount
     * of vertical deviation from the min and max depth.
     */
    default Pair<Integer, Integer> fallbackYOffset() {
        return Pair.of(-10, 10);
    }

    /**
     * The max X and Z offset that will be used when trying to place a chest if an airspace
     * can't be found at the structure coordinates. The higher this range, the greater the
     * amount of deviation from the position of the structure.
     */
    default int fallbackXZOffset() {
        return 8;
    }
}
