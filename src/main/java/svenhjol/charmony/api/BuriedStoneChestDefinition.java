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

    List<ResourceKey<LootTable>> lootTables();

    default String getSerializedName() {
        return name();
    }
}
