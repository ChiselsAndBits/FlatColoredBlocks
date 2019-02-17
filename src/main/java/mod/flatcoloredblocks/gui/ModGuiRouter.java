package mod.flatcoloredblocks.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Client / Server Gui + Container Handler
 */
public class ModGuiRouter implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(
			final int id,
			final EntityPlayer player,
			final World world,
			final int x,
			final int y,
			final int z )
	{
		try
		{
			final ModGuiTypes guiType = ModGuiTypes.values()[id];
			return guiType.container_construtor.newInstance( player, world, x, y, z );
		}
		catch ( final Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	// returns an instance of the Gui you made earlier
	@Override
	@OnlyIn( Dist.CLIENT )
	public Object getClientGuiElement(
			final int id,
			final EntityPlayer player,
			final World world,
			final int x,
			final int y,
			final int z )
	{
		try
		{
			final ModGuiTypes guiType = ModGuiTypes.values()[id];
			return guiType.gui_construtor.newInstance( player, world, x, y, z );
		}
		catch ( final Exception e )
		{
			throw new RuntimeException( e );
		}
	}
}
