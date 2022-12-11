package mod.flatcoloredblocks.core.item;

import mod.flatcoloredblocks.core.block.ColoredBlock;
import mod.flatcoloredblocks.core.util.HoverTextUtils;
import mod.flatcoloredblocks.core.util.NameUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ColoredBlockItem extends BlockItem
{
    private final ColoredBlock coloredBlock;

    public ColoredBlockItem(final ColoredBlock pBlock, final Properties pProperties)
    {
        super(pBlock, pProperties);
        coloredBlock = pBlock;
    }

    @Override
    public @NotNull Component getName(final @NotNull ItemStack pStack)
    {
        final int color = this.coloredBlock.getColor(pStack);
        return NameUtils.getName(color, this.coloredBlock.getName());
    }

    @Override
    public void appendHoverText(final @NotNull ItemStack pStack, @Nullable final Level pLevel, final @NotNull List<Component> pTooltipComponents, final @NotNull TooltipFlag pIsAdvanced)
    {
        final int color = coloredBlock.getColor(pStack);

        HoverTextUtils.appendColorHoverText(pTooltipComponents, color);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public ColoredBlock getColoredBlock() {
        return coloredBlock;
    }
}
