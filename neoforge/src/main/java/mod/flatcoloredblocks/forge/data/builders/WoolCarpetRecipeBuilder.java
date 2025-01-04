package mod.flatcoloredblocks.forge.data.builders;

import com.google.common.collect.Maps;
import mod.flatcoloredblocks.core.recipe.WoolCarpetRecipeSerializer;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class WoolCarpetRecipeBuilder
{
    private final Map<String, Criterion<?>> criterionMap = Maps.newHashMap();

    private WoolCarpetRecipeBuilder() {
    }

    public static WoolCarpetRecipeBuilder create() {
        return new WoolCarpetRecipeBuilder();
    }

    public WoolCarpetRecipeBuilder unlockedBy(String criterionName, Criterion<?> crit) {
        this.criterionMap.put(criterionName, crit);
        return this;
    }

    public void save(RecipeOutput consumer) {
        final ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "recipes/colored_wool_carpet");

        Advancement.Builder adv = consumer.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criterionMap.forEach(adv::addCriterion);

        consumer.accept(
                recipeId,
                new WoolCarpetRecipeSerializer.WoolCarpetRecipe(),
                adv.build(recipeId.withPrefix("recipes/" + RecipeCategory.DECORATIONS.getFolderName() + "/"))
        );
    }
}
