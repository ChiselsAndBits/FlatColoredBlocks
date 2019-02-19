package mod.flatcoloredblocks.block;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockFlatColored extends Block
{

	private static ArrayList<BlockFlatColored> coloredBlocks = new ArrayList<BlockFlatColored>();
	private static int offset;
	private static BlockHSVConfiguration newConfig;

	// block specific values.
	private int shadeOffset; // first shade in block
	private int maxShade; // this is the last shade in this block.
	private BlockHSVConfiguration configuration; // HSV for this block.
	private IntegerProperty shade; // block state configuration for block.
	private final int varient;

	// only used for description.
	public final int opacity;
	public final int lightValue;

	@Override
	public IBlockState getStateForPlacement(
			BlockItemUseContext context )
	{
		return super.getStateForPlacement( context ).with( shade, context.getItem().getOrCreateTag().getInt( "Shade" ) );
	}

	@Override
	public MaterialColor getMapColor(
			IBlockState state,
			IBlockReader world,
			BlockPos pos )
	{
		for ( final EnumFlatColorAttributes attr : getFlatColorAttributes( state ) )
		{
			if ( !attr.isModifier )
			{
				return attr.mapColor;
			}
		}

		return MaterialColor.SNOW;
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
			return state.get( cb.shade ) + cb.shadeOffset;
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
			BlockHSVConfiguration type,
			final float lightValue,
			final float opacity,
			final int varientNum )
	{
		super( Block.Properties.create( opacity > 0.001 ? Material.GLASS : Material.ROCK )
				.hardnessAndResistance( 1.5F, 10.0F )
				.lightValue( (int) ( FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT ? Math.max( 0, Math.min( 15, 15.0f * ( lightValue / 255.0f ) ) ) : 0 ) )
				.sound( opacity > 0.001 ? SoundType.GLASS : SoundType.STONE ) );

		final String regName = type.getBlockName( varientNum );

		// use the same name for item/block combo.
		this.setRegistryName( FlatColoredBlocks.MODID, regName );

		// mimic stone..
		// TODO: setHarvestLevel( "pickaxe", 0 );

		// TODO: setLightOpacity( opacity > 0.001 ? 0 : 255 );

		// TODO: translucent = opacity > 0.001;
		varient = varientNum;

		coloredBlocks.add( this );
		this.lightValue = (int) ( FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT ? Math.max( 0, Math.min( 15, 15.0f * ( lightValue / 255.0f ) ) ) : 0 );
		this.opacity = 100 - Math.round( opacity * 100 / 255 );
	}

	public static BlockFlatColored construct(
			final BlockHSVConfiguration type,
			final int varientNum )
	{
		// pass these to createBlockState
		newConfig = type;

		// construct the block..
		switch ( type.type )
		{
			case GLOWING:
				return new BlockFlatColored( type, type.shadeConvertVariant[varientNum], 0, varientNum );
			case NORMAL:
				return new BlockFlatColored( type, 0, 0, varientNum );
			case TRANSPARENT:
				return new BlockFlatColoredTranslucent( type, 0, type.shadeConvertVariant[varientNum], varientNum );
			default:
				throw new RuntimeException( "Invalid construction." );
		}
	}

	protected void fillStateContainer(
			StateContainer.Builder<Block, IBlockState> builder )
	{
		shadeOffset = offset;
		configuration = newConfig;
		maxShade = shadeOffset + configuration.MAX_SHADES_MINUS_ONE;

		maxShade = configuration.MAX_SHADES_MINUS_ONE;
		builder.add( shade = IntegerProperty.create( "shade", 0, maxShade - shadeOffset ) );
	}

	@Override
	public void getDrops(
			IBlockState state,
			NonNullList<ItemStack> drops,
			World world,
			BlockPos pos,
			int fortune )
	{
		drops.add( getItem( world, pos, state ) );
	}

	@Override
	public ItemStack getItem(
			IBlockReader world,
			BlockPos pos,
			IBlockState state )
	{
		ItemStack is = new ItemStack( this );
		is.getOrCreateTag().setInt( "Shade", state.get( shade ) );
		return is;
	}

	// convert block into all possible ItemStacks.
	private void outputShades(
			final List<ItemStack> list,
			final int qty )
	{
		final Item item = Item.getItemFromBlock( this );

		for ( int x = shadeOffset; x <= maxShade; ++x )
		{
			ItemStack is = new ItemStack( item, qty );
			is.getOrCreateTag().setInt( "Shade", x - shadeOffset );
			list.add( is );
		}
	}

	public void fillItemGroup(
			ItemGroup group,
			NonNullList<ItemStack> items )
	{
		for ( int x = shadeOffset; x <= maxShade; ++x )
		{
			ItemStack is = new ItemStack( this, 1 );
			is.getOrCreateTag().setInt( "Shade", x - shadeOffset );
			items.add( is );
		}
	}

	public IBlockState getstateForStack(
			ItemStack stack )
	{
		return getDefaultState().with( shade, stack.getOrCreateTag().getInt( "Shade" ) );
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
