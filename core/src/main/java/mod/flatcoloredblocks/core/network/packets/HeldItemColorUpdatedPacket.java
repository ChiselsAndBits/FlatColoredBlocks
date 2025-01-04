package mod.flatcoloredblocks.core.network.packets;

import com.communi.suggestu.scena.core.dist.DistExecutor;
import mod.flatcoloredblocks.core.item.IWithColorItem;
import mod.flatcoloredblocks.core.network.handlers.ClientPacketHandlers;
import mod.flatcoloredblocks.core.util.Constants;
import mod.flatcoloredblocks.core.util.ItemStackUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static com.communi.suggestu.scena.core.dist.Dist.CLIENT;

public final class HeldItemColorUpdatedPacket extends ModPacket
{
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "held_item_color_updated");
    public static final CustomPacketPayload.Type<HeldItemColorUpdatedPacket> TYPE = new CustomPacketPayload.Type<>(ID);

    private int color;

    public HeldItemColorUpdatedPacket(int color) {
        this.color = color;
    }

    public HeldItemColorUpdatedPacket(RegistryFriendlyByteBuf buffer) {
        readPayload(buffer);
    }

    @Override
    public void writePayload(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.color);
    }

    @Override
    public void readPayload(RegistryFriendlyByteBuf buffer) {
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

        withColorItem.setColor(heldItem, color, playerEntity.isCreative());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
