package mod.flatcoloredblocks.core.registrars;

import mod.flatcoloredblocks.core.registry.ColorizationRegistry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SolidPaints
{
    private static final Logger LOGGER = LogManager.getLogger();

    private SolidPaints()
    {
        throw new IllegalStateException("Can not instantiate an instance of: SolidPaints. This is a utility class");
    }

    public static void onModConstruction() {
        LOGGER.info("Loaded solid paint configuration.");

        for (final DyeColor value : DyeColor.values())
        {
            final DyeItem dye = DyeItem.byColor(value);
            ColorizationRegistry.getInstance().registerSolvent(dye, value.getFireworkColor());
        }
    }
}
