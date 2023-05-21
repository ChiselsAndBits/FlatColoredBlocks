package mod.flatcoloredblocks.core.item;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.ColorNameManager;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public void fillItemCategory(final @NotNull CreativeModeTab pCategory, final @NotNull NonNullList<ItemStack> pItems)
    {
        if (this.allowedIn(pCategory)) {
            pItems.add(new ItemStack(this));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFFFF0000);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFF00FF00);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFF0000FF);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFFFFFF00);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFF00FFFF);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFFFF00FF);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFFFFFFFF);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
            pItems.add(Util.make(new ItemStack(this), (pStack) -> {
                pStack.getOrCreateTag().putInt("color", 0xFF000000);
                pStack.getOrCreateTag().putInt("amount", getCapacity());
            }));
        }
    }

    public int getCapacity()
    {
        return capacity;
    }

    public final int getColor(final ItemStack pStack)
    {
        if (!pStack.hasTag())
            return 0;

        return pStack.getOrCreateTag().getInt(COLOR);
    }

    public final void setColor(final ItemStack pStack, final int pColor)
    {
        pStack.getOrCreateTag().putInt(COLOR, pColor);
    }

    public final int getAmount(final ItemStack pStack)
    {
        if (!pStack.hasTag())
            return 0;

        return pStack.getOrCreateTag().getInt(AMOUNT);
    }

    public final void setAmount(final ItemStack pStack, final int pAmount)
    {
        pStack.getOrCreateTag().putInt(AMOUNT, Math.max(0, Math.min(pAmount, capacity)));
        if (pAmount == 0) {
            pStack.setTag(null);
        }
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
    public void appendHoverText(final @NotNull ItemStack pStack, @Nullable final Level pLevel, final @NotNull List<Component> pTooltipComponents, final @NotNull TooltipFlag pIsAdvanced)
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
