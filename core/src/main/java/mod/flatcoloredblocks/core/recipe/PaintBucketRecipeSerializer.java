package mod.flatcoloredblocks.core.recipe;

import com.communi.suggestu.scena.core.fluid.IFluidManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.flatcoloredblocks.core.item.SolidDyeItem;
import mod.flatcoloredblocks.core.registrars.Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraft.world.item.Items.WATER_BUCKET;

public class PaintBucketRecipeSerializer implements RecipeSerializer<PaintBucketRecipeSerializer.PaintBucketRecipe>
{
    private static final PaintBucketRecipeSerializer INSTANCE = new PaintBucketRecipeSerializer();

    private static final MapCodec<PaintBucketRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("solidDye").forGetter(PaintBucketRecipe::isSolidDye),
                    DyeColor.CODEC.fieldOf("color").forGetter(PaintBucketRecipe::getDyeColor),
                    Codec.STRING.fieldOf("group").forGetter(PaintBucketRecipe::getGroup)
            ).apply(instance, PaintBucketRecipe::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, PaintBucketRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            recipe -> recipe.solidDye,
            ByteBufCodecs.VAR_INT,
            recipe -> recipe.dyeColor.getId(),
            ByteBufCodecs.STRING_UTF8,
            recipe -> recipe.group,
            (solid,color, group) -> new PaintBucketRecipe(solid, DyeColor.byId(color), group)
    );

    public static PaintBucketRecipeSerializer getInstance()
    {
        return INSTANCE;
    }

    private PaintBucketRecipeSerializer()
    {
    }

    @Override
    public @NotNull MapCodec<PaintBucketRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, PaintBucketRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    public static final class PaintBucketRecipe implements CraftingRecipe
    {
        private final boolean solidDye;
        private final DyeColor dyeColor;
        private final String group;

        public PaintBucketRecipe(boolean solidDye, final DyeColor dyeColor, final String group) {
            this.solidDye = solidDye;
            this.dyeColor = dyeColor;
            this.group = group;
        }

        @Override
        public boolean matches(final @NotNull CraftingInput pContainer, final @NotNull Level pLevel)
        {
            boolean hasEmptyPaintBucket = false;
            boolean hasDye = false;
            boolean hasWaterBucket = false;

            for (int i = 0; i < pContainer.size(); i++)
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
                } else if (stack.getItem() instanceof DyeItem dyeItem && !solidDye) {
                    if (hasDye)
                        return false;

                    if (!dyeItem.getDyeColor().equals(dyeColor))
                        return false;

                    hasDye = true;
                } else if (stack.getItem() instanceof SolidDyeItem && solidDye) {
                    if (hasDye)
                        return false;

                    hasDye = true;
                } else {
                    return false;
                }
            }

            return hasDye && hasEmptyPaintBucket && hasWaterBucket;
        }

        @Override
        public @NotNull ItemStack assemble(@NotNull CraftingInput craftingInput, HolderLookup.@NotNull Provider provider) {
            final ItemStack stack = new ItemStack(Items.PAINT_BUCKET.get());
            Items.PAINT_BUCKET.get().setAmount(stack, (int) IFluidManager.getInstance().getBucketAmount());
            Items.PAINT_BUCKET.get().setColor(stack, getColor(craftingInput), false);
            return stack;
        }

        public int getColor(CraftingInput input) {
            if (solidDye) {
                for (int i = 0; i < input.size(); i++) {
                    if (input.getItem(i).getItem() == Items.SOLID_DYE.get()) {
                        return Items.SOLID_DYE.get().getColor(input.getItem(i));
                    }
                }

                throw new IllegalStateException("Could not find solid dye");
            }

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

        public boolean isSolidDye() {
            return solidDye;
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
        public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider)
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

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PaintBucketRecipe that)) return false;
            return getDyeColor() == that.getDyeColor() && Objects.equals(getGroup(), that.getGroup());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getDyeColor(), getGroup());
        }
    }
}
