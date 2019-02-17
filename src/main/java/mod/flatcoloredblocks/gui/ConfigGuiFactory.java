package mod.flatcoloredblocks.gui;

import net.minecraft.client.gui.GuiScreen;

/**
 * Basic Config Factory.
 */
public class ConfigGuiFactory extends FMLConfigGuiFactory
{

	@Override
	public GuiScreen createConfigGui(
			GuiScreen parentScreen )
	{
		return new ConfigGui( parentScreen );
	}

}
