package mod.flatcoloredblocks.config;

/**
 * Textures available for use.
 */
public enum EnumFlatTransparentBlockTextures
{
	FLAWLESS_GLASS, SEMI_GLASS, GLASS, NOISE_GLASS,;

	public String resourceName()
	{
		return name().toLowerCase();
	}
}
