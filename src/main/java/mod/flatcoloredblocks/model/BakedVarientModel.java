
package mod.flatcoloredblocks.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import mod.flatcoloredblocks.block.EnumFlatBlockType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
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
	public IBakedModel bake(
			Function modelGetter,
			Function spriteGetter,
			IModelState state,
			boolean uvlock,
			VertexFormat format )
	{
		return new BakedVarientBlock( type, varient, format );
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}

	public Collection<ResourceLocation> getOverrideLocations()
	{
		return Collections.emptyList();
	}

	public Collection<ResourceLocation> getTextures(
			Function<ResourceLocation, IUnbakedModel> modelGetter,
			Set<String> missingTextureErrors )
	{
		return Collections.emptyList();
	}

}
