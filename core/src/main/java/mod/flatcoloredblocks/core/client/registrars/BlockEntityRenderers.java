package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.rendering.IRenderingManager;
import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.client.blockentityrenderer.PaintBasinOrMixerBlockEntitySpecialRenderer;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public final class BlockEntityRenderers
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private BlockEntityRenderers()
    {
        throw new IllegalStateException("Can not instantiate an instance of: BlockEntityRenderers. This is a utility class");
    }

    public static void onClientConstruct() {
        LOGGER.info("Registering Block Entity Renderers");

        IRenderingManager.getInstance().registerBlockEntityRenderer(registrar -> {
            registrar.registerBlockEntityRenderer(BlockEntityTypes.PAINT_BASIN.get(), context -> new PaintBasinOrMixerBlockEntitySpecialRenderer<>());
            registrar.registerBlockEntityRenderer(BlockEntityTypes.PAINT_MIXER.get(), context -> new PaintBasinOrMixerBlockEntitySpecialRenderer<>(false));
        });
    }
}
