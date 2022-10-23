package mod.flatcoloredblocks.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ColoredGlassBlock extends ColoredBlock
{

    public ColoredGlassBlock(final Properties pProperties)
    {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    public boolean skipRendering(@NotNull BlockState blockState, BlockState otherState, @NotNull Direction direction) {
        return otherState.is(this) || super.skipRendering(blockState, otherState, direction);
    }

    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @SuppressWarnings("deprecation")
    public float getShadeBrightness(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return true;
    }
}
