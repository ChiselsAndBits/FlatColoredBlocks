package mod.flatcoloredblocks.integration;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class PluginJEI implements IModPlugin
{
	@Override
	public void register(
			@Nonnull final IModRegistry registry )
	{
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		final IIngredientBlacklist blacklist = jeiHelpers.getIngredientBlacklist();
		for ( final ItemStack is : FlatColoredBlocks.instance.jei.items )
		{
			blacklist.addIngredientToBlacklist( is );
		}
	}
}
