package mod.flatcoloredblocks.core.block.entity;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import mod.flatcoloredblocks.core.fluid.FluidTank;
import mod.flatcoloredblocks.core.registrars.Fluids;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
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

public abstract class PaintContainingBlockEntity extends BlockEntity implements SingleSlotContainer
{
    private final FluidTank primaryTank;

    public PaintContainingBlockEntity(final BlockEntityType<?> pType, final BlockPos pPos, final BlockState pBlockState, final FluidTank primaryTank)
    {
        super(pType, pPos, pBlockState);
        this.primaryTank = primaryTank;
    }

    public FluidTank getPrimaryTank()
    {
        return primaryTank;
    }

    public FluidTank getInputTank(final int color) {
        return getPrimaryTank();
    }

    public FluidTank getOutputTank() {
        return getPrimaryTank();
    }

    public int insertPaint(final int color, final int amount)
    {
        final Optional<Integer> currentColor = getColor(getInputTank(color));
        final Optional<FluidInformation> contents = getInputTank(color).getContents();

        if (contents.isPresent() && currentColor.isPresent())
        {
            if (currentColor.get() != color) {
                return 0;
            }

            final int amountToInsert = (int) Math.min(amount, getInputTank(color).getMaximalAmount() - getInputTank(color).getAmount());
            getInputTank(color).setAmount(getInputTank(color).getAmount() + amountToInsert);

            setChanged();
            return amountToInsert;
        }
        else if (contents.isEmpty())
        {
            getInputTank(color).setContents(new FluidInformation(Fluids.PAINT.fluid().get(), amount, Util.make(new CompoundTag(), tag -> {
                tag.putInt("r", color >> 16 & 0xFF);
                tag.putInt("g", color >> 8 & 0xFF);
                tag.putInt("b", color & 0xFF);
            })));

            setChanged();
            return amount;
        }
        else
        {
            return 0;
        }

    }

    public int extractPaint(final long bucketAmount)
    {
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

    public Optional<Integer> getColor(final FluidTank tank) {
        return tank.getContents()
                       .map(FluidInformation::data)
                       .map(data -> data.getInt("r") << 16 | data.getInt("g") << 8 | data.getInt("b"));
    }

    public Collection<FluidTank> getTanks() {
        return Collections.singleton(getPrimaryTank());
    }

    @Override
    public void load(final @NotNull CompoundTag pTag)
    {
        super.load(pTag);
        getPrimaryTank().readFromNBT(pTag.getCompound("primaryTank"));
    }

    @Override
    protected void saveAdditional(final @NotNull CompoundTag pTag)
    {
        super.saveAdditional(pTag);
        pTag.put("primaryTank", getPrimaryTank().writeToNBT(new CompoundTag()));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag()
    {
        final CompoundTag pTag = super.getUpdateTag();
        pTag.put("primaryTank", getPrimaryTank().writeToNBT(new CompoundTag()));
        return pTag;
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        if (getLevel() != null)
        {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
