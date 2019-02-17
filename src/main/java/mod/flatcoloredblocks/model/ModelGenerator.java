
package mod.flatcoloredblocks.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModelGenerator implements ICustomModelLoader
{

	private static final String NORMAL_VARIENT = "normal";
	private static final String INVENTORY_VARIENT = "inventory";
	private static VertexFormat FCB = new VertexFormat();

	static
	{
		for ( final VertexFormatElement e : DefaultVertexFormats.ITEM.getElements() )
		{
			FCB.addElement( e );
		}

		FCB.addElement( DefaultVertexFormats.TEX_2S );
	}

	HashMap<ResourceLocation, BakedVarientModel> models = new HashMap<ResourceLocation, BakedVarientModel>();
	ArrayList<ModelResourceLocation> res = new ArrayList<ModelResourceLocation>();

	public void preinit()
	{
		if ( !FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT )
		{
			for ( final int varient : FlatColoredBlocks.instance.glowing.shadeConvertVariant )
			{
				final ResourceLocation name = ClientSide.instance.getTextureName( EnumFlatBlockType.GLOWING, varient );

				final BakedVarientModel var = new BakedVarientModel( EnumFlatBlockType.GLOWING, varient );
				add( name, var );
			}
		}

		for ( final int varient : FlatColoredBlocks.instance.transparent.shadeConvertVariant )
		{
			final ResourceLocation name = ClientSide.instance.getTextureName( EnumFlatBlockType.TRANSPARENT, varient );

			final BakedVarientModel var = new BakedVarientModel( EnumFlatBlockType.TRANSPARENT, varient );
			add( name, var );
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
			final BakedVarientModel bvm = getModel( rl );
			final VertexFormat format = bvm.type == EnumFlatBlockType.GLOWING && rl.getVariant().equals( NORMAL_VARIENT ) ? FCB : DefaultVertexFormats.ITEM;
			event.getModelRegistry().put( rl, bvm.bake( null, null, null, false, format ) );
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
	public IUnbakedModel loadModel(
			final ResourceLocation modelLocation )
	{
		return new IUnbakedModel() {

			@Override
			public IBakedModel bake(
					Function<ResourceLocation, IUnbakedModel> modelGetter,
					Function<ResourceLocation, TextureAtlasSprite> spriteGetter,
					IModelState state,
					boolean uvlock,
					VertexFormat format )
			{
				return getModel( modelLocation ).bake( modelGetter, spriteGetter, state, uvlock, format );
			}

			@Override
			public Collection<ResourceLocation> getTextures(
					Function<ResourceLocation, IUnbakedModel> modelGetter,
					Set<String> missingTextureErrors )
			{
				return getModel( modelLocation ).getTextures( modelGetter, missingTextureErrors );
			}

			@Override
			public Collection<ResourceLocation> getOverrideLocations()
			{
				return getModel( modelLocation ).getOverrideLocations();
			}
		};
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
