package mod.flatcoloredblocks.block;

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

}
