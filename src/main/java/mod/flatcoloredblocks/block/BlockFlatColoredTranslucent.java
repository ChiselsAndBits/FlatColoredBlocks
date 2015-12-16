package mod.flatcoloredblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFlatColoredTranslucent extends BlockFlatColored
{
	public BlockFlatColoredTranslucent(
			final int i,
			final int j )
	{
		super( i, j );

		// Its still a full block.. even if its not a opaque cube
		// C&B requires this.
		fullBlock = true;
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public boolean shouldSideBeRendered(
			final IBlockAccess worldIn,
			final BlockPos pos,
			final EnumFacing side )
	{
		final IBlockState iblockstate = worldIn.getBlockState( pos );
		final Block blk = iblockstate.getBlock();

		if ( blk instanceof BlockFlatColoredTranslucent )
		{
			return false;
		}

		return super.shouldSideBeRendered( worldIn, pos, side );
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

}
