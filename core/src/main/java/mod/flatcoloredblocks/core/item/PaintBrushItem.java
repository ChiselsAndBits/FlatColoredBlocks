package mod.flatcoloredblocks.core.item;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.block.ColoredBlock;
import mod.flatcoloredblocks.core.block.entity.ColoredBlockEntity;
import mod.flatcoloredblocks.core.block.entity.PaintBasinBlockEntity;
import mod.flatcoloredblocks.core.registry.ColorizationRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PaintBrushItem extends PaintContainingItem
{
    public PaintBrushItem(final Properties pProperties)
    {
        super(pProperties, ((int) IFluidManager.getInstance().getBucketAmount() / 5));
    }

    @Override
    protected Component getNameWithContents(final int amount, final Component colorName)
    {
        return Component.translatable("item.flatcoloredblocks.paint_brush.with_contents", amount, colorName);
    }

    @Override
    public boolean overrideStackedOnOther(final @NotNull ItemStack pStack, final @NotNull Slot pSlot, final @NotNull ClickAction pAction, final @NotNull Player pPlayer)
    {
        if (getAmount(pStack) == getCapacity())
        {
            return false;
        }

        if (pAction != ClickAction.SECONDARY)
        {
            return false;
        } else
        {
            ItemStack itemstack = pSlot.getItem();
            if (itemstack.getItem() instanceof PaintBucketItem paintBucketItem)
            {
                if (getAmount(pStack) > 0 && getColor(pStack) != paintBucketItem.getColor(itemstack))
                {
                    return false;
                }

                setColor(pStack, getColor(itemstack));
                final int amountToTransfer = Math.min(getCapacity() - getAmount(pStack), paintBucketItem.getAmount(itemstack));
                setAmount(pStack, getAmount(pStack) + amountToTransfer);
                paintBucketItem.setAmount(itemstack, paintBucketItem.getAmount(itemstack) - amountToTransfer);
                return true;
            }

            return true;
        }
    }

    @Override
    public @NotNull InteractionResult useOn(final @NotNull UseOnContext pContext)
    {
        return onInteract(pContext.getLevel(), pContext.getClickedPos(), pContext.getItemInHand(), pContext.getPlayer());
    }

    public InteractionResult onInteract(final Level level, final BlockPos targetedPosition, final ItemStack pStack, final Player player)
    {
        final BlockState blockState = level.getBlockState(targetedPosition);
        final BlockEntity blockEntity = level.getBlockEntity(targetedPosition);
        if (blockEntity instanceof ColoredBlockEntity coloredBlock) {
            if (coloredBlock.getColor() == getColor(pStack)) {
                return InteractionResult.PASS;
            }

            if (getAmount(pStack) < PaintBasinBlockEntity.PAINT_CONSUMPTION_ON_PAINT) {
                return InteractionResult.PASS;
            }

            setAmount(pStack, getAmount(pStack) - PaintBasinBlockEntity.PAINT_CONSUMPTION_ON_PAINT);
            coloredBlock.setColor(getColor(pStack));
            return InteractionResult.SUCCESS;
        }

        final Optional<Integer> vanillaColor = ColorizationRegistry.getInstance().getColorFor(blockState);
        if (vanillaColor.isPresent()) {
            final Optional<BlockState> replacedState = ColorizationRegistry.getInstance().getConversionState(blockState);
            if (replacedState.isPresent()) {
                if (getAmount(pStack) < PaintBasinBlockEntity.PAINT_CONSUMPTION_ON_PAINT) {
                    return InteractionResult.PASS;
                }

                setAmount(pStack, getAmount(pStack) - PaintBasinBlockEntity.PAINT_CONSUMPTION_ON_PAINT);
                level.setBlock(targetedPosition, replacedState.get(), 3);

                if (level.getBlockEntity(targetedPosition) instanceof ColoredBlockEntity newColoredBlockEntity) {
                    newColoredBlockEntity.setColor(getColor(pStack));
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;   
    }
}
