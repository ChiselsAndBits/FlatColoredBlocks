package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import mod.chiselsandbits.api.variant.state.IStateVariant;
import mod.chiselsandbits.api.variant.state.IStateVariantProvider;
import mod.flatcoloredblocks.core.block.ColoredBlock;
import mod.flatcoloredblocks.core.block.entity.ColoredBlockEntity;
import mod.flatcoloredblocks.core.item.ColoredBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class ColoredStateVariantProvider implements IStateVariantProvider {

    private final Supplier<ColoredBlock> blockGetter;

    public ColoredStateVariantProvider(Supplier<ColoredBlock> blockGetter) {
        this.blockGetter = blockGetter;
    }

    @Override
    public Optional<IStateVariant> getStateVariant(BlockState blockState, Optional<BlockEntity> optional) {
        if (blockState.getBlock() != blockGetter.get())
            return Optional.empty();

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
            if (coloredBlockItem.getColoredBlock() != blockGetter.get())
                return Optional.empty();

            return Optional.of(new ColoredStateVariant(coloredBlockItem.getColoredBlock().getColor(itemStack)));
        }

        return Optional.empty();
    }

    @Override
    public Optional<IStateVariant> getStateVariant(FluidInformation fluidInformation) {
        return Optional.empty();
    }

    @Override
    public Collection<IStateVariant> getAllDefaultVariants(BlockState blockState) {
        final Collection<IStateVariant> defaultVariants = new ArrayList<>();
        blockGetter.get().getDefaultColors().forEach(color -> defaultVariants.add(new ColoredStateVariant(color)));
        return defaultVariants;
    }

    @Override
    public CompoundTag serializeNBT(IStateVariant iStateVariant) {
        if (!(iStateVariant instanceof ColoredStateVariant coloredStateVariant))
            return new CompoundTag();

        final CompoundTag tag = new CompoundTag();
        tag.putInt("color", coloredStateVariant.getColor());
        return tag;
    }

    @Override
    public IStateVariant deserializeNBT(CompoundTag compoundTag) {
        if (!compoundTag.contains("color"))
            return ColoredStateVariant.WHITE;

        return new ColoredStateVariant(compoundTag.getInt("color"));
    }

    @Override
    public void serializeInto(FriendlyByteBuf friendlyByteBuf, IStateVariant iStateVariant) {
        if (!(iStateVariant instanceof ColoredStateVariant coloredStateVariant)) {
            friendlyByteBuf.writeInt(-1);
            return;
        }

        friendlyByteBuf.writeInt(coloredStateVariant.getColor());
    }

    @Override
    public IStateVariant deserializeFrom(FriendlyByteBuf friendlyByteBuf) {
        return new ColoredStateVariant(friendlyByteBuf.readInt());
    }

    @Override
    public Optional<ItemStack> getItemStack(IStateVariant iStateVariant) {
        if (!(iStateVariant instanceof ColoredStateVariant coloredStateVariant))
            return Optional.empty();

        final ItemStack stack = new ItemStack(blockGetter.get());
        blockGetter.get().setColor(stack, coloredStateVariant.getColor());
        return Optional.of(stack);
    }

    @Override
    public Optional<FluidInformation> getFluidInformation(IStateVariant iStateVariant, long l) {
        return Optional.empty();
    }
}
