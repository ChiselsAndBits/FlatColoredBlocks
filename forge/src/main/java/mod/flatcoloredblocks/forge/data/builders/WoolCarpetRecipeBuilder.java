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

public class WoolCarpetRecipeBuilder
{
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;

    private WoolCarpetRecipeBuilder() {
    }

    public static WoolCarpetRecipeBuilder create() {
        return new WoolCarpetRecipeBuilder();
    }

    public WoolCarpetRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance crit) {
        this.advancement.addCriterion(criterionName, crit);
        return this;
    }

    public WoolCarpetRecipeBuilder group(String groupName) {
        this.group = groupName;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        final ResourceLocation recipeId = new ResourceLocation(Constants.MOD_ID, "recipes/colored_wool_carpet");

        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);

        consumer.accept(new WoolCarpetRecipeBuilder.Result(
                this.group == null ? "" : this.group,
                this.advancement,
                new ResourceLocation(recipeId.getNamespace(), "recipes/" + Items.COLORED_WOOL_CARPET.get().getItemCategory().getRecipeFolderName() + "/" + recipeId.getPath())
        ));
    }


    public static class Result implements FinishedRecipe {
        private final String group;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(String group, Advancement.Builder adv, ResourceLocation advId) {
            this.group = group;
            this.advancement = adv;
            this.advancementId = advId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject output) {
            if (!this.group.isEmpty()) {
                output.addProperty("group", this.group);
            }
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return new ResourceLocation(Constants.MOD_ID, "colored_wool_carpet");
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return RecipeSerializers.WOOL_CARPET.get();
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
