package mod.flatcoloredblocks.core.registry;

import com.google.common.collect.Maps;
import mod.flatcoloredblocks.core.block.ColoredBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ColorizationRegistry
{
    private static final ColorizationRegistry INSTANCE = new ColorizationRegistry();

    public static ColorizationRegistry getInstance()
    {
        return INSTANCE;
    }

    private record ColorizationData(int color, Supplier<ColoredBlock> convertedBlock) {}

    private final Map<Block, ColorizationData> BLOCK_TO_COLOR_MAP = Maps.newHashMap();
    private final Map<Item, Integer> SOLVENT_TO_COLOR_MAP = Maps.newHashMap();

    private ColorizationRegistry()
    {
    }

    public void registerConversion(final Block source, final Supplier<ColoredBlock> target, final int color) {
        BLOCK_TO_COLOR_MAP.put(source, new ColorizationData(color, target));
    }

    public void registerSolvent(final Item solidPaint, final int color) {
        SOLVENT_TO_COLOR_MAP.put(solidPaint, color);
    }

    @NotNull
    public Optional<Integer> getColorFor(final ItemStack pStack)
    {
        return Optional.ofNullable(SOLVENT_TO_COLOR_MAP.get(pStack.getItem()));
    }

    @NotNull
    public Optional<Integer> getColorFor(final BlockState pStack)
    {
        return Optional.ofNullable(BLOCK_TO_COLOR_MAP.get(pStack.getBlock())).map(ColorizationData::color);
    }

    @NotNull
    public Optional<BlockState> getConversionState(final BlockState pStack)
    {
        return Optional.ofNullable(BLOCK_TO_COLOR_MAP.get(pStack.getBlock())).map(ColorizationData::convertedBlock).map(Supplier::get).map(Block::defaultBlockState);
    }

    @NotNull
    public ItemStack convertToColoredBlock(final ItemStack toPaint)
    {
        if (!(toPaint.getItem() instanceof final BlockItem blockItem))
            return toPaint;

        if (!BLOCK_TO_COLOR_MAP.containsKey(blockItem.getBlock()))
            return toPaint;

        final ColorizationData data = BLOCK_TO_COLOR_MAP.get(blockItem.getBlock());
        final ItemStack stack = new ItemStack(data.convertedBlock().get());
        data.convertedBlock().get().setColor(stack, data.color());

        return stack;
    }
}
