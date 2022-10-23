package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.client.models.data.IModelDataKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModelDataKeys
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelDataKeys.class);

    private ModelDataKeys()
    {
        throw new IllegalStateException("Can not instantiate an instance of: ModelDataKeys. This is a utility class");
    }

    public static final IModelDataKey<Integer> COLOR = IModelDataKey.create();

    public static void onModConstruction() {
        LOGGER.info("Registering model data keys");
    }
}
