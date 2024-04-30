package mod.flatcoloredblocks.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class ColoredWoolBlock extends ColoredBlock
{
    private static final MapCodec<PaintBasinBlock> CODEC = simpleCodec(PaintBasinBlock::new);

    public ColoredWoolBlock(final Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
