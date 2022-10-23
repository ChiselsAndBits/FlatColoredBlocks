package mod.flatcoloredblocks.core.client.colors.item;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColoredBlockItemColors implements ItemColor
{

    @Override
    public int getColor(final @NotNull ItemStack pStack, final int pTintIndex)
    {
        return pTintIndex;
    }
}
