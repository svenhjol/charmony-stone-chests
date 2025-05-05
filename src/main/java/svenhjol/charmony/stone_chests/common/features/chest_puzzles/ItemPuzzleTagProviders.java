package svenhjol.charmony.stone_chests.common.features.chest_puzzles;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.ItemPuzzleTagProvider;
import svenhjol.charmony.core.base.Setup;

import java.util.List;

public class ItemPuzzleTagProviders extends Setup<ChestPuzzles> implements ItemPuzzleTagProvider {
    public ItemPuzzleTagProviders(ChestPuzzles feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public List<TagKey<Item>> getItemPuzzleTags() {
        return List.of(
            Tags.PUZZLE_SHERDS,
            Tags.PUZZLE_GEMS
        );
    }
}
