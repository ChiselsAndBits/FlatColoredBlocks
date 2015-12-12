package mod.flatcoloredblocks.craftingitem;

import java.util.List;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.gui.ModGuiTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemColoredBlockCrafter extends Item
{

	public ItemColoredBlockCrafter()
	{
		setCreativeTab( FlatColoredBlocks.instance.creativeTab );
		setUnlocalizedName( "flatcoloredblocks.coloredcraftingitem" );
	}

	@Override
	public ItemStack onItemRightClick(
			final ItemStack itemStackIn,
			final World worldIn,
			final EntityPlayer playerIn )
	{
		if ( worldIn.isRemote )
		{
			return itemStackIn;
		}

		playerIn.openGui( FlatColoredBlocks.instance, ModGuiTypes.ColoredCrafter.ordinal(), worldIn, 0, 0, 0 );
		return itemStackIn;
	}

	@Override
	public void addInformation(
			final ItemStack stack,
			final EntityPlayer playerIn,
			final List<String> tooltip,
			final boolean advanced )
	{
		tooltip.add( StatCollector.translateToLocal( "item.flatcoloredblocks.coloredcraftingitem.tip1" ) );
		tooltip.add( StatCollector.translateToLocal( "item.flatcoloredblocks.coloredcraftingitem.tip2" ) );

		super.addInformation( stack, playerIn, tooltip, advanced );
	}

}
