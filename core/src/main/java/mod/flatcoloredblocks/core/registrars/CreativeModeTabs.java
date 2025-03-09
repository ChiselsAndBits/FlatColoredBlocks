package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.registries.deferred.IRegistrar;
import com.communi.suggestu.scena.core.registries.deferred.IRegistryObject;
import mod.flatcoloredblocks.core.item.IWithColorItem;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class CreativeModeTabs
{
    private static final short COLOR_MAX_CHANNEL = 0xFF;
    private static final int COLOR_MAX         = 0xFFFFFF;

    private static final Logger          LOGGER           = LogManager.getLogger();
    private static final IRegistrar<CreativeModeTab> REGISTRAR = IRegistrar.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    private CreativeModeTabs()
    {
        throw new IllegalStateException("Tried to initialize: CreativeModTabs but this is a Utility class.");
    }

    public static void onModConstruction()
    {
        LOGGER.info("Loaded creative mod tabs configuration.");
    }

    public static final IRegistryObject<CreativeModeTab> TOOLS = REGISTRAR.register("tools", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(Items.PAINT_MIXER.get()))
            .title(Component.translatable("itemGroup.flatcoloredblocks.tools"))
            .displayItems((parameters, output) -> {
                output.accept(Items.PAINT_MIXER.get());
                output.accept(Items.PAINT_BASIN.get());

                registerColoredItem(output, Items.PAINT_BRUSH.get());
                registerColoredItem(output, Items.PAINT_BUCKET.get());
                registerColoredItem(output, Items.SOLID_DYE.get());
            })
            .build());

    public static final IRegistryObject<CreativeModeTab> BLOCKS = REGISTRAR.register("blocks", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
            .icon(new Supplier<>() {

                private int iconIndex = 0;

                @Override
                public ItemStack get() {
                    final ItemStack stack = new ItemStack(Blocks.COLORED_CONCRETE.get());

                    final long time = iconIndex % COLOR_MAX;
                    iconIndex++;
                    final int color = (int) (time % 0xFFFFFF);
                    Blocks.COLORED_CONCRETE.get().setColor(stack, color);
                    return stack;
                }
            })
            .title(Component.translatable("itemGroup.flatcoloredblocks.blocks"))
            .displayItems((parameters, output) -> {
                registerColoredItem(output, Blocks.COLORED_CONCRETE.get());
                registerColoredItem(output, Blocks.COLORED_GLASS.get());
                registerColoredItem(output, Blocks.COLORED_WOOL.get());
                registerColoredItem(output, Blocks.COLORED_WOOL_CARPET.get());
            })
            .build());

    private static void registerColoredItem(CreativeModeTab.Output pOutput, ItemLike candidate) {
        if (candidate instanceof Block) {
            registerColoredItem(pOutput, candidate.asItem());
            return;
        }

        if (!(candidate instanceof IWithColorItem withColorItem)) {
            pOutput.accept(new ItemStack(candidate));
            return;
        }

        withColorItem.fillItemCategory(pOutput::accept);
    }
}
