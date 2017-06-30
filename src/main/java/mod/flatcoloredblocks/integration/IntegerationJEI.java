package mod.flatcoloredblocks.integration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Integration with Just Enough Items, used to blacklist large numbers of
 * uncraftable blocks.
 */
public class IntegerationJEI
{

	List<ItemStack> items = new ArrayList<ItemStack>();

	public void blackListBlock(
			final Item b )
	{
		items.add( new ItemStack( b, 1, OreDictionary.WILDCARD_VALUE ) );
	}

}
