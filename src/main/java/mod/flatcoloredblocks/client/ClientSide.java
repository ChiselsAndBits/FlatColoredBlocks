package mod.flatcoloredblocks.client;

import javax.annotation.Nonnull;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.ModUtil;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.model.ModelGenerator;
import mod.flatcoloredblocks.textures.TextureGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.common.MinecraftForge;

public class ClientSide implements IClientSide
{

	public static final ClientSide instance = new ClientSide();

	public TextureGenerator textureGenerator = new TextureGenerator();
	public ModelGenerator modelGenerator = new ModelGenerator();

	private ClientSide()
	{
	}

	@Override
	public void preinit()
	{
		modelGenerator.preinit();

		MinecraftForge.EVENT_BUS.register( textureGenerator );
		MinecraftForge.EVENT_BUS.register( modelGenerator );
	}

	@Override
	public void init()
	{
		Minecraft.getInstance().getItemColors().register( new IItemColor() {

			@Override
			@Nonnull
			public int getColor(
					final ItemStack stack,
					final int tintIndex )
			{
				final Block blk = Block.getBlockFromItem( stack.getItem() );
				return ( (BlockFlatColored) blk ).colorFromState( ModUtil.getStateFromMeta( blk, stack ) );
			}
		}, BlockFlatColored.getAllBlocks().toArray( new Block[BlockFlatColored.getAllBlocks().size()] ) );

		Minecraft.getInstance().getBlockColors().register( new IBlockColor() {

			@Override
			public int getColor(
					final IBlockState state,
					final IWorldReaderBase p_186720_2_,
					final BlockPos pos,
					final int tintIndex )
			{
				return ( (BlockFlatColored) state.getBlock() ).colorFromState( state );
			}

		}, BlockFlatColored.getAllBlocks().toArray( new Block[BlockFlatColored.getAllBlocks().size()] ) );
	}

	public ResourceLocation getTextureName(
			final EnumFlatBlockType type,
			final int varient )
	{
		if ( !FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT && type == EnumFlatBlockType.GLOWING )
		{
			return new ResourceLocation( FlatColoredBlocks.MODID, getBaseTextureName( type ) + "_" + varient );
		}

		if ( type == EnumFlatBlockType.TRANSPARENT )
		{
			return new ResourceLocation( FlatColoredBlocks.MODID, getBaseTextureName( type ) + "_" + varient );
		}

		return new ResourceLocation( FlatColoredBlocks.MODID, getBaseTextureName( type ) );
	}

	public String getBaseTextureName(
			final EnumFlatBlockType type )
	{
		return FlatColoredBlocks.MODID + ":flatcoloredblock_" + getTextureFor( type );
	}

	public String getBaseTextureNameWithBlocks(
			final EnumFlatBlockType type )
	{
		return FlatColoredBlocks.MODID + ":blocks/flatcoloredblock_" + getTextureFor( type );
	}

	public ResourceLocation getTextureResourceLocation(
			final EnumFlatBlockType type )
	{
		return new ResourceLocation( FlatColoredBlocks.MODID, "textures/blocks/flatcoloredblock_" + getTextureFor( type ) + ".png" );
	}

	private String getTextureFor(
			final EnumFlatBlockType type )
	{
		switch ( type )
		{
			case GLOWING:
				return FlatColoredBlocks.instance.config.DISPLAY_TEXTURE_GLOWING.resourceName();
			case TRANSPARENT:
				return FlatColoredBlocks.instance.config.DISPLAY_TEXTURE_TRANSPARENT.resourceName();
			default:
				return FlatColoredBlocks.instance.config.DISPLAY_TEXTURE.resourceName();
		}
	}

}
