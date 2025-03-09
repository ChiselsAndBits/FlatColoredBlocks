package mod.flatcoloredblocks.core.block.entity;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.fluid.FluidTank;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import mod.flatcoloredblocks.core.registrars.DataComponentTypes;
import mod.flatcoloredblocks.core.registrars.Fluids;
import mod.flatcoloredblocks.core.registrars.Items;
import mod.flatcoloredblocks.core.registry.ColorizationRegistry;
import mod.flatcoloredblocks.core.util.ColorizationData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.minecraft.world.level.material.Fluids.WATER;

public final class PaintMixerBlockEntity extends PaintContainingBlockEntity implements SingleSlotContainer {
    private final FluidTank leftInputTank = new FluidTank(IFluidManager.getInstance().getBucketAmount());
    private final FluidTank rightInputTank = new FluidTank(IFluidManager.getInstance().getBucketAmount());

    private ItemStack solidColorPaint = ItemStack.EMPTY;

    public PaintMixerBlockEntity(final BlockPos pPos, final BlockState pBlockState) {
        super(BlockEntityTypes.PAINT_MIXER.get(), pPos, pBlockState, new FluidTank(IFluidManager.getInstance().getBucketAmount() * 2));
    }

    @Override
    public FluidTank getInputTank(final int color) {
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
    public FluidTank getOutputTank() {
        if (getPrimaryTank().getAmount() > 0)
            return getPrimaryTank();

        if (rightInputTank.getAmount() > 0)
            return rightInputTank;

        return leftInputTank;
    }

    @Override
    public int extractPaint(final long bucketAmount) {
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
    public Collection<FluidTank> getTanks() {
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
    public Optional<Integer> getColor() {
        return getColor(getOutputTank());
    }

    public void onPowerChanged(final boolean isPowered) {
        if (isPowered) {
            final Set<ColorizationData> colorizationData = new HashSet<>();
            handleColorizationInTank(colorizationData, leftInputTank);
            handleColorizationInTank(colorizationData, rightInputTank);

            if (solidColorPaint.getItem() == Items.SOLID_DYE.get()) {
                colorizationData.add(new ColorizationData(Items.SOLID_DYE.get().getColor(solidColorPaint), 1, 0, IFluidManager.getInstance().getBucketAmount()));
            } else if (!solidColorPaint.isEmpty()) {
                final Optional<Integer> solidColor = ColorizationRegistry.getInstance().getColorFor(solidColorPaint);
                solidColor.ifPresent(integer -> colorizationData.add(new ColorizationData(integer, 1, 0, IFluidManager.getInstance().getBucketAmount())));
            }

            if (colorizationData.size() >= 2) {
                final int totalSum = colorizationData.stream().mapToInt(data -> (int) data.pigmentAmount()).sum();
                final int red = (int) colorizationData.stream().mapToLong(data -> (long) (data.red() * data.fluidImpressionFactor() * data.pigmentAmount())).sum() / totalSum;
                final int green = (int) colorizationData.stream().mapToLong(data -> (long) (data.green() * data.fluidImpressionFactor() * data.pigmentAmount())).sum() / totalSum;
                final int blue = (int) colorizationData.stream().mapToLong(data -> (long) (data.blue() * data.fluidImpressionFactor() * data.pigmentAmount())).sum() / totalSum;
                final int totalFluidAmount = (int) colorizationData.stream().mapToLong(ColorizationData::fluidAmount).sum();

                final int color = FastColor.ARGB32.color(0xFF, red, green, blue);

                leftInputTank.clear();
                rightInputTank.clear();
                solidColorPaint = ItemStack.EMPTY;

                getPrimaryTank().setContents(new FluidInformation(Fluids.PAINT.fluid().get(), totalFluidAmount, DataComponentPatch.builder()
                        .set(DataComponentTypes.COLOR.get(), color).build()));

                setChanged();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void handleColorizationInTank(Set<ColorizationData> colorizationData, final FluidTank tank) {
        if (tank.getAmount() > 0) {
            if (tank.getContents().map(contents -> contents.fluid().is(FluidTags.WATER)).orElse(false)) {
                colorizationData.add(new ColorizationData(0xFFFFFF, 0, tank.getAmount(), 0));
                return;
            }

            final Optional<Integer> leftColor = getColor(tank);
            leftColor.ifPresent(integer -> colorizationData.add(new ColorizationData(integer, 1f, tank.getAmount(), tank.getAmount())));
        }
    }


    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pProvider) {
        super.loadAdditional(pTag, pProvider);

        leftInputTank.readFromNBT(pTag.getCompound("leftInputTank"));
        rightInputTank.readFromNBT(pTag.getCompound("rightInputTank"));

        CompoundTag solidColorPaintData = pTag.getCompound("solidColorPaint");
        solidColorPaint = ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, solidColorPaintData).result().orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pProvider) {
        super.saveAdditional(pTag, pProvider);
        pTag.put("leftInputTank", leftInputTank.writeToNBT(new CompoundTag()));
        pTag.put("rightInputTank", rightInputTank.writeToNBT(new CompoundTag()));
        pTag.put("solidColorPaint", ItemStack.OPTIONAL_CODEC.encodeStart(NbtOps.INSTANCE, solidColorPaint).result().orElseThrow());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pProvider) {
        return saveWithFullMetadata(pProvider);
    }

    @Override
    public ItemStack getCurrent() {
        return solidColorPaint;
    }

    @Override
    public void setCurrent(final ItemStack stack) {
        solidColorPaint = stack;
        setChanged();
    }

    @Override
    public boolean canPlaceItem(final int pIndex, final @NotNull ItemStack pStack) {
        return pStack.getItem() == Items.SOLID_DYE.get() || ColorizationRegistry.getInstance().getColorFor(pStack).isPresent();
    }

    public void insertWater(long amount) {
        if (leftInputTank.isEmpty()) {
            leftInputTank.setContents(new FluidInformation(WATER, amount, null));
        } else if (rightInputTank.isEmpty()) {
            rightInputTank.setContents(new FluidInformation(WATER, amount, null));
        }
        setChanged();
    }

}
