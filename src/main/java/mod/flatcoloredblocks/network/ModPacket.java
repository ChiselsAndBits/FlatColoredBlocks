package mod.flatcoloredblocks.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

/**
 * Base Packet to be implemented.
 */
@SuppressWarnings( "rawtypes" )
public abstract class ModPacket implements Packet
{

	EntityPlayerMP serverEntity = null;

	public void server(
			final EntityPlayerMP playerEntity )
	{
		throw new RuntimeException( getClass().getName() + " is not a server packet." );
	}

	public void client()
	{
		throw new RuntimeException( getClass().getName() + " is not a client packet." );
	}

	abstract public void getPayload(
			PacketBuffer buffer );

	abstract public void readPayload(
			PacketBuffer buffer );

	@Override
	public void readPacketData(
			final PacketBuffer buf ) throws IOException
	{
		readPacketData( buf );
	}

	@Override
	public void writePacketData(
			final PacketBuffer buf ) throws IOException
	{
		getPayload( buf );
	}

	@Override
	public void processPacket(
			final INetHandler handler )
	{
		if ( serverEntity == null )
		{
			client();
		}
		else
		{
			server( serverEntity );
		}
	}

}
