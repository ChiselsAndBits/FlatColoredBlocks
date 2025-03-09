package mod.flatcoloredblocks.forge.data.builders;

import com.google.gson.JsonObject;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class ColorItemModelLoaderBuilder extends CustomLoaderBuilder<ItemModelBuilder> {

    public ColorItemModelLoaderBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper) {
        super(Constants.COLORED_ITEM_MODEL_LOADER_ID, parent, existingFileHelper, true);
    }
}
