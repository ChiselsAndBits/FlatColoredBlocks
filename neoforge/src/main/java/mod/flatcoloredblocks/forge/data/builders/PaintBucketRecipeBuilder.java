package mod.flatcoloredblocks.forge.data.builders;

import com.google.common.collect.Maps;
import mod.flatcoloredblocks.core.recipe.PaintBucketRecipeSerializer;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.Map;

public class PaintBucketRecipeBuilder
{
    private final DyeColor dyeColor;
    private String group;
    private final Map<String, Criterion<?>> criterionMap = Maps.newHashMap();

    private PaintBucketRecipeBuilder(final DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    public static PaintBucketRecipeBuilder bucket(DyeColor color) {
        return new PaintBucketRecipeBuilder(color);
    }

    public PaintBucketRecipeBuilder unlockedBy(String criterionName, Criterion<?> crit) {
        this.criterionMap.put(criterionName, crit);
        return this;
    }

    public PaintBucketRecipeBuilder group(String groupName) {
        this.group = groupName;
        return this;
    }

    public void save(RecipeOutput consumer) {
        final ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recipes/paint_bucket_" + dyeColor.getName());

        Advancement.Builder adv = consumer.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criterionMap.forEach(adv::addCriterion);

        consumer.accept(recipeId,
                new PaintBucketRecipeSerializer.PaintBucketRecipe(this.dyeColor, this.group == null ? "" : this.group),
                adv.build(recipeId.withPrefix("recipes/" + RecipeCategory.TOOLS.getFolderName() + "/")));
    }
}
