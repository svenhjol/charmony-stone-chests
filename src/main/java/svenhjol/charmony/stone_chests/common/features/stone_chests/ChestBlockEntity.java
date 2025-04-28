package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.api.StoneChestLockMenuData;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.helpers.WorldHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
    public static final String MATERIAL_TAG = "material";
    public static final String LOCKED_TAG = "locked";
    public static final int ROWS = 3;
    public static final int COLUMNS = 9;
    public static final int SLOTS = ROWS * COLUMNS;

    private final ChestLidController chestLidController = new ChestLidController();
    private final ContainerOpenersCounter openersCounter;
    private StoneChestMaterial material;
    private NonNullList<ItemStack> items;
    private boolean locked;

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        super(StoneChests.feature().registers.chestBlockEntity.get(), pos, state);
        var block = (ChestBlock)state.getBlock();
        this.material = block.getMaterial();
        this.items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        this.locked = true;

        this.openersCounter = new ContainerOpenersCounter() {
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
                level.blockEvent(pos, block, 1, j);
            }

            @Override
            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof UnlockedMenu chest) {
                    var container = chest.getContainer();
                    return container == ChestBlockEntity.this;
                }
                return false;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(MATERIAL_TAG, material.getId());
        tag.putBoolean(LOCKED_TAG, locked);

        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, provider);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.locked = tag.getBooleanOr(LOCKED_TAG, false);
        this.material = StoneChestMaterial.byId(tag.getIntOr(MATERIAL_TAG, 0));

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items, provider);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.charmony-stone-chests." + material.getSerializedName() + "_chest");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    public boolean triggerEvent(int i, int j) {
        if (i == 1) {
            var ttt = j > 0 && j != 255;
            this.chestLidController.shouldBeOpen(ttt);
            if (j == 255) {
                StoneChests.feature().log().warn("j == 255!");
            }
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
        if (isLocked()) {
            StoneChests.feature().log().debug("Not incrementing because chest is locked");
        }
        if (!remove && !player.isSpectator() && getLevel() != null && !isLocked()) {
            openersCounter.incrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (isLocked()) {
            StoneChests.feature().log().debug("Not decrementing because chest is locked");
        }
        if (!remove && !player.isSpectator() && getLevel() != null && !isLocked()) {
            openersCounter.decrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    public void recheckOpen() {
        if (!remove && getLevel() != null && !isLocked()) {
            openersCounter.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
        }
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, ChestBlockEntity chest) {
        chest.chestLidController.tickLid();
    }

    @Override
    public int getContainerSize() {
        return SLOTS;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Can't even");
        }
        var pos = getBlockPos();

        // Set the material ID for the chest.
        var data = new SimpleContainerData(1);
        data.set(0, material.getId());

        var providers = new ArrayList<>(feature().registers.lockProviders);
        if (providers.isEmpty()) {
            // No providers, just unlock the chest.
            unlock();
        }

        if (isLocked()) {
            // Generate a seed based on this position.
            var seed = WorldHelper.seedFromBlockPos(pos);
            var random = new Random(seed);

            Collections.shuffle(providers, random);
            var provider = providers.getFirst();

            var menuData = new StoneChestLockMenuData();
            menuData.syncId = syncId;
            menuData.playerInventory = inventory;
            menuData.level = serverLevel;
            menuData.pos = pos;
            menuData.data = data;
            menuData.seed = seed;

            var menu = provider.getMenuProvider(menuData);
            if (menu.isPresent()) {
                return menu.get();
            } else {
                unlock();
            }
        }

        return new UnlockedMenu(syncId, inventory, this, data);
    }

    public StoneChestMaterial getMaterial() {
        return material;
    }

    public boolean isLocked() {
        return locked;
    }

    public void unlock() {
        this.locked = false;
        setChanged();
    }

    private StoneChests feature() {
        return StoneChests.feature();
    }
}
