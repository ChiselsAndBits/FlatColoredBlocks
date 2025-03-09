package mod.flatcoloredblocks.fabric;

import com.mojang.logging.LogUtils;
import mod.flatcoloredblocks.core.FlatColoredBlocks;
import com.communi.suggestu.scena.core.init.PlatformInitializationHandler;
import mod.flatcoloredblocks.core.client.FlatColoredBlocksClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.resources.model.ModelBakery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fabric implements ModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    private FlatColoredBlocks flatColoredBlocks;

    public Fabric()
    {
        LOGGER.info("Initialized Flat Colored Blocks");
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

}
