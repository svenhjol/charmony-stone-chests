package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum ChestMaterial implements StringRepresentable {
    STONE(0, "stone"),
    DEEPSLATE(1, "deepslate");

    public static final StringRepresentable.EnumCodec<ChestMaterial> CODEC = StringRepresentable.fromEnum(ChestMaterial::values);
    public static final IntFunction<ChestMaterial> BY_ID = ByIdMap.continuous(ChestMaterial::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

    private final int id;
    private final String name;

    ChestMaterial(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ChestMaterial byId(int id) {
        return BY_ID.apply(id);
    }


    public int getId() {
        return id;
    }

    @Override
    public String getSerializedName() {
        return name.toLowerCase(Locale.ROOT);
    }
}
