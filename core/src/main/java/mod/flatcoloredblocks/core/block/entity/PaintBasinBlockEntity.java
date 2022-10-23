package mod.flatcoloredblocks.core.block.entity;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.block.ColoredBlock;
import mod.flatcoloredblocks.core.fluid.FluidTank;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import mod.flatcoloredblocks.core.registry.ColorizationRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public final class PaintBasinBlockEntity extends PaintContainingBlockEntity implements SingleSlotContainer
{

    public static final int PAINT_CONSUMPTION_ON_PAINT = 40;

    private ItemStack toPaint = ItemStack.EMPTY;

    public PaintBasinBlockEntity(final BlockPos pPos, final BlockState pBlockState)
    {
        super(BlockEntityTypes.PAINT_BASIN.get(), pPos, pBlockState, new FluidTank(IFluidManager.getInstance().getBucketAmount()));
    }

    @Override
    public ItemStack getCurrent()
    {
        return toPaint;
    }

    @Override
    public void setCurrent(final ItemStack stack)
    {
        toPaint = stack;
        setChanged();
    }

    @Override
    public boolean canPlaceItem(final int pIndex, final @NotNull ItemStack pStack)
    {
        return pStack.getItem() instanceof BlockItem && (((BlockItem) pStack.getItem()).getBlock() instanceof ColoredBlock ||
                         (ColorizationRegistry.getInstance().convertToColoredBlock(pStack) != pStack));
    }

    @Override
    public void setChanged()
    {
        super.setChanged();

        if (toPaint.isEmpty())
            return;

        if (getColor().isEmpty())
            return;

        if (getPrimaryTank().getAmount() < PAINT_CONSUMPTION_ON_PAINT)
            return;

        final int paintColor = getColor().get();

        if (!(toPaint.getItem() instanceof BlockItem blockItem))
            return;

        if (!(blockItem.getBlock() instanceof ColoredBlock)) {
            toPaint = ColorizationRegistry.getInstance().convertToColoredBlock(toPaint);
            if (toPaint.isEmpty())
                return;

            if (!(toPaint.getItem() instanceof BlockItem blockItem2))
                return;

            if (!(blockItem2.getBlock() instanceof ColoredBlock))
                return;

            blockItem = blockItem2;
        }

        if (!(blockItem.getBlock() instanceof ColoredBlock coloredBlock))
            return;

        final int color = coloredBlock.getColor(toPaint);
        if (paintColor != color) {
            coloredBlock.setColor(toPaint, paintColor);
            getPrimaryTank().setAmount(getPrimaryTank().getAmount() - PAINT_CONSUMPTION_ON_PAINT);
        }
    }
}
