package mod.flatcoloredblocks.craftingitem;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.item.EnumDyeColor;

/**
 * Stores the result of analsis of a player's inventory.
 */
class InventorySummary
{
	public final boolean hasCobblestone;
	public final HashMap<EnumDyeColor, HashSet<ItemCraftingSource>> stacks;
	public final EnumSet<EnumDyeColor> dyes;

	public InventorySummary(
			final boolean hasCobble,
			final HashMap<EnumDyeColor, HashSet<ItemCraftingSource>> stackList,
			final EnumSet<EnumDyeColor> dyeSet )
	{
		hasCobblestone = hasCobble;
		stacks = stackList;
		dyes = dyeSet;
	}

}