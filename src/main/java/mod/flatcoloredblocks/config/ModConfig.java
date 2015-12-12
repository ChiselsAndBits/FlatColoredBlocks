package mod.flatcoloredblocks.config;

import java.io.File;
import java.lang.reflect.Field;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.BlockFlatColored;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModConfig extends Configuration
{

	// file path...
	final private File myPath;

	// not configured..
	public int LAST_MAX_SHADES;

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

	@Configured( category = "Hue" )
	public int HUE_SHADES;

	@Configured( category = "Hue" )
	public double HUE_RANGE_EXPONENT;

	@Configured( category = "Hue" )
	public double HUE_MIN;

	@Configured( category = "Hue" )
	public double HUE_MAX;

	@Configured( category = "Value" )
	public int VALUE_SHADES;

	@Configured( category = "Value" )
	public double VALUE_RANGE_EXPONENT;

	@Configured( category = "Value" )
	public double VALUE_MIN;

	@Configured( category = "Value" )
	public double VALUE_MAX;

	@Configured( category = "Texture" )
	public EnumFlatBlockTextures DISPLAY_TEXTURE;

	void setDefaults()
	{
		LAST_MAX_SHADES = 0;

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

		DISPLAY_TEXTURE = EnumFlatBlockTextures.DRYWALL;
		ShowBlocksInJEI = false;
	}

	public ModConfig(
			final File path )
	{
		super( path );
		myPath = path;

		// connect to forge event bus ( using both for 1.8 compat. )
		MinecraftForge.EVENT_BUS.register( this );
		FMLCommonHandler.instance().bus().register( this );

		setDefaults();
		populateSettings();
		save();
	}

	public void updateLastMaxShades()
	{
		LAST_MAX_SHADES = BlockFlatColored.getNumberOfShades();
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
					else if ( f.getType() == EnumFlatBlockTextures.class )
					{
						final EnumFlatBlockTextures defaultValue = (EnumFlatBlockTextures) f.get( this );
						try
						{
							final String[] values = new String[EnumFlatBlockTextures.values().length];
							for ( int x = 0; x < values.length; ++x )
							{
								values[x] = EnumFlatBlockTextures.values()[x].name();
							}

							final EnumFlatBlockTextures value = EnumFlatBlockTextures.valueOf( get( c.category(), f.getName(), defaultValue.name(), "", values ).getString() );
							f.set( this, value );
						}
						catch ( final Exception e )
						{
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
		return prop == null ? null : prop.setRequiresMcRestart( true );
	}

	@SubscribeEvent
	public void onConfigChanged(
			final ConfigChangedEvent.OnConfigChangedEvent eventArgs )
	{
		if ( eventArgs.modID.equals( FlatColoredBlocks.MODID ) )
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

	public String getFilePath()
	{
		return myPath.getAbsolutePath();
	}

}
