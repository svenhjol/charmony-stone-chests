package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import svenhjol.charmony.api.chest_puzzles.ChestPuzzlesApi;
import svenhjol.charmony.api.stone_chests.StoneChestBlockEntity;
import svenhjol.charmony.api.stone_chests.StoneChestMaterial;

public class ChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity, StoneChestBlockEntity {
    public static final String MATERIAL_TAG = "material";
    public static final String LOCKED_TAG = "locked";
    public static final String CUSTOM_DEFINITION_TAG = "custom_definition";

    public static final int ROWS = 3;
    public static final int COLUMNS = 9;
    public static final int SLOTS = ROWS * COLUMNS;

    private final ChestLidController chestLidController = new ChestLidController();
    private final ContainerOpenersCounter openersCounter;
    private StoneChestMaterial material;
    private NonNullList<ItemStack> items;
    private boolean locked;
    private String customDefinition;

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        super(StoneChests.feature().registers.chestBlockEntity.get(), pos, state);
        var block = (ChestBlock)state.getBlock();
        this.material = block.getMaterial();
        this.items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        this.locked = false;
        this.customDefinition = "";

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
                    level.random.nextFloat() * 0.1f + 0.9f
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
                    level.random.nextFloat() * 0.1f + 0.9f
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
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.putInt(MATERIAL_TAG, material.getId());
        valueOutput.putBoolean(LOCKED_TAG, locked);
        valueOutput.putString(CUSTOM_DEFINITION_TAG, customDefinition);

        if (!this.trySaveLootTable(valueOutput)) {
            ContainerHelper.saveAllItems(valueOutput, this.items);
        }
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.locked = valueInput.getBooleanOr(LOCKED_TAG, false);
        this.material = StoneChestMaterial.byId(valueInput.getIntOr(MATERIAL_TAG, 0));
        this.customDefinition = valueInput.getStringOr(CUSTOM_DEFINITION_TAG, "");

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(valueInput)) {
            ContainerHelper.loadAllItems(valueInput, this.items);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.charmony." + material.getSerializedName() + "_chest");
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
            this.chestLidController.shouldBeOpen(j > 0 && j != 255);
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

    @SuppressWarnings("unused")
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

        if (isLocked()) {
            var opt = ChestPuzzlesApi.instance().getMenuProvider(serverLevel, this, syncId, inventory, material);
            if (opt.isPresent()) {
                return opt.get();
            }
        }

        unlock();
        return new UnlockedMenu(syncId, inventory, this, material);
    }

    @Override
    public void lock() {
        this.locked = true;
        setChanged();
    }

    @Override
    public void setCustomDefinition(String customDefinition) {
        this.customDefinition = customDefinition;
        setChanged();
    }

    @Override
    public String getCustomDefinition() {
        return this.customDefinition;
    }

    @Override
    public StoneChestMaterial getMaterial() {
        return material;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void unlock() {
        if (isLocked() && level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, getBlockPos(), feature().registers.chestUnlockSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        this.locked = false;
        setChanged();

    }

    private StoneChests feature() {
        return StoneChests.feature();
    }
}
