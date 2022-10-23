package mod.flatcoloredblocks.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface SingleSlotContainer extends Container
{

    Level getLevel();

    BlockPos getBlockPos();

    ItemStack getCurrent();

    void setCurrent( ItemStack stack );
    

    @Override
    default int getContainerSize()
    {
        return 1;
    }

    @Override
    default int getMaxStackSize()
    {
        return 1;
    }

    @Override
    default boolean isEmpty()
    {
        return getCurrent().isEmpty();
    }

    @Override
    default @NotNull ItemStack getItem(final int pSlot)
    {
        if (pSlot != 0)
            return ItemStack.EMPTY;

        return getCurrent();
    }

    @Override
    default @NotNull ItemStack removeItem(final int pSlot, final int pAmount)
    {
        if (pSlot != 0)
            return ItemStack.EMPTY;

        final ItemStack stack = getCurrent().split(pAmount);
        setChanged();
        return stack;
    }

    @Override
    default @NotNull ItemStack removeItemNoUpdate(final int pSlot)
    {
        if (pSlot != 0)
            return ItemStack.EMPTY;

        final ItemStack result = getCurrent().copy();
        setCurrent(ItemStack.EMPTY);
        setChanged();
        return result;
    }

    @Override
    default void setItem(final int pSlot, final @NotNull ItemStack pStack)
    {
        if (pSlot != 0)
            return;

        setCurrent(pStack.copy());
        setChanged();
    }

    @Override
    default boolean stillValid(final @NotNull Player pPlayer)
    {
        if (this.getLevel() != null)
        {
            if (this.getLevel().getBlockEntity(this.getBlockPos()) != this) {
                return false;
            } else {
                return pPlayer.distanceToSqr((double)this.getBlockPos().getX() + 0.5D, (double)this.getBlockPos().getY() + 0.5D, (double)this.getBlockPos().getZ() + 0.5D) <= 64.0D;
            }
        }

        return false;
    }

    @Override
    default void clearContent()
    {
        setCurrent(ItemStack.EMPTY);
    }
}
