package svenhjol.charmony.stone_chests.common.features.secret_chests;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.api.*;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.GemPuzzleMenuProvider;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.SherdPuzzleMenuProvider;

import java.util.List;

public class Providers extends Setup<SecretChests> implements SecretChestDefinitionProvider {
    public Providers(SecretChests feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public List<SecretChestDefinition> getSecretChestDefinitions() {
        return List.of(
            inSurfaceCaves(),
            inDeepCaves(),
            atBedrock()
        );
    }

    protected SecretChestDefinition inSurfaceCaves() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "in_surface_caves";
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Cave;
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Stone;
            }

            @Override
            public Pair<Integer, Integer> depth() {
                return Pair.of(25, 50);
            }

            @Override
            public List<String> lockMenus() {
                return List.of(
                    SherdPuzzleMenuProvider.ID,
                    GemPuzzleMenuProvider.ID
                );
            }

            @Override
            public List<StoneChestBreakBehavior> breakBehaviors() {
                return List.of(
                    StoneChestBreakBehavior.GiveBadEffect
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

    protected SecretChestDefinition inDeepCaves() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "in_deep_caves";
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Cave;
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Deepslate;
            }

            @Override
            public Pair<Integer, Integer> depth() {
                return Pair.of(-40, -10);
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    BuiltInLootTables.ANCIENT_CITY // Testing
                );
            }

            @Override
            public List<StoneChestBreakBehavior> breakBehaviors() {
                return List.of(
                    StoneChestBreakBehavior.GiveBadEffect,
                    StoneChestBreakBehavior.SpawnOverworldMonsters
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

    protected SecretChestDefinition atBedrock() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "at_bedrock";
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Buried;
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Deepslate;
            }

            @Override
            public Pair<Integer, Integer> depth() {
                return Pair.of(-60, -59);
            }

            @Override
            public Pair<Integer, Integer> fallbackYOffset() {
                return Pair.of(0, 2);
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    BuiltInLootTables.ANCIENT_CITY // Testing
                );
            }

            @Override
            public double difficultyAmplifier() {
                return 2.0d;
            }

            @Override
            public List<StoneChestBreakBehavior> breakBehaviors() {
                return List.of(
                    StoneChestBreakBehavior.Explode,
                    StoneChestBreakBehavior.SpawnOverworldMonsters
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
