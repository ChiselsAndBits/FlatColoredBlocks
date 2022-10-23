package mod.flatcoloredblocks.core.client.colors.item;

import mod.flatcoloredblocks.core.item.PaintContainingItem;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PaintSplattedItemColor implements ItemColor
{
    @Override
    public int getColor(@NotNull ItemStack stack, int tintIndex)
    {
        if (tintIndex != 1) return 0xFFFFFFFF;
        if (!(stack.getItem() instanceof PaintContainingItem paintContainingItem)) return 0xFFFFFFFF;
        return paintContainingItem.getColor(stack);
    }
}
