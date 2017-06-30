package mod.flatcoloredblocks.client;

import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import mod.flatcoloredblocks.craftingitem.ItemColoredBlockCrafter;

public interface IClientSide
{

	void configureBlockRender(
			BlockFlatColored cb,
			ItemBlockFlatColored cbi );

	public void configureCraftingRender(
			ItemColoredBlockCrafter crafterItem );

	void preinit();

	void init();
}
