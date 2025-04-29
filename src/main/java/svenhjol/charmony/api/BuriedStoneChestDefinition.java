package svenhjol.charmony.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public interface BuriedStoneChestDefinition extends StringRepresentable {
    String name();

    Block block();

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

    default String getSerializedName() {
        return name();
    }
}
