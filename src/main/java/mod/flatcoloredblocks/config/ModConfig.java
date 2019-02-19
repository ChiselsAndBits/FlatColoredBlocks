package mod.flatcoloredblocks.config;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;

// TODO: Loading, Managing and Saving Values
public class ModConfig
{

	// file path...
	final private File myPath;

	// not configured..
	public int LAST_MAX_SHADES;

	@Configured( category = "Crafing" )
	public String solidCraftingBlock;

	@Configured( category = "Crafing" )
	public String transparentCraftingBlock;

	@Configured( category = "Crafing" )
	public String glowingCraftingBlock;

	@Configured( category = "Crafing" )
	public int solidCraftingOutput;

	@Configured( category = "Crafing" )
	public int transparentCraftingOutput;

	@Configured( category = "Crafing" )
	public int glowingCraftingOutput;

	@Configured( category = "Crafing" )
	public boolean allowCraftingTable;

	@Configured( category = "Integration" )
	public boolean ShowBlocksInJEI;

	@Configured( category = "Saturation" )
	public int SATURATION_SHADES;

	@Configured( category = "Saturation" )
	public double SATURATION_RANGE_EXPONENT;

	@Configured( category = "Saturation" )
	public double SATURATION_MIN;

	@Configured( category = "Saturation" )
	public double SATURATION_MAX;

	@Configured( category = "Saturation" )
	public int SATURATION_SHADES_GLOWING;

	@Configured( category = "Saturation" )
	public double SATURATION_RANGE_EXPONENT_GLOWING;

	@Configured( category = "Saturation" )
	public double SATURATION_MIN_GLOWING;

	@Configured( category = "Saturation" )
	public double SATURATION_MAX_GLOWING;

	@Configured( category = "Saturation" )
	public int SATURATION_SHADES_TRANSPARENT;

	@Configured( category = "Saturation" )
	public double SATURATION_RANGE_EXPONENT_TRANSPARENT;

	@Configured( category = "Saturation" )
	public double SATURATION_MIN_TRANSPARENT;

	@Configured( category = "Saturation" )
	public double SATURATION_MAX_TRANSPARENT;

	@Configured( category = "Hue" )
	public int HUE_SHADES;

	@Configured( category = "Hue" )
	public double HUE_RANGE_EXPONENT;

	@Configured( category = "Hue" )
	public double HUE_MIN;

	@Configured( category = "Hue" )
	public double HUE_MAX;

	@Configured( category = "Hue" )
	public int HUE_SHADES_GLOWING;

	@Configured( category = "Hue" )
	public double HUE_RANGE_EXPONENT_GLOWING;

	@Configured( category = "Hue" )
	public double HUE_MIN_GLOWING;

	@Configured( category = "Hue" )
	public double HUE_MAX_GLOWING;

	@Configured( category = "Hue" )
	public int HUE_SHADES_TRANSPARENT;

	@Configured( category = "Hue" )
	public double HUE_RANGE_EXPONENT_TRANSPARENT;

	@Configured( category = "Hue" )
	public double HUE_MIN_TRANSPARENT;

	@Configured( category = "Hue" )
	public double HUE_MAX_TRANSPARENT;

	@Configured( category = "Value" )
	public int VALUE_SHADES;

	@Configured( category = "Value" )
	public double VALUE_RANGE_EXPONENT;

	@Configured( category = "Value" )
	public double VALUE_MIN;

	@Configured( category = "Value" )
	public double VALUE_MAX;

	@Configured( category = "Value" )
	public int VALUE_SHADES_GLOWING;

	@Configured( category = "Value" )
	public double VALUE_RANGE_EXPONENT_GLOWING;

	@Configured( category = "Value" )
	public double VALUE_MIN_GLOWING;

	@Configured( category = "Value" )
	public double VALUE_MAX_GLOWING;

	@Configured( category = "Value" )
	public int VALUE_SHADES_TRANSPARENT;

	@Configured( category = "Value" )
	public double VALUE_RANGE_EXPONENT_TRANSPARENT;

	@Configured( category = "Value" )
	public double VALUE_MIN_TRANSPARENT;

	@Configured( category = "Value" )
	public double VALUE_MAX_TRANSPARENT;

	@Configured( category = "Glowing" )
	public boolean GLOWING_EMITS_LIGHT;

	@Configured( category = "Glowing" )
	public int GLOWING_SHADES;

	@Configured( category = "Glowing" )
	public double GLOWING_RANGE_EXPONENT;

	@Configured( category = "Glowing" )
	public double GLOWING_MIN;

	@Configured( category = "Glowing" )
	public double GLOWING_MAX;

	@Configured( category = "Transparency" )
	public int TRANSPARENCY_SHADES;

	@Configured( category = "Transparency" )
	public double TRANSPARENCY_RANGE_EXPONENT;

