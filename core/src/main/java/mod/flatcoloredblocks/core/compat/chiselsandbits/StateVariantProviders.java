package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.mojang.logging.LogUtils;
import mod.chiselsandbits.api.variant.state.IStateVariantManager;
import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Map;

public class StateVariantProviders {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ColoredStateVariantProvider PROVIDER = new ColoredStateVariantProvider();

    @SuppressWarnings("unchecked")
    public static void onModConstruction() {
        LOGGER.info("Loaded state variant provider configuration.");

        IStateVariantManager.getInstance().registerProvider(
                PROVIDER,
                Blocks.COLORED_CONCRETE::get,
                Blocks.COLORED_GLASS::get,
                Blocks.COLORED_WOOL::get
        );
    }
}
