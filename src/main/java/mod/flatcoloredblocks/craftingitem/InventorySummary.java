package mod.flatcoloredblocks.craftingitem;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

import mod.flatcoloredblocks.block.EnumFlatBlockType;
import net.minecraft.item.EnumDyeColor;

/**
 * Stores the result of analsis of a player's inventory.
 */
class InventorySummary
{
	public final boolean hasCobblestone;
	public final boolean hasGlowstone;
	public final boolean hasGlass;
	public final HashMap<Object, HashSet<ItemCraftingSource>> stacks;
	public final EnumSet<EnumDyeColor> dyes;

	public InventorySummary(
			final boolean hasCobblestone,
			final boolean hasGlowstone,
			final boolean hasGlass,
			final HashMap<Object, HashSet<ItemCraftingSource>> stackList,
			final EnumSet<EnumDyeColor> dyeSet )
	{
		this.hasCobblestone = hasCobblestone;
		this.hasGlowstone = hasGlowstone;
		this.hasGlass = hasGlass;
		stacks = stackList;
		dyes = dyeSet;
	}

	public boolean has(
			final EnumFlatBlockType craftable )
	{
		switch ( craftable )
		{
			case GLOWING:
				return hasGlowstone;
			case NORMAL:
				return hasCobblestone;
			case TRANSPARENT:
				return hasGlass;
		}

		return false;
	}

}