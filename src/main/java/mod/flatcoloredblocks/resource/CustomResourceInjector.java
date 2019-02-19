package mod.flatcoloredblocks.resource;

import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;

public class CustomResourceInjector implements ICustomModelLoader
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
			IResourceManager resourceManager )
	{
		if ( resourceManager instanceof SimpleReloadableResourceManager )
		{
			( (SimpleReloadableResourceManager) resourceManager ).addResourcePack( generatedFiles );
			ClientSide.instance.createResources();
		}
	}

	@Override
	public boolean accepts(
			ResourceLocation modelLocation )
	{
		return false;
	}

	@Override
	public IUnbakedModel loadModel(
			ResourceLocation modelLocation ) throws Exception
	{
		return null;
	}

}
