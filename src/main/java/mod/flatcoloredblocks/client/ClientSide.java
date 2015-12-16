package mod.flatcoloredblocks.client;

import java.util.HashMap;
import java.util.Map;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.BlockHSVConfiguration;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.craftingitem.ItemColoredBlockCrafter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientSide implements IClientSide
{

	@Override
	public void configureCraftingRender(
			final ItemColoredBlockCrafter icbc )
	{
		ModelLoader.setCustomModelResourceLocation( icbc, 0, new ModelResourceLocation( FlatColoredBlocks.MODID + ":coloredcraftingitem", "inventory" ) );
	}

	@Override
	public void configureBlockRender(
			final BlockFlatColored cb )
	{
		final String flatcoloredblocks_name = getTextureName( cb.getType() );
		final ModelResourceLocation flatcoloredblocks_block = new ModelResourceLocation( flatcoloredblocks_name, "normal" );

		// map all shades to a single model...
		ModelLoader.setCustomStateMapper( cb, new IStateMapper() {

			@Override
			public Map<IBlockState, ModelResourceLocation> putStateModelLocations(
					final Block blockIn )
			{
				final Map<IBlockState, ModelResourceLocation> loc = new HashMap<IBlockState, ModelResourceLocation>();

				for ( int x = cb.getShadeOffset(); x <= cb.getMaxShade(); ++x )
				{
					loc.put( cb.getDefaultState().withProperty( cb.getShade(), x - cb.getShadeOffset() ), flatcoloredblocks_block );
				}

				return loc;
			}

		} );

		// map all shades to a single model...
		final Item cbi = Item.getItemFromBlock( cb );
		ModelBakery.addVariantName( cbi, flatcoloredblocks_name );
		for ( int z = 0; z < BlockHSVConfiguration.META_SCALE; ++z )
		{
			ModelLoader.setCustomModelResourceLocation( cbi, z, flatcoloredblocks_block );
		}
	}

	private String getTextureName(
			final EnumFlatBlockType type )
	{
		return FlatColoredBlocks.MODID + ":flatcoloredblock_" + getTextureFor( type ).name();
	}

	private Enum<?> getTextureFor(
			final EnumFlatBlockType type )
	{
		switch ( type )
		{
			case GLOWING:
				return FlatColoredBlocks.instance.config.DISPLAY_TEXTURE_GLOWING;
			case TRANSPARENT:
				return FlatColoredBlocks.instance.config.DISPLAY_TEXTURE_TRANSPARENT;
			default:
				return FlatColoredBlocks.instance.config.DISPLAY_TEXTURE;
		}
	}

}
