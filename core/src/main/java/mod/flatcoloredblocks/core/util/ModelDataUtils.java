package mod.flatcoloredblocks.core.util;

import com.communi.suggestu.scena.core.client.models.data.IBlockModelData;
import com.communi.suggestu.scena.core.client.models.data.IModelDataBuilder;
import mod.flatcoloredblocks.core.registrars.ModelDataKeys;

public final class ModelDataUtils {

    private ModelDataUtils() {
        throw new IllegalStateException("Can not instantiate an instance of: ModelDataUtils. This is a utility class");
    }

    public static IBlockModelData createModelDataForColor(final int color) {
        return IModelDataBuilder.create().withInitial(ModelDataKeys.COLOR, color).build();
    }
}
