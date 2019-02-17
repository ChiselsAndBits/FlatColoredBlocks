package mod.flatcoloredblocks.block;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBlockFlatColored extends ItemBlock
{

	public BlockFlatColored getColoredBlock()
	{
		return (BlockFlatColored) getBlock();
	}

	public IBlockState getStateFromStack(
			@Nonnull final ItemStack stack )
	{
		return ModUtil.getStateFromMeta( getBlock(), stack.getDamage() );
	}

	private String getColorPrefix(
			final Set<EnumFlatColorAttributes> which )
	{
		if ( which.contains( EnumFlatColorAttributes.dark ) )
		{
			return "flatcoloredblocks.dark";
		}

		if ( which.contains( EnumFlatColorAttributes.light ) )
		{
			return "flatcoloredblocks.light";
		}

		return "flatcoloredblocks.";
	}

	private String getColorHueName(
			final Set<EnumFlatColorAttributes> characteristics )
	{
		for ( final EnumFlatColorAttributes c : characteristics )
		{
			if ( !c.isModifier )
			{
				return c.name();
			}
		}

		return EnumFlatColorAttributes.black.name();
	}

	public ItemBlockFlatColored(
			final Block block )
	{
		super( block, ( new Item.Properties() ).group( ItemGroup.DECORATIONS ) );
		setHasSubtypes( true );
	}

	@Override
	public int getMetadata(
			final int damage )
	{
		return damage; // override and return damage instead of 0
	}

	@Override
	public String getItemStackDisplayName(
			@Nonnull final ItemStack stack )
	{
		final IBlockState state = getStateFromStack( stack );
		final int shadeNum = getColoredBlock().getShadeNumber( state );

		final Set<EnumFlatColorAttributes> colorChars = getColoredBlock().getFlatColorAttributes( state );

		final String type = getTypeLocalization();
		final String prefix = getColorPrefix( colorChars );
		final String hue = getColorHueName( colorChars );

		return type + ModUtil.translateToLocal( prefix + hue + ".name" ) + " " + ModUtil.translateToLocal( "flatcoloredblocks.Shade.name" ) + shadeNum;
	}

	private String getTypeLocalization()
	{
		switch ( getColoredBlock().getType() )
		{
			case GLOWING:
				return ModUtil.translateToLocal( "flatcoloredblocks.Glowing.name" ) + " ";
			case TRANSPARENT:
				return ModUtil.translateToLocal( "flatcoloredblocks.Transparent.name" ) + " ";
			default:
				return "";
		}
	}

	@Override
	public void addInformation(
			@Nonnull final ItemStack stack,
			final World worldIn,
			final List<String> tooltip,
			final ITooltipFlag advanced )
	{
		final IBlockState state = getStateFromStack( stack );
		final BlockFlatColored blk = getColoredBlock();

		final int hsv = blk.hsvFromState( state );
		final int rgb = ConversionHSV2RGB.toRGB( hsv );

		if ( FlatColoredBlocks.instance.config.showRGB )
		{
			addColor( ColorFormat.RGB, rgb, tooltip );
		}

		if ( FlatColoredBlocks.instance.config.showHEX )
		{
			addColor( ColorFormat.HEX, rgb, tooltip );
		}

		if ( FlatColoredBlocks.instance.config.showHSV )
		{
			addColor( ColorFormat.HSV, hsv, tooltip );
		}

		if ( FlatColoredBlocks.instance.config.showLight && blk.lightValue > 0 )
		{
			final StringBuilder sb = new StringBuilder();
			sb.append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.lightvalue" ) ).append( ' ' );
			sb.append( blk.lightValue ).append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.lightValueUnit" ) );
			tooltip.add( sb.toString() );
		}

		if ( FlatColoredBlocks.instance.config.showOpacity && blk.opacity < 100 )
		{
			final StringBuilder sb = new StringBuilder();
			sb.append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.opacity" ) ).append( ' ' );
			sb.append( blk.opacity ).append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.percent" ) );
			tooltip.add( sb.toString() );
		}

		super.addInformation( stack, worldIn, tooltip, advanced );
	}

	public static enum ColorFormat
	{
		HEX, RGB, HSV
	};

	private void addColor(
			final ColorFormat Format,
			final int value,
			final List<String> tooltip )
	{
		final int r_h = value >> 16 & 0xff;
		final int g_s = value >> 8 & 0xff;
		final int b_v = value & 0xff;

		final StringBuilder sb = new StringBuilder();

		if ( Format == ColorFormat.HEX )
		{
			sb.append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.hex" ) ).append( ' ' );
			sb.append( "#" ).append( hexPad( Integer.toString( r_h, 16 ) ) ).append( hexPad( Integer.toString( g_s, 16 ) ) ).append( hexPad( Integer.toString( b_v, 16 ) ) );
		}
		else if ( Format == ColorFormat.RGB )
		{
			sb.append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.rgb" ) ).append( ' ' );
			sb.append( TextFormatting.RED ).append( r_h ).append( ' ' );
			sb.append( TextFormatting.GREEN ).append( g_s ).append( ' ' );
			sb.append( TextFormatting.BLUE ).append( b_v );
		}
		else
		{
			sb.append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.hsv" ) ).append( ' ' );
			sb.append( 360 * r_h / 255 ).append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.deg" ) + ' ' );
			sb.append( 100 * g_s / 255 ).append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.percent" ) + ' ' );
			sb.append( 100 * b_v / 255 ).append( ModUtil.translateToLocal( "flatcoloredblocks.tooltips.percent" ) );
		}

		tooltip.add( sb.toString() );
	}

	public static String hexPad(
			String string )
	{
		if ( string.length() == 0 )
			return "00";
		if ( string.length() == 1 )
			return "0" + string;
		return string;
	}

	public int getColorFromItemStack(
			@Nonnull final ItemStack stack,
			final int renderPass )
	{
		final IBlockState state = getStateFromStack( stack );
		return getColoredBlock().colorFromState( state );
	}

}
