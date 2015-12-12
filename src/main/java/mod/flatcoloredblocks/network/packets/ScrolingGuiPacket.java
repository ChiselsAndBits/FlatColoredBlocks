package mod.flatcoloredblocks.network.packets;

import mod.flatcoloredblocks.craftingitem.ContainerColoredBlockCrafter;
import mod.flatcoloredblocks.network.ModPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;

/**
 * Used to synchronize scroll bar on crafting item between server and client.
 * Sent from client to server.
 */
public class ScrolingGuiPacket extends ModPacket
{
	public float scroll = 0;

	@Override
	public void server(
			final EntityPlayerMP player )
	{
		final Container c = player.openContainer;
		if ( c instanceof ContainerColoredBlockCrafter )
		{
			final ContainerColoredBlockCrafter ccc = (ContainerColoredBlockCrafter) c;
			ccc.setScroll( scroll );
		}
	}

	@Override
	public void getPayload(
			final PacketBuffer buffer )
	{
		buffer.writeFloat( scroll );
	}

	@Override
	public void readPayload(
			final PacketBuffer buffer )
	{
		// no data..
		scroll = buffer.readFloat();
	}

}
