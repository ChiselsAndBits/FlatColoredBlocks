package mod.flatcoloredblocks.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class AlphaModifiedTexture extends TextureAtlasSprite
{

	public static TextureAtlasSprite generate(
			final String Name,
			final BufferedImage bi,
			final float alphaMultiplier,
			final TextureMap map )
	{
		final BufferedImage image = new BufferedImage( bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB );
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

		final AlphaModifiedTexture out = new AlphaModifiedTexture( Name, image );
		out.register( map );
		return out;
	}

	final BufferedImage image;

	protected AlphaModifiedTexture(
			final String spriteName,
			final BufferedImage image )
	{
		super( spriteName );
		this.image = image;
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
			final IResourceManager manager,
			final ResourceLocation location )
	{
		try
		{
			final BufferedImage[] images = new BufferedImage[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1];
			images[0] = image;
			loadSprite( images, null );
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}

		return false;
	}

	public void register(
			final TextureMap map )
	{
		map.setTextureEntry( getIconName(), this );
	}

}
