package mod.flatcoloredblocks.core.item;

import mod.flatcoloredblocks.core.ColorNameManager;
import mod.flatcoloredblocks.core.registrars.DataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SolidDyeItem extends Item implements IWithColorItem {

    public SolidDyeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(final @NotNull ItemStack pStack) {
        final int color = getColor(pStack);
        final Component colorName = ColorNameManager.getInstance().getNameSuffixed(color);

        return Component.translatable("item.flatcoloredblocks.solid_dye", colorName);
    }

    @Override
    public void appendHoverText(final @NotNull ItemStack pStack, @Nullable final TooltipContext pLevel, final @NotNull List<Component> pTooltipComponents, final @NotNull TooltipFlag pIsAdvanced) {
        final int color = getColor(pStack);

        final int red = (color >> 16) & 0xFF;
        final int green = (color >> 8) & 0xFF;
        final int blue = color & 0xFF;

        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.red", red).withStyle(ChatFormatting.RED));
        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.green", green).withStyle(ChatFormatting.GREEN));
        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.blue", blue).withStyle(ChatFormatting.BLUE));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void setColor(ItemStack target, int value, boolean setByCreativePlayer) {
        target.set(DataComponentTypes.COLOR.get(), value);
    }

    @Override
    public void fillItemCategory(final @NotNull Consumer<ItemStack> pItems)
    {
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFFFF0000);
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF00FF00);
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF0000FF);
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFFFFFF00);
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF00FFFF);
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFFFF00FF);
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFFFFFFFF);
        }));
        pItems.accept(Util.make(new ItemStack(this), (pStack) -> {
            pStack.set(DataComponentTypes.COLOR.get(), 0xFF000000);
        }));
    }

    public final int getColor(final ItemStack pStack)
    {
        return pStack.getOrDefault(DataComponentTypes.COLOR.get(), 0xFFFFFFFF);
    }
}
