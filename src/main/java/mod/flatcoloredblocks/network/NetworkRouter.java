package mod.flatcoloredblocks.network;

import io.netty.buffer.Unpooled;
import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkRegistry;

/**
 * Sends packets and handles networking.
 */
public class NetworkRouter
{

	/**
	 * Receives packets on the client and dispatches them to the processor.
	 */
	private class ClientPacketHandler
	{

		@SuppressWarnings( "unchecked" )
		public void onPacketData(
				final FMLProxyPacket packet,
				final INetHandler handler )
		{
			final PacketBuffer buffer = new PacketBuffer( packet.payload() );
			final ModPacket innerPacket = parsePacket( buffer );

			PacketThreadUtil.checkThreadAndEnqueue( innerPacket, handler, Minecraft.getMinecraft() );
			innerPacket.client();
		}

	};

	/**
	 * Receives packets on the server and dispatches them to the processor.
	 */
	private class ServerPacketHandler
	{

		@SuppressWarnings( "unchecked" )
		public void onPacketData(
				final FMLProxyPacket packet,
				final INetHandler handler,
				final EntityPlayerMP playerEntity )
		{
			if ( playerEntity == null )
			{
				return;
			}

			final PacketBuffer buffer = new PacketBuffer( packet.payload() );
			final ModPacket innerPacket = parsePacket( buffer );
			innerPacket.serverEntity = playerEntity;

			PacketThreadUtil.checkThreadAndEnqueue( innerPacket, handler, playerEntity.getServer() );
			innerPacket.server( playerEntity );
		}
	};

	public static NetworkRouter instance;

	final FMLEventChannel ec;
	final String channelName = FlatColoredBlocks.MODID;

	final ServerPacketHandler serverPacketHandler;
	final ClientPacketHandler clientPacketHandler;

	public NetworkRouter()
	{
		ModPacketTypes.init();

		ec = NetworkRegistry.INSTANCE.newEventDrivenChannel( channelName );
		ec.register( this );

		MinecraftForge.EVENT_BUS.register( this );

		clientPacketHandler = new ClientPacketHandler();
		serverPacketHandler = new ServerPacketHandler();
	}

	@SubscribeEvent
	public void serverPacket(
			final ServerCustomPacketEvent ev )
	{
		// find player
		final NetHandlerPlayServer srv = (NetHandlerPlayServer) ev.getPacket().handler();

		try
		{
			if ( serverPacketHandler != null )
			{
				serverPacketHandler.onPacketData( ev.getPacket(), ev.getHandler(), srv.playerEntity );
			}
		}
		catch ( final ThreadQuickExitException ext )
		{
			;
		}
	}

	@SubscribeEvent
	public void clientPacket(
			final ClientCustomPacketEvent ev )
	{
		try
		{
			if ( clientPacketHandler != null )
			{
				clientPacketHandler.onPacketData( ev.getPacket(), ev.getHandler() );
			}
		}
		catch ( final ThreadQuickExitException ext )
		{
			;
		}
	}

	private ModPacket parsePacket(
			final PacketBuffer buffer )
	{
		final int id = buffer.readByte();

		try
		{
			final ModPacket packet = ModPacketTypes.constructByID( id );
			packet.readPayload( buffer );
			return packet;
		}
		catch ( final InstantiationException e )
		{
			throw new RuntimeException( e );
		}
		catch ( final IllegalAccessException e )
		{
			throw new RuntimeException( e );
		}
	}

	private FMLProxyPacket getProxyPacket(
			final ModPacket packet )
	{
		final PacketBuffer buffer = new PacketBuffer( Unpooled.buffer() );

		buffer.writeByte( ModPacketTypes.getID( packet.getClass() ) );
		packet.getPayload( buffer );

		return new FMLProxyPacket( buffer, channelName );
	}

	/**
	 * from client to server
	 *
	 * @param packet
	 */
	public void sendToServer(
			final ModPacket packet )
	{
		ec.sendToServer( getProxyPacket( packet ) );
	}

	/**
	 * from server to clients...
	 *
	 * @param message
	 */
	public void sendToAll(
			final ModPacket packet )
	{
		ec.sendToAll( getProxyPacket( packet ) );
	}

	/**
	 * from server to specific client.
	 *
	 * @param message
	 * @param player
	 */
	public void sendTo(
			final ModPacket packet,
			final EntityPlayerMP player )
	{
		ec.sendTo( getProxyPacket( packet ), player );
	}

	/**
	 * send to any players around the location.
	 *
	 * @param packet
	 * @param point
	 */
	public void sendToAllAround(
			final ModPacket packet,
			final NetworkRegistry.TargetPoint point )
	{
		ec.sendToAllAround( getProxyPacket( packet ), point );
	}

}