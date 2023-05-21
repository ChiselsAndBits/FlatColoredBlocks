package mod.flatcoloredblocks.core.client.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DynamicItemStackPreviewWidget extends AbstractFlatColoredBlocksWidget {

    private final Supplier<ItemStack> stackSupplier;

    /**
     * Creates a new widget.
     *
     * @param x             The x position.
     * @param y             The y position.
     * @param width         The width.
     * @param height        The height.
     * @param narration     The narration text when selected.
     * @param stackSupplier The supplier for the stack to render.
     */
    public DynamicItemStackPreviewWidget(int x, int y, int width, int height, Component narration, Supplier<ItemStack> stackSupplier) {
        super(x, y, width, height, narration);
        this.stackSupplier = stackSupplier;
    }

    @Override
    public void renderButton(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        final float scaleX = (float) width / 16f;
        final float scaleY = (float) height / 16f;

        pPoseStack.pushPose();
        pPoseStack.translate(x, y, 0);
        pPoseStack.scale(scaleX, scaleY, 1);

        renderItemStack(pPoseStack, stackSupplier.get(), false);

        pPoseStack.popPose();
    }
}
