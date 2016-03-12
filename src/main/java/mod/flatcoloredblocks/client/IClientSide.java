package mod.flatcoloredblocks.client;

import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.craftingitem.ItemColoredBlockCrafter;

public interface IClientSide
{

	public void configureBlockRender(
			BlockFlatColored block );

	public void configureCraftingRender(
			ItemColoredBlockCrafter crafterItem );

	void preinit();

	void init();
}
