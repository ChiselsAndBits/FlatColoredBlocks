package mod.flatcoloredblocks.core.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.flatcoloredblocks.core.client.screen.IFlatColoredBlocksWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * All flat colored blocks widgets inherit from this class.
 * Most notably provides init support, invoked when the window itself has its init method called.
 */
public class AbstractFlatColoredBlocksWidget extends AbstractWidget implements IFlatColoredBlocksWidget
{
    /**
     * Creates a new widget.
     *
     * @param x The x position.
     * @param y The y position.
     * @param width The width.
     * @param height The height.
     * @param narration The narration text when selected.
     */
    public AbstractFlatColoredBlocksWidget(final int x, final int y, final int width, final int height, final Component narration)
    {
        super(x, y, width, height, narration);
    }

    /**
     * Gives access to the current instance of minecraft.
     *
     * @return The current instance of minecraft.
     */
    @NotNull
    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    /**
     * The font used in this widget.
     *
     * @return The font used to render text in this widget.
     */
    @NotNull
    public Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Override
    public void init() {
    }

    @Override
    public void removed() {
    }

    @Override
    public void updateNarration(final @NotNull NarrationElementOutput output)
    {
        this.defaultButtonNarrationText(output);
    }

    public void renderItemStack(PoseStack poseStack, @NotNull ItemStack ingredient, final boolean renderDecorations) {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        {
            modelViewStack.mulPoseMatrix(poseStack.last().pose());

            RenderSystem.enableDepthTest();

            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            itemRenderer.renderAndDecorateFakeItem(ingredient, 0, 0);

            if (renderDecorations) {
                itemRenderer.renderGuiItemDecorations(font, ingredient, 0, 0);
            }

            RenderSystem.disableBlend();
        }
        modelViewStack.popPose();
        // Restore model-view matrix now that the item has been rendered
        RenderSystem.applyModelViewMatrix();
    }
}
