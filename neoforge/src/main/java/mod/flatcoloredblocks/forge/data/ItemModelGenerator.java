package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.util.Constants;
import mod.flatcoloredblocks.forge.data.builders.ColorItemModelLoaderBuilder;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ItemModelGenerator extends BlockStateProvider
{

    public ItemModelGenerator(final PackOutput gen, final ExistingFileHelper exFileHelper)
    {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new ItemModelGenerator(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
    }

    @Override
    protected void registerStatesAndModels()
    {
        var bucketWithPaint = itemModels().getBuilder("paint_bucket_with_paint")
                                          .texture("base", "item/paint_bucket_full")
                                          .texture("paint", "item/paint_bucket_full_overlay")
                                          .guiLight(BlockModel.GuiLight.FRONT)
                                          .transforms()
                                          .transform(ItemDisplayContext.GROUND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 2, 0)
                                          .scale(0.5f, 0.5f, 0.5f)
                                          .end()
                                          .transform(ItemDisplayContext.HEAD)
                                          .rotation(0, 180, 0)
                                          .translation(0, 13, 7)
                                          .scale(1, 1, 1)
                                          .end()
                                          .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 3, 1)
                                          .scale(0.55f, 0.55f, 0.55f)
                                          .end()
                                          .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                                          .rotation(0, -90, 25)
                                          .translation(1.13f, 3.2f, 1.13f)
                                          .scale(0.68f, 0.68f, 0.68f)
                                          .end()
                                          .transform(ItemDisplayContext.FIXED)
                                          .rotation(0, 180, 0)
                                          .scale(1, 1, 1)
                                          .end()
                                          .end()
                                          .customLoader((itemModelBuilder, existingFileHelper) -> new CustomLoaderBuilder<>(Constants.PAINT_SPLATTED_ITEM_LOADER_ID, itemModelBuilder, existingFileHelper, false) {})
                                          .end();

        itemModels().getBuilder("paint_bucket")
                    .texture("layer0", "item/paint_bucket")
                    .parent(itemModels().getExistingFile(ResourceLocation.withDefaultNamespace("item/generated")))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .transforms()
                    .transform(ItemDisplayContext.GROUND)
                    .rotation(0, 0, 0)
                    .translation(0, 2, 0)
                    .scale(0.5f, 0.5f, 0.5f)
                    .end()
                    .transform(ItemDisplayContext.HEAD)
                    .rotation(0, 180, 0)
                    .translation(0, 13, 7)
                    .scale(1, 1, 1)
                    .end()
                    .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                    .rotation(0, 0, 0)
                    .translation(0, 3, 1)
                    .scale(0.55f, 0.55f, 0.55f)
                    .end()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, -90, 25)
                    .translation(1.13f, 3.2f, 1.13f)
                    .scale(0.68f, 0.68f, 0.68f)
                    .end()
                    .transform(ItemDisplayContext.FIXED)
                    .rotation(0, 180, 0)
                    .scale(1, 1, 1)
                    .end()
                    .end()
                    .override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "has_paint"), 1)
                    .model(bucketWithPaint);

        var brushWithPaint = itemModels().getBuilder("paint_brush_with_paint")
                                          .texture("base", "item/paint_brush_full")
                                          .texture("paint", "item/paint_brush_full_overlay")
                                          .guiLight(BlockModel.GuiLight.FRONT)
                                          .transforms()
                                          .transform(ItemDisplayContext.GROUND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 2, 0)
                                          .scale(0.5f, 0.5f, 0.5f)
                                          .end()
                                          .transform(ItemDisplayContext.HEAD)
                                          .rotation(0, 180, 0)
                                          .translation(0, 13, 7)
                                          .scale(1, 1, 1)
                                          .end()
                                          .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 3, 1)
                                          .scale(0.55f, 0.55f, 0.55f)
                                          .end()
                                          .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                                          .rotation(0, -90, 25)
                                          .translation(1.13f, 3.2f, 1.13f)
                                          .scale(0.68f, 0.68f, 0.68f)
                                          .end()
                                          .transform(ItemDisplayContext.FIXED)
                                          .rotation(0, 180, 0)
                                          .scale(1, 1, 1)
                                          .end()
                                          .end()
                                          .customLoader((itemModelBuilder, existingFileHelper) -> new CustomLoaderBuilder<>(Constants.PAINT_SPLATTED_ITEM_LOADER_ID, itemModelBuilder, existingFileHelper, false) {})
                                          .end();

        itemModels().getBuilder("paint_brush")
                    .texture("layer0", "item/paint_brush")
                    .parent(itemModels().getExistingFile(ResourceLocation.withDefaultNamespace("item/generated")))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .transforms()
                    .transform(ItemDisplayContext.GROUND)
                    .rotation(0, 0, 0)
                    .translation(0, 2, 0)
                    .scale(0.5f, 0.5f, 0.5f)
                    .end()
                    .transform(ItemDisplayContext.HEAD)
                    .rotation(0, 180, 0)
                    .translation(0, 13, 7)
                    .scale(1, 1, 1)
                    .end()
                    .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                    .rotation(0, 0, 0)
                    .translation(0, 3, 1)
                    .scale(0.55f, 0.55f, 0.55f)
                    .end()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, -90, 25)
                    .translation(1.13f, 3.2f, 1.13f)
                    .scale(0.68f, 0.68f, 0.68f)
                    .end()
                    .transform(ItemDisplayContext.FIXED)
                    .rotation(0, 180, 0)
                    .scale(1, 1, 1)
                    .end()
                    .end()
                    .override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "has_paint"), 1)
                    .model(brushWithPaint);

        itemModels().withExistingParent("solid_dye", "white_dye")
                .guiLight(BlockModel.GuiLight.FRONT)
                .transforms()
                .transform(ItemDisplayContext.GROUND)
                .rotation(0, 0, 0)
                .translation(0, 2, 0)
                .scale(0.5f, 0.5f, 0.5f)
                .end()
                .transform(ItemDisplayContext.HEAD)
                .rotation(0, 180, 0)
                .translation(0, 13, 7)
                .scale(1, 1, 1)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(0, 0, 0)
                .translation(0, 3, 1)
                .scale(0.55f, 0.55f, 0.55f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, -90, 25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68f, 0.68f, 0.68f)
                .end()
                .transform(ItemDisplayContext.FIXED)
                .rotation(0, 180, 0)
                .scale(1, 1, 1)
                .end()
                .end()
                .customLoader(ColorItemModelLoaderBuilder::new)
                .end();
    }

    @Override
    public @NotNull String getName()
    {
        return "Item models: " + Constants.MOD_ID;
    }
}
