package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ItemPuzzleSlot extends Slot {
    public ItemPuzzleSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean isFake() {
        return true;
    }

    @Override
    public boolean isHighlightable() {
        return false;
    }
}
