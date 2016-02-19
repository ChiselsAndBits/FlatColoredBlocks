package mod.flatcoloredblocks.integration;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class PluginJEI extends BlankModPlugin
{
	@Override
	public void register(
			@Nonnull final IModRegistry registry )
	{
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		final IItemBlacklist blacklist = jeiHelpers.getItemBlacklist();
		for ( final ItemStack is : FlatColoredBlocks.instance.jei.items )
		{
			blacklist.addItemToBlacklist( is );
		}
	}
}
