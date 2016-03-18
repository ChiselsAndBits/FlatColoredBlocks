package mod.flatcoloredblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		fullBlock = true;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public boolean shouldSideBeRendered(
			final IBlockState blockState,
			final IBlockAccess blockAccess,
			final BlockPos pos,
			final EnumFacing side )
	{
		final IBlockState iblockstate = blockAccess.getBlockState( pos.offset( side ) );
		final Block block = iblockstate.getBlock();

		if ( block instanceof BlockFlatColoredTranslucent )
		{
			return false;
		}

		return super.shouldSideBeRendered( blockState, blockAccess, pos, side );
	}

	@Override
	public boolean isFullCube(
			final IBlockState state )
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(
			final IBlockState state )
	{
		return false;
	}

}
