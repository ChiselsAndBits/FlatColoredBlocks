package mod.flatcoloredblocks.gui;

import java.util.ArrayList;
import java.util.List;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.config.ModConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 * Basic Config Gui, minor changes to hide startupgui.
 */
public class ConfigGui extends GuiConfig
{

	public ConfigGui(
			final GuiScreen parent )
	{
		super( parent, getConfigElements(), FlatColoredBlocks.MODID, false, false, GuiConfig.getAbridgedConfigPath( FlatColoredBlocks.instance.config.getFilePath().getAbsolutePath() ) );
	}

	@Override
	public void initGui()
	{
		for ( final IConfigElement e : configElements )
		{
			if ( e instanceof ConfigElement )
			{
				final ConfigElement cat = (ConfigElement) e;
				if ( cat.getName().equals( "startupgui" ) )
				{
					configElements.remove( e );
					break;
				}
			}
		}

		super.initGui();
	}

	private static List<IConfigElement> getConfigElements()
	{
		final List<IConfigElement> list = new ArrayList<IConfigElement>();

		final ModConfig config = FlatColoredBlocks.instance.config;

		for ( final String cat : config.getCategoryNames() )
		{
			final ConfigCategory cc = config.getCategory( cat );

			if ( cat == "startupgui" || cc.isChild() )
			{
				continue;
			}

			list.add( new ConfigElement( cc ) );
		}

		return list;
	}

}
