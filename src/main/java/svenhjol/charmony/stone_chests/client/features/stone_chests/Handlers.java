package svenhjol.charmony.stone_chests.client.features.stone_chests;

import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.stone_chests.StoneChestsMod;
import svenhjol.charmony.stone_chests.common.features.stone_chests.ChestBlockEntity;
import svenhjol.charmony.api.materials.StoneChestMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Handlers extends Setup<StoneChests> {
    public static final Map<StoneChestMaterial, Material> CHEST_MATERIALS = new HashMap<>();

    public Handlers(StoneChests feature) {
        super(feature);

        for (var coreMaterial : StoneChestMaterial.values()) {
            var name = coreMaterial.getSerializedName();
            var mapper = new MaterialMapper(Sheets.CHEST_SHEET, "entity/chest");
            var sheetMaterial = mapper.apply(StoneChestsMod.id(name));
            CHEST_MATERIALS.put(coreMaterial, sheetMaterial);
        }
    }

    public Optional<Material> useCustomMaterial(BlockEntity blockEntity) {
        if (blockEntity instanceof ChestBlockEntity chest) {
            var material = chest.getMaterial();
            if (CHEST_MATERIALS.containsKey(material)) {
                return Optional.of(CHEST_MATERIALS.get(material));
            }
        }
        return Optional.empty();
    }
}
