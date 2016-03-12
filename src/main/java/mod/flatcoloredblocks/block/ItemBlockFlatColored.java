package mod.flatcoloredblocks.block;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemBlockFlatColored extends ItemBlock
{

	public BlockFlatColored getColoredBlock()
	{
		return (BlockFlatColored) block;
	}

	private IBlockState getStateFromStack(
			final ItemStack stack )
	{
		return getBlock().getStateFromMeta( stack.getItemDamage() );
	}

	private String getColorPrefix(
			final Set<EnumFlatColorAttributes> which )
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
			final Set<EnumFlatColorAttributes> characteristics )
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
		final int shadeNum = getColoredBlock().getShadeNumber( state );

		final Set<EnumFlatColorAttributes> colorChars = getColoredBlock().getFlatColorAttributes( state );

		final String type = getTypeLocalization();
		final String prefix = getColorPrefix( colorChars );
		final String hue = getColorHueName( colorChars );

		return type + I18n.translateToLocal( prefix + hue + ".name" ) + " #" + shadeNum;
	}

	private String getTypeLocalization()
	{
		switch ( getColoredBlock().getType() )
		{
			case GLOWING:
				return I18n.translateToLocal( "flatcoloredblocks.Glowing.name" ) + " ";
			case TRANSPARENT:
				return I18n.translateToLocal( "flatcoloredblocks.Transparent.name" ) + " ";
			default:
				return "";
		}
	}

	public int getColorFromItemStack(
			final ItemStack stack,
			final int renderPass )
	{
		final IBlockState state = getStateFromStack( stack );
		return getColoredBlock().colorFromState( state );
	}

}
