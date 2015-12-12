package mod.flatcoloredblocks.block;

import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockFlatColored extends ItemBlock
{

	private IBlockState getStateFromStack(
			final ItemStack stack )
	{
		return getBlock().getStateFromMeta( stack.getItemDamage() );
	}

	private String getColorPrefix(
			final EnumSet<EnumFlatColorAttributes> which )
	{
		if ( which.contains( EnumFlatColorAttributes.dark ) )
		{
			return "flatcoloredblocks.dark";
		}

		if ( which.contains( EnumFlatColorAttributes.light ) )
		{
			return "flatcoloredblocks.light";
		}

		return "flatcoloredblocks.";
	}

	private String getColorHueName(
			final EnumSet<EnumFlatColorAttributes> characteristics )
	{
		for ( final EnumFlatColorAttributes c : characteristics )
		{
			if ( !c.isModifier )
			{
				return c.name();
			}
		}

		return EnumFlatColorAttributes.black.name();
	}

	public ItemBlockFlatColored(
			final Block block )
	{
		super( block );
		setHasSubtypes( true );
	}

	@Override
	public int getMetadata(
			final int damage )
	{
		return damage; // override and return damage instead of 0
	}

	@Override
	public String getItemStackDisplayName(
			final ItemStack stack )
	{
		final IBlockState state = getStateFromStack( stack );
		final int shadeNum = BlockFlatColored.getShadeNumber( state );

		final EnumSet<EnumFlatColorAttributes> colorChars = BlockFlatColored.getFlatColorAttributes( state );

		final String prefix = getColorPrefix( colorChars );
		final String hue = getColorHueName( colorChars );

		return StatCollector.translateToLocal( prefix + hue + ".name" ) + " #" + shadeNum;
	}

	@Override
	public int getColorFromItemStack(
			final ItemStack stack,
			final int renderPass )
	{
		final IBlockState state = getStateFromStack( stack );
		return BlockFlatColored.colorFromState( state );
	}

}
