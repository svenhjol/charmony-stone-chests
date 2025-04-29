package svenhjol.charmony.stone_chests.common.features.stone_chest_puzzles;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import svenhjol.charmony.api.StoneChestLockMenuProvider;
import svenhjol.charmony.core.Api;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<StoneChestPuzzles> {
    public static final String ITEM_PUZZLE_ID = "item_puzzle_stone_chest";

    public final Supplier<MenuType<ItemPuzzleMenu>> itemPuzzleMenu;
    public final Map<String, StoneChestLockMenuProvider> lockMenuProviders = new HashMap<>();

    public Registers(StoneChestPuzzles feature) {
        super(feature);

        var registry = CommonRegistry.forFeature(feature);

        itemPuzzleMenu = registry.menuType(ITEM_PUZZLE_ID, () -> new MenuType<>(ItemPuzzleMenu::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public Runnable boot() {
        return () -> {
            Api.consume(StoneChestLockMenuProvider.class,
                provider -> lockMenuProviders.put(provider.getMenuProviderId(), provider));
        };
    }
}
