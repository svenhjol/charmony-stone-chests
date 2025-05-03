package svenhjol.charmony.stone_chests.common.features.secret_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import svenhjol.charmony.core.base.Setup;

import javax.annotation.Nullable;
import java.util.List;

public class Handlers extends Setup<SecretChests> {
    public @Nullable String template; // Used by End Cities
    public @Nullable ResourceLocation jigsawTemplate; // Used by Bastions

    public static final List<String> END_CITY_TEMPLATES = List.of(
        "third_roof", "bridge_piece", "base_floor"
    );

    public Handlers(SecretChests feature) {
        super(feature);
    }

    public void createNetherFortressChest(StructurePiece piece, WorldGenLevel level, RandomSource random) {
        if (random.nextInt(1000) < 1) {
            BlockPos pos;
            if (random.nextBoolean()) {
                pos = piece.getWorldPos(1, 2, 2);
            } else {
                pos = piece.getWorldPos(3, 2, 2);
            }
            var state = StructurePiece.reorient(level, pos, Blocks.FURNACE.defaultBlockState());
            level.setBlock(pos, state, 2);
        }
    }

    public boolean createEndCityChest(ServerLevelAccessor level, BlockPos pos) {
        if (template != null && END_CITY_TEMPLATES.contains(template)) {
            if (level.getRandom().nextInt(1000) < 1) {
                var state = StructurePiece.reorient(level, pos, Blocks.FURNACE.defaultBlockState());
                level.setBlock(pos, state, 2);
                return true;
            }
        }

        return false;
    }

    public boolean createBastionChest(ServerLevelAccessor level, BlockPos pos) {
        if (jigsawTemplate != null && jigsawTemplate.getPath().contains("bastion")) {
            if (level.getRandom().nextInt(1000) < 1) {
                var state = StructurePiece.reorient(level, pos, Blocks.FURNACE.defaultBlockState());
                level.setBlock(pos, state, 2);
                return true;
            }
        }

        return false;
    }
}
