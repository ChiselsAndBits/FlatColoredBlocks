package mod.flatcoloredblocks.block;

import java.util.List;
import java.util.Set;

import com.sun.istack.internal.NotNull;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBlockFlatColored extends ItemBlock
{

	public BlockFlatColored getColoredBlock()
	{
		return (BlockFlatColored) block;
	}

	private IBlockState getStateFromStack(
			@NotNull final ItemStack stack )
	{
		return ModUtil.getStateFromMeta( getBlock(), stack.getItemDamage() );
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
		super( block );
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
			@NotNull final ItemStack stack )
	{
		final IBlockState state = getStateFromStack( stack );
		final int shadeNum = getColoredBlock().getShadeNumber( state );

		final Set<EnumFlatColorAttributes> colorChars = getColoredBlock().getFlatColorAttributes( state );

		final String type = getTypeLocalization();
		final String prefix = getColorPrefix( colorChars );
		final String hue = getColorHueName( colorChars );

		return type + ModUtil.translateToLocal( prefix + hue + ".name" ) + " #" + shadeNum;
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
			@NotNull final ItemStack stack,
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
			addColor( true, rgb, tooltip );
		}

		if ( FlatColoredBlocks.instance.config.showHSV )
		{
			addColor( false, hsv, tooltip );
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

	private void addColor(
			final boolean isRgb,
			final int value,
			final List<String> tooltip )
	{
		final int r_h = value >> 16 & 0xff;
		final int g_s = value >> 8 & 0xff;
		final int b_v = value & 0xff;

		final StringBuilder sb = new StringBuilder();

		if ( isRgb )
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

	public int getColorFromItemStack(
			@NotNull final ItemStack stack,
			final int renderPass )
	{
		final IBlockState state = getStateFromStack( stack );
		return getColoredBlock().colorFromState( state );
	}

}
