package mod.flatcoloredblocks.core.util;

import java.util.Optional;

public record ColorizationData(int color, float fluidImpressionFactor, long fluidAmount, long pigmentAmount) {

    public int red() {
        return (color >> 16) & 0xFF;
    }

    public int green() {
        return (color >> 8) & 0xFF;
    }

    public int blue() {
        return color & 0xFF;
    }
}
