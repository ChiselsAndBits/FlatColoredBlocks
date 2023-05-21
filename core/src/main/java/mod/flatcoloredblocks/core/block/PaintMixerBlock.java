package mod.flatcoloredblocks.core.block;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.block.entity.PaintMixerBlockEntity;
import mod.flatcoloredblocks.core.fluid.FluidTank;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class PaintMixerBlock extends HorizontalDirectionalBlock implements EntityBlock
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

    public PaintMixerBlock(final Properties pProperties)
    {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(FACING, p_48689_.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(final @NotNull BlockState pState)
    {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean triggerEvent(final @NotNull BlockState pState, final @NotNull Level pLevel, final @NotNull BlockPos pPos, final int pId, final int pParam)
    {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity != null && blockentity.triggerEvent(pId, pParam);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final @NotNull BlockState pState, final Level pLevel, final @NotNull BlockPos pPos)
    {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof MenuProvider ? (MenuProvider)blockentity : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(final @NotNull BlockState pState, final @NotNull Level pLevel, final @NotNull BlockPos pPos, final @NotNull Player pPlayer, final @NotNull InteractionHand pHand, final @NotNull BlockHitResult pHit)
    {
        if (!pLevel.isClientSide)
        {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof PaintMixerBlockEntity paintMixerBlockEntity)
            {
                final ItemStack stack = pPlayer.getItemInHand(pHand);
                if (!stack.isEmpty()) {
                    if (paintMixerBlockEntity.canPlaceItem(0, stack)) {
                        final ItemStack toInsert = stack.split(1);
                        paintMixerBlockEntity.setItem(0, toInsert);
                    } else if (IFluidManager.getInstance().get(stack).isPresent()) {
                        IFluidManager.getInstance().get(stack).ifPresent(fluid -> {
                            if (fluid.fluid().is(FluidTags.WATER)) {
                                paintMixerBlockEntity.insertWater(fluid.amount());
                            }
                        });
                    }
                } else if (stack.isEmpty() && !paintMixerBlockEntity.getItem(0).isEmpty()) {
                    pPlayer.addItem(paintMixerBlockEntity.getItem(0));
                    paintMixerBlockEntity.setItem(0, ItemStack.EMPTY);
                }
            }

        }

        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(final @NotNull BlockPos pPos, final @NotNull BlockState pState)
    {
        return BlockEntityTypes.PAINT_MIXER.get().create(pPos, pState);
    }

    @Override
    public void neighborChanged(final @NotNull BlockState pState, final @NotNull Level pLevel, final @NotNull BlockPos pPos, final @NotNull Block pBlock, final @NotNull BlockPos pFromPos, final boolean pIsMoving)
    {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        boolean isPowered = pLevel.hasNeighborSignal(pPos);

        final BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (!(blockEntity instanceof PaintMixerBlockEntity paintMixerBlockEntity))
            return;

        paintMixerBlockEntity.onPowerChanged(isPowered);
    }
}
