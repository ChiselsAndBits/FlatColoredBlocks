package mod.flatcoloredblocks.core;

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

public class FlatColoredBlocks
{
    public FlatColoredBlocks()
    {
        CreativeModeTabs.onModConstruction();
        Blocks.onModConstruction();
        Items.onModConstruction();
        BlockEntityTypes.onModConstruction();
        Fluids.onModConstruction();
        ModelDataKeys.onModConstruction();
        ColorizationOverrides.onModConstruction();
        SolidPaints.onModConstruction();
        RecipeSerializers.onModConstruction();
    }

    public void onInit() {
        DispenserBehaviors.onModInitialization();
    }
}
