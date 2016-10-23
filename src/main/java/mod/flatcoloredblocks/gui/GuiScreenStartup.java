package mod.flatcoloredblocks.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

/**
 * Screen displayed to inform the user about block usage, only is displayed when
 * the shade count changes.
 */
public class GuiScreenStartup extends GuiScreen
{
	String[] lines;

	@Override
	public void initGui()
	{
		final String msga = I18n.translateToLocal( "flatcoloredblocks.startup_a" );
		final String msgb = I18n.translateToLocal( "flatcoloredblocks.startup_b" );
		final String msgc = I18n.translateToLocal( "flatcoloredblocks.startup_c" );
		final String msgd = I18n.translateToLocal( "flatcoloredblocks.startup_d" );
		final String msge = I18n.translateToLocal( "flatcoloredblocks.startup_e" );
		final String msgf = I18n.translateToLocal( "flatcoloredblocks.startup_f" );

		String msg = msga + "\n\n" + msgb + "\n\n";
		msg += FlatColoredBlocks.instance.getFullNumberOfShades() + " ";
		msg += msgc;
		msg += " " + FlatColoredBlocks.instance.getFullNumberOfBlocks() + " ";
		msg += msgd + "\n\n" + msge + "\n\n" + msgf;

		lines = msg.split( "\n" );

		buttonList.add( new GuiButton( 0, width / 2 - 144 / 2, height / 2 + 96, 144, 20, "Ok" ) );
	}

	@Override
	public void drawScreen(
			final int mouseX,
			final int mouseY,
			final float partialTicks )
	{
		GL11.glEnable( GL11.GL_TEXTURE_2D );
		drawDefaultBackground();

		int heightLoc = 90;
		drawCenteredString( fontRendererObj, TextFormatting.YELLOW + "Flat Colored Blocks", width / 2, height / 2 - 110, 0xFFFFFF );

		for ( final String s : lines )
		{
			final List<String> info = fontRendererObj.listFormattedStringToWidth( s, width - 40 );
			for ( final String infoCut : info )
			{
				drawCenteredString( fontRendererObj, infoCut, width / 2, height / 2 - heightLoc, 0xFFFFFF );
				heightLoc = heightLoc - 12;
			}
		}

		super.drawScreen( mouseX, mouseY, partialTicks );
	}

	@Override
	public void actionPerformed(
			final GuiButton button )
	{
		switch ( button.id )
		{
			case 0:
			{
				for ( final GuiButton b : buttonList )
				{
					b.enabled = false;
				}

				FlatColoredBlocks.instance.config.updateLastMaxShades();
				FlatColoredBlocks.instance.config.save();

				mc.displayGuiScreen( null );

				break;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
