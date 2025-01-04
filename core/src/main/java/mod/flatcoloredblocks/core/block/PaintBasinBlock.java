package mod.flatcoloredblocks.core.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import mod.flatcoloredblocks.core.block.entity.PaintBasinBlockEntity;
import mod.flatcoloredblocks.core.block.entity.PaintMixerBlockEntity;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.stream.Stream;

public class PaintBasinBlock extends BaseEntityBlock
{
    private static final MapCodec<PaintBasinBlock> CODEC = simpleCodec(PaintBasinBlock::new);

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(0, 4, 0, 16, 15, 2),
            Block.box(1, 15, 1, 15, 16, 2),
            Block.box(1, 15, 14, 15, 16, 15),
            Block.box(1, 15, 2, 2, 16, 14),
            Block.box(14, 15, 2, 15, 16, 14),
            Block.box(0, 4, 14, 16, 15, 16),
            Block.box(0, 4, 2, 2, 15, 14),
            Block.box(14, 4, 2, 16, 15, 14),
            Block.box(1, 3, 1, 15, 5, 15),
            Block.box(12.5, 0, 0.5, 15.5, 4, 3.5),
            Block.box(12.5, 0, 12.5, 15.5, 4, 15.5),
            Block.box(0.5, 0, 12.5, 3.5, 4, 15.5),
            Block.box(0.5, 0, 0.5, 3.5, 4, 3.5),
            Block.box(3.5, 1, 1.5, 12.5, 2, 2.5),
            Block.box(3.5, 1, 13.5, 12.5, 2, 14.5),
            Block.box(1.5, 1, 3.5, 2.5, 2, 12.5),
            Block.box(13.5, 1, 3.5, 14.5, 2, 12.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public PaintBasinBlock(final Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(final @NotNull BlockState pState)
    {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(final @NotNull BlockPos pPos, final @NotNull BlockState pState)
    {
        return BlockEntityTypes.PAINT_BASIN.get().create(pPos, pState);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide())
        {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof PaintBasinBlockEntity paintBasinBlockEntity)
            {
                if (!paintBasinBlockEntity.getItem(0).isEmpty()) {
                    player.addItem(paintBasinBlockEntity.getItem(0));
                    paintBasinBlockEntity.setItem(0, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }

        }

        return InteractionResult.PASS;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide())
        {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof PaintBasinBlockEntity paintBasinBlockEntity)
            {
                if (paintBasinBlockEntity.canPlaceItem(0, stack)) {
                    paintBasinBlockEntity.setItem(0, stack.split(1));
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
