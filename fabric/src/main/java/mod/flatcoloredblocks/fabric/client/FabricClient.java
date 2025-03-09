package mod.flatcoloredblocks.fabric.client;

import com.communi.suggestu.scena.core.init.PlatformInitializationHandler;
import com.mojang.logging.LogUtils;
import mod.flatcoloredblocks.core.client.FlatColoredBlocksClient;
import mod.flatcoloredblocks.fabric.Fabric;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FabricClient implements ClientModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();
    private FlatColoredBlocksClient flatColoredBlocksClient;

    public FabricClient() {
        LOGGER.info("Initialized Flat Colored Block client");
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
