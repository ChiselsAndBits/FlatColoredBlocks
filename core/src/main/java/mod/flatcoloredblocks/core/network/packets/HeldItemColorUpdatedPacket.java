package mod.flatcoloredblocks.core.network.packets;

import com.communi.suggestu.scena.core.dist.DistExecutor;
import mod.flatcoloredblocks.core.item.IWithColorItem;
import mod.flatcoloredblocks.core.network.handlers.ClientPacketHandlers;
import mod.flatcoloredblocks.core.util.ItemStackUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static com.communi.suggestu.scena.core.dist.Dist.CLIENT;

public final class HeldItemColorUpdatedPacket extends ModPacket
{
    private int color;

    public HeldItemColorUpdatedPacket(int color) {
        this.color = color;
    }

    public HeldItemColorUpdatedPacket(FriendlyByteBuf buffer) {
        readPayload(buffer);
    }

    @Override
    public void writePayload(FriendlyByteBuf buffer) {
        buffer.writeInt(this.color);
    }

    @Override
    public void readPayload(FriendlyByteBuf buffer) {
        this.color = buffer.readInt();
    }

    @Override
    public void client() {
        DistExecutor.unsafeRunWhenOn(CLIENT, () -> () -> ClientPacketHandlers.handleHeldItemColorUpdatedPacket(color));
    }

    @Override
    public void server(ServerPlayer playerEntity) {
        final ItemStack heldItem = ItemStackUtils.getWithColorItemStackFromPlayer(playerEntity);
        if (heldItem.isEmpty())
            return;

        if (!(heldItem.getItem() instanceof IWithColorItem withColorItem))
            return;

        withColorItem.setColor(heldItem, color);
    }
}
