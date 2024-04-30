package mod.flatcoloredblocks.core.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IWithColorItem {
    /**
     * Sets the color on the target stack to the given value.
     *
     * @param target              The stack to set the color on.
     * @param value               The color value to set.
     * @param setByCreativePlayer True if the color was set by a creative player.
     */
    void setColor(ItemStack target, int value, boolean setByCreativePlayer);

    /**
     * Fills the item category with the target stack.
     *
     * @param pItems The consumer which adds an item stack to the category.
     */
    void fillItemCategory(@NotNull Consumer<ItemStack> pItems);

    /**
     * Gets the color from the target stack.
     *
     * @param target The stack to get the color from.
     * @return The color value.
     */
    int getColor(ItemStack target);
}
