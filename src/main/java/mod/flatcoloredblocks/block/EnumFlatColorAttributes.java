package mod.flatcoloredblocks.block;

import java.util.Set;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.EnumDyeColor;

public enum EnumFlatColorAttributes
{
	// non-colors
	black( true, true, EnumDyeColor.BLACK, MaterialColor.BLACK ),
	grey( true, true, EnumDyeColor.GRAY, MaterialColor.GRAY ),
	silver( true, true, EnumDyeColor.LIGHT_GRAY, MaterialColor.LIGHT_GRAY ),
	white( true, true, EnumDyeColor.WHITE, MaterialColor.SNOW ),

	// colors...
	red( true, false, EnumDyeColor.RED, MaterialColor.RED ),
	orange( true, false, EnumDyeColor.ORANGE, MaterialColor.ADOBE ),
	yellow( true, false, EnumDyeColor.YELLOW, MaterialColor.YELLOW ),
	lime( true, false, EnumDyeColor.LIME, MaterialColor.LIME ),
	green( true, false, EnumDyeColor.GREEN, MaterialColor.GREEN ),
	emerald( true, false, EnumDyeColor.GREEN, EnumDyeColor.CYAN, MaterialColor.GREEN ),
	cyan( true, false, EnumDyeColor.CYAN, MaterialColor.CYAN ),
	azure( true, false, EnumDyeColor.BLUE, EnumDyeColor.CYAN, MaterialColor.LIGHT_BLUE ),
	blue( true, false, EnumDyeColor.BLUE, MaterialColor.BLUE ),
	violet( true, false, EnumDyeColor.PURPLE, MaterialColor.PURPLE ),
	magenta( true, false, EnumDyeColor.MAGENTA, MaterialColor.MAGENTA ),
	pink( true, false, EnumDyeColor.PINK, MaterialColor.PINK ),

	// color modifiers
	dark( false, false, EnumDyeColor.BLACK, MaterialColor.BLACK ),
	light( false, false, EnumDyeColor.WHITE, MaterialColor.SNOW );

	// description of characteristic
	public final boolean isModifier;
	public final boolean isSaturated;

	// dye information
	public final EnumDyeColor primaryDye;
	public final EnumDyeColor secondaryDye;

	// map color
	public final MaterialColor mapColor;

	EnumFlatColorAttributes(
			final boolean isColor,
			final boolean isSaturated,
			final EnumDyeColor dye1,
			final EnumDyeColor dye2,
			final MaterialColor mapColor )
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
			final MaterialColor mapColor )
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
