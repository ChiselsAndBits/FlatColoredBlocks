package mod.flatcoloredblocks.core.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class ModPacket
{

    public ModPacket()
    {
    }

    public void server(
			final ServerPlayer playerEntity )
	{
		throw new RuntimeException( getClass().getName() + " is not a server packet." );
	}

	public void client()
	{
		throw new RuntimeException( getClass().getName() + " is not a client packet." );
	}

	abstract public void writePayload(
			FriendlyByteBuf buffer );

	abstract public void readPayload(
			FriendlyByteBuf buffer );

	public void processPacket(
			final Player senderOnServer,
            final Boolean onServer)
	{
		if (!onServer)
		{
			client();
		}
		else if (senderOnServer instanceof ServerPlayer serverPlayer)
		{
			server( serverPlayer );
		}
	}

}
