package mod.flatcoloredblocks.block;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import mod.flatcoloredblocks.FlatColoredBlocks;
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

	// block specific values.
	private int shadeOffset; // first shade in block
	private int maxShade; // this is the last shade in this block.
	private BlockHSVConfiguration configuration; // HSV for this block.
	private PropertyInteger shade; // block state configuration for block.
	final private int varient;

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
	int colorFromState(
			final IBlockState state )
	{
		return ConversionHSV2RGB.toRGB( hsvFromState( state ) );
	}

	public int getShadeNumber(
			final IBlockState state )
	{
		if ( state.getBlock() instanceof BlockFlatColored )
		{
			final BlockFlatColored cb = (BlockFlatColored) state.getBlock();
			return state.getValue( cb.shade ) + cb.shadeOffset;
		}

		return 0;
	}

	public int hsvFromState(
			final IBlockState state )
	{
		if ( state == null )
		{
			return 0x000000;
		}

		return configuration.hsvFromNumber( getShadeNumber( state ) );
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

	protected BlockFlatColored(
			final float lightValue,
			final float opacity,
			final int varientNum )
	{
		super( opacity > 0.001 ? Material.glass : Material.rock );
		setUnlocalizedName( "flatcoloredblocks.flatcoloredblock." + offset );

		// mimic stone..
		setHardness( 1.5F );
		setResistance( 10.0F );
		setStepSound( soundTypePiston );
		setHarvestLevel( "pickaxe", 0 );
		setLightLevel( FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT ? Math.max( 0, Math.min( 15, lightValue / 255.0f ) ) : 0 );
		setLightOpacity( opacity > 0.001 ? 0 : 255 );
		setStepSound( opacity > 0.001 ? soundTypeGlass : soundTypeStone );

		translucent = opacity > 0.001;
		varient = varientNum;

		coloredBlocks.add( this );
	}

	private static int offset;
	private static BlockHSVConfiguration newConfig;

	public static BlockFlatColored construct(
			final BlockHSVConfiguration type,
			final int offsetIn,
			final int varientNum )
	{
		// pass these to createBlockState
		offset = offsetIn;
		newConfig = type;

		// construct the block..
		switch ( type.type )
		{
			case GLOWING:
				return new BlockFlatColored( type.shadeConvertVariant[varientNum], 0, varientNum );
			case NORMAL:
				return new BlockFlatColored( 0, 0, varientNum );
			case TRANSPARENT:
				return new BlockFlatColoredTranslucent( 0, type.shadeConvertVariant[varientNum], varientNum );
		}

		throw new RuntimeException( "Invalid construction." );
	}

	/**
	 * Called by Block's Constructor.
	 */
	@Override
	protected BlockState createBlockState()
	{
		shadeOffset = offset;
		configuration = newConfig;
		maxShade = Math.min( configuration.MAX_SHADES_MINUS_ONE, shadeOffset + configuration.META_SCALE_MINUS_ONE );
		return new BlockState( this, new IProperty[] { shade = PropertyInteger.create( "shade", 0, configuration.META_SCALE_MINUS_ONE ) } );
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

	// get details about a shade.
	public EnumSet<EnumFlatColorAttributes> getFlatColorAttributes(
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

	public EnumFlatBlockType getType()
	{
		return configuration.type;
	}

	public int getVarient()
	{
		return configuration.shadeConvertVariant[varient];
	}

	public EnumFlatBlockType getCraftable()
	{
		return configuration.type;
	}

}
