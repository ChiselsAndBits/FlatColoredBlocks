package mod.flatcoloredblocks.core;

import mod.flatcoloredblocks.core.network.NetworkChannel;
import mod.flatcoloredblocks.core.registrars.*;
import mod.flatcoloredblocks.core.util.Constants;

public class FlatColoredBlocks
{
    private static FlatColoredBlocks instance;
    private final NetworkChannel networkChannel = new NetworkChannel(Constants.MOD_ID);

    public FlatColoredBlocks()
    {
        instance = this;

        CreativeModeTabs.onModConstruction();
        Blocks.onModConstruction();
        Items.onModConstruction();
        BlockEntityTypes.onModConstruction();
        Fluids.onModConstruction();
        ModelDataKeys.onModConstruction();
        ColorizationOverrides.onModConstruction();
        SolidPaints.onModConstruction();
        RecipeSerializers.onModConstruction();
        DataComponentTypes.onModConstruction();

        networkChannel.registerCommonMessages();
    }

    public void onInit() {
        DispenserBehaviors.onModInitialization();
    }

    public static FlatColoredBlocks instance() {
        return instance;
    }

    public NetworkChannel getNetworkChannel() {
        return networkChannel;
    }
}
