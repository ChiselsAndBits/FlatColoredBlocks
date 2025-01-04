package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import com.mojang.serialization.MapCodec;
import mod.chiselsandbits.api.blockinformation.BlockInformation;
import mod.chiselsandbits.api.variant.state.IStateVariant;
import mod.chiselsandbits.api.variant.state.IStateVariantProvider;
import mod.flatcoloredblocks.core.block.ColoredBlock;
import mod.flatcoloredblocks.core.block.entity.ColoredBlockEntity;
import mod.flatcoloredblocks.core.item.ColoredBlockItem;
import mod.flatcoloredblocks.core.util.Constants;
import mod.flatcoloredblocks.core.util.NameUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ColoredStateVariantProvider implements IStateVariantProvider {

    @Override
    public ResourceLocation getRegistryName() {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "color");
    }

    @Override
    public Optional<IStateVariant> getStateVariant(BlockState blockState, Optional<BlockEntity> optional) {
        return optional.filter(ColoredBlockEntity.class::isInstance)
                .map(ColoredBlockEntity.class::cast)
                .map(be -> new ColoredStateVariant(be.getColor()));
    }

    @Override
    public Optional<IStateVariant> getStateVariant(FluidState fluidState) {
        return Optional.empty();
    }

    @Override
    public Optional<IStateVariant> getStateVariant(BlockState blockState, ItemStack itemStack) {
        if (itemStack.getItem() instanceof ColoredBlockItem coloredBlockItem) {
            return Optional.of(new ColoredStateVariant(coloredBlockItem.getColoredBlock().getColor(itemStack)));
        }

        return Optional.empty();
    }

    @Override
    public Optional<IStateVariant> getStateVariant(FluidInformation fluidInformation) {
        return Optional.empty();
    }

    @Override
    public Collection<? extends IStateVariant> getAllDefaultVariants(BlockState blockState) {
        if (blockState.getBlock() instanceof ColoredBlock coloredBlock) {
            final List<IStateVariant> variants = new ArrayList<>();
            for (int color : coloredBlock.getDefaultColors()) {
                variants.add(new ColoredStateVariant(color));
            }
            return variants;
        }
        return List.of();
    }

    @Override
    public Optional<ItemStack> getItemStack(BlockInformation blockInformation) {
        final IStateVariant iStateVariant = blockInformation.variant().orElseThrow();
        if (!(iStateVariant instanceof ColoredStateVariant(int color)))
            return Optional.empty();

        if (!(blockInformation.blockState().getBlock() instanceof ColoredBlock coloredBlock))
            return Optional.empty();

        final ItemStack stack = new ItemStack(coloredBlock);
        coloredBlock.setColor(stack, color);
        return Optional.of(stack);
    }

    @Override
    public Optional<FluidInformation> getFluidInformation(IStateVariant iStateVariant, long l) {
        return Optional.empty();
    }

    @Override
    public Optional<Component> getName(BlockInformation blockInformation) {
        final IStateVariant iStateVariant = blockInformation.variant().orElseThrow();
        if (!(iStateVariant instanceof ColoredStateVariant(int color)))
            return Optional.empty();

        if (!(blockInformation.blockState().getBlock() instanceof ColoredBlock coloredBlock))
            return Optional.empty();

        return Optional.of(NameUtils.getName(color, coloredBlock.getName()));
    }

    @Override
    public MapCodec<? extends IStateVariant> mapCodec() {
        return ColoredStateVariant.MAP_CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ? extends IStateVariant> streamCodec() {
        return ColoredStateVariant.STREAM_CODEC;
    }

    @Override
    public void setFullBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockInformation blockInformation) {
        final IStateVariant iStateVariant = blockInformation.variant().orElseThrow();
        if (!(iStateVariant instanceof ColoredStateVariant(int color)))
            return;

        if (!(blockInformation.blockState().getBlock() instanceof ColoredBlock coloredBlock))
            return;

        levelAccessor.setBlock(
                blockPos,
                coloredBlock.defaultBlockState(),
                Block.UPDATE_ALL
        );
        final BlockEntity blockEntity = levelAccessor.getBlockEntity(blockPos);
        if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
            coloredBlockEntity.setColor(color);
        }
    }

    @Override
    public @NotNull Optional<Integer> getBeaconColorMultiplier(BlockInformation blockInformation, LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1) {
        if (!(blockInformation.blockState().getBlock() instanceof ColoredBlock))
            return Optional.empty();

        final IStateVariant iStateVariant = blockInformation.variant().orElseThrow();
        if (!(iStateVariant instanceof ColoredStateVariant(int color)))
            return Optional.empty();

        return Optional.of(color);
    }
}
