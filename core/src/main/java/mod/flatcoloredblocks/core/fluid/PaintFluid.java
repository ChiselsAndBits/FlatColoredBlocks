package mod.flatcoloredblocks.core.fluid;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import com.communi.suggestu.scena.core.fluid.FluidWithHandler;
import com.communi.suggestu.scena.core.fluid.IFluidManager;
import com.communi.suggestu.scena.core.fluid.IFluidVariantHandler;
import mod.flatcoloredblocks.core.registrars.Fluids;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.minecraft.world.level.material.Fluids.WATER;

public class PaintFluid extends FluidWithHandler
{
    @Override
    public @NotNull Item getBucket()
    {
        return Items.BUCKET;
    }

    @Override
    protected boolean canBeReplacedWith(final @NotNull FluidState pState, final @NotNull BlockGetter pLevel, final @NotNull BlockPos pPos, final @NotNull Fluid pFluid, final @NotNull Direction pDirection)
    {
        return false;
    }

    @Override
    protected @NotNull Vec3 getFlow(final @NotNull BlockGetter pBlockReader, final @NotNull BlockPos pPos, final @NotNull FluidState pFluidState)
    {
        return Vec3.ZERO;
    }

    @Override
    public int getTickDelay(final @NotNull LevelReader pLevel)
    {
        return 0;
    }

    @Override
    protected float getExplosionResistance()
    {
        return 0;
    }

    @Override
    public float getHeight(final @NotNull FluidState pState, final @NotNull BlockGetter pLevel, final @NotNull BlockPos pPos)
    {
        return 0;
    }

    @Override
    public float getOwnHeight(final @NotNull FluidState pState)
    {
        return 0;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(final @NotNull FluidState pState)
    {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSource(final @NotNull FluidState pState)
    {
        return true;
    }

    @Override
    public int getAmount(final @NotNull FluidState pState)
    {
        return 0;
    }

    @Override
    public @NotNull VoxelShape getShape(final @NotNull FluidState pState, final @NotNull BlockGetter pLevel, final @NotNull BlockPos pPos)
    {
        return Shapes.empty();
    }

    @Override
    public IFluidVariantHandler getVariantHandler()
    {
        return Fluids.PAINT.variantHandler().get();
    }

    public static final class VariantHandler implements IFluidVariantHandler {

        @Override
        public Component getName(final FluidInformation fluidInformation)
        {
            return Component.translatable("block.minecraft.paint");
        }

        @Override
        public Optional<SoundEvent> getFillSound(final FluidInformation fluidInformation)
        {
            return IFluidManager.getInstance().getVariantHandlerFor(WATER)
                           .flatMap(handler -> handler.getFillSound(new FluidInformation(WATER)));
        }

        @Override
        public Optional<SoundEvent> getEmptySound(final FluidInformation fluidInformation)
        {
            return IFluidManager.getInstance().getVariantHandlerFor(WATER)
                                .flatMap(handler -> handler.getEmptySound(new FluidInformation(WATER)));
        }

        @Override
        public int getLuminance(final FluidInformation fluidInformation)
        {
            return IFluidManager.getInstance().getVariantHandlerFor(WATER)
                                .map(handler -> handler.getLuminance(new FluidInformation(WATER)))
                                .orElse(0);
        }

        @Override
        public int getTemperature(final FluidInformation fluidInformation)
        {
            return IFluidManager.getInstance().getVariantHandlerFor(WATER)
                                .map(handler -> handler.getTemperature(new FluidInformation(WATER)))
                                .orElse(0);
        }

        @Override
        public int getViscosity(final FluidInformation fluidInformation)
        {
            return IFluidManager.getInstance().getVariantHandlerFor(WATER)
                                .map(handler -> handler.getViscosity(new FluidInformation(WATER)))
                                .orElse(0);
        }

        @Override
        public int getDensity(final FluidInformation fluidInformation)
        {
            return IFluidManager.getInstance().getVariantHandlerFor(WATER)
                                .map(handler -> handler.getDensity(new FluidInformation(WATER)))
                                .orElse(0);
        }

        @Override
        public int getTintColor(final FluidInformation fluidInformation)
        {
            final int alpha = fluidInformation.data().getInt("a") & 0xFF;
            final int red = fluidInformation.data().getInt("r") & 0xFF;
            final int green = fluidInformation.data().getInt("g") & 0xFF;
            final int blue = fluidInformation.data().getInt("b") & 0xFF;

            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        @Override
        public Optional<ResourceLocation> getStillTexture(final FluidInformation fluidInformation)
        {
            return Optional.of(new ResourceLocation(Constants.MOD_ID, "block/paint_still"));
        }

        @Override
        public Optional<ResourceLocation> getFlowingTexture(final FluidInformation fluidInformation)
        {
            return getStillTexture(fluidInformation);
        }
    }
}
