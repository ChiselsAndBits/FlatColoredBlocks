package mod.flatcoloredblocks.textures;

import java.awt.image.BufferedImage;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class AlphaModifiedTexture extends TextureAtlasSprite
{
	final BufferedImage image;

	protected AlphaModifiedTexture(
			final ResourceLocation spriteName,
			final BufferedImage image )
	{
		super( spriteName, image.getWidth(), image.getHeight() );
		this.image = image;
	}

	public static TextureAtlasSprite generate(
			final ResourceLocation name,
			final BufferedImage bi,
			final float alphaMultiplier,
			final TextureMap map )
	{
		final BufferedImage image = new BufferedImage( bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR );
		final int xx = bi.getWidth();
		final int yy = bi.getHeight();

		for ( int x = 0; x < xx; ++x )
		{
			for ( int y = 0; y < yy; ++y )
			{
				final int color = bi.getRGB( x, y );
				final int a = (int) ( ( color >> 24 & 0xff ) * alphaMultiplier );
				image.setRGB( x, y, color & 0xffffff | a << 24 );
			}
		}

		final AlphaModifiedTexture out = new AlphaModifiedTexture( name, image );
		out.register( map );
		return out;
	}

	@Override
	public boolean hasCustomLoader(
			final IResourceManager manager,
			final ResourceLocation location )
	{
		return true;
	}

	@Override
	public boolean load(
			IResourceManager manager,
			ResourceLocation location,
			Function<ResourceLocation, TextureAtlasSprite> textureGetter )
	{
		final BufferedImage[] images = new BufferedImage[Minecraft.getInstance().gameSettings.mipmapLevels + 1];
		images[0] = image;
		final int[][] pixels = new int[Minecraft.getInstance().gameSettings.mipmapLevels + 1][];
		pixels[0] = new int[image.getWidth() * image.getHeight()];
		image.getRGB( 0, 0, image.getWidth(), image.getHeight(), pixels[0], 0, image.getWidth() );
		framesTextureData.add( pixels );

		return false;
	}

	public void register(
			final TextureMap map )
	{
		map.setTextureEntry( this );
	}

}
