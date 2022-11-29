package mod.flatcoloredblocks.core.compat.chiselsandbits;

import mod.chiselsandbits.api.variant.state.IStateVariant;
import org.jetbrains.annotations.NotNull;

public class ColoredStateVariant implements IStateVariant {

    public static ColoredStateVariant WHITE = new ColoredStateVariant(-1);

    private final int color;

    public ColoredStateVariant(int color) {
        this.color = color;
    }

    @Override
    public int compareTo(@NotNull IStateVariant o) {
        if (!(o instanceof ColoredStateVariant coloredStateVariant))
            return -1;

        return color - ((ColoredStateVariant) o).color;
    }

    @Override
    public IStateVariant createSnapshot() {
        return this;
    }

    public int getColor() {
        return color;
    }
}
