package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.api.StoneChestBreakBehavior;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;

public class ChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
    public static final String MATERIAL_TAG = "material";
    public static final String LOCKED_TAG = "locked";
    public static final String LOCK_MENU_TAG = "lock_menu";
    public static final String UNLOCKED_LOOT_TABLE_TAG = "unlocked_loot_table";
    public static final String BREAK_BEHAVIOR_TAG = "broken_behaviour";
    public static final String DIFFICULTY_AMPLIFIER_TAG = "difficulty_amplifier";

    public static final int ROWS = 3;
    public static final int COLUMNS = 9;
    public static final int SLOTS = ROWS * COLUMNS;

    private final ChestLidController chestLidController = new ChestLidController();
    private final ContainerOpenersCounter openersCounter;
    private StoneChestMaterial material;
    private NonNullList<ItemStack> items;
    private boolean locked;
    private String lockMenu;
    private String unlockedLootTable;
    private StoneChestBreakBehavior breakBehavior;
    private double difficultyAmplifier;

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        super(StoneChests.feature().registers.chestBlockEntity.get(), pos, state);
        var block = (ChestBlock)state.getBlock();
        this.material = block.getMaterial();
        this.items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        this.locked = false;
        this.lockMenu = "";
        this.unlockedLootTable = "";
        this.difficultyAmplifier = 1.0d;
        this.breakBehavior = StoneChestBreakBehavior.Nothing;

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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(MATERIAL_TAG, material.getId());
        tag.putBoolean(LOCKED_TAG, locked);
        tag.putString(LOCK_MENU_TAG, lockMenu);
        tag.putString(UNLOCKED_LOOT_TABLE_TAG, unlockedLootTable);
        tag.putDouble(DIFFICULTY_AMPLIFIER_TAG, difficultyAmplifier);

        if (breakBehavior != null) {
            tag.putString(BREAK_BEHAVIOR_TAG, breakBehavior.getSerializedName());
        }

        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, provider);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.locked = tag.getBooleanOr(LOCKED_TAG, false);
        this.material = StoneChestMaterial.byId(tag.getIntOr(MATERIAL_TAG, 0));
        this.lockMenu = tag.getStringOr(LOCK_MENU_TAG, "");
        this.unlockedLootTable = tag.getStringOr(UNLOCKED_LOOT_TABLE_TAG, "");
        this.difficultyAmplifier = tag.getDoubleOr(DIFFICULTY_AMPLIFIER_TAG, 1.0d);

        tag.getString(LOCK_MENU_TAG).ifPresent(str -> this.breakBehavior = StoneChestBreakBehavior.getOrDefault(str));

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

        // Set the material ID for the chest.
        var data = new SimpleContainerData(1);
        data.set(0, material.getId());

        if (isLocked() && puzzles().enabled()) {
            var opt = puzzles().handlers.getMenuProvider(serverLevel, this, syncId, inventory, data);
            if (opt.isPresent()) {
                return opt.get();
            }
        }

        unlock();
        return new UnlockedMenu(syncId, inventory, this, data);
    }

    public StoneChestMaterial getMaterial() {
        return material;
    }

    public String lockMenu() {
        return lockMenu;
    }

    public StoneChestBreakBehavior getBreakBehavior() {
        return breakBehavior;
    }

    public double getDifficultyAmplifier() {
        return difficultyAmplifier;
    }

    public ResourceKey<LootTable> getUnlockedLootTable() {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(unlockedLootTable));
    }

    public boolean isLocked() {
        return locked;
    }

    public void unlock() {
        this.locked = false;
        setChanged();
    }

    public void lock(String lockMenu) {
        this.locked = true;
        this.lockMenu = lockMenu;
        setChanged();
    }

    public void setDifficultyAmplifier(double amplifier) {
        this.difficultyAmplifier = amplifier;
        setChanged();
    }

    public void setUnlockedLootTable(ResourceKey<LootTable> lootTable) {
        this.unlockedLootTable = lootTable.location().toString();
        setChanged();
    }

    public void setBreakBehavior(StoneChestBreakBehavior breakBehavior) {
        this.breakBehavior = breakBehavior;
        setChanged();
    }

    private ChestPuzzles puzzles() {
        return ChestPuzzles.feature();
    }
}
