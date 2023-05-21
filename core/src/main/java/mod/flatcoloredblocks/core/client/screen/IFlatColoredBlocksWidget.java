package mod.flatcoloredblocks.core.client.screen;

public interface IFlatColoredBlocksWidget {
    /**
     * Invoked by the screen, when said screen is initialized.
     */
    void init();

    /**
     * Invoked by the screen, when it is removed from the display.
     */
    void removed();
}
