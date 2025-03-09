package mod.flatcoloredblocks.forge.data.builders;

import com.google.gson.JsonObject;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class ColorBlockModelLoaderBuilder extends CustomLoaderBuilder<ItemModelBuilder> {

    private final ResourceLocation mimickedBlock;

    public ColorBlockModelLoaderBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper, ResourceLocation mimickedBlock) {
        super(Constants.COLORED_BLOCK_MODEL_LOADER_ID, parent, existingFileHelper, true);
        this.mimickedBlock = mimickedBlock;
    }

    @Override
    public @NotNull JsonObject toJson(@NotNull JsonObject json) {
        final JsonObject result = super.toJson(json);
        result.addProperty("mimickedBlock", this.mimickedBlock.toString());
        return result;
    }
}
