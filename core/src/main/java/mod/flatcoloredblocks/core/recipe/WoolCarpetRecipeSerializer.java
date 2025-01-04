package mod.flatcoloredblocks.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import mod.flatcoloredblocks.core.item.ColoredBlockItem;
import mod.flatcoloredblocks.core.registrars.Blocks;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WoolCarpetRecipeSerializer implements RecipeSerializer<WoolCarpetRecipeSerializer.WoolCarpetRecipe> {

    private static final WoolCarpetRecipeSerializer INSTANCE = new WoolCarpetRecipeSerializer();

    private static final MapCodec<WoolCarpetRecipe> CODEC = MapCodec.unit(new WoolCarpetRecipe());

    private static final StreamCodec<RegistryFriendlyByteBuf, WoolCarpetRecipe> STREAM_CODEC = StreamCodec.unit(new WoolCarpetRecipe());

    public static WoolCarpetRecipeSerializer getInstance()
    {
        return INSTANCE;
    }

    private WoolCarpetRecipeSerializer()
    {
    }

    @Override
    public MapCodec<WoolCarpetRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WoolCarpetRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    public static class WoolCarpetRecipe implements CraftingRecipe {

        public WoolCarpetRecipe() {
        }



        @Override
        public boolean matches(@NotNull CraftingInput pContainer, @Nullable Level pLevel) {

            Integer row = findWoolRow(pContainer);
            if (row == null) return false;

            for (int r = 0; r < pContainer.height(); r++) {
                if (r == row) {
                    final ColorResult color = getColor(pContainer, r, 0);
                    for (int c = 1; c < pContainer.width(); c++) {
                        final ColorResult otherColor = getColor(pContainer, r, c);
                        if (!otherColor.valid()) {
                            return false;
                        }

                        if (otherColor.color() != color.color()) {
                            return false;
                        }
                    }
                }

                for (int c = 0; c < pContainer.width(); c++) {
                    final ItemStack stack = pContainer.getItem(r * pContainer.width() + c);
                    if (!stack.isEmpty()) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public @NotNull ItemStack assemble(@NotNull CraftingInput craftingContainer, @NotNull HolderLookup.Provider registryAccess) {
            if (!matches(craftingContainer, null)) {
                return ItemStack.EMPTY;
            }

            Integer row = findWoolRow(craftingContainer);
            if (row == null) return ItemStack.EMPTY;

            final ColorResult color = getColor(craftingContainer, row, 0);
            if (!color.valid()) return ItemStack.EMPTY;

            return Util.make(new ItemStack(Blocks.COLORED_WOOL_CARPET.get()), stack -> Blocks.COLORED_WOOL_CARPET.get().setColor(stack, color.color()));
        }

        @Override
        public boolean canCraftInDimensions(int pWidth, int pHeight) {
            return pWidth * pHeight >= 3;
        }

        @Override
        public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider registryAccess) {
            return new ItemStack(Blocks.COLORED_WOOL_CARPET.get());
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return WoolCarpetRecipeSerializer.getInstance();
        }

        private ColorResult getColor(final CraftingInput container, final int row, final int column) {
            final ItemStack stack = container.getItem(row * container.width() + column);
            if (stack.isEmpty()) {
                return ColorResult.empty();
            }

            if (!(stack.getItem() instanceof ColoredBlockItem coloredBlockItem)) {
                return ColorResult.invalid();
            }

            if (coloredBlockItem.getColoredBlock() != Blocks.COLORED_WOOL.get()) {
                return ColorResult.invalid();
            }

            return ColorResult.valid(coloredBlockItem.getColoredBlock().getColor(stack));
        }

        @Nullable
        private Integer findWoolRow(CraftingInput pContainer) {
            int row = -1;

            for (int i = 0; i < pContainer.height(); i++) {
                final ColorResult color = getColor(pContainer, i, 0);
                if (!color.valid()) {
                    return null;
                }

                if (color.air()) {
                    continue;
                }

                if (row == -1) {
                    row = i;
                } else {
                    return null;
                }
            }
            return row;
        }

        @Override
        public @NotNull CraftingBookCategory category() {
            return CraftingBookCategory.BUILDING;
        }

        private record ColorResult(int color, boolean valid, boolean air) {

            public static ColorResult empty() {
                return new ColorResult(0, false, true);
            }

            public static ColorResult invalid() {
                return new ColorResult(0, false, false);
            }

            public static ColorResult valid(int color) {
                return new ColorResult(color, true, false);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                return o instanceof WoolCarpetRecipeSerializer.WoolCarpetRecipe;
            }
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }
}
