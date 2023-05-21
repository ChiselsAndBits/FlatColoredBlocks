package mod.flatcoloredblocks.core.network.handlers;

import mod.flatcoloredblocks.core.item.IWithColorItem;
import mod.flatcoloredblocks.core.util.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ClientPacketHandlers
{

    private ClientPacketHandlers()
    {
        throw new IllegalStateException("Can not instantiate an instance of: ClientPacketHandlers. This is a utility class");
    }

    public static void handleHeldItemColorUpdatedPacket(final int color) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Player player = minecraft.player;
        final ItemStack heldItem = ItemStackUtils.getWithColorItemStackFromPlayer(player);
        if (heldItem.isEmpty())
            return;

        if (!(heldItem.getItem() instanceof IWithColorItem withColorItem))
            return;

        withColorItem.setColor(heldItem, color);
    }
}
