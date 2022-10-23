package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.ColorNameManager;
import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.registrars.Items;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TranslationsGenerator extends LanguageProvider
{
    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
{
    event.getGenerator().addProvider(true, new TranslationsGenerator(event.getGenerator()));
}

    public TranslationsGenerator(final DataGenerator gen)
    {
        super(gen, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        addBlock(Blocks.PAINT_MIXER, "Paint Mixer");
        addBlock(Blocks.PAINT_BASIN, "Paint Basin");
        addBlock(Blocks.COLORED_CONCRETE, "Concrete");
        addBlock(Blocks.COLORED_GLASS, "Glass");

        addItem(Items.PAINT_BUCKET, "Empty Paint Bucket");
        addItem(Items.PAINT_BRUSH, "Empty Paint Roller");

        add("itemGroup.flatcoloredblocks.tools", "Flat Colored Blocks (Tools)");
        add("itemGroup.flatcoloredblocks.blocks", "Flat Colored Blocks (Examples)");

        ColorNameManager.getInstance().getAllColorNameKeys()
                .forEach(key -> add(key, extractNameFromColorKey(key)));

        add("color.flatcoloredblocks.__default__suffixed", "a custom color");
        add("color.flatcoloredblocks.__default__prefixed", "Custom Color");

        add("item.flatcoloredblocks.paint_bucket.with_contents", "Paint Bucket with %s mB of %s Paint");
        add("item.flatcoloredblocks.paint_brush.with_contents", "Paint Roller with %s mB of %s Paint");
        add("item.flatcoloredblocks.colored_block.with_contents", "%s %s");

        add("item.flatcoloredblocks.tooltip.color.red", "Red: %d");
        add("item.flatcoloredblocks.tooltip.color.green", "Green: %d");
        add("item.flatcoloredblocks.tooltip.color.blue", "Blue: %d");
    }

    private static String extractNameFromColorKey(final String key) {
        final String name = key.substring(key.lastIndexOf('.') + 1);
        final String spaced = name.replace("_", " ");
        final String[] sections = spaced.split(" ");
        final StringBuilder builder = new StringBuilder();
        for (final String section : sections) {
            if (!section.isBlank()) {
                builder.append(section.substring(0, 1).toUpperCase());
                builder.append(section.substring(1));
            }
            builder.append(' ');
        }
        return builder.toString().trim();
    }
}
