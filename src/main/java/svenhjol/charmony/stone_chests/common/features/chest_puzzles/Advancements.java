package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.world.entity.player.Player;
import svenhjol.charmony.api.chest_puzzles.ChestPuzzleType;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.AdvancementHelper;

public class Advancements extends Setup<ChestPuzzles> {
    public Advancements(ChestPuzzles feature) {
        super(feature);
    }

    public void unlockedPuzzleChest(ChestPuzzleType type, Player player) {
        if (type == ChestPuzzleType.Item) {
            unlockedItemChest(player);
        } else if (type == ChestPuzzleType.Time) {
            unlockedTimedChest(player);
        }
    }

    public void unlockedItemChest(Player player) {
        AdvancementHelper.trigger("unlocked_item_chest", player);
    }

    public void unlockedTimedChest(Player player) {
        AdvancementHelper.trigger("unlocked_timed_chest", player);
    }
}
