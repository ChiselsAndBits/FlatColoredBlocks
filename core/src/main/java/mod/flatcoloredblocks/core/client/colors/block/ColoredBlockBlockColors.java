package mod.flatcoloredblocks.core.client.colors.block;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColoredBlockBlockColors implements BlockColor
{
    @Override
    public int getColor(final @NotNull BlockState pState, @Nullable final BlockAndTintGetter pLevel, @Nullable final BlockPos pPos, final int pTintIndex)
    {
        return pTintIndex;
    }
}
