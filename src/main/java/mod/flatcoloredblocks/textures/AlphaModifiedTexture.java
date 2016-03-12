package mod.flatcoloredblocks.textures;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class AlphaModifiedTexture extends TextureAtlasSprite
{
	final BufferedImage image;

	protected AlphaModifiedTexture(
			final String spriteName,
			final BufferedImage image )
	{
		super( spriteName );
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
	}

	public static TextureAtlasSprite generate(
			final String name,
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
			final IResourceManager manager,
			final ResourceLocation location )
	{
		final BufferedImage[] images = new BufferedImage[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1];
		images[0] = image;
		final int[][] pixels = new int[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1][];
		pixels[0] = new int[image.getWidth() * image.getHeight()];
		image.getRGB( 0, 0, image.getWidth(), image.getHeight(), pixels[0], 0, image.getWidth() );
		framesTextureData.add( pixels );

		return false;
	}

	public void register(
			final TextureMap map )
	{
		map.setTextureEntry( getIconName(), this );
	}

}
