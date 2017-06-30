package mod.flatcoloredblocks.client;

import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import mod.flatcoloredblocks.craftingitem.ItemColoredBlockCrafter;

public class DummyClientSide implements IClientSide
{

	@Override
	public void configureBlockRender(
			BlockFlatColored cb,
			ItemBlockFlatColored cbi )
	{
	}

	@Override
	public void configureCraftingRender(
			final ItemColoredBlockCrafter icbc )
	{
	}

	@Override
	public void preinit()
	{

	}

	@Override
	public void init()
	{

	}

}
