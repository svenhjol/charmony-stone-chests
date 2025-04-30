package svenhjol.charmony.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public interface BuriedStoneChestDefinition extends StringRepresentable {
    /**
     * Name of this definition.
     * This will be used as a map key for this definition.
     */
    String name();

    /**
     * The chest block to use when placing the chest.
     * This must be a block that belongs to the stone chest block entity!
     */
    Block block();

    /**
     * Min and max depth that the chest can be buried.
     */
    Pair<Integer, Integer> depth();

    /**
     * Loot tables to use for the chest contents.
     * If more than one is specified, one is randomly chosen from the list.
     */
    List<ResourceKey<LootTable>> lootTables();

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
