
package mod.flatcoloredblocks.resource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.BlockHSVConfiguration;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class ResourceGenerator
{

	public void init()
	{
		IResourceManager manager = Minecraft.getInstance().getResourceManager();

		if ( manager instanceof IReloadableResourceManager )
		{
			( (IReloadableResourceManager) manager ).addReloadListener( new CustomResourceInjector() );
		}

		MinecraftForge.EVENT_BUS.register( this );
	}

	public void populateResources()
	{
		registerConfiguredResources( EnumFlatBlockType.NORMAL, FlatColoredBlocks.instance.normal );
		registerConfiguredResources( EnumFlatBlockType.GLOWING, FlatColoredBlocks.instance.glowing );
		registerConfiguredResources( EnumFlatBlockType.TRANSPARENT, FlatColoredBlocks.instance.transparent );
	}

	private void registerConfiguredResources(
			EnumFlatBlockType type,
			BlockHSVConfiguration config )
	{
		for ( int varient = 0; varient < config.shadeConvertVariant.length; varient++ )
		{
			String name = config.getBlockName( varient );

			String blockstates = "{\"multipart\":[{\"apply\":{\"model\":\"flatcoloredblocks:block/" + name + "\"}}]}";
			CustomResourceInjector.addResource( "blockstates", name, ".json", blockstates.getBytes() );

			if ( type == EnumFlatBlockType.TRANSPARENT )
			{
				final ResourceLocation sourceLoc = ClientSide.instance.getTextureResourceLocation( EnumFlatBlockType.TRANSPARENT );
				final ResourceLocation textureName = ClientSide.instance.getTextureName( EnumFlatBlockType.TRANSPARENT, varient );

				try
				{
					final IResource iresource = Minecraft.getInstance().getResourceManager().getResource( sourceLoc );
					final NativeImage bi = NativeImage.read( iresource.getInputStream() );

					final BufferedImage image = new BufferedImage( bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR );
					final int xx = bi.getWidth();
					final int yy = bi.getHeight();

					float alphaMultiplier = config.shadeConvertVariant[varient] / 255.0f;
					for ( int x = 0; x < xx; ++x )
					{
						for ( int y = 0; y < yy; ++y )
						{
							final int color = bi.getPixelRGBA( x, y );
							final int a = (int) ( ( color >> 24 & 0xff ) * alphaMultiplier );
							image.setRGB( x, y, color & 0xffffff | a << 24 );
						}
					}

					ByteArrayOutputStream data = new ByteArrayOutputStream( 0 );
					ImageIO.write( image, "png", data );
					CustomResourceInjector.addResource( "textures/blocks", textureName.getPath(), ".png", data.toByteArray() );
				}
				catch ( IOException e )
				{
					// fails the first time it runs.
				}

				String model = "{\"parent\":\"flatcoloredblocks:block/flatcoloredblock_" + config.textureStyle + "\","
						+ "\"textures\":{\"all\":\"flatcoloredblocks:blocks/" + textureName.getPath() + "\",\"particle\":\"flatcoloredblocks:blocks/" + textureName.getPath() + "\"}}";
				CustomResourceInjector.addResource( "models/block", name, ".json", model.getBytes() );
				CustomResourceInjector.addResource( "models/item", name, ".json", model.getBytes() );
			}
			else
			{
				String model = "{\"parent\":\"flatcoloredblocks:block/flatcoloredblock_" + config.textureStyle + "\"}";
				CustomResourceInjector.addResource( "models/block", name, ".json", model.getBytes() );
				CustomResourceInjector.addResource( "models/item", name, ".json", model.getBytes() );
			}
		}
	}

}
