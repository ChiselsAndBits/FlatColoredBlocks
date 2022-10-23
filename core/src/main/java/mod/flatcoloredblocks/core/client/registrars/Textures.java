package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.textures.ITextureManager;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Textures
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private Textures()
    {
        throw new IllegalStateException("Can not instantiate an instance of: Textures. This is a utility class");
    }

    public static void onClientConstruction() {
        LOGGER.info("Registering Textures");

        ITextureManager.getInstance().registerTextures(TextureAtlas.LOCATION_BLOCKS, registrar -> registrar.registerTextureToAtlas(new ResourceLocation(Constants.MOD_ID, "block/paint_still")));
    }
}
