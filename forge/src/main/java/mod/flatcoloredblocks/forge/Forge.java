package mod.flatcoloredblocks.forge;

import mod.flatcoloredblocks.core.FlatColoredBlocks;
import mod.flatcoloredblocks.core.client.FlatColoredBlocksClient;
import mod.flatcoloredblocks.core.client.registrars.BlockEntityRenderers;
import mod.flatcoloredblocks.core.util.Constants;
import com.communi.suggestu.scena.core.init.PlatformInitializationHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class Forge
{
    private static final Logger LOGGER = LoggerFactory.getLogger("FCB-Forge");

    private FlatColoredBlocks flatColoredBlocks;

    private void setFlatColoredBlocks(final FlatColoredBlocks flatColoredBlocks)
    {
        this.flatColoredBlocks = flatColoredBlocks;
    }

    public Forge()
	{
        LOGGER.info("Initialized FlatColoredBlocks-Forge");
        //We need to use the platform initialization manager to handle the init in the constructor since this runs in parallel with scena itself.
        PlatformInitializationHandler.getInstance().onInit(platform -> {
            setFlatColoredBlocks(new FlatColoredBlocks());

            DistExecutor.runWhenOn(Dist.CLIENT, () -> Client::init);
        });

        Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener((Consumer<FMLCommonSetupEvent>) event -> flatColoredBlocks.onInit());
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
