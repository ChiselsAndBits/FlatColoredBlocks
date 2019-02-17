package mod.flatcoloredblocks;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

@SuppressWarnings( "deprecation" )
public class ModUtil
{

	public static void alterStack(
			@Nonnull final ItemStack stack,
			final int deltaStackSize )
	{
		setStackSize( stack, getStackSize( stack ) + deltaStackSize );
	}

	public static void setStackSize(
			@Nonnull final ItemStack stack,
			final int stackSize )
	{
		stack.setCount( stackSize );
	}

	public static int getStackSize(
			@Nonnull final ItemStack stack )
	{
		return stack.getCount();
	}

	public static ItemStack getEmptyStack()
	{
		return ItemStack.EMPTY;
	}

	public static String translateToLocal(
			final String string )
	{
		return I18n.format( string );
	}

	public static boolean isEmpty(
			final ItemStack i )
	{
		return i.isEmpty();
	}

	public static IBlockState getStateFromMeta(
			Block blk,
			ItemStack stack )
	{
		// TODO Auto-generated method stub
		return null;
	}

}
