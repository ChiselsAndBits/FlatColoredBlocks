package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.registries.deferred.IRegistrar;
import com.communi.suggestu.scena.core.registries.deferred.IRegistryObject;
import mod.flatcoloredblocks.core.recipe.PaintBucketRecipeSerializer;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RecipeSerializers
{

    private static final Logger LOGGER = LogManager.getLogger();

    private RecipeSerializers()
    {
        throw new IllegalStateException("Can not instantiate an instance of: RecipeSerializers. This is a utility class");
    }

    public static void onModConstruction() {
        LOGGER.info("Registering recipe serializers");
    }

    private static final IRegistrar<RecipeSerializer<?>> RECIPE_SERIALIZER_REGISTRAR = IRegistrar.create(Registry.RECIPE_SERIALIZER_REGISTRY, Constants.MOD_ID);

    public static final IRegistryObject<PaintBucketRecipeSerializer> PAINT_BUCKET = RECIPE_SERIALIZER_REGISTRAR.register("paint_bucket", PaintBucketRecipeSerializer::getInstance);
}
