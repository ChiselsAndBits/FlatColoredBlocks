package mod.flatcoloredblocks.craftingitem;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.block.EnumFlatColorAttributes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Generates and Crafts items that are seen in the crafting item's gui.
 */
public class InventoryColoredBlockCrafter implements IInventory
{

	private final EntityPlayer thePlayer;
	private final ContainerColoredBlockCrafter craftingContainer;

	private final ArrayList<ItemStack> options = new ArrayList<ItemStack>();
	public int offset = 0;

	public InventoryColoredBlockCrafter(
			final EntityPlayer thePlayer,
			final ContainerColoredBlockCrafter coloredCrafterContainer )
	{
		this.thePlayer = thePlayer;
		craftingContainer = coloredCrafterContainer;
	}

	public static HashMap<Object, List<ItemStack>> getDyeList()
	{
		final HashMap<Object, List<ItemStack>> dyeList = new HashMap<Object, List<ItemStack>>();

		dyeList.put( EnumDyeColor.BLACK, OreDictionary.getOres( "dyeBlack" ) );
		dyeList.put( EnumDyeColor.RED, OreDictionary.getOres( "dyeRed" ) );
		dyeList.put( EnumDyeColor.GREEN, OreDictionary.getOres( "dyeGreen" ) );
		dyeList.put( EnumDyeColor.BROWN, OreDictionary.getOres( "dyeBrown" ) );
		dyeList.put( EnumDyeColor.BLUE, OreDictionary.getOres( "dyeBlue" ) );
		dyeList.put( EnumDyeColor.PURPLE, OreDictionary.getOres( "dyePurple" ) );
		dyeList.put( EnumDyeColor.CYAN, OreDictionary.getOres( "dyeCyan" ) );
		dyeList.put( EnumDyeColor.SILVER, OreDictionary.getOres( "dyeLightGray" ) );
		dyeList.put( EnumDyeColor.GRAY, OreDictionary.getOres( "dyeGray" ) );
		dyeList.put( EnumDyeColor.PINK, OreDictionary.getOres( "dyePink" ) );
		dyeList.put( EnumDyeColor.LIME, OreDictionary.getOres( "dyeLime" ) );
		dyeList.put( EnumDyeColor.YELLOW, OreDictionary.getOres( "dyeYellow" ) );
		dyeList.put( EnumDyeColor.LIGHT_BLUE, OreDictionary.getOres( "dyeLightBlue" ) );
		dyeList.put( EnumDyeColor.MAGENTA, OreDictionary.getOres( "dyeMagenta" ) );
		dyeList.put( EnumDyeColor.ORANGE, OreDictionary.getOres( "dyeOrange" ) );
		dyeList.put( EnumDyeColor.WHITE, OreDictionary.getOres( "dyeWhite" ) );
		dyeList.put( EnumFlatBlockType.NORMAL, getItems( FlatColoredBlocks.instance.config.solidCraftingBlock ) );
		dyeList.put( EnumFlatBlockType.GLOWING, getItems( FlatColoredBlocks.instance.config.glowingCraftingBlock ) );
		dyeList.put( EnumFlatBlockType.TRANSPARENT, getItems( FlatColoredBlocks.instance.config.transparentCraftingBlock ) );

		return dyeList;
	}

	private InventorySummary scanPlayerInventory()
	{
		final EnumSet<EnumDyeColor> dyes = EnumSet.noneOf( EnumDyeColor.class );
		final InventoryPlayer ip = thePlayer.inventory;

		final HashMap<Object, List<ItemStack>> dyeList = getDyeList();
		final HashMap<Object, HashSet<ItemCraftingSource>> stacks = new HashMap<Object, HashSet<ItemCraftingSource>>();

		boolean hasCobblestone = false;
		boolean hasGlowstone = false;
		boolean hasGlass = false;

		for ( final Entry<Object, List<ItemStack>> items : dyeList.entrySet() )
		{
			stacks.put( items.getKey(), new HashSet<ItemCraftingSource>() );
		}

		stacks.put( null, new HashSet<ItemCraftingSource>() );

		for ( int x = 0; x < ip.getSizeInventory(); ++x )
		{
			final ItemStack is = ip.getStackInSlot( x );

			if ( is != null )
			{
				for ( final Entry<Object, List<ItemStack>> items : dyeList.entrySet() )
				{
					for ( final ItemStack ore : items.getValue() )
					{
						if ( OreDictionary.itemMatches( ore, is, false ) )
						{

							if ( items.getKey() instanceof EnumDyeColor )
							{
								dyes.add( (EnumDyeColor) items.getKey() );
							}
							else
							{
								if ( items.getKey() == EnumFlatBlockType.NORMAL )
								{
									hasCobblestone = true;
								}

								if ( items.getKey() == EnumFlatBlockType.TRANSPARENT )
								{
									hasGlass = true;
								}

								if ( items.getKey() == EnumFlatBlockType.GLOWING )
								{
									hasGlowstone = true;
								}
							}

							stacks.get( items.getKey() ).add( new ItemCraftingSource( ip, x ) );
						}
					}
				}
			}
		}

		return new InventorySummary( hasCobblestone, hasGlowstone, hasGlass, stacks, dyes );
	}

	private static List<ItemStack> getItems(
			final String name )
	{
		List<ItemStack> items = OreDictionary.getOres( name, false );

		if ( items.isEmpty() )
		{
			items = new ArrayList<ItemStack>();
			final Item it = Item.REGISTRY.getObject( new ResourceLocation( name ) );
			if ( it != null )
			{
				items.add( new ItemStack( it, 1, OreDictionary.WILDCARD_VALUE ) );
			}
		}

		return items;
	}

