package mod.flatcoloredblocks.core.block.entity;

import com.communi.suggestu.scena.core.client.models.data.IBlockModelData;
import com.communi.suggestu.scena.core.client.models.data.IModelDataManager;
import com.communi.suggestu.scena.core.dist.Dist;
import com.communi.suggestu.scena.core.dist.DistExecutor;
import com.communi.suggestu.scena.core.entity.block.IBlockEntityWithModelData;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import mod.flatcoloredblocks.core.util.ModelDataUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ColoredBlockEntity extends BlockEntity implements IBlockEntityWithModelData
{
    private int color = 0xFFFFFFFF;
    private IBlockModelData modelData = ModelDataUtils.createModelDataForColor(color);

    public ColoredBlockEntity(final BlockPos pPos, final BlockState pBlockState)
    {
        super(BlockEntityTypes.COLORED_BLOCK.get(), pPos, pBlockState);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pProvider) {
        super.loadAdditional(pTag, pProvider);

        color = 0xFFFFFFFF;

        if (pTag.contains("color", Tag.TAG_INT)) {
            color = pTag.getInt("color");
        }

        updateModelDataIfInLoadedChunk();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pProvider) {
        super.saveAdditional(pTag, pProvider);
        pTag.putInt("color", color);
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        if (getLevel() != null)
        {
            getLevel().sendBlockUpdated(getBlockPos(), Blocks.AIR.defaultBlockState(), getBlockState(), Block.UPDATE_ALL);
            updateModelData();
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider pProvider) {
        return super.saveWithFullMetadata(pProvider);
    }

    public void updateModelData()
    {
        this.modelData = ModelDataUtils.createModelDataForColor(this.color);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> IModelDataManager.getInstance().requestModelDataRefresh(this));
        Objects.requireNonNull(getLevel()).sendBlockUpdated(getBlockPos(), Blocks.AIR.defaultBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    private void updateModelDataIfInLoadedChunk()
    {
        if (level != null && level.isClientSide())
            updateModelData();
    }


    public void setModelData(final IBlockModelData modelData)
    {
        this.modelData = modelData;
    }

    @NotNull
    public IBlockModelData getBlockModelData()
    {
        return this.modelData;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(final int color)
    {
        this.color = color;
        setChanged();
    }
}
