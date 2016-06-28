
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad.Builder;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedVarientBlock implements IBakedModel, IPerspectiveAwareModel
{

	private static final Matrix4f ground;
	private static final Matrix4f gui;
	private static final Matrix4f fixed;
	private static final Matrix4f firstPerson_righthand;
	private static final Matrix4f firstPerson_lefthand;
	private static final Matrix4f thirdPerson_righthand;
	private static final Matrix4f thirdPerson_lefthand;

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
		b.setTexture( g.getSprite() );

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

	public List<BakedQuad>[] getFace()
	{
		return face;
	}

	@Override
	public List<BakedQuad> getQuads(
			final IBlockState state,
			final EnumFacing side,
			final long rand )
	{
		if ( side == null )
		{
			return Collections.emptyList();
		}

		return face[side.ordinal()];
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
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return texture;
	}

	static
	{
		// for some reason these are not identical to vanilla's Block.json, I
		// don't know why.. but its close.

		{
			final javax.vecmath.Vector3f translation = new javax.vecmath.Vector3f( 0, 0, 0 );
			final javax.vecmath.Vector3f scale = new javax.vecmath.Vector3f( 0.625f, 0.625f, 0.625f );
			final Quat4f rotation = TRSRTransformation.quatFromXYZDegrees( new javax.vecmath.Vector3f( 30, 225, 0 ) );

			final TRSRTransformation transform = new TRSRTransformation( translation, rotation, scale, null );
			gui = transform.getMatrix();
		}

		{
			final javax.vecmath.Vector3f translation = new javax.vecmath.Vector3f( 0, 0, 0 );
			final javax.vecmath.Vector3f scale = new javax.vecmath.Vector3f( 0.25f, 0.25f, 0.25f );
			final Quat4f rotation = TRSRTransformation.quatFromXYZDegrees( new javax.vecmath.Vector3f( 0, 0, 0 ) );

			final TRSRTransformation transform = new TRSRTransformation( translation, rotation, scale, null );
			ground = transform.getMatrix();
		}

		{
			final javax.vecmath.Vector3f translation = new javax.vecmath.Vector3f( 0, 0, 0 );
			final javax.vecmath.Vector3f scale = new javax.vecmath.Vector3f( 0.5f, 0.5f, 0.5f );
			final Quat4f rotation = TRSRTransformation.quatFromXYZDegrees( new javax.vecmath.Vector3f( 0, 0, 0 ) );

			final TRSRTransformation transform = new TRSRTransformation( translation, rotation, scale, null );
			fixed = transform.getMatrix();
		}

		{
			final javax.vecmath.Vector3f translation = new javax.vecmath.Vector3f( 0, 0, 0 );
			final javax.vecmath.Vector3f scale = new javax.vecmath.Vector3f( 0.375f, 0.375f, 0.375f );
			final Quat4f rotation = TRSRTransformation.quatFromXYZDegrees( new javax.vecmath.Vector3f( 75, 45, 0 ) );

			final TRSRTransformation transform = new TRSRTransformation( translation, rotation, scale, null );
			thirdPerson_lefthand = thirdPerson_righthand = transform.getMatrix();
		}

		{
			final javax.vecmath.Vector3f translation = new javax.vecmath.Vector3f( 0, 0, 0 );
			final javax.vecmath.Vector3f scale = new javax.vecmath.Vector3f( 0.40f, 0.40f, 0.40f );
			final Quat4f rotation = TRSRTransformation.quatFromXYZDegrees( new javax.vecmath.Vector3f( 0, 45, 0 ) );

			final TRSRTransformation transform = new TRSRTransformation( translation, rotation, scale, null );
			firstPerson_righthand = transform.getMatrix();
		}

		{
			final javax.vecmath.Vector3f translation = new javax.vecmath.Vector3f( 0, 0, 0 );
			final javax.vecmath.Vector3f scale = new javax.vecmath.Vector3f( 0.40f, 0.40f, 0.40f );
			final Quat4f rotation = TRSRTransformation.quatFromXYZDegrees( new javax.vecmath.Vector3f( 0, 225, 0 ) );

			final TRSRTransformation transform = new TRSRTransformation( translation, rotation, scale, null );
			firstPerson_lefthand = transform.getMatrix();
		}
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
			final TransformType cameraTransformType )
	{
		switch ( cameraTransformType )
		{
			case FIRST_PERSON_LEFT_HAND:
				return new ImmutablePair<IBakedModel, Matrix4f>( this, firstPerson_lefthand );
			case FIRST_PERSON_RIGHT_HAND:
				return new ImmutablePair<IBakedModel, Matrix4f>( this, firstPerson_righthand );
			case THIRD_PERSON_LEFT_HAND:
				return new ImmutablePair<IBakedModel, Matrix4f>( this, thirdPerson_lefthand );
			case THIRD_PERSON_RIGHT_HAND:
				return new ImmutablePair<IBakedModel, Matrix4f>( this, thirdPerson_righthand );
			case FIXED:
				return new ImmutablePair<IBakedModel, Matrix4f>( this, fixed );
			case GROUND:
				return new ImmutablePair<IBakedModel, Matrix4f>( this, ground );
			case GUI:
				return new ImmutablePair<IBakedModel, Matrix4f>( this, gui );
			default:
				break;
		}

		return new ImmutablePair<IBakedModel, Matrix4f>( this, fixed );
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}
}
