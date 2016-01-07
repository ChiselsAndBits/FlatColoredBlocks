package mod.flatcoloredblocks.block;

import mod.flatcoloredblocks.config.ModConfig;

public class BlockHSVConfiguration
{

	// fact, 16 meta per block.
	public final static int META_SCALE = 16;

	// collection of semi-constants used to generate blocks and shades.
	public int MAX_SHADE_HUE;
	public int MAX_SHADE_SATURATION;
	public int MAX_SHADE_VALUE;
	public int MAX_SHADE_VARIANT;

	public double SATURATION_EXPONENT;
	public double SATURATION_MIN;
	public double SATURATION_MAX;

	public double HUE_EXPONENT;
	public double HUE_MIN;
	public double HUE_MAX;

	public double VALUE_EXPONENT;
	public double VALUE_MIN;
	public double VALUE_MAX;

	public double VARIANT_EXPONENT;
	public double VARIANT_MIN;
	public double VARIANT_MAX;

	final public int MAX_SHADE_HUE_MINUS_ONE;
	final public int MAX_SHADE_SATURATION_MINUS_ONE;
	final public int MAX_SHADE_VALUE_MINUS_ONE;
	final public int MAX_SHADE_VARIANT_MINUS_ONE;

	final public int MAX_SHADES;
	final public int META_SCALE_MINUS_ONE;
	final public int MAX_SHADE_BLOCKS;
	final public int MAX_SHADES_MINUS_ONE;

	final public int finalShades;

	// shade number to color conversion tables.
	final public int[] shadeConvertHue;
	final public int[] shadeConvertSat;
	final public int[] shadeConvertValue;
	final public int[] shadeConvertVariant;

	final public EnumFlatBlockType type;

	public BlockHSVConfiguration(
			final EnumFlatBlockType type,
			final ModConfig config )
	{
		this.type = type;
		populateFromConfig( config, type );

		MAX_SHADE_HUE_MINUS_ONE = MAX_SHADE_HUE - 1;

		MAX_SHADE_SATURATION_MINUS_ONE = MAX_SHADE_SATURATION - 1;

		MAX_SHADE_VALUE_MINUS_ONE = MAX_SHADE_VALUE - 1;

		MAX_SHADE_VARIANT_MINUS_ONE = MAX_SHADE_VARIANT - 1;

		MAX_SHADES = MAX_SHADE_HUE * MAX_SHADE_SATURATION * MAX_SHADE_VALUE + MAX_SHADE_VALUE;
		META_SCALE_MINUS_ONE = META_SCALE - 1;
		MAX_SHADE_BLOCKS = ( MAX_SHADES + META_SCALE_MINUS_ONE ) / META_SCALE;
		MAX_SHADES_MINUS_ONE = MAX_SHADES - 1;

		finalShades = MAX_SHADES - MAX_SHADE_VALUE;

		shadeConvertHue = new int[MAX_SHADE_HUE];
		shadeConvertSat = new int[MAX_SHADE_SATURATION];
		shadeConvertValue = new int[MAX_SHADE_VALUE];
		shadeConvertVariant = new int[MAX_SHADE_VARIANT];

		for ( int x = 0; x < MAX_SHADE_HUE; x++ )
		{
			final double p = x / (double) MAX_SHADE_HUE_MINUS_ONE;
			shadeConvertHue[x] = adjustRange( p, HUE_EXPONENT, HUE_MIN, HUE_MAX );
		}

		for ( int x = 0; x < MAX_SHADE_VALUE; x++ )
		{
			final double p = x / (double) MAX_SHADE_VALUE_MINUS_ONE;
			shadeConvertValue[x] = adjustRange( p, VALUE_EXPONENT, VALUE_MIN, VALUE_MAX );
		}

		for ( int x = 0; x < MAX_SHADE_SATURATION; x++ )
		{
			final double p = x / (double) MAX_SHADE_SATURATION_MINUS_ONE;
			shadeConvertSat[x] = adjustRange( p, SATURATION_EXPONENT, SATURATION_MIN, SATURATION_MAX );
		}
		for ( int x = 0; x < MAX_SHADE_VARIANT; x++ )
		{
			final double p = x / (double) MAX_SHADE_VARIANT_MINUS_ONE;
			shadeConvertVariant[x] = adjustRange( p, VARIANT_EXPONENT, VARIANT_MIN, VARIANT_MAX );
		}
	}

	// used to generate color conversion tables.
	private static int adjustRange(
			double value,
			final double exponent,
			final double min,
			final double max )
	{
		// insure its in range...
		value = Math.min( 1.0, Math.max( 0.0, value ) );

		// if you only have a single varient just map to the lowest value..
		if ( Double.isInfinite( value ) || Double.isNaN( value ) )
		{
			value = 0;
		}

		// raise it to the correct exponent.
		value = Math.pow( value, exponent );

		// interpolate..
		value = value * max + min * ( 1.0f - value );

		// convert to 0 - 255 integer.
		return Math.min( 255, Math.max( 0, (int) ( 0xff * value ) ) );
	}

