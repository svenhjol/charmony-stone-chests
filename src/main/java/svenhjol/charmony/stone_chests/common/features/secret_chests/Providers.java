package svenhjol.charmony.stone_chests.common.features.secret_chests;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.secret_chests.SecretChestDefinition;
import svenhjol.charmony.api.secret_chests.SecretChestDefinitionProvider;
import svenhjol.charmony.api.secret_chests.SecretChestPlacement;
import svenhjol.charmony.api.stone_chests.StoneChestMaterial;
import svenhjol.charmony.api.secret_chests.SecretChestSideEffects;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.puzzles.*;

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
            inOcean(),
            atBedrock(),
            onMountain(),
            onEndIslands(),
            inFortresses(),
            inEndCities(),
            inBastions()
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
            public Pair<Integer, Integer> height() {
                return Pair.of(25, 50);
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    GenericItemPuzzleProvider.ID
                );
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.GiveBadEffect
                );
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_EMERALDS,
                    Tags.LOOT_GOLD,
                    Tags.LOOT_ORES,
                    Tags.LOOT_POTIONS
                );
            }

            @Override
            public void decorateSurface(WorldGenLevel level, BlockPos pos, RandomSource random) {
                feature().handlers.createFlowerRing(level, pos, random, Tags.FLOWER_RING_FOR_SURFACE);
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
            public Pair<Integer, Integer> height() {
                return Pair.of(-50, -5);
            }

            @Override
            public int difficultyAmplifier() {
                return 2;
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_EMERALDS,
                    Tags.LOOT_GOLD,
                    Tags.LOOT_BOOKS,
                    Tags.LOOT_ILLAGERS,
                    Tags.LOOT_ORES,
                    Tags.LOOT_TREASURE1,
                    Tags.LOOT_TREASURE2
                );
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.GiveBadEffect,
                    SecretChestSideEffects.SpawnOverworldMonsters
                );
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    GenericItemPuzzleProvider.ID,
                    EnchantedItemPuzzleMenuProvider.ID
                );
            }

            @Override
            public void decorateSurface(WorldGenLevel level, BlockPos pos, RandomSource random) {
                feature().handlers.createFlowerRing(level, pos, random, Tags.FLOWER_RING_FOR_DEEPSLATE);
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
            public boolean strict() {
                return true;
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Tuff;
            }

            @Override
            public Pair<Integer, Integer> height() {
                return Pair.of(-60, -59);
            }

            @Override
            public Pair<Integer, Integer> fallbackYOffset() {
                return Pair.of(0, 2);
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_DIAMONDS,
                    Tags.LOOT_TREASURE1,
                    Tags.LOOT_TREASURE2
                );
            }

            @Override
            public int difficultyAmplifier() {
                return 3;
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.Explode,
                    SecretChestSideEffects.SpawnOverworldMonsters
                );
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    ClockPuzzleMenuProvider.ID,
                    MoonPuzzleMenuProvider.ID,
                    EnchantedBookPuzzleMenuProvider.ID,
                    EnchantedItemPuzzleMenuProvider.ID
                );
            }

            @Override
            public void decorateSurface(WorldGenLevel level, BlockPos pos, RandomSource random) {
                feature().handlers.createFlowerRing(level, pos, random, Tags.FLOWER_RING_FOR_BEDROCK);
            }
        };
    }

    protected SecretChestDefinition onMountain() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "on_mountain";
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Surface;
            }

            @Override
            public Pair<Integer, Integer> height() {
                return Pair.of(130, 200);
            }

            @Override
            public Pair<Integer, Integer> fallbackYOffset() {
                return Pair.of(0, 10);
            }

            @Override
            public boolean strict() {
                return true;
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Calcite;
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_EMERALDS,
                    Tags.LOOT_GOLD,
                    Tags.LOOT_ILLAGERS,
                    Tags.LOOT_TREASURE1,
                    Tags.LOOT_TREASURE2
                );
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.GiveBadEffect,
                    SecretChestSideEffects.SpawnOverworldMonsters
                );
            }

            @Override
            public int difficultyAmplifier() {
                return 2;
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    MoonPuzzleMenuProvider.ID,
                    GenericItemPuzzleProvider.ID
                );
            }
        };
    }

    protected SecretChestDefinition inOcean() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "in_ocean";
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Surface;
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Prismarine;
            }

            @Override
            public Pair<Integer, Integer> height() {
                return Pair.of(30, 50);
            }

            @Override
            public Pair<Integer, Integer> fallbackYOffset() {
                return Pair.of(0, 10);
            }

            @Override
            public boolean canBeFullyBuried() {
                return false;
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_POTIONS,
                    Tags.LOOT_BOOKS,
                    Tags.LOOT_TREASURE1,
                    Tags.LOOT_TREASURE2
                );
            }

            @Override
            public int difficultyAmplifier() {
                return 2;
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    GenericItemPuzzleProvider.ID,
                    EnchantedItemPuzzleMenuProvider.ID
                );
            }
        };
    }

    protected SecretChestDefinition onEndIslands() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "on_end_islands";
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Surface;
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Purpur;
            }

            @Override
            public double chance() {
                return 0.5d;
            }

            @Override
            public Pair<Integer, Integer> height() {
                return Pair.of(30, 90);
            }

            @Override
            public int fallbackXZOffset() {
                return 12;
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_DIAMONDS,
                    Tags.LOOT_END1,
                    Tags.LOOT_END2
                );
            }

            @Override
            public int difficultyAmplifier() {
                return 3;
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    EnderItemPuzzleProvider.ID
                );
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.SpawnEndMonsters,
                    SecretChestSideEffects.Explode,
                    SecretChestSideEffects.GiveBadEffect
                );
            }

            /**
             * @see net.minecraft.world.level.levelgen.feature.EndIslandFeature
             */
            @Override
            public boolean generateSurface(WorldGenLevel level, BlockPos pos, RandomSource random) {
                float f = random.nextInt(3) + 4.0f;

                var block = Blocks.END_STONE;
                for (int i = 0; f > 0.5f; i--) {
                    for (int j = Mth.floor(-f); j <= Mth.ceil(f); j++) {
                        for (int k = Mth.floor(-f); k <= Mth.ceil(f); k++) {
                            if (j * j + k * k <= (f + 1.0f) * (f + 1.0f)) {
                                level.setBlock(pos.offset(j, i, k), block.defaultBlockState(), 3);
                            }
                        }
                    }

                    f -= random.nextInt(2) + 0.5f;
                }

                return true;
            }
        };
    }

    protected SecretChestDefinition inFortresses() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "in_fortresses";
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.NetherBrick;
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Fortress;
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    NetherItemPuzzleProvider.ID
                );
            }

            @Override
            public int difficultyAmplifier() {
                return 1;
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_NETHER1,
                    Tags.LOOT_NETHER2,
                    Tags.LOOT_DIAMONDS
                );
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.SpawnNetherMonsters,
                    SecretChestSideEffects.Explode,
                    SecretChestSideEffects.GiveBadEffect
                );
            }
        };
    }

    protected SecretChestDefinition inEndCities() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "in_end_cities";
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Purpur;
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.EndCity;
            }

            @Override
            public int difficultyAmplifier() {
                return 2;
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    EnderItemPuzzleProvider.ID
                );
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_DIAMONDS,
                    Tags.LOOT_END1,
                    Tags.LOOT_END2
                );
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.SpawnEndMonsters,
                    SecretChestSideEffects.Explode,
                    SecretChestSideEffects.GiveBadEffect
                );
            }
        };
    }

    protected SecretChestDefinition inBastions() {
        return new SecretChestDefinition() {
            @Override
            public String name() {
                return "in_bastions";
            }

            @Override
            public StoneChestMaterial material() {
                return StoneChestMaterial.Blackstone;
            }

            @Override
            public SecretChestPlacement placement() {
                return SecretChestPlacement.Bastion;
            }

            @Override
            public List<String> puzzleMenus() {
                return List.of(
                    NetherItemPuzzleProvider.ID
                );
            }

            @Override
            public int difficultyAmplifier() {
                return 3;
            }

            @Override
            public List<ResourceKey<LootTable>> lootTables() {
                return List.of(
                    Tags.LOOT_NETHER1,
                    Tags.LOOT_NETHER2
                );
            }

            @Override
            public List<SecretChestSideEffects> sideEffects() {
                return List.of(
                    SecretChestSideEffects.SpawnNetherMonsters,
                    SecretChestSideEffects.Explode,
                    SecretChestSideEffects.GiveBadEffect
                );
            }
        };
    }
}
