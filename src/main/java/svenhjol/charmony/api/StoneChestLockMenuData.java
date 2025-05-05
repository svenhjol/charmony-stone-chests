package svenhjol.charmony.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charmony.api.materials.StoneChestMaterial;

/**
 * Data holder passed to the lock menu provider.
 * Populated by the stone chest block entity when the player first tries to open a chest.
 */
public class StoneChestLockMenuData {
    public int syncId;
    public Inventory playerInventory;
    public ServerLevel level;
    public BlockPos pos;
    public StoneChestMaterial material;
    public long seed;
    public int difficultyAmplifier;
}
