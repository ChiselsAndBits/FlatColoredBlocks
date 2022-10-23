package mod.flatcoloredblocks.core.block.entity;

import com.communi.suggestu.scena.core.client.models.data.IBlockModelData;
import com.communi.suggestu.scena.core.client.models.data.IModelDataBuilder;
import com.communi.suggestu.scena.core.client.models.data.IModelDataManager;
import com.communi.suggestu.scena.core.dist.Dist;
import com.communi.suggestu.scena.core.dist.DistExecutor;
import com.communi.suggestu.scena.core.entity.block.IBlockEntityWithModelData;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import mod.flatcoloredblocks.core.registrars.ModelDataKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ColoredBlockEntity extends BlockEntity implements IBlockEntityWithModelData
{
    private int color = 0xFFFFFFFF;
    private IBlockModelData modelData = IModelDataBuilder.create().withInitial(ModelDataKeys.COLOR, color).build();

    public ColoredBlockEntity(final BlockPos pPos, final BlockState pBlockState)
    {
        super(BlockEntityTypes.COLORED_BLOCK.get(), pPos, pBlockState);
    }

    @Override
    public void load(final @NotNull CompoundTag pTag)
    {
        super.load(pTag);

        color = 0xFFFFFFFF;

        if (pTag.contains("color", Tag.TAG_INT)) {
            color = pTag.getInt("color");
        }

        updateModelDataIfInLoadedChunk();
    }

    @Override
    protected void saveAdditional(final @NotNull CompoundTag pTag)
    {
        super.saveAdditional(pTag);
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
    public @NotNull CompoundTag getUpdateTag()
    {
        return super.saveWithFullMetadata();
    }

    public void updateModelData()
    {
        this.modelData = IModelDataBuilder.create()
                                 .withInitial(ModelDataKeys.COLOR, color)
                                 .build();

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
