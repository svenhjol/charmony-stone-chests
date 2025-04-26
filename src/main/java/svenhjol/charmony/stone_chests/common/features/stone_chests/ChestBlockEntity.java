package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChestBlockEntity extends BlockEntity implements LidBlockEntity {
    public static final String MATERIAL_TAG = "material";

    private final ChestLidController chestLidController = new ChestLidController();
    private ChestMaterial material;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
            level.playSound(
                null,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                StoneChests.feature().registers.chestOpenSound.get(),
                SoundSource.BLOCKS,
                0.5f,
                level.random.nextFloat() * 0.1F + 0.9F
            );
        }

        @Override
        protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
            level.playSound(
                null,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                StoneChests.feature().registers.chestCloseSound.get(),
                SoundSource.BLOCKS,
                0.5f,
                level.random.nextFloat() * 0.1F + 0.9F
            );
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int i, int j) {
            var block = (ChestBlock)state.getBlock();
            level.blockEvent(ChestBlockEntity.this.worldPosition, block, 1, j);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            // TODO: container checking
            return false;
        }
    };

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        super(StoneChests.feature().registers.chestBlockEntity.get(), pos, state);
        var block = (ChestBlock)state.getBlock();
        this.material = block.getMaterial();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(MATERIAL_TAG, material.getId());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        var material = tag.getIntOr(MATERIAL_TAG, 0);
        this.material = ChestMaterial.byId(material);
    }

    /**
     * Copypasta from EnderChestBlockEntity.
     */
    @Override
    public boolean triggerEvent(int i, int j) {
        if (i == 1) {
            this.chestLidController.shouldBeOpen(j > 0);
            return true;
        } else {
            return super.triggerEvent(i, j);
        }
    }

    @Override
    public float getOpenNess(float ticks) {
        return this.chestLidController.getOpenness(ticks);
    }

    public void startOpen(Player player) {
        if (!remove && !player.isSpectator() && getLevel() != null) {
            openersCounter.incrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!remove && !player.isSpectator() && getLevel() != null) {
            openersCounter.decrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    public void recheckOpen() {
        if (!remove && getLevel() != null) {
            openersCounter.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
        }
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, ChestBlockEntity chest) {
        chest.chestLidController.tickLid();
    }

    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public ChestMaterial getMaterial() {
        return material;
    }
}
