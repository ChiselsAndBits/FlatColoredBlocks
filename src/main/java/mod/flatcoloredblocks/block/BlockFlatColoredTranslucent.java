package mod.flatcoloredblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockFlatColoredTranslucent extends BlockFlatColored
{
	public BlockFlatColoredTranslucent(
			final int i,
			final int j,
			final int varientNum )
	{
		super( i, j, varientNum );

		// Its still a full block.. even if its not a opaque cube
		// C&B requires this.
		// fullBlock = true; -- cannot set this.
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public boolean isSideInvisible(
			IBlockState state,
			IBlockState adjacentBlockState,
			EnumFacing side )
	{
		final Block block = adjacentBlockState.getBlock();

		if ( block instanceof BlockFlatColoredTranslucent )
		{
			return false;
		}

		return super.isSideInvisible( state, adjacentBlockState, side );
	}

	@Override
	public boolean isFullCube(
			final IBlockState state )
	{
		return false;
	}

	@Override
	public boolean isNormalCube(
			IBlockState state )
	{
		return false;
	}

	@Override
	public float[] getBeaconColorMultiplier(
			IBlockState state,
			IWorldReader world,
			BlockPos pos,
			BlockPos beaconPos )
	{
		int o = ConversionHSV2RGB.toRGB( hsvFromState( state ) );
		return new float[] { byteToFloat( ( o >> 16 ) & 0xff ), byteToFloat( ( o >> 8 ) & 0xff ), byteToFloat( ( o ) & 0xff ) };
	}

	private float byteToFloat(
			int i )
	{
		return Math.max( 0.0f, Math.min( 1.0f, (float) i / 255.0f ) );
	}
}
