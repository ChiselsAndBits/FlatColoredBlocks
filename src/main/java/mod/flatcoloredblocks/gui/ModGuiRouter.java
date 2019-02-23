package mod.flatcoloredblocks.gui;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.FMLPlayMessages.OpenContainer;

/**
 * Client / Server Gui + Container Handler
 */
public class ModGuiRouter implements Function<FMLPlayMessages.OpenContainer, GuiScreen>, Supplier<Function<FMLPlayMessages.OpenContainer, GuiScreen>>
{

	public static Container createContainer(
			ModGuiTypes type,
			final EntityPlayer player,
			final World world,
			final int x,
			final int y,
			final int z )
	{
		try
		{
			return (Container) type.container_construtor.newInstance( player, world, x, y, z );
		}
		catch ( final Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	@Override
	public GuiScreen apply(
			OpenContainer t )
	{
		try
		{
			final ModGuiTypes guiType = ModGuiTypes.valueOf( t.getId().getPath() );
			return (GuiScreen) guiType.gui_construtor.newInstance( Minecraft.getInstance().player, Minecraft.getInstance().player.world, 0, 0, 0 );
		}
		catch ( final Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	@Override
	public Function<OpenContainer, GuiScreen> get()
	{
		return this;
	}
}
