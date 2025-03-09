package mod.flatcoloredblocks.core.client.colors.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ColoredItemColors implements ItemColor
{

    @Override
    public int getColor(final @NotNull ItemStack pStack, final int pTintIndex)
    {
        return pTintIndex;
    }
}
