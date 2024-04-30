package mod.flatcoloredblocks.core.recipe;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.flatcoloredblocks.core.registrars.Items;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.item.Items.WATER_BUCKET;

public class PaintBucketRecipeSerializer implements RecipeSerializer<PaintBucketRecipeSerializer.PaintBucketRecipe>
{
    private static final PaintBucketRecipeSerializer INSTANCE = new PaintBucketRecipeSerializer();

    private static final Codec<PaintBucketRecipe> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    DyeColor.CODEC.fieldOf("color").forGetter(PaintBucketRecipe::getDyeColor),
                    Codec.STRING.fieldOf("group").forGetter(PaintBucketRecipe::getGroup)
            ).apply(instance, PaintBucketRecipe::new)
    );

    public static PaintBucketRecipeSerializer getInstance()
    {
        return INSTANCE;
    }

    private PaintBucketRecipeSerializer()
    {
    }

    @Override
    public Codec<PaintBucketRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull PaintBucketRecipe fromNetwork(final @NotNull FriendlyByteBuf pBuffer)
    {
        final String colorName = pBuffer.readUtf(100);
        final String group = pBuffer.readUtf();
        final DyeColor color = DyeColor.byName(colorName, null);
        if (color == null)
        {
            throw new JsonParseException("Invalid color: " + colorName);
        }

        return new PaintBucketRecipe(color, group);
    }

    @Override
    public void toNetwork(final @NotNull FriendlyByteBuf pBuffer, final @NotNull PaintBucketRecipe pRecipe)
    {
        pBuffer.writeUtf(pRecipe.dyeColor.getName(), 100);
        pBuffer.writeUtf(pRecipe.group);
    }

    public static final class PaintBucketRecipe implements CraftingRecipe
    {
        private final DyeColor dyeColor;
        private final String group;

        public PaintBucketRecipe(final DyeColor dyeColor, final String group) {
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
        public @NotNull ItemStack assemble(final @NotNull CraftingContainer pContainer, @NotNull RegistryAccess registryAccess)
        {
            final ItemStack stack = new ItemStack(Items.PAINT_BUCKET.get());
            Items.PAINT_BUCKET.get().setAmount(stack, (int) IFluidManager.getInstance().getBucketAmount());
            Items.PAINT_BUCKET.get().setColor(stack, getColor(), false);
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

        public DyeColor getDyeColor() {
            return dyeColor;
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
        public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess)
        {
            return new ItemStack(Items.PAINT_BUCKET.get());
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

        @Override
        public @NotNull CraftingBookCategory category() {
            return CraftingBookCategory.BUILDING;
        }
    }
}
