package mod.flatcoloredblocks.core.util;

import mod.flatcoloredblocks.core.ColorNameManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class NameUtils {

    private NameUtils() {
        throw new IllegalStateException("Tried to instantiate: 'NameUtils', but this is a utility class.");
    }

    @NotNull
    public static MutableComponent getName(int color, Component blockName) {
        final Component colorName = ColorNameManager.getInstance().getNameSuffixed(color);

        return Component.translatable("item.flatcoloredblocks.colored_block.with_contents", colorName, blockName);
    }
}
