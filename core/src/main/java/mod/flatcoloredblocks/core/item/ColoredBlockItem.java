package mod.flatcoloredblocks.core.item;

import mod.flatcoloredblocks.core.ColorNameManager;
import mod.flatcoloredblocks.core.block.ColoredBlock;
import net.minecraft.ChatFormatting;
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
        final Component colorName = ColorNameManager.getInstance().getNameSuffixed(color);

        return Component.translatable("item.flatcoloredblocks.colored_block.with_contents", colorName, coloredBlock.getName());
    }

    @Override
    public void appendHoverText(final @NotNull ItemStack pStack, @Nullable final Level pLevel, final @NotNull List<Component> pTooltipComponents, final @NotNull TooltipFlag pIsAdvanced)
    {
        final int color = coloredBlock.getColor(pStack);

        final int red = (color >> 16) & 0xFF;
        final int green = (color >> 8) & 0xFF;
        final int blue = color & 0xFF;

        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.red", red).withStyle(ChatFormatting.RED));
        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.green", green).withStyle(ChatFormatting.GREEN));
        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.blue", blue).withStyle(ChatFormatting.BLUE));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
