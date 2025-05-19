package svenhjol.charmony.stone_chests.client.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charmony.stone_chests.client.features.stone_chests.StoneChests;

import java.util.Optional;

@Mixin(Sheets.class)
public class SheetsMixin {
    @WrapMethod(
        method = "chooseMaterial(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/level/block/state/properties/ChestType;Z)Lnet/minecraft/client/resources/model/Material;"
    )
    private static Material hookChooseMaterial(BlockEntity blockEntity, ChestType chestType, boolean bl, Operation<Material> original) {
        Optional<Material> opt = StoneChests.feature().handlers.useCustomMaterial(blockEntity);
        return opt.orElseGet(() -> original.call(blockEntity, chestType, bl));
    }
}
