package mod.flatcoloredblocks.core;

import mod.flatcoloredblocks.core.network.NetworkChannel;
import mod.flatcoloredblocks.core.registrars.BlockEntityTypes;
import mod.flatcoloredblocks.core.registrars.Blocks;
import mod.flatcoloredblocks.core.registrars.ColorizationOverrides;
import mod.flatcoloredblocks.core.registrars.CreativeModeTabs;
import mod.flatcoloredblocks.core.registrars.DispenserBehaviors;
import mod.flatcoloredblocks.core.registrars.Fluids;
import mod.flatcoloredblocks.core.registrars.Items;
import mod.flatcoloredblocks.core.registrars.ModelDataKeys;
import mod.flatcoloredblocks.core.registrars.RecipeSerializers;
import mod.flatcoloredblocks.core.registrars.SolidPaints;
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
