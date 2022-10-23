package mod.flatcoloredblocks.core.block;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.ColorNameManager;
import mod.flatcoloredblocks.core.block.entity.ColoredBlockEntity;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ColoredBlock extends BaseEntityBlock
{
    protected ColoredBlock(final Properties pProperties)
    {
        super(pProperties);
    }


    @Override
    public @NotNull RenderShape getRenderShape(final @NotNull BlockState pState)
    {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(final @NotNull BlockPos pPos, final @NotNull BlockState pState)
    {
        return BlockEntityTypes.COLORED_BLOCK.get().create(pPos, pState);
    }

    @Override
    public void fillItemCategory(final @NotNull CreativeModeTab pTab, final NonNullList<ItemStack> pItems)
    {
        pItems.add(new ItemStack(this));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFFFF0000);
        }));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFF00FF00);
        }));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFF0000FF);
        }));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFFFFFF00);
        }));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFF00FFFF);
        }));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFFFF00FF);
        }));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFFFFFFFF);
        }));
        pItems.add(Util.make(new ItemStack(this), (pStack) -> {
            pStack.getOrCreateTag().putInt("color", 0xFF000000);
        }));
    }

    @Override
    public void setPlacedBy(final @NotNull Level pLevel, final @NotNull BlockPos pPos, final @NotNull BlockState pState, @Nullable final LivingEntity pPlacer, final @NotNull ItemStack pStack)
    {
        final int color = getColor(pStack);
        final BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity)
        {
            coloredBlockEntity.setColor(color);
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(final @NotNull BlockGetter pLevel, final @NotNull BlockPos pPos, final @NotNull BlockState pState)
    {
        final ItemStack stack = new ItemStack(this);
        final BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity)
        {
            setColor(stack, coloredBlockEntity.getColor());
        }

        return stack;
    }

    public void setColor(final ItemStack stack, final int color) {
        stack.getOrCreateTag().putInt("color", color);
    }

    public int getColor(final ItemStack stack)
    {
        if (!stack.hasTag() || stack.getTag() == null)
            return 0XFFFFFFFF;

        if (!stack.getTag().contains("color"))
            return 0XFFFFFFFF;

        return stack.getTag().getInt("color");
    }
}
