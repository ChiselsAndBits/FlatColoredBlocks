package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockStateGenerator extends BlockStateProvider
{
    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new BlockStateGenerator(event.getGenerator(), event.getExistingFileHelper()));
    }

    public BlockStateGenerator(final DataGenerator gen, final ExistingFileHelper exFileHelper)
    {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        horizontalBlock(Blocks.PAINT_MIXER.get(), models().getExistingFile(new ResourceLocation(Constants.MOD_ID, "paint_mixer")));
        simpleBlock(Blocks.PAINT_BASIN.get(), models().getExistingFile(new ResourceLocation(Constants.MOD_ID, "paint_basin")));
        simpleBlock(Blocks.COLORED_CONCRETE.get(), itemModels().getBuilder("colored_concrete")
                                                               .parent(itemModels().getExistingFile(new ResourceLocation("white_concrete")))
                                                               .customLoader((modelBuilder, existingFileHelper) -> new CustomLoaderBuilder<ItemModelBuilder>(Constants.COLORED_MODEL_LOADER_ID, modelBuilder, existingFileHelper) {})
                                                               .end());
        simpleBlock(Blocks.COLORED_GLASS.get(), itemModels().getBuilder("colored_glass")
                                                               .parent(itemModels().getExistingFile(new ResourceLocation("white_stained_glass")))
                                                               .customLoader((modelBuilder, existingFileHelper) -> new CustomLoaderBuilder<ItemModelBuilder>(Constants.COLORED_MODEL_LOADER_ID, modelBuilder, existingFileHelper) {})
                                                               .end());

        itemModels().getBuilder("paint_mixer")
                .parent(models().getExistingFile(new ResourceLocation(Constants.MOD_ID, "paint_mixer")));
        itemModels().getBuilder("paint_basin")
                .parent(models().getExistingFile(new ResourceLocation(Constants.MOD_ID, "paint_basin")));
    }
}
