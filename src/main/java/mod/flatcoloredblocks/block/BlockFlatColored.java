package mod.flatcoloredblocks.block;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFlatColored extends Block
{

	// fact, 16 meta per block.
	static public final int META_SCALE = 16;

	// collection of semi-constants used to generate blocks and shades.
	static private int MAX_SHADE_HUE;
	static private int MAX_SHADE_SATURATION;
	static private int MAX_SHADE_VALUE;

	static private double SATURATION_EXPONENT;
	static private double SATURATION_MIN;
	static private double SATURATION_MAX;

	static private double HUE_EXPONENT;
	static private double HUE_MIN;
	static private double HUE_MAX;

	static private double VALUE_EXPONENT;
	static private double VALUE_MIN;
	static private double VALUE_MAX;

	static private int MAX_SHADE_HUE_MINUS_ONE;
	static private int MAX_SHADE_SATURATION_MINUS_ONE;
	static private int MAX_SHADE_VALUE_MINUS_ONE;

	static private int MAX_SHADES;
	static private int META_SCALE_MINUS_ONE;
	static private int MAX_SHADE_BLOCKS;
	static private int MAX_SHADES_MINUS_ONE;

	static public int finalShades = MAX_SHADES - MAX_SHADE_VALUE;

	// shade number to color conversion tables.
	static private int[] shadeConvertHue;
	static private int[] shadeConvertSat;
	static private int[] shadeConvertValue;

	// used to generate color conversion tables.
	private static int adjustRange(
			double value,
			final double exponent,
			final double min,
			final double max )
	{
		// insure its in range...
		value = Math.min( 1.0, Math.max( 0.0, value ) );

		// raise it to the correct exponent.
		value = Math.pow( value, exponent );

		// interpolate..
		value = value * max + min * ( 1.0f - value );

		// convert to 0 - 255 integer.
		return Math.min( 255, Math.max( 0, (int) ( 0xff * value ) ) );
	}

	// transfers config to constants and generates conversion tables.
	public static void loadConfig(
			final ModConfig config )
	{
		MAX_SHADE_HUE = config.HUE_SHADES;
		MAX_SHADE_SATURATION = config.SATURATION_SHADES;
		MAX_SHADE_VALUE = config.VALUE_SHADES;

		SATURATION_EXPONENT = config.SATURATION_RANGE_EXPONENT;
		SATURATION_MIN = config.SATURATION_MIN;
		SATURATION_MAX = config.SATURATION_MAX;

		HUE_EXPONENT = config.HUE_RANGE_EXPONENT;
		HUE_MIN = config.HUE_MIN;
		HUE_MAX = config.HUE_MAX;

		VALUE_EXPONENT = config.VALUE_RANGE_EXPONENT;
		VALUE_MIN = config.VALUE_MIN;
		VALUE_MAX = config.VALUE_MAX;

		MAX_SHADE_HUE_MINUS_ONE = MAX_SHADE_HUE - 1;

		MAX_SHADE_SATURATION_MINUS_ONE = MAX_SHADE_SATURATION - 1;

		MAX_SHADE_VALUE_MINUS_ONE = MAX_SHADE_VALUE - 1;

		MAX_SHADES = MAX_SHADE_HUE * MAX_SHADE_SATURATION * MAX_SHADE_VALUE + MAX_SHADE_VALUE;
		META_SCALE_MINUS_ONE = META_SCALE - 1;
		MAX_SHADE_BLOCKS = ( MAX_SHADES + META_SCALE_MINUS_ONE ) / META_SCALE;
		MAX_SHADES_MINUS_ONE = MAX_SHADES - 1;

		finalShades = MAX_SHADES - MAX_SHADE_VALUE;

		shadeConvertHue = new int[MAX_SHADE_HUE];
		shadeConvertSat = new int[MAX_SHADE_SATURATION];
		shadeConvertValue = new int[MAX_SHADE_VALUE];

		for ( int x = 0; x < MAX_SHADE_HUE; x++ )
		{
			final double p = (double) x / (double) MAX_SHADE_HUE_MINUS_ONE;
			shadeConvertHue[x] = adjustRange( p, HUE_EXPONENT, HUE_MIN, HUE_MAX );
		}

		for ( int x = 0; x < MAX_SHADE_VALUE; x++ )
		{
			final double p = (double) x / (double) MAX_SHADE_VALUE_MINUS_ONE;
			shadeConvertValue[x] = adjustRange( p, VALUE_EXPONENT, VALUE_MIN, VALUE_MAX );
		}

		for ( int x = 0; x < MAX_SHADE_SATURATION; x++ )
		{
			final double p = (double) x / (double) MAX_SHADE_SATURATION_MINUS_ONE;
			shadeConvertSat[x] = adjustRange( p, SATURATION_EXPONENT, SATURATION_MIN, SATURATION_MAX );
		}
	}

	// block specific values.
	private int shadeOffset; // first shade in block
	private int maxShade; // this is the last shade in this block.
	private PropertyInteger shade; // block state configuration for block.

	@Override
	public int getRenderColor(
			final IBlockState state )
	{
		return colorFromState( state );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public int colorMultiplier(
			final IBlockAccess worldIn,
			final BlockPos pos,
			final int renderPass )
	{
		return colorFromState( worldIn.getBlockState( pos ) );
	}

	@Override
	public MapColor getMapColor(
			final IBlockState state )
	{
		for ( final EnumFlatColorAttributes attr : getFlatColorAttributes( state ) )
		{
			if ( !attr.isModifier )
			{
				return attr.mapColor;
			}
		}

		return MapColor.snowColor;
	}

	// also used in item for item stack color.
	static int colorFromState(
			final IBlockState state )
	{
		return ConversionHSV2RGB.toRGB( hsvFromState( state ) );
	}

	public static int getShadeNumber(
			final IBlockState state )
	{
		if ( state.getBlock() instanceof BlockFlatColored )
		{
			final BlockFlatColored cb = (BlockFlatColored) state.getBlock();
			return state.getValue( cb.shade ) + cb.shadeOffset;
		}

		return 0;
	}

	public static int hsvFromState(
			final IBlockState state )
	{
		if ( state == null )
		{
			return 0x000000;
		}

		int shadeNum = getShadeNumber( state );

		int v = 0;
		int s = 0;
		int h = 0;

		if ( shadeNum >= finalShades )
		{
			v = shadeNum - finalShades;
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

	public int getShadeOffset()
	{
		return shadeOffset;
	}

	public int getMaxShade()
	{
		return maxShade;
	}

	public IProperty<Integer> getShade()
	{
		return shade;
	}

	private static ArrayList<BlockFlatColored> coloredBlocks = new ArrayList<BlockFlatColored>();

	public BlockFlatColored()
	{
		super( Material.rock );
		setUnlocalizedName( "flatcoloredblocks.flatcoloredblock." + offset );

		// mimic stone..
		setHardness( 1.5F );
		setResistance( 10.0F );
		setStepSound( soundTypePiston );
		setHarvestLevel( "pickaxe", 0 );

		coloredBlocks.add( this );
	}

	public static int offset = 0;

	@Override
	protected BlockState createBlockState()
	{
		shadeOffset = offset;
		maxShade = Math.min( MAX_SHADES_MINUS_ONE, shadeOffset + META_SCALE_MINUS_ONE );
		return new BlockState( this, new IProperty[] { shade = PropertyInteger.create( "shade", 0, META_SCALE_MINUS_ONE ) } );
	}

	@Override
	public int getMetaFromState(
			final IBlockState state )
	{
		return state.getValue( shade );
	}

	@Override
	public IBlockState getStateFromMeta(
			final int meta )
	{
		return getDefaultState().withProperty( shade, meta );
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return FlatColoredBlocks.instance.creativeTab;
	}

	@Override
	public int damageDropped(
			final IBlockState state )
	{
		return getMetaFromState( state );
	}

	// convert block into all possible ItemStacks.
	private void outputShades(
			final List<ItemStack> list )
	{
		final Item item = Item.getItemFromBlock( this );

		for ( int x = shadeOffset; x <= maxShade; ++x )
		{
			list.add( new ItemStack( item, 1, x - shadeOffset ) );
		}
	}

	@Override
	public void getSubBlocks(
			final Item itemIn,
			final CreativeTabs tab,
			final List<ItemStack> list )
	{
		outputShades( list );
	}

	// generates a list of all shades without caring about which block it is.
	public static void getAllShades(
			final List<ItemStack> list )
	{
		for ( final BlockFlatColored cb : coloredBlocks )
		{
			cb.outputShades( list );
		}
	}

	// how many blocks are generated?
	public static int getNumberOfBlocks()
	{
		return MAX_SHADE_BLOCKS;
	}

	// total number of available shades?
	public static int getNumberOfShades()
	{
		return MAX_SHADES;
	}

	// get details about a shade.
	public static EnumSet<EnumFlatColorAttributes> getFlatColorAttributes(
			final IBlockState state )
	{
		final int out = hsvFromState( state );

		final EnumSet<EnumFlatColorAttributes> result = EnumSet.noneOf( EnumFlatColorAttributes.class );

		final int h = ( out >> 16 & 0xff ) * 360 / 0xff;
		final int s = out >> 8 & 0xff;
		final int v = out & 0xff;

		if ( s == 0 )
		{
			if ( v < 64 )
			{
				return EnumSet.of( EnumFlatColorAttributes.black );
			}
			else if ( v > 256 - 64 )
			{
				return EnumSet.of( EnumFlatColorAttributes.white );
			}
			else if ( v > 256 - 128 )
			{
				return EnumSet.of( EnumFlatColorAttributes.silver );
			}
			else
			{
				return EnumSet.of( EnumFlatColorAttributes.grey );
			}
		}

		if ( v < 128 && s > 0.001 )
		{
			result.add( EnumFlatColorAttributes.dark );
		}
		else if ( v > 256 - 64 && s < 128 && s > 0.001 )
		{
			result.add( EnumFlatColorAttributes.light );
		}

		if ( h >= 15 && h <= 45 )
		{
			result.add( EnumFlatColorAttributes.orange );
		}
		else if ( h >= 255 && h <= 285 )
		{
			result.add( EnumFlatColorAttributes.violet );
		}
		else if ( h >= 315 && h <= 345 )
		{
			result.add( EnumFlatColorAttributes.pink );
		}
		else if ( h >= 75 - 15 && h <= 75 + 15 )
		{
			result.add( EnumFlatColorAttributes.lime );
		}
		else if ( h >= 210 - 15 && h <= 210 + 15 )
		{
			result.add( EnumFlatColorAttributes.azure );
		}
		else if ( h >= 140 - 15 && h <= 140 + 15 )
		{
			result.add( EnumFlatColorAttributes.emerald );
		}
		else if ( h >= 330 || h <= 30 )
		{
			result.add( EnumFlatColorAttributes.red );
		}
		else if ( h >= 30 && h <= 90 )
		{
			result.add( EnumFlatColorAttributes.yellow );
		}
		else if ( h >= 90 && h <= 150 )
		{
			result.add( EnumFlatColorAttributes.green );
		}
		else if ( h >= 150 && h <= 210 )
		{
			result.add( EnumFlatColorAttributes.cyan );
		}
		else if ( h >= 210 && h <= 270 )
		{
			result.add( EnumFlatColorAttributes.blue );
		}
		else
		{
			result.add( EnumFlatColorAttributes.magenta );
		}

		return result;
	}

}
