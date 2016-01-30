package mod.flatcoloredblocks.integration;

import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class PluginJEI implements IModPlugin
{
	@Override
	public void onJeiHelpersAvailable(
			final IJeiHelpers jeiHelpers )
	{
		final IItemBlacklist blacklist = jeiHelpers.getItemBlacklist();
		for ( final ItemStack is : FlatColoredBlocks.instance.jei.items )
		{
			blacklist.addItemToBlacklist( is );
		}
	}

	@Override
	public void onItemRegistryAvailable(
			final IItemRegistry itemRegistry )
	{

	}

	@Override
	public void register(
			final IModRegistry registry )
	{

	}

	@Override
	public void onRecipeRegistryAvailable(
			final IRecipeRegistry recipeRegistry )
	{

	}

}
