package mod.flatcoloredblocks.core.recipe;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import com.mojang.serialization.MapCodec;
import mod.flatcoloredblocks.core.registrars.Items;
import mod.flatcoloredblocks.core.registry.ColorizationRegistry;
import mod.flatcoloredblocks.core.util.ColorizationData;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SolidDyeRecipeSerializer implements RecipeSerializer<SolidDyeRecipeSerializer.Recipe> {
    private static final SolidDyeRecipeSerializer INSTANCE = new SolidDyeRecipeSerializer();
    private static final MapCodec<SolidDyeRecipeSerializer.Recipe> CODEC = MapCodec.unit(new SolidDyeRecipeSerializer.Recipe());

    private static final StreamCodec<RegistryFriendlyByteBuf, SolidDyeRecipeSerializer.Recipe> STREAM_CODEC = StreamCodec.unit(new SolidDyeRecipeSerializer.Recipe());

    public static SolidDyeRecipeSerializer getInstance() {
        return INSTANCE;
    }

    private SolidDyeRecipeSerializer() {
    }

    @Override
    public @NotNull MapCodec<Recipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, Recipe> streamCodec() {
        return STREAM_CODEC;
    }

    public record Recipe() implements CraftingRecipe {

        @Override
        public @NotNull CraftingBookCategory category() {
            return CraftingBookCategory.MISC;
        }

        @Override
        public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
            int count = 0;

            for (int i = 0; i < input.size(); i++) {
                final ItemStack stack = input.getItem(i);
                if (stack.isEmpty()) {
                    continue;
                }

                count++;

                final boolean isSolidDye = stack.getItem() == Items.SOLID_DYE.get();
                final boolean isSolvent = ColorizationRegistry.getInstance().getColorFor(stack).isPresent();

                if (!isSolidDye && !isSolvent) {
                    return false;
                }
            }

            return count >= 2;
        }

        @Override
        public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider registries) {
            final List<ColorizationData> colorizationData = new ArrayList<>();

            for (int i = 0; i < input.size(); i++) {
                final ItemStack stack = input.getItem(i);
                if (stack.isEmpty()) {
                    continue;
                }

                final boolean isSolidDye = stack.getItem() == Items.SOLID_DYE.get();
                final boolean isSolvent = ColorizationRegistry.getInstance().getColorFor(stack).isPresent();

                if (!isSolidDye && !isSolvent) {
                    throw new IllegalStateException("Can't assemble recipe for item " + stack);
                }

                final ColorizationData data;
                if (isSolidDye) {
                    data = new ColorizationData(Items.SOLID_DYE.get().getColor(stack), 1, 0, IFluidManager.getInstance().getBucketAmount());
                } else {
                    final Optional<Integer> solidColor = ColorizationRegistry.getInstance().getColorFor(stack);
                    if (solidColor.isEmpty()) {
                        throw  new IllegalStateException("Can't assemble recipe for item " + stack);
                    }

                    data = new ColorizationData(solidColor.get(), 1, 0, IFluidManager.getInstance().getBucketAmount());
                }

                colorizationData.add(data);
            }

            if (colorizationData.size() < 2) {
                throw new IllegalStateException("Can not craft recipe. Not enough inputs.");
            }

            final int totalSum = colorizationData.stream().mapToInt(data -> (int) data.pigmentAmount()).sum();
            final int red = (int) colorizationData.stream().mapToLong(data -> (long) (data.red() * data.fluidImpressionFactor() * data.pigmentAmount())).sum() / totalSum;
            final int green = (int) colorizationData.stream().mapToLong(data -> (long) (data.green() * data.fluidImpressionFactor() * data.pigmentAmount())).sum() / totalSum;
            final int blue = (int) colorizationData.stream().mapToLong(data -> (long) (data.blue() * data.fluidImpressionFactor() * data.pigmentAmount())).sum() / totalSum;

            final int color = FastColor.ARGB32.color(0xFF, red, green, blue);

            final ItemStack result = new ItemStack(Items.SOLID_DYE.get());
            Items.SOLID_DYE.get().setColor(result, color, false);
            return result;
        }

        @Override
        public boolean canCraftInDimensions(int width, int height) {
            return width <= 3 && height <= 3;
        }

        @Override
        public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
            return new ItemStack(Items.SOLID_DYE.get());
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return SolidDyeRecipeSerializer.INSTANCE;
        }
    }
}
