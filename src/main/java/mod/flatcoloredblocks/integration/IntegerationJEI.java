package mod.flatcoloredblocks.integration;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IItemBlacklist;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Integration with Just Enough Items, used to blacklist large numbers of
 * uncraftable blocks.
 */
public class IntegerationJEI
{

	List<ItemStack> items = new ArrayList<ItemStack>();

	public void blackListBlock(
			final Block b )
	{
		items.add( new ItemStack( b, 1, OreDictionary.WILDCARD_VALUE ) );
	}

	public void init()
	{
		if ( Loader.isModLoaded( "JEI" ) )
		{
			sendtoJEI();
		}

		items = null;
	}

	private void sendtoJEI()
	{
		final IItemBlacklist blacklist = mezz.jei.api.JEIManager.itemBlacklist;

		for ( final ItemStack is : items )
		{
			blacklist.addItemToBlacklist( is );
		}
	}

}