	/**
	 * recalculate the entire container.
	 */
	public void updateContents()
	{
		options.clear();
		BlockFlatColored.getAllShades( options );

		final InventorySummary da = scanPlayerInventory();
		final EnumSet<EnumDyeColor> dyes = da.dyes;

		final Iterator<ItemStack> i = options.iterator();
		while ( i.hasNext() )
		{
			final ItemStack is = i.next();
			final Block blk = Block.getBlockFromItem( is.getItem() );
			final IBlockState state = blk.getStateFromMeta( is.getItemDamage() );

			final Set<EnumFlatColorAttributes> charistics = ( (BlockFlatColored) blk ).getFlatColorAttributes( state );
			boolean isGood = true;

			for ( final EnumFlatColorAttributes cc : charistics )
			{
				if ( !dyes.contains( cc.primaryDye ) || !dyes.contains( cc.secondaryDye ) )
				{
					isGood = false;
				}
			}

			final EnumDyeColor alternateDye = EnumFlatColorAttributes.getAlternateDye( charistics );
			if ( alternateDye != null && dyes.contains( alternateDye ) )
			{
				isGood = true;
			}

			if ( !isGood || !da.has( ( (BlockFlatColored) blk ).getCraftable() ) )
			{
				i.remove();
			}
		}

		craftingContainer.setScroll( craftingContainer.scrollPercent );
	}

	@Override
	public String getName()
	{
		return thePlayer.getName();
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public int getSizeInventory()
	{
		return options.size();
	}

	@Override
	public ItemStack getStackInSlot(
			int index )
	{

		index += offset;
		if ( index < options.size() )
		{
			return options.get( index ).copy();
		}

		return null;
	}

	@Override
	public ItemStack decrStackSize(
			int index,
			final int count )
	{

		index += offset;
		if ( index < options.size() )
		{
			ItemStack out = options.get( index );

			if ( out.stackSize <= 0 )
			{
				out = null;
			}

			return out;
		}

		return null;
	}

	public ItemStack craftItem(
			ItemStack out,
			final int count,
			final boolean simulate )
	{
		if ( out == null )
		{
			return null;
		}

		out = out.copy();
		out.stackSize = 0;

		final InventorySummary da = scanPlayerInventory();
		final Block blk = Block.getBlockFromItem( out.getItem() );
		final IBlockState state = blk.getStateFromMeta( out.getItemDamage() );

		final Set<EnumFlatColorAttributes> charistics = ( (BlockFlatColored) blk ).getFlatColorAttributes( state );
		final Object Craftable = ( (BlockFlatColored) blk ).getCraftable();
		final HashSet<EnumDyeColor> requiredDyes = new HashSet<EnumDyeColor>();

		final int craftAmount = Craftable instanceof EnumFlatBlockType ? ( (EnumFlatBlockType) Craftable ).getOutputCount() : 1;

		final EnumDyeColor alternateDye = EnumFlatColorAttributes.getAlternateDye( charistics );
		final HashSet<EnumDyeColor> alternateSet = new HashSet<EnumDyeColor>();

		if ( alternateDye != null )
		{
			alternateSet.add( alternateDye );
		}

		for ( final EnumFlatColorAttributes cc : charistics )
		{
			requiredDyes.add( cc.primaryDye );
			requiredDyes.add( cc.secondaryDye );
		}

		for ( int x = 0; x < count && out.stackSize + craftAmount <= 64; ++x )
		{
			boolean isGood = true;

			final ItemCraftingSource isx = findItem( da.stacks.get( Craftable ), simulate );
			if ( isx == null )
			{
				isGood = false;
			}

			HashSet<EnumDyeColor> usedSet = alternateSet;
			availableDyeTest:
			{

				// test to see if there is an alternate, and if there is, see if
				// the player has one...
				if ( alternateDye != null )
				{
					final ItemCraftingSource is = findItem( da.stacks.get( alternateDye ), simulate );
					if ( is != null )
					{
						break availableDyeTest;
					}
				}

				// no alternate, try standard set.
				usedSet = requiredDyes;
				for ( final EnumDyeColor dye : requiredDyes )
				{
					final ItemCraftingSource is = findItem( da.stacks.get( dye ), simulate );
					if ( is == null )
					{
						isGood = false;
					}
				}
			}

			if ( isGood )
			{
				for ( final EnumDyeColor dye : usedSet )
				{
					final ItemCraftingSource is = findItem( da.stacks.get( dye ), simulate );

					is.consume( 1 );
				}

				isx.consume( 1 );
				out.stackSize += craftAmount;
			}
			else
			{
				break;
			}
		}

		updateContents();

		if ( out.stackSize <= 0 )
		{
			out = null;
		}

		return out;
	}

	private ItemCraftingSource findItem(
			final HashSet<ItemCraftingSource> hashSet,
			final boolean simulate )
	{
		for ( final ItemCraftingSource src : hashSet )
		{
			src.simulate = simulate;
			final ItemStack is = src.getStack();
			if ( is != null && is.stackSize > 0 )
			{
				return src;
			}
		}

		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(
			final int index )
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(
			final int index,
			final ItemStack stack )
	{

	}

	@Override
	public int getInventoryStackLimit()
	{
		return 0;
	}

	@Override
	public void markDirty()
	{

	}

	@Override
	public boolean isUseableByPlayer(
			final EntityPlayer player )
	{
		return true;
	}

	@Override
	public void openInventory(
			final EntityPlayer player )
	{

	}

	@Override
	public void closeInventory(
			final EntityPlayer player )
	{

	}

	@Override
	public boolean isItemValidForSlot(
			final int index,
			final ItemStack stack )
	{
		return false;
	}

	@Override
	public int getField(
			final int id )
	{
		return 0;
	}

	@Override
	public void setField(
			final int id,
			final int value )
	{

	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		options.clear();
	}

}
