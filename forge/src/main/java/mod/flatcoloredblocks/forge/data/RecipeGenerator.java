package mod.flatcoloredblocks.forge.data;

import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.registrars.Items;
import mod.flatcoloredblocks.core.util.Constants;
import mod.flatcoloredblocks.forge.data.builders.PaintBucketRecipeBuilder;
import mod.flatcoloredblocks.forge.data.builders.WoolCarpetRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.minecraft.world.item.Items.IRON_INGOT;
import static net.minecraft.world.item.Items.IRON_NUGGET;
import static net.minecraft.world.item.Items.SMOOTH_STONE;
import static net.minecraft.world.item.Items.WATER_BUCKET;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeGenerator extends RecipeProvider
{
    private RecipeGenerator(final DataGenerator pGenerator)
    {
        super(pGenerator);
    }

    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new RecipeGenerator(event.getGenerator()));
    }

    @Override
    protected void buildCraftingRecipes(final @NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer)
    {
        for (final DyeColor color : DyeColor.values())
        {
            PaintBucketRecipeBuilder.bucket(color)
                                    .group("colored_paint_bucket")
                                    .unlockedBy("has_bucket", has(Items.PAINT_BUCKET.get()))
                                    .unlockedBy("has_dye", has(DyeItem.byColor(color)))
                                    .unlockedBy("has_water", has(WATER_BUCKET))
                                    .save(pFinishedRecipeConsumer);
        }

        ShapedRecipeBuilder.shaped(Items.PAINT_BUCKET.get(), 1)
                           .group("paint_bucket")
                           .unlockedBy("has_iron_ingot", has(IRON_INGOT))
                           .unlockedBy("has_iron_nugget", has(IRON_NUGGET))
                           .define('I', IRON_INGOT)
                           .define('N', IRON_NUGGET)
                           .pattern(" N ")
                           .pattern("I I")
                           .pattern("III")
                           .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(Items.PAINT_BRUSH.get(), 1)
                           .group("paint_brush")
                           .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                           .unlockedBy("has_wood", has(ItemTags.PLANKS))
                           .unlockedBy("has_wool", has(ItemTags.WOOL))
                           .define('S', Tags.Items.RODS_WOODEN)
                           .define('P', ItemTags.PLANKS)
                           .define('W', ItemTags.WOOL)
                           .pattern(" PW")
                           .pattern(" SP")
                           .pattern("S  ")
                           .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(Blocks.PAINT_MIXER.get())
                           .group("paint_mixer")
                           .unlockedBy("has_paint_basin", has(Blocks.PAINT_BASIN.get()))
                           .unlockedBy("has_iron_ingot", has(IRON_INGOT))
                           .unlockedBy("has_wood_slab", has(ItemTags.WOODEN_SLABS))
                           .define('B', Blocks.PAINT_BASIN.get())
                           .define('I', IRON_INGOT)
                           .define('S', ItemTags.WOODEN_SLABS)
                           .pattern("SSS")
                           .pattern(" I ")
                           .pattern(" B ")
                           .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(Blocks.PAINT_BASIN.get())
                           .group("paint_basin")
                           .unlockedBy("has_iron_ingot", has(IRON_INGOT))
                           .unlockedBy("has_paint_bucket", has(Items.PAINT_BUCKET.get()))
                           .unlockedBy("has_smooth_stone", has(SMOOTH_STONE))
                           .define('I', IRON_INGOT)
                           .define('P', Items.PAINT_BUCKET.get())
                           .define('S', SMOOTH_STONE)
                           .pattern("S S")
                           .pattern("IPI")
                           .pattern("SSS")
                           .save(pFinishedRecipeConsumer);

        WoolCarpetRecipeBuilder.create()
                .group("wool_carpet")
                 .unlockedBy("has_wool", has(Items.COLORED_WOOL.get()))
                 .save(pFinishedRecipeConsumer);
    }
}
