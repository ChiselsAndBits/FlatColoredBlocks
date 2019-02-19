package mod.flatcoloredblocks.resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.resources.AbstractResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;

public class CustomFileProvider extends AbstractResourcePack
{

	Map<String, byte[]> fakeFiles = new HashMap<String, byte[]>();

	public CustomFileProvider()
	{
		super( new File( "internal_generator" ) );
	}

	@Override
	public Collection<ResourceLocation> getAllResourceLocations(
			ResourcePackType type,
			String pathIn,
			int maxDepth,
			Predicate<String> filter )
	{
		return Collections.emptyList();
	}

	@Override
	public Set<String> getResourceNamespaces(
			ResourcePackType type )
	{
		return Collections.singleton( FlatColoredBlocks.MODID );
	}

	@Override
	public void close() throws IOException
	{
	}

	String prefix = "assets/flatcoloredblocks/";

	@Override
	protected InputStream getInputStream(
			String resourcePath ) throws IOException
	{
		if ( resourcePath.startsWith( prefix ) )
		{
			return new ByteArrayInputStream( fakeFiles.get( resourcePath.substring( prefix.length() ) ) );
		}

		throw new IOException( "No such stream." );
	}

	@Override
	protected boolean resourceExists(
			String resourcePath )
	{
		if ( resourcePath.startsWith( prefix ) )
		{
			return fakeFiles.containsKey( resourcePath.substring( prefix.length() ) );
		}

		return false;
	}

}
