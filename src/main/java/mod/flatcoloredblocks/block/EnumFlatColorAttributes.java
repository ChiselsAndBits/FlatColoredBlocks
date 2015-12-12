package mod.flatcoloredblocks.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;

public enum EnumFlatColorAttributes
{
	// non-colors
	black( true, true, EnumDyeColor.BLACK, MapColor.blackColor ),
	gray( true, true, EnumDyeColor.GRAY, MapColor.grayColor ),
	silver( true, true, EnumDyeColor.SILVER, MapColor.silverColor ),
	white( true, true, EnumDyeColor.WHITE, MapColor.snowColor ),

	// colors...
	red( true, false, EnumDyeColor.RED, MapColor.redColor ),
	orange( true, false, EnumDyeColor.ORANGE, MapColor.adobeColor ),
	yellow( true, false, EnumDyeColor.YELLOW, MapColor.yellowColor ),
	lime( true, false, EnumDyeColor.LIME, MapColor.limeColor ),
	green( true, false, EnumDyeColor.GREEN, MapColor.greenColor ),
	emerald( true, false, EnumDyeColor.GREEN, EnumDyeColor.CYAN, MapColor.greenColor ),
	cyan( true, false, EnumDyeColor.CYAN, MapColor.cyanColor ),
	azure( true, false, EnumDyeColor.BLUE, EnumDyeColor.CYAN, MapColor.blueColor ),
	blue( true, false, EnumDyeColor.BLUE, MapColor.blueColor ),
	violet( true, false, EnumDyeColor.PURPLE, MapColor.purpleColor ),
	magenta( true, false, EnumDyeColor.MAGENTA, MapColor.magentaColor ),
	pink( true, false, EnumDyeColor.PINK, MapColor.pinkColor ),

	// color modifiers
	dark( false, false, EnumDyeColor.BLACK, MapColor.blackColor ),
	light( false, false, EnumDyeColor.WHITE, MapColor.snowColor );

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

}
