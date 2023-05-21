package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.rendering.type.IRenderTypeManager;
import mod.flatcoloredblocks.core.registrars.Blocks;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public final class ItemBlockRenderTypes
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private ItemBlockRenderTypes()
    {
        throw new IllegalStateException("Can not instantiate an instance of: ItemBlockRenderTypes. This is a utility class");
    }

    public static void onModConstruction() {
        LOGGER.info("Registering Item Block Render Types");

        IRenderTypeManager.getInstance().registerBlockFallbackRenderTypes(
                registrar -> {
                    registrar.register(Blocks.COLORED_CONCRETE.get(), RenderType.solid());
                    registrar.register(Blocks.COLORED_GLASS.get(), RenderType.translucent());
                    registrar.register(Blocks.COLORED_WOOL.get(), RenderType.cutoutMipped());
                    registrar.register(Blocks.COLORED_WOOL_CARPET.get(), RenderType.cutoutMipped());
                }
        );
    }
}
