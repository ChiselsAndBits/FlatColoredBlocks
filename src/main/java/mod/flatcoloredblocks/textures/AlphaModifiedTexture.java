package mod.flatcoloredblocks.textures;

import java.util.function.Function;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class AlphaModifiedTexture extends TextureAtlasSprite
{
	final NativeImage image;

	protected AlphaModifiedTexture(
			final ResourceLocation spriteName,
			final NativeImage image )
	{
		super( spriteName, image.getWidth(), image.getHeight() );
		this.image = image;
	}

	public static TextureAtlasSprite generate(
			final ResourceLocation name,
			final NativeImage bi,
			final float alphaMultiplier,
			final TextureMap map )
	{
		final NativeImage image = new NativeImage( bi.getWidth(), bi.getHeight(), false );
		final int xx = bi.getWidth();
		final int yy = bi.getHeight();

		for ( int x = 0; x < xx; ++x )
		{
			for ( int y = 0; y < yy; ++y )
			{
				final int color = bi.getPixelRGBA( x, y );
				final int a = (int) ( ( color >> 24 & 0xff ) * alphaMultiplier );
				image.setPixelRGBA( x, y, color & 0xffffff | a << 24 );
			}
		}

		final AlphaModifiedTexture out = new AlphaModifiedTexture( name, image );
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
		frames = new NativeImage[1];
		frames[0] = image;
		return false;
	}

}
