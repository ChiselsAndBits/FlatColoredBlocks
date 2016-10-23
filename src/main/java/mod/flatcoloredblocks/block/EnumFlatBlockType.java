package mod.flatcoloredblocks.block;

import mod.flatcoloredblocks.FlatColoredBlocks;

public enum EnumFlatBlockType
{
	NORMAL( "flatcoloredblock" ),
	TRANSPARENT( "flatcoloredblock_transparent" ),
	GLOWING( "flatcoloredblock_glowing" );

	final public String blockName;

	private EnumFlatBlockType(
			final String name )
	{
		blockName = name;
	}

	public int getOutputCount()
	{
		switch ( this )
		{
			case NORMAL:
				return FlatColoredBlocks.instance.config.solidCraftingOutput;

			case TRANSPARENT:
				return FlatColoredBlocks.instance.config.transparentCraftingOutput;

			case GLOWING:
				return FlatColoredBlocks.instance.config.glowingCraftingOutput;
		}

		return 1;
	}

}
