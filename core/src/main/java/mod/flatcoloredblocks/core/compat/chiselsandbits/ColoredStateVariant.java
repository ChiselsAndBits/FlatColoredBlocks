package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mod.chiselsandbits.api.variant.state.IStateVariant;
import mod.chiselsandbits.api.variant.state.IStateVariantProvider;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public record ColoredStateVariant(int color) implements IStateVariant {
    public static final MapCodec<ColoredStateVariant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("color").forGetter(ColoredStateVariant::color)
    ).apply(instance, ColoredStateVariant::new));
    public static final StreamCodec<ByteBuf, ColoredStateVariant> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(ColoredStateVariant::new, ColoredStateVariant::color);

    public static ColoredStateVariant WHITE = new ColoredStateVariant(-1);

    @Override
    public int compareTo(@NotNull IStateVariant o) {
        if (!(o instanceof ColoredStateVariant(int otherColor)))
            return -1;

        return color - otherColor;
    }

    @Override
    public IStateVariant createSnapshot() {
        return this;
    }

    @Override
    public IStateVariantProvider provider() {
        return StateVariantProviders.PROVIDER;
    }
}
