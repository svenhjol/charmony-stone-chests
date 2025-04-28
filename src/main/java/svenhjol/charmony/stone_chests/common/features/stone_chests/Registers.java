package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlock.ChestBlockItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<StoneChests> {
    public static final String CHEST_ID = "stone_chest";
    public static final String UNLOCKED_ID = "unlocked_stone_chest";
    public static final String ITEM_PUZZLE_ID = "item_puzzle_stone_chest";

    public final Map<StoneChestMaterial, Supplier<ChestBlock>> chestBlocks = new HashMap<>();
    public final Map<StoneChestMaterial, Supplier<ChestBlockItem>> chestBlockItems = new HashMap<>();

    public final Supplier<BlockEntityType<ChestBlockEntity>> chestBlockEntity;

    public final Supplier<SoundEvent> chestOpenSound;
    public final Supplier<SoundEvent> chestCloseSound;

    public final Supplier<MenuType<UnlockedMenu>> unlockedMenu;
    public final Supplier<MenuType<ItemPuzzleMenu>> itemPuzzleMenu;

    public Registers(StoneChests feature) {
        super(feature);

        var registry = CommonRegistry.forFeature(feature);

        for (var material : StoneChestMaterial.values()) {
            var chestName = material.getSerializedName() + "_chest";
            var chestBlock = registry.block(chestName, key -> new ChestBlock(key, material));
            var chestItem = registry.item(chestName, key -> new ChestBlockItem(key, chestBlock));

            chestBlocks.put(material, chestBlock);
            chestBlockItems.put(material, chestItem);
        }

        chestBlockEntity = registry.blockEntity(CHEST_ID, () -> ChestBlockEntity::new, chestBlocks.values().stream().toList());

        chestOpenSound = registry.sound("chest_open");
        chestCloseSound = registry.sound("chest_close");

        unlockedMenu = registry.menuType(UNLOCKED_ID, () -> new MenuType<>(UnlockedMenu::new, FeatureFlags.VANILLA_SET));
        itemPuzzleMenu = registry.menuType(ITEM_PUZZLE_ID, () -> new MenuType<>(ItemPuzzleMenu::new, FeatureFlags.VANILLA_SET));
    }
}
