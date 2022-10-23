package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemModelGenerator extends BlockStateProvider
{

    public ItemModelGenerator(final DataGenerator gen, final ExistingFileHelper exFileHelper)
    {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new ItemModelGenerator(event.getGenerator(), event.getExistingFileHelper()));
    }

    @Override
    protected void registerStatesAndModels()
    {
        var bucketWithPaint = itemModels().getBuilder("paint_bucket_with_paint")
                                          .texture("base", "item/paint_bucket_full")
                                          .texture("paint", "item/paint_bucket_full_overlay")
                                          .guiLight(BlockModel.GuiLight.FRONT)
                                          .transforms()
                                          .transform(ItemTransforms.TransformType.GROUND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 2, 0)
                                          .scale(0.5f, 0.5f, 0.5f)
                                          .end()
                                          .transform(ItemTransforms.TransformType.HEAD)
                                          .rotation(0, 180, 0)
                                          .translation(0, 13, 7)
                                          .scale(1, 1, 1)
                                          .end()
                                          .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 3, 1)
                                          .scale(0.55f, 0.55f, 0.55f)
                                          .end()
                                          .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                                          .rotation(0, -90, 25)
                                          .translation(1.13f, 3.2f, 1.13f)
                                          .scale(0.68f, 0.68f, 0.68f)
                                          .end()
                                          .transform(ItemTransforms.TransformType.FIXED)
                                          .rotation(0, 180, 0)
                                          .scale(1, 1, 1)
                                          .end()
                                          .end()
                                          .customLoader((itemModelBuilder, existingFileHelper) -> new CustomLoaderBuilder<>(Constants.PAINT_SPLATTED_ITEM_LOADER_ID, itemModelBuilder, existingFileHelper) {})
                                          .end();

        itemModels().getBuilder("paint_bucket")
                    .texture("layer0", "item/paint_bucket")
                    .parent(itemModels().getExistingFile(new ResourceLocation("item/generated")))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .transforms()
                    .transform(ItemTransforms.TransformType.GROUND)
                    .rotation(0, 0, 0)
                    .translation(0, 2, 0)
                    .scale(0.5f, 0.5f, 0.5f)
                    .end()
                    .transform(ItemTransforms.TransformType.HEAD)
                    .rotation(0, 180, 0)
                    .translation(0, 13, 7)
                    .scale(1, 1, 1)
                    .end()
                    .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                    .rotation(0, 0, 0)
                    .translation(0, 3, 1)
                    .scale(0.55f, 0.55f, 0.55f)
                    .end()
                    .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, -90, 25)
                    .translation(1.13f, 3.2f, 1.13f)
                    .scale(0.68f, 0.68f, 0.68f)
                    .end()
                    .transform(ItemTransforms.TransformType.FIXED)
                    .rotation(0, 180, 0)
                    .scale(1, 1, 1)
                    .end()
                    .end()
                    .override()
                    .predicate(new ResourceLocation(Constants.MOD_ID, "has_paint"), 1)
                    .model(bucketWithPaint);

        var brushWithPaint = itemModels().getBuilder("paint_brush_with_paint")
                                          .texture("base", "item/paint_brush_full")
                                          .texture("paint", "item/paint_brush_full_overlay")
                                          .guiLight(BlockModel.GuiLight.FRONT)
                                          .transforms()
                                          .transform(ItemTransforms.TransformType.GROUND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 2, 0)
                                          .scale(0.5f, 0.5f, 0.5f)
                                          .end()
                                          .transform(ItemTransforms.TransformType.HEAD)
                                          .rotation(0, 180, 0)
                                          .translation(0, 13, 7)
                                          .scale(1, 1, 1)
                                          .end()
                                          .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                                          .rotation(0, 0, 0)
                                          .translation(0, 3, 1)
                                          .scale(0.55f, 0.55f, 0.55f)
                                          .end()
                                          .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                                          .rotation(0, -90, 25)
                                          .translation(1.13f, 3.2f, 1.13f)
                                          .scale(0.68f, 0.68f, 0.68f)
                                          .end()
                                          .transform(ItemTransforms.TransformType.FIXED)
                                          .rotation(0, 180, 0)
                                          .scale(1, 1, 1)
                                          .end()
                                          .end()
                                          .customLoader((itemModelBuilder, existingFileHelper) -> new CustomLoaderBuilder<>(Constants.PAINT_SPLATTED_ITEM_LOADER_ID, itemModelBuilder, existingFileHelper) {})
                                          .end();

        itemModels().getBuilder("paint_brush")
                    .texture("layer0", "item/paint_brush")
                    .parent(itemModels().getExistingFile(new ResourceLocation("item/generated")))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .transforms()
                    .transform(ItemTransforms.TransformType.GROUND)
                    .rotation(0, 0, 0)
                    .translation(0, 2, 0)
                    .scale(0.5f, 0.5f, 0.5f)
                    .end()
                    .transform(ItemTransforms.TransformType.HEAD)
                    .rotation(0, 180, 0)
                    .translation(0, 13, 7)
                    .scale(1, 1, 1)
                    .end()
                    .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                    .rotation(0, 0, 0)
                    .translation(0, 3, 1)
                    .scale(0.55f, 0.55f, 0.55f)
                    .end()
                    .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, -90, 25)
                    .translation(1.13f, 3.2f, 1.13f)
                    .scale(0.68f, 0.68f, 0.68f)
                    .end()
                    .transform(ItemTransforms.TransformType.FIXED)
                    .rotation(0, 180, 0)
                    .scale(1, 1, 1)
                    .end()
                    .end()
                    .override()
                    .predicate(new ResourceLocation(Constants.MOD_ID, "has_paint"), 1)
                    .model(brushWithPaint);
    }

    @Override
    public @NotNull String getName()
    {
        return "Item models: " + Constants.MOD_ID;
    }
}
