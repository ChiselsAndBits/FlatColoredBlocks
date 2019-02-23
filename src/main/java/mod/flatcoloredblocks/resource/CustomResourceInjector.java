package mod.flatcoloredblocks.resource;

import java.util.function.Predicate;

import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

public class CustomResourceInjector implements ISelectiveResourceReloadListener
{
	static CustomFileProvider generatedFiles = new CustomFileProvider();

	public static void addResource(
			String folder,
			String resourceName,
			String ext,
			byte[] data )
	{
		generatedFiles.fakeFiles.put( folder + "/" + resourceName + ext, data );
	}

	@Override
	public void onResourceManagerReload(
			IResourceManager resourceManager,
			Predicate<IResourceType> resourcePredicate )
	{
		if ( resourcePredicate.test( VanillaResourceType.MODELS ) )
		{
			if ( resourceManager instanceof SimpleReloadableResourceManager )
			{
				( (SimpleReloadableResourceManager) resourceManager ).addResourcePack( generatedFiles );
				ClientSide.instance.createResources();
			}
		}
	}

}
