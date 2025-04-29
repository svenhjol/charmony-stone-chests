package svenhjol.charmony.stone_chests.common.features.buried_stone_chests;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.Fluids;
import svenhjol.charmony.api.BuriedStoneChestDefinition;
import svenhjol.charmony.core.base.Log;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlock;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

import java.util.ArrayList;

public class BuriedStoneChestPiece extends StructurePiece {
    private BuriedStoneChestDefinition definition;

    public BuriedStoneChestPiece(BuriedStoneChestDefinition definition, BlockPos pos) {
        super(feature().registers.structurePiece.get(), 0, new BoundingBox(pos));
        this.definition = definition;
    }

    public BuriedStoneChestPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(feature().registers.structurePiece.get(), tag);
        tag.getString(Registers.DEFINITION_ID).ifPresent(t -> this.definition = feature().registers.buriedChestDefinitions.get(t));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString(Registers.DEFINITION_ID, this.definition.getSerializedName());
    }

    @Override
    public void postProcess(
        WorldGenLevel level,
        StructureManager structureManager,
        ChunkGenerator chunkGenerator,
        RandomSource random,
        BoundingBox boundingBox,
        ChunkPos chunkPos,
        BlockPos pos
    ) {
        var offsetPos = new BlockPos(boundingBox.minX(), pos.getY(), boundingBox.minZ());

        var state = level.getBlockState(offsetPos);
        var stateBelow = level.getBlockState(offsetPos.below());
        if (stateBelow.isSolidRender() && state.isAir()) {
            this.boundingBox = new BoundingBox(offsetPos);
            this.createChest(level, this.boundingBox, random, offsetPos, false);
            return;
        }

        var range = 8;

        // Check for air spaces for the chest.
        for (var y = -10; y < 20; y++) {
            for (var x = -range; x < range; x++) {
                for (var z = -range; z < range; z++) {
                    var tryPos = offsetPos.offset(x, y, z);
                    var tryState = level.getBlockState(tryPos);
                    var tryStateBelow = level.getBlockState(tryPos.below());
                    var fluidState = level.getFluidState(tryPos);

                    var isAir = tryState.isAir();
                    var isWater = fluidState.is(Fluids.WATER);

                    if (tryStateBelow.isSolidRender() && (isAir || isWater)) {
                        this.boundingBox = new BoundingBox(tryPos);
                        this.createChest(level, this.boundingBox, random, tryPos, isWater);
                        return;
                    }
                }
            }
        }

        // Force the chest into solid blocks.
        for (var y = -10; y < 20; y++) {
            var tryPos = offsetPos.offset(0, y, 0);
            if (stateBelow.isSolidRender()) {
                this.boundingBox = new BoundingBox(tryPos);
                this.createChest(level, this.boundingBox, random, tryPos, false);
                return;
            }
        }

        log().debug("Could not find valid position for buried stone chest at " + pos);
    }

    protected void createChest(
        ServerLevelAccessor level,
        BoundingBox boundingBox,
        RandomSource random,
        BlockPos pos,
        boolean waterlogged
    ) {
        var block = definition.block();
        var lootTables = new ArrayList<>(definition.lootTables());
        var menus = new ArrayList<>(definition.lockMenus());

        if (lootTables.isEmpty()) {
            log().debug("No loot tables for buried stone chest");
            return;
        }

        Util.shuffle(lootTables, random);
        var lootTable = lootTables.getFirst();

        if (!boundingBox.isInside(pos)) {
            log().debug("Bounding box is incorrect");
            return;
        }

        var state = reorient(level, pos, block.defaultBlockState());
        if (waterlogged) {
            state = state.setValue(ChestBlock.WATERLOGGED, true);
        }

        level.setBlock(pos, state, 2);
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            chest.setLootTable(lootTable, random.nextLong());

            // Lock the chest with a random lock menu.
            if (!menus.isEmpty()) {
                Util.shuffle(menus, random);
                chest.lock(menus.getFirst());
            }
        }

        log().debug("Generated buried stone chest at " + pos);
    }

    public static BuriedStoneChests feature() {
        return BuriedStoneChests.feature();
    }

    protected Log log() {
        return feature().log();
    }
}
