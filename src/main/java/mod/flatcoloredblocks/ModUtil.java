package mod.flatcoloredblocks;

import com.sun.istack.internal.NotNull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings( "deprecation" )
public class ModUtil
{

	public static void alterStack(
			@NotNull final ItemStack stack,
			final int deltaStackSize )
	{
		setStackSize( stack, getStackSize( stack ) + deltaStackSize );
	}

	public static void setStackSize(
			@NotNull final ItemStack stack,
			final int stackSize )
	{
		stack.func_190920_e( stackSize );
	}

	public static int getStackSize(
			@NotNull final ItemStack stack )
	{
		return stack.func_190916_E();
	}

	public static ItemStack getEmptyStack()
	{
		return ItemStack.field_190927_a;
	}

	public static String translateToLocal(
			final String string )
	{
		return I18n.translateToLocal( string );
	}

	public static IBlockState getStateFromMeta(
			final Block blk,
			final int metadata )
	{
		return blk.getStateFromMeta( metadata );
	}

	public static boolean isEmpty(
			final ItemStack i )
	{
		return i.func_190926_b();
	}

}
