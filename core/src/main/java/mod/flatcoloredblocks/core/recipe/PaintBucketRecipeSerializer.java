package mod.flatcoloredblocks.core.recipe;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mod.flatcoloredblocks.core.registrars.Items;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.item.Items.WATER_BUCKET;

public class PaintBucketRecipeSerializer implements RecipeSerializer<PaintBucketRecipeSerializer.PaintBucketRecipe>
{
    private static final PaintBucketRecipeSerializer INSTANCE = new PaintBucketRecipeSerializer();

    public static PaintBucketRecipeSerializer getInstance()
    {
        return INSTANCE;
    }

    private PaintBucketRecipeSerializer()
    {
    }

    @Override
    public @NotNull PaintBucketRecipe fromJson(final @NotNull ResourceLocation pRecipeId, final @NotNull JsonObject pSerializedRecipe)
    {
        if (!pSerializedRecipe.has("color"))
        {
            throw new JsonParseException("Missing color");
        }

        final String colorName = pSerializedRecipe.get("color").getAsString();
        final DyeColor color = DyeColor.byName(colorName, null);
        if (color == null)
        {
            throw new JsonParseException("Invalid color: " + colorName);
        }

        return new PaintBucketRecipe(pRecipeId, color, pSerializedRecipe.has("group") ? pSerializedRecipe.get("group").getAsString(): "");
    }

    @Override
    public @NotNull PaintBucketRecipe fromNetwork(final @NotNull ResourceLocation pRecipeId, final @NotNull FriendlyByteBuf pBuffer)
    {
        final String colorName = pBuffer.readUtf(100);
        final String group = pBuffer.readUtf();
        final DyeColor color = DyeColor.byName(colorName, null);
        if (color == null)
        {
            throw new JsonParseException("Invalid color: " + colorName);
        }

        return new PaintBucketRecipe(pRecipeId, color, group);
    }

    @Override
    public void toNetwork(final @NotNull FriendlyByteBuf pBuffer, final @NotNull PaintBucketRecipe pRecipe)
    {
        pBuffer.writeUtf(pRecipe.dyeColor.getName(), 100);
        pBuffer.writeUtf(pRecipe.group);
    }

    public static final class PaintBucketRecipe implements CraftingRecipe
    {
        private final ResourceLocation id;
        private final DyeColor dyeColor;
        private final String group;

        public PaintBucketRecipe(final ResourceLocation id, final DyeColor dyeColor, final String group) {
            this.id = id;
            this.dyeColor = dyeColor;
            this.group = group;
        }

        @Override
        public boolean matches(final @NotNull CraftingContainer pContainer, final @NotNull Level pLevel)
        {
            boolean hasEmptyPaintBucket = false;
            boolean hasDye = false;
            boolean hasWaterBucket = false;

            for (int i = 0; i < pContainer.getContainerSize(); i++)
            {
                final ItemStack stack = pContainer.getItem(i);
                if (stack.isEmpty())
                    continue;

                if (stack.getItem() == Items.PAINT_BUCKET.get()) {
                    if (hasEmptyPaintBucket)
                        return false;

                    if (!Items.PAINT_BUCKET.get().hasAmount(stack))
                    {
                        hasEmptyPaintBucket = true;
                    } else {
                        return false;
                    }
                } else if (stack.getItem() == WATER_BUCKET) {
                    if (hasWaterBucket)
                        return false;

                    hasWaterBucket = true;
                } else if (stack.getItem() instanceof DyeItem dyeItem) {
                    if (hasDye)
                        return false;

                    if (!dyeItem.getDyeColor().equals(dyeColor))
                        return false;

                    hasDye = true;
                } else {
                    return false;
                }
            }

            return hasDye && hasEmptyPaintBucket && hasWaterBucket;
        }

        @Override
        public @NotNull ItemStack assemble(final @NotNull CraftingContainer pContainer)
        {
            final ItemStack stack = new ItemStack(Items.PAINT_BUCKET.get());
            Items.PAINT_BUCKET.get().setAmount(stack, (int) IFluidManager.getInstance().getBucketAmount());
            Items.PAINT_BUCKET.get().setColor(stack, getColor());
            return stack;
        }

        public int getColor() {
            return switch (dyeColor) {
                case WHITE -> 0xFFFFFF;
                case RED -> 0xFF0000;
                case BLUE -> 0x0000FF;
                case GREEN -> 0x00FF00;
                case BLACK -> 0x000000;
                case YELLOW -> 0xFFFF00;
                case PURPLE -> 0xFF00FF;
                case CYAN -> 0x00FFFF;
                default -> dyeColor.getFireworkColor();
            };
        }

        @Override
        public @NotNull NonNullList<Ingredient> getIngredients()
        {
            return NonNullList.of(Ingredient.of(Items.PAINT_BUCKET.get()), Ingredient.of(WATER_BUCKET), Ingredient.of(new ItemStack(DyeItem.byColor(dyeColor))));
        }

        @Override
        public boolean canCraftInDimensions(final int pWidth, final int pHeight)
        {
            return pWidth * pHeight >= 3;
        }

        @Override
        public @NotNull ItemStack getResultItem()
        {
            return new ItemStack(Items.PAINT_BUCKET.get());
        }

        @Override
        public @NotNull ResourceLocation getId()
        {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer()
        {
            return PaintBucketRecipeSerializer.getInstance();
        }

        @Override
        public @NotNull String getGroup()
        {
            return group;
        }
    }
}
