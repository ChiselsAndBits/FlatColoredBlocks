package mod.flatcoloredblocks.core.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Custom screens which inherit from this class implement custom logic related to chisels and bits widgets and buttons.
 */
public class AbstractFlatColoredBlocksScreen extends Screen
{
    private boolean isInitialized = false;

    private final List<IFlatColoredBlocksWidget> widgets = Lists.newArrayList();
    private final List<Widget> renderables = Lists.newArrayList();

    /**
     * Creates a new screen, playing the narration message when opened.
     * @param narrationMessage The narration message for the screen.
     */
    protected AbstractFlatColoredBlocksScreen(final Component narrationMessage)
    {
        super(narrationMessage);
    }

    @Override
    protected void init()
    {
        super.init();
        this.isInitialized = true;

        this.children().stream().filter(IFlatColoredBlocksWidget.class::isInstance)
                .map(IFlatColoredBlocksWidget.class::cast)
                .forEach(IFlatColoredBlocksWidget::init);
    }

    @Override
    public <T extends GuiEventListener & Widget & NarratableEntry> @NotNull T addRenderableWidget(final @NotNull T button)
    {
        return super.addRenderableWidget(button);
    }

    @Override
    protected <T extends Widget> @NotNull T addRenderableOnly(final @NotNull T widget)
    {
        final T resultingWidget =  super.addRenderableOnly(widget);
        this.renderables.add(resultingWidget);

        return resultingWidget;
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> @NotNull T addWidget(final @NotNull T widget)
    {
        final T resultingWidget = super.addWidget(widget);

        if (isInitialized() && widget instanceof IFlatColoredBlocksWidget)
        {
            widgets.add((IFlatColoredBlocksWidget) resultingWidget);
            ((IFlatColoredBlocksWidget) widget).init();
        }

        if (resultingWidget instanceof Widget) {
            this.renderables.add((Widget) resultingWidget);
        }

        return resultingWidget;
    }

    @Override
    public void removeWidget(final @NotNull GuiEventListener listener)
    {
        super.removeWidget(listener);
        if (listener instanceof IFlatColoredBlocksWidget)
        {
            this.widgets.remove(listener);
            ((IFlatColoredBlocksWidget) listener).removed();
        }
        if (listener instanceof Widget) {
            this.renderables.remove(listener);
        }
    }

    @Override
    protected void clearWidgets()
    {
        this.widgets.stream()
                .filter(Objects::nonNull)
                .map(IFlatColoredBlocksWidget.class::cast)
                .forEach(IFlatColoredBlocksWidget::removed);

        this.widgets.clear();
        this.renderables.clear();
        super.clearWidgets();
    }

    @Override
    public void removed()
    {
        this.widgets.stream()
                .filter(Objects::nonNull)
                .map(IFlatColoredBlocksWidget.class::cast)
                .forEach(IFlatColoredBlocksWidget::removed);
    }

    public boolean isInitialized()
    {
        return isInitialized;
    }

    /**
     * Returns the widgets which are included in the screen.
     *
     * @return The widgets on this screen.
     */
    public List<IFlatColoredBlocksWidget> getWidgets()
    {
        return widgets;
    }

    @Override
    public void render(final @NotNull PoseStack poseStack, final int mouseX, final int mouseY, final float partialTickTime)
    {
        this.fillGradient(poseStack, 0, 0, this.width, this.height, -1072689136, -804253680);

        final  List<Widget> renderTargets = new ArrayList<>(this.renderables);
        for(Widget widget : renderTargets) {
            widget.render(poseStack, mouseX, mouseY, partialTickTime);
        }
    }
}
