package mod.flatcoloredblocks.craftingitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mod.flatcoloredblocks.ModUtil;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.block.EnumFlatColorAttributes;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

public class FlatColoredBlockRecipe implements IRecipe
{

	public ItemStack getRequirements(
			final InventoryCrafting is )
	{
		if ( is == null )
		{
			return ModUtil.getEmptyStack();
		}

		ItemStack target = null;
		IBlockState state = null;
		BlockFlatColored flatBlock = null;
		boolean hasCrafter = false;

		final List<ItemStack> otherItems = new ArrayList<ItemStack>();
		for ( int x = 0; x < is.getSizeInventory(); ++x )
		{
			final ItemStack i = is.getStackInSlot( x );
			if ( i == null || i.getItem() == null || ModUtil.isEmpty( i ) )
			{
				continue;
			}

			final Block blk = Block.getBlockFromItem( i.getItem() );
			if ( blk instanceof BlockFlatColored )
			{
				if ( state != null )
				{
					return ModUtil.getEmptyStack();
				}

				flatBlock = (BlockFlatColored) blk;
				target = i.copy();
				state = ModUtil.getStateFromMeta( blk, i.getItemDamage() );
			}
			else if ( i.getItem() instanceof ItemColoredBlockCrafter )
			{
				if ( hasCrafter )
				{
					return ModUtil.getEmptyStack();
				}

				hasCrafter = true;
			}
			else
			{
				otherItems.add( i );
			}
		}

		if ( hasCrafter && target != null && flatBlock != null )
		{
			final Set<EnumFlatColorAttributes> charistics = flatBlock.getFlatColorAttributes( state );
			final Object Craftable = flatBlock.getCraftable();
			final HashSet<EnumDyeColor> requiredDyes = new HashSet<EnumDyeColor>();

			final int craftAmount = Craftable instanceof EnumFlatBlockType ? ( (EnumFlatBlockType) Craftable ).getOutputCount() : 1;
			ModUtil.setStackSize( target, craftAmount );

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

			final HashMap<Object, List<ItemStack>> dyeList = InventoryColoredBlockCrafter.getDyeList();
			if ( !alternateSet.isEmpty() && testRequirements( alternateSet, flatBlock.getCraftable(), otherItems, dyeList ) )
			{
				return target;
			}

			if ( testRequirements( requiredDyes, flatBlock.getCraftable(), otherItems, dyeList ) )
			{
				return target;
			}
		}

		return ModUtil.getEmptyStack();
	}

	private boolean testRequirements(
			final HashSet<EnumDyeColor> requiredDyes,
			final EnumFlatBlockType craftable,
			final List<ItemStack> otherItems,
			final HashMap<Object, List<ItemStack>> dyeList )
	{
		final List<ItemStack> testList = cloneList( otherItems );
		final List<ItemStack> buildingMaterials = dyeList.get( craftable );

		if ( findAndRemove( buildingMaterials, testList ) )
		{
			for ( final EnumDyeColor dye : requiredDyes )
			{
				if ( !findAndRemove( dyeList.get( dye ), testList ) )
				{
					return false;
				}
			}

			return testList.isEmpty();
		}

		return false;
	}

	private boolean findAndRemove(
			final List<ItemStack> matchList,
			final List<ItemStack> testList )
	{
		for ( final ItemStack match : matchList )
		{
			for ( int idx = 0; idx < testList.size(); ++idx )
			{
				final ItemStack test = testList.get( idx );
				if ( OreDictionary.itemMatches( match, test, false ) )
				{
					testList.remove( idx );
					return true;
				}
			}
		}
		return false;
	}

	private List<ItemStack> cloneList(
			final List<ItemStack> otherItems )
	{
		final List<ItemStack> l = new ArrayList<ItemStack>();
		l.addAll( otherItems );
		return l;
	}

	@Override
	public boolean matches(
			final InventoryCrafting inv,
			final World worldIn )
	{
		return getRequirements( inv ) != null;
	}

	@Override
	public ItemStack getCraftingResult(
			final InventoryCrafting inv )
	{
		return getRequirements( inv );
	}

	@Override
	public int getRecipeSize()
	{
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ModUtil.getEmptyStack();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(
			final InventoryCrafting inv )
	{
		final NonNullList<ItemStack> ret = NonNullList.<ItemStack> func_191197_a( inv.getSizeInventory(), ItemStack.field_190927_a );

		for ( int i = 0; i < ret.size(); i++ )
		{
			final ItemStack is = inv.getStackInSlot( i );
			if ( is != null )
			{
				if ( is.getItem() instanceof ItemColoredBlockCrafter || is.getItem() instanceof ItemBlockFlatColored )
				{
					ModUtil.alterStack( is, 1 );
				}
				else
				{
					final ItemStack containerItem = ForgeHooks.getContainerItem( is );

					if ( containerItem != null && !ModUtil.isEmpty( containerItem ) )
					{
						ret.set( i, containerItem );
					}
				}
			}
		}
		return ret;
	}

}
