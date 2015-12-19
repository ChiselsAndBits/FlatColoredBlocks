
package mod.flatcoloredblocks.model;

import java.util.ArrayList;
import java.util.HashMap;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelGenerator implements ICustomModelLoader
{

	private static final String NORMAL_VARIENT = "normal";
	private static final String INVENTORY_VARIENT = "inventory";

	HashMap<ResourceLocation, BakedVarientModel> models = new HashMap<ResourceLocation, BakedVarientModel>();
	ArrayList<ModelResourceLocation> res = new ArrayList<ModelResourceLocation>();

	public void preinit()
	{
		if ( !FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT )
		{
			for ( final int varient : FlatColoredBlocks.instance.glowing.shadeConvertVariant )
			{
				final String name = ClientSide.instance.getTextureName( EnumFlatBlockType.GLOWING, varient );

				final BakedVarientModel var = new BakedVarientModel( EnumFlatBlockType.GLOWING, varient );
				add( new ResourceLocation( name ), var );
			}
		}

		for ( final int varient : FlatColoredBlocks.instance.transparent.shadeConvertVariant )
		{
			final String name = ClientSide.instance.getTextureName( EnumFlatBlockType.TRANSPARENT, varient );

			final BakedVarientModel var = new BakedVarientModel( EnumFlatBlockType.TRANSPARENT, varient );
			add( new ResourceLocation( name ), var );
		}

		MinecraftForge.EVENT_BUS.register( this );
	}

	private void add(
			final ResourceLocation modelLocation,
			final BakedVarientModel clz )
	{
		res.add( new ModelResourceLocation( modelLocation, INVENTORY_VARIENT ) );
		models.put( new ModelResourceLocation( modelLocation, INVENTORY_VARIENT ), clz );

		res.add( new ModelResourceLocation( modelLocation, NORMAL_VARIENT ) );
		models.put( new ModelResourceLocation( modelLocation, NORMAL_VARIENT ), clz );
	}

	@SubscribeEvent
	public void onModelBakeEvent(
			final ModelBakeEvent event )
	{
		for ( final ModelResourceLocation rl : res )
		{
			final VertexFormat format = rl.getVariant().equals( NORMAL_VARIENT ) ? DefaultVertexFormats.BLOCK : DefaultVertexFormats.ITEM;
			event.modelRegistry.putObject( rl, getModel( rl ).bake( null, format, null ) );
		}
	}

	@Override
	public void onResourceManagerReload(
			final IResourceManager resourceManager )
	{
	}

	@Override
	public boolean accepts(
			final ResourceLocation modelLocation )
	{
		return models.containsKey( modelLocation );
	}

	@Override
	public IModel loadModel(
			final ResourceLocation modelLocation )
	{
		return getModel( modelLocation );
	}

	private BakedVarientModel getModel(
			final ResourceLocation modelLocation )
	{
		try
		{
			return models.get( modelLocation );
		}
		catch ( final Exception e )
		{
			throw new RuntimeException( "The Model: " + e.toString() + " was not available was requested.", e );
		}
	}

}
