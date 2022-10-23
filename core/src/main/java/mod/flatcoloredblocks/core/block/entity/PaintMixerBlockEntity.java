package mod.flatcoloredblocks.core.block.entity;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.fluid.FluidTank;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import mod.flatcoloredblocks.core.registrars.Fluids;
import mod.flatcoloredblocks.core.registry.ColorizationRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class PaintMixerBlockEntity extends PaintContainingBlockEntity implements SingleSlotContainer
{
    private final FluidTank leftInputTank = new FluidTank(IFluidManager.getInstance().getBucketAmount());
    private final FluidTank rightInputTank = new FluidTank(IFluidManager.getInstance().getBucketAmount());

    private ItemStack solidColorPaint = ItemStack.EMPTY;

    public PaintMixerBlockEntity(final BlockPos pPos, final BlockState pBlockState)
    {
        super(BlockEntityTypes.PAINT_MIXER.get(), pPos, pBlockState, new FluidTank(IFluidManager.getInstance().getBucketAmount() * 2));
    }

    @Override
    public FluidTank getInputTank(final int color)
    {
        if (getPrimaryTank().getContents().isPresent())
            return getPrimaryTank();

        final Optional<Integer> leftColor = getColor(leftInputTank);
        if (leftColor.isPresent() && leftColor.get() == color && leftInputTank.getMaximalAmount() != leftInputTank.getAmount())
            return leftInputTank;

        if (leftColor.isEmpty())
            return leftInputTank;

        return rightInputTank;
    }

    @Override
    public FluidTank getOutputTank()
    {
        if (getPrimaryTank().getAmount() > 0)
            return getPrimaryTank();

        if (rightInputTank.getAmount() > 0)
            return rightInputTank;

        return leftInputTank;
    }

    @Override
    public int extractPaint(final long bucketAmount)
    {
        final FluidTank outputTank = getOutputTank();
        final boolean outputIsPrimary = outputTank == getPrimaryTank();
        final int result = super.extractPaint(bucketAmount);
        if (outputIsPrimary) {
            if (getPrimaryTank().getAmount() <= IFluidManager.getInstance().getBucketAmount() && getPrimaryTank().getContents().isPresent()) {
                leftInputTank.clear();
                rightInputTank.clear();

                leftInputTank.setContents(getPrimaryTank().getContents().orElseThrow());

                getPrimaryTank().clear();
            }
        }

        setChanged();
        return result;
    }

    @Override
    public Collection<FluidTank> getTanks()
    {
        if (super.getPrimaryTank().getAmount() > 0) {
            return super.getTanks();
        }

        if (rightInputTank.getAmount() > 0) {
            if (leftInputTank.getAmount() > 0) {
                return List.of(leftInputTank, rightInputTank);
            } else {
                return List.of(rightInputTank);
            }
        } else if (leftInputTank.getAmount() > 0) {
            return List.of(leftInputTank);
        } else {
            return List.of();
        }
    }

    @Override
    public Optional<Integer> getColor()
    {
        return getColor(getOutputTank());
    }

    public void onPowerChanged(final boolean isPowered)
    {
        record ColorizationData(int color, long fluidAmount, long pigmentAmount) {
            public int red() {
                return (color >> 16) & 0xFF;
            }

            public int green() {
                return (color >> 8) & 0xFF;
            }

            public int blue() {
                return color & 0xFF;
            }
        }


        if (isPowered) {
            final Set<ColorizationData> colorizationData = new HashSet<>();
            if (leftInputTank.getAmount() > 0) {
                final Optional<Integer> leftColor = getColor(leftInputTank);
                leftColor.ifPresent(integer -> colorizationData.add(new ColorizationData(integer, leftInputTank.getAmount(), leftInputTank.getAmount())));
            }
            if (rightInputTank.getAmount() > 0) {
                final Optional<Integer> rightColor = getColor(rightInputTank);
                rightColor.ifPresent(integer -> colorizationData.add(new ColorizationData(integer, rightInputTank.getAmount(), rightInputTank.getAmount())));
            }
            if (!solidColorPaint.isEmpty()) {
                final Optional<Integer> solidColor = ColorizationRegistry.getInstance().getColorFor(solidColorPaint);
                solidColor.ifPresent(integer -> colorizationData.add(new ColorizationData(integer, 0, IFluidManager.getInstance().getBucketAmount())));
            }

            if (colorizationData.size() >= 2) {
                final int totalSum = colorizationData.stream().mapToInt(data -> (int) data.pigmentAmount()).sum();
                final int red = (int) colorizationData.stream().mapToLong(data -> data.red() * data.pigmentAmount()).sum() / totalSum;
                final int green = (int) colorizationData.stream().mapToLong(data -> data.green() * data.pigmentAmount()).sum() / totalSum;
                final int blue = (int) colorizationData.stream().mapToLong(data -> data.blue() * data.pigmentAmount()).sum() / totalSum;
                final int totalFluidAmount = (int) colorizationData.stream().mapToLong(ColorizationData::fluidAmount).sum();

                leftInputTank.clear();
                rightInputTank.clear();
                solidColorPaint = ItemStack.EMPTY;

                getPrimaryTank().setContents(new FluidInformation(Fluids.PAINT.fluid().get(), totalFluidAmount, Util.make(new CompoundTag(), tag -> {
                    tag.putInt("r", red);
                    tag.putInt("g", green);
                    tag.putInt("b", blue);
                })));

                setChanged();
            }
        }
    }

    @Override
    public void load(final @NotNull CompoundTag pTag)
    {
        super.load(pTag);
        leftInputTank.readFromNBT(pTag.getCompound("leftInputTank"));
        rightInputTank.readFromNBT(pTag.getCompound("rightInputTank"));
        solidColorPaint = ItemStack.of(pTag.getCompound("solidColorPaint"));
    }

    @Override
    protected void saveAdditional(final @NotNull CompoundTag pTag)
    {
        super.saveAdditional(pTag);
        pTag.put("leftInputTank", leftInputTank.writeToNBT(new CompoundTag()));
        pTag.put("rightInputTank", rightInputTank.writeToNBT(new CompoundTag()));
        pTag.put("solidColorPaint", solidColorPaint.save(new CompoundTag()));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag()
    {
        return saveWithFullMetadata();
    }

    @Override
    public ItemStack getCurrent()
    {
        return solidColorPaint;
    }

    @Override
    public void setCurrent(final ItemStack stack)
    {
        solidColorPaint = stack;
        setChanged();
    }

    @Override
    public boolean canPlaceItem(final int pIndex, final @NotNull ItemStack pStack)
    {
        return ColorizationRegistry.getInstance().getColorFor(pStack).isPresent();
    }
}
