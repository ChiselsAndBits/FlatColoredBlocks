package mod.flatcoloredblocks.core.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class HoverTextUtils {

    private HoverTextUtils() {
        throw new IllegalStateException("Tried to instantiate: 'HoverTextUtils', but this is a utility class.");
    }

    public static void appendColorHoverText(@NotNull List<Component> pTooltipComponents, int color) {
        final int red = (color >> 16) & 0xFF;
        final int green = (color >> 8) & 0xFF;
        final int blue = color & 0xFF;

        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.red", red).withStyle(ChatFormatting.RED));
        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.green", green).withStyle(ChatFormatting.GREEN));
        pTooltipComponents.add(Component.translatable("item.flatcoloredblocks.tooltip.color.blue", blue).withStyle(ChatFormatting.BLUE));
    }
}
