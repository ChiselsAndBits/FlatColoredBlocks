package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.creativetab.ICreativeTabManager;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public final class CreativeModeTabs
{
    private static final short COLOR_MAX_CHANNEL = 0xFF;
    private static final int COLOR_MAX         = 0xFFFFFF;

    private static final Logger          LOGGER           = LogManager.getLogger();

    private CreativeModeTabs()
    {
        throw new IllegalStateException("Tried to initialize: CreativeModTabs but this is a Utility class.");
    }

    public static void onModConstruction()
    {
        LOGGER.info("Loaded creative mod tabs configuration.");
    }

    public static final CreativeModeTab TOOLS = ICreativeTabManager.getInstance().register((index) -> new CreativeModeTab(index, Constants.MOD_ID + ".tools")
    {
        @Override
        public @NotNull ItemStack makeIcon()
        {
            return new ItemStack(Items.PAINT_MIXER.get());
        }

        @Override
        public @NotNull String getRecipeFolderName()
        {
            return Constants.MOD_ID + "_" + "tools";
        }
    });

    public static final CreativeModeTab BLOCKS = ICreativeTabManager.getInstance().register((index) -> new CreativeModeTab(index, Constants.MOD_ID + ".blocks")
    {
        private int iconIndex = 0;

        @Override
        public @NotNull ItemStack makeIcon()
        {
            final ItemStack stack = new ItemStack(Blocks.COLORED_CONCRETE.get());

            final long time = iconIndex % COLOR_MAX;
            iconIndex++;
            final int color = (int) (time % 0xFFFFFF);
            Blocks.COLORED_CONCRETE.get().setColor(stack, color);
            return stack;
        }

        @Override
        public @NotNull ItemStack getIconItem()
        {
            return makeIcon();
        }

        @Override
        public @NotNull String getRecipeFolderName()
        {
            return Constants.MOD_ID + "_" + "blocks";
        }
    });
}
