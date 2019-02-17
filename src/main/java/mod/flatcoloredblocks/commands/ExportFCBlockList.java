package mod.flatcoloredblocks.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.ConversionHSV2RGB;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class ExportFCBlockList
{

	@Override
	public String getCommandName()
	{
		return "flatcoloredblock_export_list";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public String getCommandUsage(
			ICommandSender sender )
	{
		return "flatcoloredblocks.commands.export_list.usage";
	}

	@Override
	public void execute(
			MinecraftServer server,
			ICommandSender sender,
			String[] args ) throws CommandException
	{
		File configPath = FlatColoredBlocks.instance.config.getFilePath();
		String path = configPath.getParentFile().getAbsolutePath() + File.separator + "flatcoloredblocks.csv";

		try
		{
			List<ItemStack> list = new ArrayList<ItemStack>();
			BlockFlatColored.getAllShades( list );

			FileWriter writer = new FileWriter( path );

			writer.write( "Shade Number,Name,HEX,Red,Blue,Green,Hue,Saturation,Value,Opacity,Light Value\n" );

			for ( ItemStack is : list )
			{
				Item it = is.getItem();
				if ( it instanceof ItemBlockFlatColored )
				{
					final IBlockState state = ( (ItemBlockFlatColored) it ).getStateFromStack( is );
					final BlockFlatColored blk = ( (ItemBlockFlatColored) it ).getColoredBlock();

					final int hsv = blk.hsvFromState( state );
					final int rgb = ConversionHSV2RGB.toRGB( hsv );

					final int h = hsv >> 16 & 0xff;
					final int s = hsv >> 8 & 0xff;
					final int v = hsv & 0xff;

					final int r = rgb >> 16 & 0xff;
					final int g = rgb >> 8 & 0xff;
					final int b = rgb & 0xff;

					StringBuilder line = new StringBuilder();

					line.append( blk.getShadeNumber( state ) );
					line.append( ",\"" );
					line.append( is.getDisplayName().getUnformattedComponentText().replace( "\"", "\"\"" ) );
					line.append( "\"," );

					line.append( "#" ).append( ItemBlockFlatColored.hexPad( Integer.toString( r, 16 ) ) ).append( ItemBlockFlatColored.hexPad( Integer.toString( g, 16 ) ) ).append( ItemBlockFlatColored.hexPad( Integer.toString( b, 16 ) ) );
					line.append( "," );

					line.append( r );
					line.append( "," );
					line.append( g );
					line.append( "," );
					line.append( b );
					line.append( "," );

					line.append( 360 * h / 255 );
					line.append( "," );
					line.append( 100 * s / 255 );
					line.append( "," );
					line.append( 100 * v / 255 );
					line.append( "," );

					line.append( blk.opacity );
					line.append( "," );
					line.append( blk.lightValue );
					line.append( "\n" );

					writer.write( line.toString() );
				}
			}

			writer.close();
			sender.addChatMessage( new TextComponentTranslation( "flatcoloredblocks.commands.export_list.savedfile", path ) );
		}
		catch ( IOException e )
		{
			throw new CommandException( new TextComponentTranslation( "flatcoloredblocks.commands.export_list.unabletosave", path ) );
		}
	}

}
