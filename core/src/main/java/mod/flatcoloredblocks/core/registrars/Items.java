package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import mod.flatcoloredblocks.core.item.ColoredBlockItem;
import mod.flatcoloredblocks.core.item.PaintBrushItem;
import mod.flatcoloredblocks.core.item.PaintBucketItem;
import mod.flatcoloredblocks.core.util.Constants;
import com.communi.suggestu.scena.core.registries.deferred.IRegistrar;
import com.communi.suggestu.scena.core.registries.deferred.IRegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Items
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Items.class);

    private Items()
    {
        throw new IllegalStateException("Can not instantiate an instance of: Items. This is a utility class");
    }

    private final static IRegistrar<Item> ITEM_REGISTRAR               = IRegistrar.create(Registry.ITEM_REGISTRY, Constants.MOD_ID);

    public final static IRegistryObject<BlockItem> PAINT_MIXER = ITEM_REGISTRAR.register("paint_mixer", () -> new BlockItem(Blocks.PAINT_MIXER.get(), new Item.Properties().tab(CreativeModeTabs.TOOLS)));
    public final static IRegistryObject<BlockItem> PAINT_BASIN = ITEM_REGISTRAR.register("paint_basin", () -> new BlockItem(Blocks.PAINT_BASIN.get(), new Item.Properties().tab(CreativeModeTabs.TOOLS)));
    public final static IRegistryObject<BlockItem> COLORED_CONCRETE = ITEM_REGISTRAR.register("colored_concrete", () -> new ColoredBlockItem(Blocks.COLORED_CONCRETE.get(), new Item.Properties().tab(CreativeModeTabs.BLOCKS)));
    public final static IRegistryObject<BlockItem> COLORED_GLASS = ITEM_REGISTRAR.register("colored_glass", () -> new ColoredBlockItem(Blocks.COLORED_GLASS.get(), new Item.Properties().tab(CreativeModeTabs.BLOCKS)));
    public final static IRegistryObject<PaintBucketItem> PAINT_BUCKET = ITEM_REGISTRAR.register("paint_bucket", () -> new PaintBucketItem(new Item.Properties().tab(CreativeModeTabs.TOOLS).stacksTo(1)));
    public final static IRegistryObject<PaintBrushItem> PAINT_BRUSH = ITEM_REGISTRAR.register("paint_brush", () -> new PaintBrushItem(new Item.Properties().tab(CreativeModeTabs.TOOLS).stacksTo(1)));

    public static void onModConstruction() {
        LOGGER.info("Registering items");
    }
}
