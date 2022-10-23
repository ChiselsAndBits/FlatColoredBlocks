package mod.flatcoloredblocks.fabric;

import mod.flatcoloredblocks.core.FlatColoredBlocks;
import com.communi.suggestu.scena.core.init.PlatformInitializationHandler;
import mod.flatcoloredblocks.core.client.FlatColoredBlocksClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.resources.model.ModelBakery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fabric implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("formula-fabric");

    private FlatColoredBlocks flatColoredBlocks;

    public Fabric()
    {
        LOGGER.info("Initialized formula-forge");
        PlatformInitializationHandler.getInstance().onInit(platform -> setFlatColoredBlocks(new FlatColoredBlocks()));
    }

    @Override
    public void onInitialize()
    {
        //Noop for now.
    }

    public void setFlatColoredBlocks(final FlatColoredBlocks flatColoredBlocks)
    {
        this.flatColoredBlocks = flatColoredBlocks;
    }

    public static final class Client implements ClientModInitializer
    {

        private FlatColoredBlocksClient flatColoredBlocksClient;

        public Client()
        {
            LOGGER.info("Initialized formula-forge client");
            PlatformInitializationHandler.getInstance().onInit(platform -> setFlatColoredBlocksClient(new FlatColoredBlocksClient()));
        }

        @Override
        public void onInitializeClient()
        {
            //Noop for now.
        }

        public void setFlatColoredBlocksClient(final FlatColoredBlocksClient flatColoredBlocksClient)
        {
            this.flatColoredBlocksClient = flatColoredBlocksClient;
        }
    }
}
