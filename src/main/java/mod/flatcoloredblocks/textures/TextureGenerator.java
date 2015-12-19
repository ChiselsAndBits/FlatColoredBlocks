package mod.flatcoloredblocks.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.Log;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureGenerator
{

	private final Map<Integer, TextureAtlasSprite> generatedTransparentTexture = new HashMap<Integer, TextureAtlasSprite>();
	private TextureAtlasSprite glowingTexture;

	@SubscribeEvent
	void registerTransparentTextures(
			final TextureStitchEvent.Pre ev )
	{
		try
		{
			final ResourceLocation sourceLoc = ClientSide.instance.getTextureResourceLocation( EnumFlatBlockType.TRANSPARENT );
			final IResource iresource = Minecraft.getMinecraft().getResourceManager().getResource( sourceLoc );
			final BufferedImage bi = TextureUtil.readBufferedImage( iresource.getInputStream() );

			for ( final int varient : FlatColoredBlocks.instance.transparent.shadeConvertVariant )
			{
				final String name = ClientSide.instance.getTextureName( EnumFlatBlockType.TRANSPARENT, varient );
				final TextureAtlasSprite out = AlphaModifiedTexture.generate( name, bi, varient / 255.0f, ev.map );

				// register.
				generatedTransparentTexture.put( varient, out );
			}
		}
		catch ( final IOException e )
		{
			// just load the texture if for some reason the above fails.
			Log.logError( "Unable to load Base Texture", e );

			final TextureAtlasSprite out = ev.map.registerSprite( new ResourceLocation( ClientSide.instance.getBaseTextureNameWithBlocks( EnumFlatBlockType.TRANSPARENT ) ) );

			for ( final int varient : FlatColoredBlocks.instance.transparent.shadeConvertVariant )
			{
				generatedTransparentTexture.put( varient, out );
			}
		}

		if ( !FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT )
		{
			glowingTexture = ev.map.registerSprite( new ResourceLocation( ClientSide.instance.getBaseTextureNameWithBlocks( EnumFlatBlockType.GLOWING ) ) );
		}
	}

	public TextureAtlasSprite getGlowingTexture(
			final int varient )
	{
		return glowingTexture;
	}

	public TextureAtlasSprite getTransparentTexture(
			final int varient )
	{
		return generatedTransparentTexture.get( varient );
	}

}
