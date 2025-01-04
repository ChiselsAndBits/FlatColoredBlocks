package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BlockStateGenerator extends BlockStateProvider
{
    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new BlockStateGenerator(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
    }

    public BlockStateGenerator(final PackOutput gen, final ExistingFileHelper exFileHelper)
    {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        horizontalBlock(Blocks.PAINT_MIXER.get(), models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "paint_mixer")));
        simpleBlock(Blocks.PAINT_BASIN.get(), models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "paint_basin")));
        simpleBlock(Blocks.COLORED_CONCRETE.get(), itemModels().getBuilder("colored_concrete")
                                                               .parent(itemModels().getExistingFile(ResourceLocation.withDefaultNamespace("white_concrete")))
                                                               .customLoader((modelBuilder, existingFileHelper) -> new CustomLoaderBuilder<ItemModelBuilder>(Constants.COLORED_MODEL_LOADER_ID, modelBuilder, existingFileHelper, false) {})
                                                               .end());
        simpleBlock(Blocks.COLORED_WOOL.get(), itemModels().getBuilder("colored_wool")
                .parent(itemModels().getExistingFile(ResourceLocation.withDefaultNamespace("white_wool")))
                .customLoader((modelBuilder, existingFileHelper) -> new CustomLoaderBuilder<ItemModelBuilder>(Constants.COLORED_MODEL_LOADER_ID, modelBuilder, existingFileHelper, false) {})
                .end());

        simpleBlock(Blocks.COLORED_WOOL_CARPET.get(), itemModels().getBuilder("colored_wool_carpet")
                .parent(itemModels().getExistingFile(ResourceLocation.withDefaultNamespace("white_carpet")))
                .customLoader((modelBuilder, existingFileHelper) -> new CustomLoaderBuilder<ItemModelBuilder>(Constants.COLORED_MODEL_LOADER_ID, modelBuilder, existingFileHelper, false) {})
                .end());

        simpleBlock(Blocks.COLORED_GLASS.get(), itemModels().getBuilder("colored_glass")
                                                               .parent(itemModels().getExistingFile(ResourceLocation.withDefaultNamespace("white_stained_glass")))
                                                               .customLoader((modelBuilder, existingFileHelper) -> new CustomLoaderBuilder<ItemModelBuilder>(Constants.COLORED_MODEL_LOADER_ID, modelBuilder, existingFileHelper, false) {})
                                                               .end());

        itemModels().getBuilder("paint_mixer")
                .parent(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "paint_mixer")));
        itemModels().getBuilder("paint_basin")
                .parent(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "paint_basin")));
    }
}
