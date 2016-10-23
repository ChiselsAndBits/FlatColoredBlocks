package mod.flatcoloredblocks.block;

import java.util.Set;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;

public enum EnumFlatColorAttributes
{
	// non-colors
	black( true, true, EnumDyeColor.BLACK, MapColor.BLACK ),
	grey( true, true, EnumDyeColor.GRAY, MapColor.GRAY ),
	silver( true, true, EnumDyeColor.SILVER, MapColor.SILVER ),
	white( true, true, EnumDyeColor.WHITE, MapColor.SNOW ),

	// colors...
	red( true, false, EnumDyeColor.RED, MapColor.RED ),
	orange( true, false, EnumDyeColor.ORANGE, MapColor.ADOBE ),
	yellow( true, false, EnumDyeColor.YELLOW, MapColor.YELLOW ),
	lime( true, false, EnumDyeColor.LIME, MapColor.LIME ),
	green( true, false, EnumDyeColor.GREEN, MapColor.GREEN ),
	emerald( true, false, EnumDyeColor.GREEN, EnumDyeColor.CYAN, MapColor.GREEN ),
	cyan( true, false, EnumDyeColor.CYAN, MapColor.CYAN ),
	azure( true, false, EnumDyeColor.BLUE, EnumDyeColor.CYAN, MapColor.LIGHT_BLUE ),
	blue( true, false, EnumDyeColor.BLUE, MapColor.BLUE ),
	violet( true, false, EnumDyeColor.PURPLE, MapColor.PURPLE ),
	magenta( true, false, EnumDyeColor.MAGENTA, MapColor.MAGENTA ),
	pink( true, false, EnumDyeColor.PINK, MapColor.PINK ),

	// color modifiers
	dark( false, false, EnumDyeColor.BLACK, MapColor.BLACK ),
	light( false, false, EnumDyeColor.WHITE, MapColor.SNOW );

	// description of characteristic
	public final boolean isModifier;
	public final boolean isSaturated;

	// dye information
	public final EnumDyeColor primaryDye;
	public final EnumDyeColor secondaryDye;

	// map color
	public final MapColor mapColor;

	EnumFlatColorAttributes(
			final boolean isColor,
			final boolean isSaturated,
			final EnumDyeColor dye1,
			final EnumDyeColor dye2,
			final MapColor mapColor )
	{
		isModifier = !isColor;
		this.isSaturated = isSaturated;
		primaryDye = dye1;
		secondaryDye = dye1;
		this.mapColor = mapColor;
	}

	EnumFlatColorAttributes(
			final boolean isColor,
			final boolean isSaturated,
			final EnumDyeColor dye,
			final MapColor mapColor )
	{
		isModifier = !isColor;
		this.isSaturated = isSaturated;
		primaryDye = dye;
		secondaryDye = dye;
		this.mapColor = mapColor;
	}

	public static EnumDyeColor getAlternateDye(
			final Set<EnumFlatColorAttributes> characteristics )
	{
		if ( characteristics.contains( EnumFlatColorAttributes.orange ) && characteristics.contains( EnumFlatColorAttributes.dark ) )
		{
			return EnumDyeColor.BROWN;
		}

		if ( characteristics.contains( EnumFlatColorAttributes.blue ) && characteristics.contains( EnumFlatColorAttributes.light ) )
		{
			return EnumDyeColor.LIGHT_BLUE;
		}

		return null;
	}

}
