package mod.flatcoloredblocks.core.block.entity;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import mod.flatcoloredblocks.core.fluid.FluidTank;
import mod.flatcoloredblocks.core.registrars.DataComponentTypes;
import mod.flatcoloredblocks.core.registrars.Fluids;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public abstract class PaintContainingBlockEntity extends BlockEntity implements SingleSlotContainer {
    private final FluidTank primaryTank;

    public PaintContainingBlockEntity(final BlockEntityType<?> pType, final BlockPos pPos, final BlockState pBlockState, final FluidTank primaryTank) {
        super(pType, pPos, pBlockState);
        this.primaryTank = primaryTank;
    }

    public FluidTank getPrimaryTank() {
        return primaryTank;
    }

    public FluidTank getInputTank(final int color) {
        return getPrimaryTank();
    }

    public FluidTank getOutputTank() {
        return getPrimaryTank();
    }

    public int insertPaint(final int color, final int amount) {
        final Optional<Integer> currentColor = getColor(getInputTank(color));
        final Optional<FluidInformation> contents = getInputTank(color).getContents();

        if (contents.isPresent() && currentColor.isPresent()) {
            if (currentColor.get() != color) {
                return 0;
            }

            final int amountToInsert = (int) Math.min(amount, getInputTank(color).getMaximalAmount() - getInputTank(color).getAmount());
            getInputTank(color).setAmount(getInputTank(color).getAmount() + amountToInsert);

            setChanged();
            return amountToInsert;
        } else if (contents.isEmpty()) {
            getInputTank(color).setContents(new FluidInformation(Fluids.PAINT.fluid().get(), amount, DataComponentPatch.builder()
                    .set(DataComponentTypes.COLOR.get(), color).build()));

            setChanged();
            return amount;
        } else {
            return 0;
        }

    }

    public int extractPaint(final long bucketAmount) {
        if (getOutputTank().getContents().isEmpty())
            return 0;

        final int amountToExtract = (int) Math.min(bucketAmount, getOutputTank().getAmount());
        getOutputTank().setAmount(getOutputTank().getAmount() - amountToExtract);

        setChanged();

        return amountToExtract;
    }

    public Optional<Integer> getColor() {
        return getColor(getPrimaryTank());
    }

    @SuppressWarnings({"DataFlowIssue", "OptionalAssignedToNull"})
    public Optional<Integer> getColor(final FluidTank tank) {
        return tank.getContents()
                .map(FluidInformation::data)
                .filter(data -> data.get(DataComponentTypes.COLOR.get()) != null)
                .flatMap(data -> data.get(DataComponentTypes.COLOR.get()));
    }

    public Collection<FluidTank> getTanks() {
        return Collections.singleton(getPrimaryTank());
    }


    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pProvider) {
        super.loadAdditional(pTag, pProvider);
        getPrimaryTank().readFromNBT(pTag.getCompound("primaryTank"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pProvider) {
        super.saveAdditional(pTag, pProvider);
        pTag.put("primaryTank", getPrimaryTank().writeToNBT(new CompoundTag()));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pProvider) {
        final CompoundTag pTag = super.getUpdateTag(pProvider);
        pTag.put("primaryTank", getPrimaryTank().writeToNBT(new CompoundTag()));
        return pTag;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (getLevel() != null) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
