package mod.flatcoloredblocks.core.item;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.ColorNameManager;
import mod.flatcoloredblocks.core.block.entity.PaintContainingBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PaintBucketItem extends PaintContainingItem
{
    public PaintBucketItem(final Properties pProperties)
    {
        super(pProperties, (int) IFluidManager.getInstance().getBucketAmount());
    }

    @Override
    protected Component getNameWithContents(final int amount, final Component colorName)
    {
        return Component.translatable("item.flatcoloredblocks.paint_bucket.with_contents", amount, colorName);
    }

    @Override
    public @NotNull InteractionResult useOn(final @NotNull UseOnContext pContext)
    {
        return onInteract(pContext.getLevel(), pContext.getClickedPos(), pContext.getItemInHand(), pContext.getPlayer());
    }

    @NotNull
    public InteractionResult onInteract(final Level level, final BlockPos pos, final ItemStack stack, @Nullable final Player player) {
        if (!hasAmount(stack))
            return extractPaint(level, pos, stack, player);

        return insertPaint(level, pos, stack);
    }

    @NotNull
    public InteractionResult insertPaint(final Level level, final BlockPos pos, final ItemStack stack)
    {
        final BlockEntity clickedBlockEntity = level.getBlockEntity(pos);
        if (!(clickedBlockEntity instanceof PaintContainingBlockEntity paintContainingBlockEntity))
            return InteractionResult.PASS;

        final int consumedAmount = paintContainingBlockEntity.insertPaint(getColor(stack), getAmount(stack));

        removeAmount(stack, consumedAmount);
        return InteractionResult.SUCCESS;
    }

    @NotNull
    public InteractionResult extractPaint(final Level level, final BlockPos pos, final ItemStack stack, @Nullable final Player player)
    {
        final BlockEntity clickedBlockEntity = level.getBlockEntity(pos);
        if (!(clickedBlockEntity instanceof PaintContainingBlockEntity paintContainingBlockEntity))
            return InteractionResult.PASS;

        final Optional<Integer> containedColor = paintContainingBlockEntity.getColor();
        if (containedColor.isEmpty())
            return InteractionResult.PASS;

        int amountToExtract = (int) IFluidManager.getInstance().getBucketAmount();
        if (player != null && player.isCrouching()) {
            amountToExtract /= 2;
        }

        final int extractedAmount = paintContainingBlockEntity.extractPaint(amountToExtract);

        setColor(stack, containedColor.get());
        addAmount(stack, extractedAmount);
        return InteractionResult.SUCCESS;
    }
}