	protected void populateFromConfig(
			final ModConfig config,
			final EnumFlatBlockType type )
	{
		switch ( type )
		{
			case NORMAL:
				MAX_SHADE_HUE = config.HUE_SHADES;
				MAX_SHADE_SATURATION = config.SATURATION_SHADES;
				MAX_SHADE_VALUE = config.VALUE_SHADES;
				MAX_SHADE_VARIANT = 1;

				SATURATION_EXPONENT = config.SATURATION_RANGE_EXPONENT;
				SATURATION_MIN = config.SATURATION_MIN;
				SATURATION_MAX = config.SATURATION_MAX;

				HUE_EXPONENT = config.HUE_RANGE_EXPONENT;
				HUE_MIN = config.HUE_MIN;
				HUE_MAX = config.HUE_MAX;

				VALUE_EXPONENT = config.VALUE_RANGE_EXPONENT;
				VALUE_MIN = config.VALUE_MIN;
				VALUE_MAX = config.VALUE_MAX;

				VARIANT_EXPONENT = 1;
				VARIANT_MIN = 1;
				VARIANT_MAX = 1;
				break;
			case GLOWING:
				MAX_SHADE_HUE = config.HUE_SHADES_GLOWING;
				MAX_SHADE_SATURATION = config.SATURATION_SHADES_GLOWING;
				MAX_SHADE_VALUE = config.VALUE_SHADES_GLOWING;
				MAX_SHADE_VARIANT = config.GLOWING_SHADES;

				SATURATION_EXPONENT = config.SATURATION_RANGE_EXPONENT_GLOWING;
				SATURATION_MIN = config.SATURATION_MIN_GLOWING;
				SATURATION_MAX = config.SATURATION_MAX_GLOWING;

				HUE_EXPONENT = config.HUE_RANGE_EXPONENT_GLOWING;
				HUE_MIN = config.HUE_MIN_GLOWING;
				HUE_MAX = config.HUE_MAX_GLOWING;

				VALUE_EXPONENT = config.VALUE_RANGE_EXPONENT_GLOWING;
				VALUE_MIN = config.VALUE_MIN_GLOWING;
				VALUE_MAX = config.VALUE_MAX_GLOWING;

				VARIANT_EXPONENT = config.GLOWING_RANGE_EXPONENT;
				VARIANT_MIN = config.GLOWING_MIN;
				VARIANT_MAX = config.GLOWING_MAX;
				break;
			case TRANSPARENT:
				MAX_SHADE_HUE = config.HUE_SHADES_TRANSPARENT;
				MAX_SHADE_SATURATION = config.SATURATION_SHADES_TRANSPARENT;
				MAX_SHADE_VALUE = config.VALUE_SHADES_TRANSPARENT;
				MAX_SHADE_VARIANT = config.TRANSPARENCY_SHADES;

				SATURATION_EXPONENT = config.SATURATION_RANGE_EXPONENT_TRANSPARENT;
				SATURATION_MIN = config.SATURATION_MIN_TRANSPARENT;
				SATURATION_MAX = config.SATURATION_MAX_TRANSPARENT;

				HUE_EXPONENT = config.HUE_RANGE_EXPONENT_TRANSPARENT;
				HUE_MIN = config.HUE_MIN_TRANSPARENT;
				HUE_MAX = config.HUE_MAX_TRANSPARENT;

				VALUE_EXPONENT = config.VALUE_RANGE_EXPONENT_TRANSPARENT;
				VALUE_MIN = config.VALUE_MIN_TRANSPARENT;
				VALUE_MAX = config.VALUE_MAX_TRANSPARENT;

				VARIANT_EXPONENT = config.TRANSPARENCY_RANGE_EXPONENT;
				VARIANT_MIN = config.TRANSPARENCY_MIN;
				VARIANT_MAX = config.TRANSPARENCY_MAX;
				break;
		}
	}

	public int hsvFromNumber(
			int shadeNum )
	{
		int v = 0;
		int s = 0;
		int h = 0;

		if ( shadeNum >= finalShades )
		{
			v = ( shadeNum - finalShades ) % MAX_SHADE_VALUE;
			s = 0;
			h = 0;
		}
		else
		{
			v = shadeNum % MAX_SHADE_VALUE;

			shadeNum -= v;
			shadeNum /= MAX_SHADE_VALUE;

			s = shadeNum % MAX_SHADE_SATURATION;

			shadeNum -= s;
			shadeNum /= MAX_SHADE_SATURATION;

			h = shadeNum % MAX_SHADE_HUE;
		}

		v = shadeConvertValue[v];
		s = shadeNum >= finalShades ? 0 : shadeConvertSat[s];
		h = shadeConvertHue[h];

		return combine( h, s, v );
	}

	private static int combine(
			final int r,
			final int g,
			final int b )
	{
		return r << 16 | g << 8 | b;
	}

	// total number of available shades?
	public int getNumberOfShades()
	{
		return MAX_SHADES;
	}

	// how many blocks are generated?
	public int getNumberOfBlocks()
	{
		return MAX_SHADE_BLOCKS;
	}

	public String getBlockName(
			final int varientNum )
	{
		if ( type == EnumFlatBlockType.NORMAL )
		{
			return type.blockName;
		}

		return type.blockName + varientNum + "_";
	}

}
