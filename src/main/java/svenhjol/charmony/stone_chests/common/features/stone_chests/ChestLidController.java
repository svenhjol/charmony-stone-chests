package svenhjol.charmony.stone_chests.common.features.stone_chests;

import net.minecraft.util.Mth;

public class ChestLidController {
    private boolean shouldBeOpen;
    private float openness;
    private float oOpenness;

    public void tickLid() {
        this.oOpenness = this.openness;
        float openSpeed = 0.05f;
        float closeSpeed = 0.20f;
        if (!this.shouldBeOpen && this.openness > 0.0f) {
            this.openness = Math.max(this.openness - closeSpeed, 0.0f);
        } else if (this.shouldBeOpen && this.openness < 1.0f) {
            this.openness = Math.min(this.openness + openSpeed, 1.0f);
        }
    }

    public float getOpenness(float f) {
        return Mth.lerp(f, this.oOpenness, this.openness);
    }

    public void shouldBeOpen(boolean bl) {
        this.shouldBeOpen = bl;
    }
}
