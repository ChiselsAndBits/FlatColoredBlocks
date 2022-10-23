package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.models.IModelManager;
import mod.flatcoloredblocks.core.item.PaintContainingItem;
import mod.flatcoloredblocks.core.registrars.Items;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ItemProperties
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private ItemProperties()
    {
        throw new IllegalStateException("Can not instantiate an instance of: ItemProperties. This is a utility class");
    }

    public static void onClientConstruction() {
        LOGGER.info("Registering Item Properties");

        IModelManager.getInstance().registerItemModelProperty(
                registrar -> {
                    registrar.registerItemModelProperty(
                            Items.PAINT_BUCKET.get(),
                            new ResourceLocation(Constants.MOD_ID, "has_paint"),
                            (pStack, pLevel, pEntity, pSeed) -> {
                                if (!(pStack.getItem() instanceof PaintContainingItem paintContainingItem))
                                    return 0f;

                                return paintContainingItem.hasAmount(pStack, 1) ? 1f : 0f;
                            }
                    );
                    registrar.registerItemModelProperty(
                            Items.PAINT_BRUSH.get(),
                            new ResourceLocation(Constants.MOD_ID, "has_paint"),
                            (pStack, pLevel, pEntity, pSeed) -> {
                                if (!(pStack.getItem() instanceof PaintContainingItem paintContainingItem))
                                    return 0f;

                                return paintContainingItem.hasAmount(pStack, 1) ? 1f : 0f;
                            }
                    );
                }
        );
    }
}
