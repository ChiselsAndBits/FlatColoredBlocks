package mod.flatcoloredblocks.core.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

public class ColorChannelValueSelectorWidget extends AbstractFlatColoredBlocksWidget {

    private static final WidgetSprites SPRITES = new WidgetSprites(new ResourceLocation("widget/button"), new ResourceLocation("widget/button_disabled"), new ResourceLocation("widget/button_highlighted"));

    private static final int STEPS = 360;
    private static final float STEP = 1f / STEPS;
    private static final float START = 1f;
    private static final float END = 0f;
    private static final int VERTICAL_BAR_OFFSET = 6;
    private static final int BUTTON_WIDTH = 10;

    private final Float2IntFunction colorProducer;
    private final FloatConsumer valueConsumer;

    private final float barWidth;
    private final float barHeight;
    private final float barX;
    private final float barY;

    private final float barScaleX;

    private float value = 0.5f;

    /**
     * Creates a new widget.
     *
     * @param x             The x position.
     * @param y             The y position.
     * @param width         The width.
     * @param height        The height.
     * @param narration     The narration text when selected.
     * @param colorProducer Creates a color from a value for the channel that this selector represents.
     * @param valueConsumer The consumer of the value when a new value is selected.
     */
    public ColorChannelValueSelectorWidget(int x, int y, int width, int height, Component narration, final Float2IntFunction colorProducer, final FloatConsumer valueConsumer, float value) {
        super(x, y, width, height, narration);
        this.colorProducer = colorProducer;
        this.valueConsumer = valueConsumer;

        this.barWidth = width - BUTTON_WIDTH;
        this.barHeight = height - 2 * VERTICAL_BAR_OFFSET;
        this.barX = x + (BUTTON_WIDTH / 2F);
        this.barY = y + VERTICAL_BAR_OFFSET;

        this.barScaleX = STEPS / barWidth;

        this.value = value;

        Validate.isTrue(width > 2 * VERTICAL_BAR_OFFSET + BUTTON_WIDTH, "Width must be greater than 12");
        Validate.isTrue(height > 2 * VERTICAL_BAR_OFFSET, "Height must be greater than 4");
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        drawColorBar(graphics);
        drawSelectorButton(graphics);
    }

    private void drawColorBar(@NotNull final GuiGraphics graphics) {
        graphics.pose().pushPose();

        graphics.pose().translate(barX, barY, 0);
        graphics.pose().scale(1 / barScaleX, 1, 1);

        for (float v = START; v >= END; v -= STEP) {
            drawColorBarSlice(graphics, v);
        }

        graphics.pose().popPose();
    }

    private void drawColorBarSlice(
            @NotNull final GuiGraphics graphics, float v) {
        final int sliceStartX = (int) (STEPS * v);
        final int sliceEndX = sliceStartX + 1;
        final int sliceStartY = 0;
        final int sliceEndY = (int) (sliceStartY + barHeight);

        final int color = colorProducer.get(v);
        graphics.fillGradient(sliceStartX, sliceStartY, sliceEndX, sliceEndY, color, color);
    }

    private void drawSelectorButton(@NotNull final GuiGraphics graphics) {
        final int buttonX = (int) (getX() + barWidth * value);


        graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), buttonX, this.getY(), BUTTON_WIDTH / 2, this.getHeight());
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        //graphics.blit();
        //graphics.blit(buttonX + BUTTON_WIDTH / 2, this.getY(), 200 - BUTTON_WIDTH / 2, 46 + imageY * 20, BUTTON_WIDTH / 2, this.getHeight());
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);

        if (pMouseX < barX || pMouseX > barX + barWidth) {
            return;
        }

        if (pMouseY < barY || pMouseY > barY + barHeight) {
            return;
        }

        value = Math.max(0, Math.min(1, (float) ((pMouseX - barX) / barWidth)));
        valueConsumer.accept(value);
    }

    @Override
    protected void onDrag(double pMouseX, double pMouseY, double pDragX, double pDragY) {
        super.onDrag(pMouseX, pMouseY, pDragX, pDragY);

        value = Math.max(0, Math.min(1, (float) ((pMouseX - barX) / barWidth)));
        valueConsumer.accept(value);
    }
}
