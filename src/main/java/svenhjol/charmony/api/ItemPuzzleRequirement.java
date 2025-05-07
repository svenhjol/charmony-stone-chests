package svenhjol.charmony.api;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public record ItemPuzzleRequirement(
    ResourceKey<LootTable> lootTable,
    int slots
) {
}
