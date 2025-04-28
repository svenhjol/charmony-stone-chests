package svenhjol.charmony.stone_chests.common.features.stone_chests;

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
import svenhjol.charmony.api.BuriedStoneChestDefinition;
import svenhjol.charmony.core.base.Log;

import java.util.ArrayList;

public class BuriedStoneChestPiece extends StructurePiece {
    private BuriedStoneChestDefinition definition;

    public BuriedStoneChestPiece(BuriedStoneChestDefinition definition, BlockPos pos) {
        super(StoneChests.feature().registers.structurePiece.get(), 0, new BoundingBox(pos));
        this.definition = definition;
    }

    public BuriedStoneChestPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(StoneChests.feature().registers.structurePiece.get(), tag);
        tag.getString(Registers.DEFINITION_ID).ifPresent(t -> this.definition = StoneChests.feature().registers.buriedChestDefinitions.get(t));
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
            this.createChest(level, this.boundingBox, random, offsetPos);
            return;
        }

        var range = 8;

        for (var y = -10; y < 20; y++) {
            for (var x = -range; x < range; x++) {
                for (var z = -range; z < range; z++) {
                    var tryPos = offsetPos.offset(x, y, z);
                    var tryState = level.getBlockState(tryPos);
                    var tryStateBelow = level.getBlockState(tryPos.below());

                    if (tryStateBelow.isSolidRender() && tryState.isAir()) {
                        this.boundingBox = new BoundingBox(tryPos);
                        this.createChest(level, this.boundingBox, random, tryPos);
                        return;
                    }
                }
            }
        }

        log().debug("Could not find valid position for buried stone chest at " + pos);
    }

    protected void createChest(
        ServerLevelAccessor level,
        BoundingBox boundingBox,
        RandomSource random,
        BlockPos pos
    ) {
        var block = definition.block();
        var lootTables = new ArrayList<>(definition.lootTables());
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

        level.setBlock(pos, state, 2);
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            chest.setLootTable(lootTable, random.nextLong());
        }

        log().debug("Generated buried stone chest at " + pos);
    }

    protected Log log() {
        return StoneChests.feature().log();
    }
}
