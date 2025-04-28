package svenhjol.charmony.stone_chests.common.features.stone_chests;

import com.mojang.serialization.Codec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import svenhjol.charmony.api.BuriedStoneChestDefinition;
import svenhjol.charmony.api.BuriedStoneChestDefinitionProvider;
import svenhjol.charmony.api.StoneChestItemPuzzleInputProvider;
import svenhjol.charmony.api.StoneChestLockProvider;
import svenhjol.charmony.api.materials.StoneChestMaterial;
import svenhjol.charmony.core.Api;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlock.ChestBlockItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<StoneChests> {
    public static final String CHEST_ID = "stone_chest";
    public static final String UNLOCKED_ID = "unlocked_stone_chest";
    public static final String ITEM_PUZZLE_ID = "item_puzzle_stone_chest";
    public static final String STRUCTURE_ID = "buried_stone_chest";
    public static final String PIECE_ID = "buried_stone_chest_piece";
    public static final String DEFINITION_ID = "buried_stone_chest_definition";

    public final List<StoneChestLockProvider> lockProviders = new ArrayList<>();
    public final List<StoneChestItemPuzzleInputProvider> itemPuzzleInputs = new ArrayList<>();

    public final Map<StoneChestMaterial, Supplier<ChestBlock>> chestBlocks = new HashMap<>();
    public final Map<StoneChestMaterial, Supplier<ChestBlockItem>> chestBlockItems = new HashMap<>();

    public final Supplier<BlockEntityType<ChestBlockEntity>> chestBlockEntity;

    public final Supplier<SoundEvent> chestOpenSound;
    public final Supplier<SoundEvent> chestCloseSound;

    public final Supplier<MenuType<UnlockedMenu>> unlockedMenu;
    public final Supplier<MenuType<ItemPuzzleMenu>> itemPuzzleMenu;

    public final Supplier<StructureType<BuriedStoneChestStructure>> structureType;
    public final Supplier<StructurePieceType> structurePiece;
    public Codec<BuriedStoneChestDefinition> buriedChestCodec;
    public final Map<String, BuriedStoneChestDefinition> buriedChestDefinitions = new HashMap<>();

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

        structureType = registry.structure(STRUCTURE_ID, () -> BuriedStoneChestStructure.CODEC);
        structurePiece = registry.structurePiece(PIECE_ID, () -> BuriedStoneChestPiece::new);
    }

    @Override
    public Runnable boot() {
        return () -> {
            Api.consume(StoneChestLockProvider.class, lockProviders::add);
            Api.consume(StoneChestItemPuzzleInputProvider.class, itemPuzzleInputs::add);

            // Consumer of buried chest definitions.
            Api.consume(BuriedStoneChestDefinitionProvider.class, provider -> {
                for (var definition : provider.getBuriedStoneChestDefinitions()) {
                    this.buriedChestDefinitions.put(definition.name(), definition);
                }

                buriedChestCodec = StringRepresentable.fromValues(
                    () -> buriedChestDefinitions.values().toArray(new BuriedStoneChestDefinition[0]));
            });
        };
    }
}
