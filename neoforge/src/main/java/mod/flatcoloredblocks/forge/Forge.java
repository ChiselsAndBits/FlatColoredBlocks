package mod.flatcoloredblocks.forge;

import com.communi.suggestu.scena.core.dist.Dist;
import com.communi.suggestu.scena.core.dist.DistExecutor;
import mod.flatcoloredblocks.core.FlatColoredBlocks;
import mod.flatcoloredblocks.core.client.FlatColoredBlocksClient;
import mod.flatcoloredblocks.core.util.Constants;
import com.communi.suggestu.scena.core.init.PlatformInitializationHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@SuppressWarnings("deprecation")
@Mod(Constants.MOD_ID)
public class Forge
{
    private static final Logger LOGGER = LoggerFactory.getLogger("FCB-Forge");

    private FlatColoredBlocks flatColoredBlocks;

    private void setFlatColoredBlocks(final FlatColoredBlocks flatColoredBlocks)
    {
        this.flatColoredBlocks = flatColoredBlocks;
    }

    public Forge(IEventBus modBus)
	{
        LOGGER.info("Initialized FlatColoredBlocks-Forge");
        //We need to use the platform initialization manager to handle the init in the constructor since this runs in parallel with scena itself.
        PlatformInitializationHandler.getInstance().onInit(platform -> {
            setFlatColoredBlocks(new FlatColoredBlocks());

            DistExecutor.runWhenOn(Dist.CLIENT, () -> Client::init);
        });

        modBus.addListener((Consumer<FMLCommonSetupEvent>) event -> flatColoredBlocks.onInit());
	}

    public static final class Client {

        private static FlatColoredBlocksClient flatColoredBlocksClient;

        public static void setFlatColoredBlocksClient(final FlatColoredBlocksClient flatColoredBlocksClient)
        {
            Client.flatColoredBlocksClient = flatColoredBlocksClient;
        }

        public static void init() {
            LOGGER.info("Initialized FlatColoredBlocks-Forge client");
            //We need to use the platform initialization manager to handle the init in the constructor since this runs in parallel with scena itself.
            PlatformInitializationHandler.getInstance().onInit(platform -> {
                setFlatColoredBlocksClient(new FlatColoredBlocksClient());
            });
        }
    }
}
