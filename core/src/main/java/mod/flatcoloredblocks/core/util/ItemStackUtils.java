package mod.flatcoloredblocks.core.util;

import mod.flatcoloredblocks.core.item.IWithColorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ItemStackUtils {

    private ItemStackUtils() {
        throw new IllegalStateException("Can not instantiate an instance of: ItemStackUtils. This is a utility class");
    }

    public static ItemStack getWithColorItemStackFromPlayer(Player playerEntity) {
        if (playerEntity == null)
        {
            return ItemStack.EMPTY;
        }

        if (playerEntity.getMainHandItem().getItem() instanceof IWithColorItem)
        {
            return playerEntity.getMainHandItem();
        }

        if (playerEntity.getOffhandItem().getItem() instanceof IWithColorItem)
        {
            return playerEntity.getOffhandItem();
        }

        return ItemStack.EMPTY;

    }
}
