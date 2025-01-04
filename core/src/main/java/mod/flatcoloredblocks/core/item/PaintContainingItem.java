package mod.flatcoloredblocks.core.item;

import mod.flatcoloredblocks.core.ColorNameManager;
import mod.flatcoloredblocks.core.registrars.DataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class PaintContainingItem extends Item implements IWithColorItem
{
    private static final String COLOR = "color";
    private static final String AMOUNT = "amount";

    private final int capacity;

    public PaintContainingItem(final Properties pProperties, final int capacity)
    {
        super(pProperties);
        this.capacity = capacity;
    }

    @Override
    public void fillItemCategory(final @NotNull Consumer<ItemStack> pItems)
    {
        pItems.accept(new ItemStack(this));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF000000);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF00FF00);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF0000FF);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFFFFFF00);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF00FFFF);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFFFF00FF);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFFFFFFFF);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF000000);
            pStack.set(DataComponentTypes.AMOUNT.get(), getCapacity());
        }));
    }

    public int getCapacity()
    {
        return capacity;
    }

    public final int getColor(final ItemStack pStack)
    {
        return pStack.getOrDefault(DataComponentTypes.COLOR.get(), 0);
    }

    public final void setColor(final ItemStack pStack, final int pColor, boolean setByCreativePlayer)
    {
        pStack.set(DataComponentTypes.COLOR.get(), pColor);
        if (setByCreativePlayer) {
            setAmount(pStack, getCapacity());
        }
    }

    public final int getAmount(final ItemStack pStack)
    {
        return pStack.getOrDefault(DataComponentTypes.AMOUNT.get(), 0);
    }

    public final void setAmount(final ItemStack pStack, final int pAmount)
    {
        pStack.set(DataComponentTypes.AMOUNT.get(), Math.min(Math.max(0, pAmount), getCapacity()));
    }

    public final void addAmount(final ItemStack pStack, final int pAmount)
    {
        setAmount(pStack, getAmount(pStack) + pAmount);
    }

    public final void removeAmount(final ItemStack pStack, final int pAmount)
    {
        setAmount(pStack, getAmount(pStack) - pAmount);
    }

    public final boolean hasAmount(final ItemStack pStack)
    {
        return getAmount(pStack) >= 1;
    }
    public final boolean hasAmount(final ItemStack pStack, final int pAmount)
    {
        return getAmount(pStack) >= pAmount;
    }

    protected abstract Component getNameWithContents(final int amount, final Component colorName);

    @Override
    public @NotNull Component getName(final @NotNull ItemStack pStack)
    {
        if (!hasAmount(pStack))
            return super.getName(pStack);

        final int color = getColor(pStack);
        final int amount = getAmount(pStack);
        final Component colorName = ColorNameManager.getInstance().getNameSuffixed(color);

        return getNameWithContents(amount, colorName);
    }

    @Override
    public void appendHoverText(final @NotNull ItemStack pStack, @Nullable final TooltipContext pLevel, final @NotNull List<Component> pTooltipComponents, final @NotNull TooltipFlag pIsAdvanced)
    {
        if (hasAmount(pStack)) {
            final int color = getColor(pStack);

            final int red = (color >> 16) & 0xFF;
            final int green = (color >> 8) & 0xFF;
            final int blue = color & 0xFF;

            pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.red", red).withStyle(ChatFormatting.RED));
            pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.green", green).withStyle(ChatFormatting.GREEN));
            pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.blue", blue).withStyle(ChatFormatting.BLUE));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
