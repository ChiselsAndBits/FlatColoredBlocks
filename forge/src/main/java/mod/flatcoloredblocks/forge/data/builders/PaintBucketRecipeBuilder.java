package mod.flatcoloredblocks.forge.data.builders;

import com.google.gson.JsonObject;
import mod.flatcoloredblocks.core.registrars.Items;
import mod.flatcoloredblocks.core.registrars.RecipeSerializers;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class PaintBucketRecipeBuilder
{
    private final DyeColor dyeColor;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;

    private PaintBucketRecipeBuilder(final DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    public static PaintBucketRecipeBuilder bucket(DyeColor color) {
        return new PaintBucketRecipeBuilder(color);
    }

    public PaintBucketRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance crit) {
        this.advancement.addCriterion(criterionName, crit);
        return this;
    }

    public PaintBucketRecipeBuilder group(String groupName) {
        this.group = groupName;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        final ResourceLocation recipeId = new ResourceLocation(Constants.MOD_ID, "recipes/paint_bucket_" + dyeColor.getName());

        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);


        consumer.accept(new PaintBucketRecipeBuilder.Result(
                this.dyeColor,
                this.group == null ? "" : this.group,
                this.advancement,
                new ResourceLocation(recipeId.getNamespace(), "recipes/" + Items.PAINT_BUCKET.get().getItemCategory().getRecipeFolderName() + "/" + recipeId.getPath())
        ));
    }


    public static class Result implements FinishedRecipe {
        private final DyeColor color;
        private final String group;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(DyeColor color, String group, Advancement.Builder adv, ResourceLocation advId) {
            this.color = color;
            this.group = group;
            this.advancement = adv;
            this.advancementId = advId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject output) {
            if (!this.group.isEmpty()) {
                output.addProperty("group", this.group);
            }

            output.addProperty("color", this.color.getName());
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return new ResourceLocation(Constants.MOD_ID, "paint_buckets/" + this.color.getName());
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return RecipeSerializers.PAINT_BUCKET.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
