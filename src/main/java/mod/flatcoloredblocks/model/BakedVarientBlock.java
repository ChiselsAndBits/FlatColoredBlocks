
package mod.flatcoloredblocks.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad.Builder;

@SuppressWarnings( "deprecation" )
public class BakedVarientBlock implements IFlexibleBakedModel, IPerspectiveAwareModel
{

	private static final Matrix4f identity;
	private static final Matrix4f thirdPerson;

	@SuppressWarnings( "unchecked" )
	final List<BakedQuad>[] face = new List[6];

	final TextureAtlasSprite texture;

	public BakedVarientBlock(
			final EnumFlatBlockType type,
			final int varient,
			final VertexFormat format )
	{
		// create lists...
		face[0] = new ArrayList<BakedQuad>();
		face[1] = new ArrayList<BakedQuad>();
		face[2] = new ArrayList<BakedQuad>();
		face[3] = new ArrayList<BakedQuad>();
		face[4] = new ArrayList<BakedQuad>();
		face[5] = new ArrayList<BakedQuad>();

		final float[] afloat = new float[] { 0, 0, 16, 16 };
		final BlockFaceUV uv = new BlockFaceUV( afloat, 0 );
		final FaceBakery faceBakery = new FaceBakery();

		if ( type == EnumFlatBlockType.TRANSPARENT )
		{
			texture = ClientSide.instance.textureGenerator.getTransparentTexture( varient );
		}
		else
		{
			texture = ClientSide.instance.textureGenerator.getGlowingTexture( varient );
		}

		final Vector3f to = new Vector3f( 0.0f, 0.0f, 0.0f );
		final Vector3f from = new Vector3f( 16.0f, 16.0f, 16.0f );

		final BlockPartRotation bpr = null;
		final ModelRotation mr = ModelRotation.X0_Y0;

		final float maxLightmap = 32.0f / 0xffff;
		int lightValue = 0;

		if ( !FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT && type == EnumFlatBlockType.GLOWING )
		{
			lightValue = varient * 15 / 255;
		}

		final float lightMap = maxLightmap * Math.max( 0, Math.min( 15, lightValue ) );

		for ( final EnumFacing side : EnumFacing.VALUES )
		{
			final BlockPartFace bpf = new BlockPartFace( side, 1, "", uv );

			Vector3f toB, fromB;

			switch ( side )
			{
				case UP:
					toB = new Vector3f( to.x, from.y, to.z );
					fromB = new Vector3f( from.x, from.y, from.z );
					break;
				case EAST:
					toB = new Vector3f( from.x, to.y, to.z );
					fromB = new Vector3f( from.x, from.y, from.z );
					break;
				case NORTH:
					toB = new Vector3f( to.x, to.y, to.z );
					fromB = new Vector3f( from.x, from.y, to.z );
					break;
				case SOUTH:
					toB = new Vector3f( to.x, to.y, from.z );
					fromB = new Vector3f( from.x, from.y, from.z );
					break;
				case DOWN:
					toB = new Vector3f( to.x, to.y, to.z );
					fromB = new Vector3f( from.x, to.y, from.z );
					break;
				case WEST:
					toB = new Vector3f( to.x, to.y, to.z );
					fromB = new Vector3f( to.x, from.y, from.z );
					break;
				default:
					throw new NullPointerException();
			}

			final BakedQuad g = faceBakery.makeBakedQuad( toB, fromB, bpf, texture, side, mr, bpr, false, true );
			face[side.ordinal()].add( finishFace( g, side, format, lightMap ) );
		}
	}

	private BakedQuad finishFace(
			final BakedQuad g,
			final EnumFacing myFace,
			final VertexFormat format,
			final float lightMap )
	{
		final int[] vertData = g.getVertexData();
		final int wrapAt = vertData.length / 4;

		final UnpackedBakedQuad.Builder b = new Builder( format );
		b.setQuadOrientation( myFace );
		b.setQuadTint( 1 );

		for ( int vertNum = 0; vertNum < 4; vertNum++ )
		{
			for ( int elementIndex = 0; elementIndex < format.getElementCount(); elementIndex++ )
			{
				final VertexFormatElement element = format.getElement( elementIndex );
				switch ( element.getUsage() )
				{
					case POSITION:
						b.put( elementIndex, Float.intBitsToFloat( vertData[0 + wrapAt * vertNum] ), Float.intBitsToFloat( vertData[1 + wrapAt * vertNum] ), Float.intBitsToFloat( vertData[2 + wrapAt * vertNum] ) );
						break;

					case COLOR:
						final float light = LightUtil.diffuseLight( myFace );
						b.put( elementIndex, light, light, light, 1f );
						break;

					case NORMAL:
						b.put( elementIndex, myFace.getFrontOffsetX(), myFace.getFrontOffsetY(), myFace.getFrontOffsetZ() );
						break;

					case UV:

						if ( element.getIndex() == 1 )
						{
							b.put( elementIndex, lightMap, lightMap );
						}
						else
						{
							final float u = Float.intBitsToFloat( vertData[4 + wrapAt * vertNum] );
							final float v = Float.intBitsToFloat( vertData[5 + wrapAt * vertNum] );
							b.put( elementIndex, u, v );
						}

						break;

					default:
						b.put( elementIndex );
						break;
				}
			}
		}

		return b.build();
	}

	@Override
	public List<BakedQuad> getFaceQuads(
			final EnumFacing side )
	{
		return face[side.ordinal()];
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public VertexFormat getFormat()
	{
		return null;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return texture;
	}

	static
	{/*
		 * "rotation": [ 10, -45, 170 ],
		 *
		 * "translation": [ 0, 1.5, -2.75 ]
		 *
		 * "scale": [ 0.375, 0.375, 0.375 ] } }
		 */
		final javax.vecmath.Vector3f translation = new javax.vecmath.Vector3f( 0, 1.5f / 16.0f, -2.75f / 16.0f );
		final javax.vecmath.Vector3f scale = new javax.vecmath.Vector3f( 0.375f, 0.375f, 0.375f );
		final Quat4f rotation = TRSRTransformation.quatFromYXZDegrees( new javax.vecmath.Vector3f( 10.0f, -45.0f, 170.0f ) );

		final TRSRTransformation transform = new TRSRTransformation( translation, rotation, scale, null );
		thirdPerson = transform.getMatrix();

		// yerp.
		identity = new Matrix4f();
		identity.setIdentity();
	}

	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(
			final TransformType cameraTransformType )
	{
		if ( cameraTransformType == TransformType.THIRD_PERSON )
		{
			return new ImmutablePair<IFlexibleBakedModel, Matrix4f>( this, thirdPerson );
		}

		return new ImmutablePair<IFlexibleBakedModel, Matrix4f>( this, identity );
	}
}
