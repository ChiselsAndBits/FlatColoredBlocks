package mod.flatcoloredblocks.core.client;

import mod.flatcoloredblocks.core.client.registrars.BlockColors;
import mod.flatcoloredblocks.core.client.registrars.BlockEntityRenderers;
import mod.flatcoloredblocks.core.client.registrars.ItemBlockRenderTypes;
import mod.flatcoloredblocks.core.client.registrars.ItemColors;
import mod.flatcoloredblocks.core.client.registrars.ItemProperties;
import mod.flatcoloredblocks.core.client.registrars.ModelLoaders;
import mod.flatcoloredblocks.core.client.registrars.Textures;

public class FlatColoredBlocksClient
{
    public FlatColoredBlocksClient()
    {
        BlockEntityRenderers.onClientConstruct();
        ItemColors.onClientConstruct();
        BlockColors.onClientConstruct();
        ModelLoaders.onClientConstruct();
        ItemProperties.onClientConstruction();
        Textures.onClientConstruction();
        ItemBlockRenderTypes.onModConstruction();
    }
}
