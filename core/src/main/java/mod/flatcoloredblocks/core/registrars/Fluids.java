package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.fluid.FluidRegistration;
import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.fluid.PaintFluid;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Fluids
{

    private static final Logger LOGGER    = LogManager.getLogger();

    public static final FluidRegistration PAINT = IFluidManager.getInstance().registerFluidAndVariant(
            new ResourceLocation(Constants.MOD_ID, "paint"),
            PaintFluid::new,
            PaintFluid.VariantHandler::new
        );

    public static void onModConstruction()
    {
        LOGGER.info("Loaded fluid configuration.");
    }
}
