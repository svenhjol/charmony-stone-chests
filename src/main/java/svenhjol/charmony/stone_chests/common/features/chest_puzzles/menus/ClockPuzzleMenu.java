package svenhjol.charmony.stone_chests.common.features.chest_puzzles.menus;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.api.stone_chests.StoneChestMaterial;
import svenhjol.charmony.core.common.ContainerMenu;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.stone_chests.common.features.chest_puzzles.PuzzleMenu;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;

public class ClockPuzzleMenu extends ContainerMenu implements PuzzleMenu {
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public ClockPuzzleMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(1), StoneChestMaterial.Stone, 0, ContainerLevelAccess.NULL);
    }

    public ClockPuzzleMenu(int id, Inventory playerInventory, Container container, StoneChestMaterial material, int time, ContainerLevelAccess access) {
        super(feature().registers.clockPuzzleMenu.get(), id, playerInventory, container);
        this.access = access;
        this.data = new SimpleContainerData(2);
        this.data.set(0, material.getId());
        this.data.set(1, time);

        this.addStandardInventorySlots(playerInventory, 8, 113);
        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        access.execute((level, pos) -> {
            if (level instanceof ServerLevel serverLevel && serverLevel.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                var clockTime = level.getTimeOfDay(1.0f) * 64;
                var tolerance = 0.95d;

                var reqTime = getTime() - 0.5d;
                if (reqTime < 0) {
                    if (clockTime > 60) {
                        reqTime = 63.5;
                    } else {
                        reqTime = 0;
                        tolerance = 0.5d;
                    }
                } else if (reqTime > 62) {
                    if (clockTime > 63) {
                        reqTime = 63;
                        tolerance = 0.5d;
                    }
                }

                var valid = clockTime >= reqTime && clockTime < reqTime + tolerance;
                feature().log().debug("clockTime: " + clockTime + ", reqTime: " + reqTime + ", valid: " + valid);
                feature().handlers.solve(container, player, chest, valid);
            }
        });
        return true;
    }

    public StoneChestMaterial getMaterial() {
        var id = this.data.get(0);
        return StoneChestMaterial.byId(id);
    }

    public int getTime() {
        return this.data.get(1);
    }

    private static ChestPuzzles feature() {
        return ChestPuzzles.feature();
    }
}
