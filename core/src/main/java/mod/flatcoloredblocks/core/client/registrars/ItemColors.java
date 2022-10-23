package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.rendering.IColorManager;
import mod.flatcoloredblocks.core.client.colors.item.ColoredBlockItemColors;
import mod.flatcoloredblocks.core.client.colors.item.PaintSplattedItemColor;
import mod.flatcoloredblocks.core.registrars.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ItemColors
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private ItemColors()
    {
        throw new IllegalStateException("Can not instantiate an instance of: ItemColors. This is a utility class");
    }

    public static void onClientConstruct() {
        LOGGER.info("Registering Item Colors");

        IColorManager.getInstance().setupItemColors(colors -> {
            colors.register(new PaintSplattedItemColor(), Items.PAINT_BUCKET.get(), Items.PAINT_BRUSH.get());
            colors.register(new ColoredBlockItemColors(), Items.COLORED_CONCRETE.get(), Items.COLORED_GLASS.get());
        });
    }
}
