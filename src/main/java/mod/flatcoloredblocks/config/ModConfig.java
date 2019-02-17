package mod.flatcoloredblocks.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

public class ModConfig extends Configuration
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
		solidCraftingBlock = "cobblestone";
		transparentCraftingBlock = "blockGlass";
		glowingCraftingBlock = "glowstone";
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
		super( path );
		myPath = path;

		MinecraftForge.EVENT_BUS.register( this );

		setDefaults();
		populateSettings();
		save();
	}

	public void updateLastMaxShades()
	{
		LAST_MAX_SHADES = FlatColoredBlocks.instance.getFullNumberOfShades();
		get( "StartupGui", "LAST_MAX_SHADES", LAST_MAX_SHADES ).set( LAST_MAX_SHADES );
	}

	void populateSettings()
	{
		LAST_MAX_SHADES = get( "StartupGui", "LAST_MAX_SHADES", 0 ).getInt();

		final Class<ModConfig> me = ModConfig.class;
		for ( final Field f : me.getDeclaredFields() )
		{
			final Configured c = f.getAnnotation( Configured.class );
			if ( c != null )
			{
				try
				{
					if ( f.getType() == long.class || f.getType() == Long.class )
					{
						final long defaultValue = f.getLong( this );
						final long value = get( c.category(), f.getName(), (int) defaultValue ).getInt();
						f.set( this, value );
					}
					else if ( f.getType().isEnum() )
					{
						final Enum<?> defaultValue = (Enum<?>) f.get( this );
						try
						{
							final Enum<?>[] valuesList = (Enum[]) f.getType().getMethod( "values" ).invoke( null );
							final String[] values = new String[valuesList.length];

							for ( int x = 0; x < values.length; ++x )
							{
								values[x] = valuesList[x].name();
							}

							final String strValue = get( c.category(), f.getName(), defaultValue.name(), "", values ).getString();
							final Method method = f.getType().getMethod( "valueOf", Class.class, String.class );
							final Enum<?> value = (Enum<?>) method.invoke( null, f.getType(), strValue );
							f.set( this, value );
						}
						catch ( final Exception e )
						{
							e.printStackTrace();
						}
					}
					else if ( f.getType() == String.class )
					{
						final String defaultValue = (String) f.get( this );
						final String value = get( c.category(), f.getName(), defaultValue ).getString();
						f.set( this, value );
					}
					else if ( f.getType() == int.class || f.getType() == Integer.class )
					{
						final int defaultValue = f.getInt( this );
						final int value = get( c.category(), f.getName(), defaultValue ).getInt();
						f.set( this, value );
					}
					else if ( f.getType() == double.class || f.getType() == Double.class )
					{
						final double defaultValue = f.getDouble( this );
						final double value = get( c.category(), f.getName(), defaultValue ).getDouble();
						f.set( this, value );
					}
					else if ( f.getType() == boolean.class || f.getType() == Boolean.class )
					{
						final boolean defaultValue = f.getBoolean( this );
						final boolean value = get( c.category(), f.getName(), defaultValue ).getBoolean();
						f.set( this, value );
					}
				}
				catch ( final Throwable e )
				{
					throw new RuntimeException( e );
				}
			}
		}
	}

	@Override
	public Property get(
			final String category,
			final String key,
			final String defaultValue,
			final String comment,
			final Property.Type type )
	{
		final Property prop = super.get( category, key, defaultValue, comment, type );

		if ( prop != null && !category.equals( "Client Settings" ) )
		{
			prop.setRequiresMcRestart( true );
		}

		return prop == null ? null : prop;
	}

	@SubscribeEvent
	public void onConfigChanged(
			final ConfigChangedEvent.OnConfigChangedEvent eventArgs )
	{
		if ( eventArgs.getModID().equals( FlatColoredBlocks.MODID ) )
		{
			populateSettings();
			save();
		}
	}

	@Override
	public void save()
	{
		if ( hasChanged() )
		{
			super.save();
		}
	}

	public File getFilePath()
	{
		return myPath;
	}

}
