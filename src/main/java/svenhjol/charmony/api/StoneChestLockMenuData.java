package svenhjol.charmony.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

/**
 * Data holder passed to the lock menu provider.
 * Populated by the stone chest block entity when the player first tries to open a chest.
 */
public class StoneChestLockMenuData {
    public int syncId;
    public Inventory playerInventory;
    public ServerLevel level;
    public BlockPos pos;
    public ContainerData data;
    public long seed;
    public double difficultyAmplifier;
}
