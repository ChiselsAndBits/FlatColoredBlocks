package mod.flatcoloredblocks.core.client.screen;

import mod.flatcoloredblocks.core.FlatColoredBlocks;
import mod.flatcoloredblocks.core.client.screen.widget.ColorChannelValueSelectorWidget;
import mod.flatcoloredblocks.core.client.screen.widget.DynamicItemStackPreviewWidget;
import mod.flatcoloredblocks.core.item.IWithColorItem;
import mod.flatcoloredblocks.core.network.packets.HeldItemColorUpdatedPacket;
import mod.flatcoloredblocks.core.util.ColorUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ColorSelectionScreen extends AbstractFlatColoredBlocksScreen {

    private final ItemStack target;
    private final IWithColorItem source;

    public static Screen create(final ItemStack target, final IWithColorItem source) {
        return new ColorSelectionScreen(target, source);
    }

    private ColorSelectionScreen(ItemStack target, IWithColorItem source) {
        super(Component.translatable("gui.flatcoloredblocks.color_selection.title", target.getHoverName()));
        this.target = target;
        this.source = source;
    }


    @Override
    protected void init() {
        super.init();

        final int centerX = this.width / 2;
        final int centerY = this.height / 2;

        final int sixtyProcentWidth = (int) (this.width * 0.6);
        final int thirtyProcentWidth = (int) (this.width * 0.3);

        this.addRenderableWidget(new ColorChannelValueSelectorWidget(centerX - thirtyProcentWidth, centerY - 34, sixtyProcentWidth * 2/3 - 10, 20, Component.translatable("gui.flatcoloredblocks.color_selection.selectors.channels.red"), ColorSelectionScreen::red, this::withRed, ColorUtils.getRed(source.getColor(target))));
        this.addRenderableWidget(new ColorChannelValueSelectorWidget(centerX - thirtyProcentWidth, centerY - 10, sixtyProcentWidth * 2/3 - 10, 20, Component.translatable("gui.flatcoloredblocks.color_selection.selectors.channels.green"), ColorSelectionScreen::green, this::withGreen, ColorUtils.getGreen(source.getColor(target))));
        this.addRenderableWidget(new ColorChannelValueSelectorWidget(centerX - thirtyProcentWidth, centerY + 14, sixtyProcentWidth * 2/3 - 10, 20, Component.translatable("gui.flatcoloredblocks.color_selection.selectors.channels.blue"), ColorSelectionScreen::blue, this::withBlue, ColorUtils.getBlue(source.getColor(target))));

        this.addRenderableWidget(new DynamicItemStackPreviewWidget(centerX - thirtyProcentWidth + sixtyProcentWidth * 2/3 + 20, centerY - 34, 68, 68, Component.translatable("gui.flatcoloredblocks.color_selection.selectors.preview"), () -> target));
    }

    private static int red(final float v) {
        return ColorUtils.fromRGB(v, 0, 0);
    }

    private static int green(final float v) {
        return ColorUtils.fromRGB(0, v, 0);
    }

    private static int blue(final float v) {
        return ColorUtils.fromRGB(0, 0, v);
    }

    private void withRed(final float v) {
        final int current = source.getColor(target);
        final int newColor = ColorUtils.fromRGB(v, ColorUtils.getGreen(current), ColorUtils.getBlue(current));

        setColor(newColor);
    }

    private void withGreen(final float v) {
        final int current = source.getColor(target);
        final int newColor = ColorUtils.fromRGB(ColorUtils.getRed(current), v, ColorUtils.getBlue(current));

        setColor(newColor);
    }

    private void withBlue(final float v) {
        final int current = source.getColor(target);
        final int newColor = ColorUtils.fromRGB(ColorUtils.getRed(current), ColorUtils.getGreen(current), v);

        setColor(newColor);
    }

    private void setColor(int newColor) {
        source.setColor(target, newColor);
        FlatColoredBlocks.instance().getNetworkChannel().sendToServer(new HeldItemColorUpdatedPacket(newColor));
    }

}
