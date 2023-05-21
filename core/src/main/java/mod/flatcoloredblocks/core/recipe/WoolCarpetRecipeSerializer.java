package mod.flatcoloredblocks.core.recipe;

import com.google.gson.JsonObject;
import mod.flatcoloredblocks.core.item.ColoredBlockItem;
import mod.flatcoloredblocks.core.registrars.Blocks;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WoolCarpetRecipeSerializer implements RecipeSerializer<WoolCarpetRecipeSerializer.WoolCarpetRecipe> {

    private static final WoolCarpetRecipeSerializer INSTANCE = new WoolCarpetRecipeSerializer();

    public static WoolCarpetRecipeSerializer getInstance()
    {
        return INSTANCE;
    }

    private WoolCarpetRecipeSerializer()
    {
    }

    @Override
    public WoolCarpetRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        return new WoolCarpetRecipe(pRecipeId);
    }

    @Override
    public WoolCarpetRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return new WoolCarpetRecipe(pRecipeId);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, WoolCarpetRecipe pRecipe) {
        pBuffer.writeResourceLocation(pRecipe.getId());
    }

    public class WoolCarpetRecipe implements CraftingRecipe {

        private final ResourceLocation id;

        public WoolCarpetRecipe(ResourceLocation id) {
            this.id = id;
        }

        @Override
        public boolean matches(CraftingContainer pContainer, Level pLevel) {

            Integer row = findWoolRow(pContainer);
            if (row == null) return false;

            for (int r = 0; r < pContainer.getHeight(); r++) {
                if (r == row) {
                    final ColorResult color = getColor(pContainer, r, 0);
                    for (int c = 1; c < pContainer.getWidth(); c++) {
                        final ColorResult otherColor = getColor(pContainer, r, c);
                        if (!otherColor.valid()) {
                            return false;
                        }

                        if (otherColor.color() != color.color()) {
                            return false;
                        }
                    }
                }

                for (int c = 0; c < pContainer.getWidth(); c++) {
                    final ItemStack stack = pContainer.getItem(r * pContainer.getWidth() + c);
                    if (!stack.isEmpty()) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public ItemStack assemble(CraftingContainer pContainer) {
            if (!matches(pContainer, null)) {
                return ItemStack.EMPTY;
            }

            Integer row = findWoolRow(pContainer);
            if (row == null) return ItemStack.EMPTY;

            final ColorResult color = getColor(pContainer, row, 0);
            if (!color.valid()) return ItemStack.EMPTY;

            return Util.make(new ItemStack(Blocks.COLORED_WOOL_CARPET.get()), stack -> Blocks.COLORED_WOOL_CARPET.get().setColor(stack, color.color()));
        }

        @Override
        public boolean canCraftInDimensions(int pWidth, int pHeight) {
            return pWidth * pHeight >= 3;
        }

        @Override
        public ItemStack getResultItem() {
            return new ItemStack(Blocks.COLORED_WOOL_CARPET.get());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return WoolCarpetRecipeSerializer.getInstance();
        }

        private ColorResult getColor(final CraftingContainer container, final int row, final int column) {
            final ItemStack stack = container.getItem(row * container.getWidth() + column);
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
        private Integer findWoolRow(CraftingContainer pContainer) {
            int row = -1;

            for (int i = 0; i < pContainer.getHeight(); i++) {
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
    }
}
