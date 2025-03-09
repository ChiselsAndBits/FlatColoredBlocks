package mod.flatcoloredblocks.fabric.client;

import com.communi.suggestu.scena.core.init.PlatformInitializationHandler;
import mod.flatcoloredblocks.core.client.FlatColoredBlocksClient;
import mod.flatcoloredblocks.fabric.Fabric;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClient implements ClientModInitializer {

    private FlatColoredBlocksClient flatColoredBlocksClient;

    public FabricClient() {
        Fabric.LOGGER.info("Initialized formula-forge client");
        PlatformInitializationHandler.getInstance().onInit(platform -> setFlatColoredBlocksClient(new FlatColoredBlocksClient()));
    }

    @Override
    public void onInitializeClient() {
        //Noop for now.
    }

    public void setFlatColoredBlocksClient(final FlatColoredBlocksClient flatColoredBlocksClient) {
        this.flatColoredBlocksClient = flatColoredBlocksClient;
    }
}
