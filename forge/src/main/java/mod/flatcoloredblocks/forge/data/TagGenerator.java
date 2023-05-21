package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TagGenerator extends BlockTagsProvider
{
    public TagGenerator(final DataGenerator pGenerator, @Nullable final ExistingFileHelper existingFileHelper)
    {
        super(pGenerator, Constants.MOD_ID, existingFileHelper);
    }

    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new TagGenerator(event.getGenerator(), event.getExistingFileHelper()));
    }

    @Override
    protected void addTags()
    {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Blocks.COLORED_CONCRETE.get(), Blocks.COLORED_GLASS.get());

        tag(BlockTags.WOOL).add(Blocks.COLORED_WOOL.get());
        tag(BlockTags.WOOL_CARPETS).add(Blocks.COLORED_WOOL_CARPET.get());
    }
}
