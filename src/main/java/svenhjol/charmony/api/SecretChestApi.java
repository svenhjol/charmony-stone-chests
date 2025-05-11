package svenhjol.charmony.api;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;

public class SecretChestApi {
    private static SecretChestApi instance;
    private ChestCreator creator;

    public static SecretChestApi instance() {
        if (instance == null) {
            instance = new SecretChestApi();
        }
        return instance;
    }

    private SecretChestApi() {
        this.creator = (definition, level, random, pos, waterlogged) -> false;
    }

    public boolean createChest(
        SecretChestDefinition definition,
        ServerLevelAccessor level,
        RandomSource random,
        BlockPos pos,
        boolean waterlogged
    ) {
        return creator.create(definition, level, random, pos, waterlogged);
    }

    public void setChestCreator(ChestCreator creator) {
        this.creator = creator;
    }

    @FunctionalInterface
    public interface ChestCreator {
        boolean create(
            SecretChestDefinition definition,
            ServerLevelAccessor level,
            RandomSource random,
            BlockPos pos,
            boolean waterlogged
        );
    }
}
