package mod.flatcoloredblocks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log
{

	private Log()
	{

	}

	private static Logger getLogger()
	{
		return LogManager.getLogger( FlatColoredBlocks.MODID );
	}

	public static void logError(
			final String message,
			final Exception e )
	{
		getLogger().error( message, e );
	}

	public static void info(
			final String message )
	{
		getLogger().info( message );
	}

}
