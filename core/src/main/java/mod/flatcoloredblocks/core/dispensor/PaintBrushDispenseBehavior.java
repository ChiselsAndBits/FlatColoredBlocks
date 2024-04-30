package mod.flatcoloredblocks.core.dispensor;

import mod.flatcoloredblocks.core.item.PaintBrushItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class PaintBrushDispenseBehavior implements DispenseItemBehavior
{
    @Override
    public @NotNull ItemStack dispense(final @NotNull BlockSource pSource, final @NotNull ItemStack pStack)
    {
        if (!(pStack.getItem() instanceof PaintBrushItem paintBrushItem))
            return pStack;

        final BlockPos targetedPosition = pSource.pos().relative(pSource.state().getValue(DispenserBlock.FACING));
        final Level level = pSource.level();

        paintBrushItem.onInteract(level, targetedPosition, pStack, null);

        return pStack;
    }
}
