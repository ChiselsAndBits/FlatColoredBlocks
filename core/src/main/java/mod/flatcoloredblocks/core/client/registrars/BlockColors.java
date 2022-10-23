package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.rendering.IColorManager;
import mod.flatcoloredblocks.core.client.colors.block.ColoredBlockBlockColors;
import mod.flatcoloredblocks.core.registrars.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class BlockColors
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private BlockColors()
    {
        throw new IllegalStateException("Can not instantiate an instance of: BlockColors. This is a utility class");
    }

    public static void onClientConstruct() {
        LOGGER.info("Registering Block Colors");

        IColorManager.getInstance().setupBlockColors(colors -> {
            colors.register(new ColoredBlockBlockColors(), Blocks.COLORED_CONCRETE.get());
            colors.register(new ColoredBlockBlockColors(), Blocks.COLORED_GLASS.get());
        });
    }
}
