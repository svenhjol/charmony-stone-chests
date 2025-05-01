package svenhjol.charmony.stone_chests.common.features.buried_stone_chests;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.BuriedStoneChestDefinition;
import svenhjol.charmony.api.BuriedStoneChestDefinitionProvider;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.BreakBehavior;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.puzzles.GemPuzzleMenuProvider;
import svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles.puzzles.SherdPuzzleMenuProvider;

import java.util.List;

public class Providers extends Setup<BuriedStoneChests> implements BuriedStoneChestDefinitionProvider {
    public Providers(BuriedStoneChests feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public List<BuriedStoneChestDefinition> getBuriedStoneChestDefinitions() {
        return List.of(
            inStoneLayer(),
            inDeepslateLayer()
        );
    }

    protected BuriedStoneChestDefinition inStoneLayer() {
        return new BuriedStoneChestDefinition() {
            @Override
            public String name() {
                return "in_stone_layer";
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.STONE;
            }

            @Override
            public Pair<Integer, Integer> depth() {
                return Pair.of(10, 40);
            }

            @Override
            public List<String> lockMenus() {
                return List.of(
                    SherdPuzzleMenuProvider.ID,
                    GemPuzzleMenuProvider.ID
                );
            }

            @Override
            public List<BreakBehavior> breakBehaviors() {
                return List.of(
                    BreakBehavior.EXPLODE
                );
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    BuiltInLootTables.SIMPLE_DUNGEON
                );
            }
        };
    }

    protected BuriedStoneChestDefinition inDeepslateLayer() {
        return new BuriedStoneChestDefinition() {
            @Override
            public String name() {
                return "in_deepslate_layer";
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.DEEPSLATE;
            }

            @Override
            public Pair<Integer, Integer> depth() {
                return Pair.of(-50, -10);
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    BuiltInLootTables.ANCIENT_CITY // Testing
                );
            }

            @Override
            public List<BreakBehavior> breakBehaviors() {
                return List.of(
                    BreakBehavior.EXPLODE
                );
            }

            @Override
            public List<String> lockMenus() {
                return List.of(
                    SherdPuzzleMenuProvider.ID,
                    GemPuzzleMenuProvider.ID
                );
            }
        };
    }
}
