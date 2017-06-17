package mod.flatcoloredblocks.block;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockFlatColored extends Block
{

	private static ArrayList<BlockFlatColored> coloredBlocks = new ArrayList<BlockFlatColored>();
	private static int offset;
	private static BlockHSVConfiguration newConfig;

	// block specific values.
	private int shadeOffset; // first shade in block
	private int maxShade; // this is the last shade in this block.
	private BlockHSVConfiguration configuration; // HSV for this block.
	private PropertyInteger shade; // block state configuration for block.
	private final int varient;

	// only used for description.
	public final int opacity;
	public final int lightValue;

	@Override
	public MapColor getMapColor(
			final IBlockState state,
			final IBlockAccess world,
			final BlockPos pos )
	{
		for ( final EnumFlatColorAttributes attr : getFlatColorAttributes( state ) )
		{
			if ( !attr.isModifier )
			{
				return attr.mapColor;
			}
		}

		return MapColor.SNOW;
	}

	// also used in item for item stack color.
	public int colorFromState(
			final IBlockState state )
	{
		final int fullAlpha = 0xff << 24;
		return ConversionHSV2RGB.toRGB( hsvFromState( state ) ) | fullAlpha;
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

	protected BlockFlatColored(
			final float lightValue,
			final float opacity,
			final int varientNum )
	{
		super( opacity > 0.001 ? Material.GLASS : Material.ROCK );
		setUnlocalizedName( "flatcoloredblocks.flatcoloredblock." + offset );

		// mimic stone..
		setHardness( 1.5F );
		setResistance( 10.0F );
		setHarvestLevel( "pickaxe", 0 );
		setLightLevel( FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT ? Math.max( 0, Math.min( 15, lightValue / 255.0f ) ) : 0 );
		setLightOpacity( opacity > 0.001 ? 0 : 255 );
		setSoundType( opacity > 0.001 ? SoundType.GLASS : SoundType.STONE );

		translucent = opacity > 0.001;
		varient = varientNum;

		coloredBlocks.add( this );
		this.opacity = 100 - Math.round( opacity * 100 / 255 );
		this.lightValue = (int) ( lightValue * 15 / 255 );
	}

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
			default:
				throw new RuntimeException( "Invalid construction." );
		}
	}

	/**
	 * Called by Block's Constructor.
	 */
	@Override
	protected BlockStateContainer createBlockState()
	{
		shadeOffset = offset;
		configuration = newConfig;
		maxShade = shadeOffset + configuration.META_SCALE_MINUS_ONE;

		if ( configuration.MAX_SHADES_MINUS_ONE < maxShade )
		{
			maxShade = configuration.MAX_SHADES_MINUS_ONE;
			return new BlockStateContainer( this, new IProperty[] { shade = PropertyInteger.create( "shade", 0, maxShade - shadeOffset ) } );
		}

		return new BlockStateContainer( this, new IProperty[] { shade = PropertyInteger.create( "shade", 0, configuration.META_SCALE_MINUS_ONE ) } );
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
			final List<ItemStack> list,
			final int qty )
	{
		final Item item = Item.getItemFromBlock( this );

		for ( int x = shadeOffset; x <= maxShade; ++x )
		{
			list.add( new ItemStack( item, qty, x - shadeOffset ) );
		}
	}

	@Override
	public void getSubBlocks(
			final CreativeTabs tab,
			final NonNullList<ItemStack> list )
	{
		outputShades( list, 1 );
	}

	// generates a list of all shades without caring about which block it is.
	public static void getAllShades(
			final List<ItemStack> list )
	{
		for ( final BlockFlatColored cb : coloredBlocks )
		{
			cb.outputShades( list, cb.getCraftCount() );
		}
	}

	static public List<BlockFlatColored> getAllBlocks()
	{
		return coloredBlocks;
	}

	private int getCraftCount()
	{
		if ( getCraftable() == EnumFlatBlockType.NORMAL )
		{
			return FlatColoredBlocks.instance.config.solidCraftingOutput;
		}

		if ( getCraftable() == EnumFlatBlockType.TRANSPARENT )
		{
			return FlatColoredBlocks.instance.config.transparentCraftingOutput;
		}

		if ( getCraftable() == EnumFlatBlockType.GLOWING )
		{
			return FlatColoredBlocks.instance.config.glowingCraftingOutput;
		}

		return 1;
	}

	// get details about a shade.
	public Set<EnumFlatColorAttributes> getFlatColorAttributes(
			final IBlockState state )
	{
		final int out = hsvFromState( state );

		final Set<EnumFlatColorAttributes> result = EnumSet.noneOf( EnumFlatColorAttributes.class );

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
