package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class TagGenerator extends BlockTagsProvider
{
    public TagGenerator(final PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper)
    {
        super(pGenerator, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new TagGenerator(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Blocks.COLORED_CONCRETE.get(), Blocks.COLORED_GLASS.get());

        tag(BlockTags.WOOL).add(Blocks.COLORED_WOOL.get());
        tag(BlockTags.WOOL_CARPETS).add(Blocks.COLORED_WOOL_CARPET.get());
    }
}