	@Configured( category = "Transparency" )
	public double TRANSPARENCY_MIN;

	@Configured( category = "Transparency" )
	public double TRANSPARENCY_MAX;

	@Configured( category = "Texture" )
	public EnumFlatBlockTextures DISPLAY_TEXTURE;

	@Configured( category = "Texture" )
	public EnumFlatBlockTextures DISPLAY_TEXTURE_GLOWING;

	@Configured( category = "Texture" )
	public EnumFlatTransparentBlockTextures DISPLAY_TEXTURE_TRANSPARENT;

	@Configured( category = "Client Settings" )
	public boolean showHSV;

	@Configured( category = "Client Settings" )
	public boolean showRGB;

	@Configured( category = "Client Settings" )
	public boolean showHEX;

	@Configured( category = "Client Settings" )
	public boolean showLight;

	@Configured( category = "Client Settings" )
	public boolean showOpacity;

	void setDefaults()
	{
		solidCraftingBlock = "forge:cobblestone";
		transparentCraftingBlock = "forge:glass";
		glowingCraftingBlock = "forge:glowstone";
		solidCraftingOutput = 1;
		transparentCraftingOutput = 1;
		glowingCraftingOutput = 1;
		allowCraftingTable = true;
		showHEX = showHSV = showRGB = showLight = showOpacity = true;

		LAST_MAX_SHADES = 0;

		// shades of 4th dimension
		GLOWING_SHADES = 1;
		GLOWING_RANGE_EXPONENT = 1.0;
		GLOWING_MIN = 1.0;
		GLOWING_MAX = 1.0;
		GLOWING_EMITS_LIGHT = true;

		TRANSPARENCY_SHADES = 1;
		TRANSPARENCY_RANGE_EXPONENT = 1.0;
		TRANSPARENCY_MIN = 0.5;
		TRANSPARENCY_MAX = 0.5;

		// normal
		HUE_SHADES = 32;
		SATURATION_SHADES = 4;
		VALUE_SHADES = 10;

		SATURATION_RANGE_EXPONENT = 0.9;
		SATURATION_MIN = 0.2;
		SATURATION_MAX = 1.0;

		HUE_RANGE_EXPONENT = 1.0;
		HUE_MIN = 0.0;
		HUE_MAX = 0.96;

		VALUE_RANGE_EXPONENT = 1.0;
		VALUE_MIN = 0.2;
		VALUE_MAX = 1.0;

		// glowing
		HUE_SHADES_GLOWING = 32;
		SATURATION_SHADES_GLOWING = 4;
		VALUE_SHADES_GLOWING = 10;

		SATURATION_RANGE_EXPONENT_GLOWING = 0.9;
		SATURATION_MIN_GLOWING = 0.2;
		SATURATION_MAX_GLOWING = 1.0;

		HUE_RANGE_EXPONENT_GLOWING = 1.0;
		HUE_MIN_GLOWING = 0.0;
		HUE_MAX_GLOWING = 0.96;

		VALUE_RANGE_EXPONENT_GLOWING = 1.0;
		VALUE_MIN_GLOWING = 0.2;
		VALUE_MAX_GLOWING = 1.0;

		// transparent
		HUE_SHADES_TRANSPARENT = 32;
		SATURATION_SHADES_TRANSPARENT = 4;
		VALUE_SHADES_TRANSPARENT = 10;

		SATURATION_RANGE_EXPONENT_TRANSPARENT = 0.9;
		SATURATION_MIN_TRANSPARENT = 0.2;
		SATURATION_MAX_TRANSPARENT = 1.0;

		HUE_RANGE_EXPONENT_TRANSPARENT = 1.0;
		HUE_MIN_TRANSPARENT = 0.0;
		HUE_MAX_TRANSPARENT = 0.96;

		VALUE_RANGE_EXPONENT_TRANSPARENT = 1.0;
		VALUE_MIN_TRANSPARENT = 0.2;
		VALUE_MAX_TRANSPARENT = 1.0;

		// textures
		DISPLAY_TEXTURE = EnumFlatBlockTextures.DRYWALL;
		DISPLAY_TEXTURE_GLOWING = EnumFlatBlockTextures.PULSE;
		DISPLAY_TEXTURE_TRANSPARENT = EnumFlatTransparentBlockTextures.SEMI_GLASS;

		// Integration
		ShowBlocksInJEI = false;
	}

	public ModConfig(
			final File path )
	{
		myPath = path;

		MinecraftForge.EVENT_BUS.register( this );

		setDefaults();
	}

	public File getFilePath()
	{
		return myPath;
	}

	public void updateLastMaxShades()
	{
		// TODO Auto-generated method stub

	}

	public void save()
	{
		// TODO Auto-generated method stub

	}

}
