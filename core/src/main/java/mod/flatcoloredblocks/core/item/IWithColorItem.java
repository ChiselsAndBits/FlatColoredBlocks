package mod.flatcoloredblocks.core.item;

import net.minecraft.world.item.ItemStack;

public interface IWithColorItem {
    /**
     * Sets the color on the target stack to the given value.
     *
     * @param target The stack to set the color on.
     * @param value The color value to set.
     */
    void setColor(ItemStack target, int value);

    /**
     * Gets the color from the target stack.
     *
     * @param target The stack to get the color from.
     * @return The color value.
     */
    int getColor(ItemStack target);
}
