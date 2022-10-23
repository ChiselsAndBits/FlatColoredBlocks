package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.models.IModelManager;
import mod.flatcoloredblocks.core.client.model.loader.ColoredBlockModelLoader;
import mod.flatcoloredblocks.core.client.model.loader.PaintSplattedItemModelLoader;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModelLoaders
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private ModelLoaders()
    {
        throw new IllegalStateException("Can not instantiate an instance of: ModelLoaders. This is a utility class");
    }

    public static void onClientConstruct() {
        LOGGER.info("Registering Model Loaders");

        IModelManager.getInstance().registerModelLoader(Constants.PAINT_SPLATTED_ITEM_LOADER_ID, PaintSplattedItemModelLoader.getInstance());
        IModelManager.getInstance().registerModelLoader(Constants.COLORED_MODEL_LOADER_ID, ColoredBlockModelLoader.getInstance());
    }
}
