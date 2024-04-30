package mod.flatcoloredblocks.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class ColoredConcreteBlock extends ColoredBlock
{
    private static final MapCodec<ColoredConcreteBlock> CODEC = simpleCodec(ColoredConcreteBlock::new);

    public ColoredConcreteBlock(final Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
