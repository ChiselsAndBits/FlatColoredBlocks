package mod.flatcoloredblocks.core.util;

import net.minecraft.resources.ResourceLocation;

public class Constants
{

    private Constants()
    {
        throw new IllegalStateException("Can not instantiate an instance of: Constants. This is a utility class");
    }

    public static final String MOD_ID = "flatcoloredblocks";

    public static final ResourceLocation COLORED_BLOCK_MODEL_LOADER_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "colored_block");
    public static final ResourceLocation COLORED_ITEM_MODEL_LOADER_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "colored_item");
    public static final ResourceLocation PAINT_SPLATTED_ITEM_LOADER_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "paint_splatted");
}
