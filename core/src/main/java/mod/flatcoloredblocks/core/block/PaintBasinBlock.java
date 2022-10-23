package mod.flatcoloredblocks.core.block;

import com.google.common.collect.ImmutableMap;
import mod.flatcoloredblocks.core.block.entity.PaintBasinBlockEntity;
import mod.flatcoloredblocks.core.block.entity.PaintMixerBlockEntity;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
    public @NotNull InteractionResult use(final @NotNull BlockState pState, final @NotNull Level pLevel, final @NotNull BlockPos pPos, final @NotNull Player pPlayer, final @NotNull InteractionHand pHand, final @NotNull BlockHitResult pHit)
    {
        if (!pLevel.isClientSide)
        {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof PaintBasinBlockEntity paintBasinBlockEntity)
            {
                final ItemStack stack = pPlayer.getItemInHand(pHand);
                if (!stack.isEmpty() && paintBasinBlockEntity.canPlaceItem(0, stack)) {
                    final ItemStack toInsert = stack.split(1);
                    paintBasinBlockEntity.setItem(0, toInsert);
                }
                else if (stack.isEmpty() && !paintBasinBlockEntity.getItem(0).isEmpty()) {
                    pPlayer.addItem(paintBasinBlockEntity.getItem(0));
                    paintBasinBlockEntity.setItem(0, ItemStack.EMPTY);
                }
            }

        }

        return InteractionResult.PASS;
    }
}
