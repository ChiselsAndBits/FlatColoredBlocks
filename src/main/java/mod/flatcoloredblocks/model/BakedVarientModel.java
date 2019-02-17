
package mod.flatcoloredblocks.model;

import java.util.Collection;
import java.util.Collections;

import mod.flatcoloredblocks.block.EnumFlatBlockType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedVarientModel implements IModel
{
	private final int varient;
	public final EnumFlatBlockType type;

	public BakedVarientModel(
			final EnumFlatBlockType type,
			final int varient )
	{
		this.type = type;
		this.varient = varient;
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return Collections.emptyList();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return Collections.emptyList();
	}

	@Override
	public IBakedModel bake(
			IModelState state,
			VertexFormat format,
			java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter )
	{
		return new BakedVarientBlock( type, varient, format );
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}

}
