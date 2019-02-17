package mod.flatcoloredblocks;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import mod.flatcoloredblocks.block.BlockFlatColored;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CreativeTab extends ItemGroup
{

	public CreativeTab()
	{
		super( "FlatColoredBlocks" );
	}

	private int listOffset = 0;
	private Stopwatch offsetTimer;
	private NonNullList<ItemStack> list;

	public ItemStack createIcon()
	{
		if ( list == null )
		{
			initalizeList();
		}

		// check timer, and if 0.7 seconds has elapsed, step it forwards ( and
		// keep it under list.size )
		if ( offsetTimer.elapsed( TimeUnit.MILLISECONDS ) > 700 )
		{
			listOffset = ++listOffset % list.size();
			offsetTimer = Stopwatch.createStarted();
		}

		return list.get( listOffset );
	}

	// initialize cycling icon.
	private void initalizeList()
	{
		offsetTimer = Stopwatch.createStarted();
		list = NonNullList.create();
		fill( list );

		for ( int x = 0; x < list.size(); ++x )
		{
			final ItemStack is = list.get( x );

			if ( !( is.getItem() instanceof ItemBlock ) )
			{
				list.remove( x );
				--x;
				continue;
			}

			final ItemBlock ib = (ItemBlock) is.getItem();
			final Block b = ib.getBlock();

			// Remove other peoples stuff list..
			if ( !( b instanceof BlockFlatColored ) )
			{
				list.remove( x );
				--x;
				continue;
			}

			final int out = ( (BlockFlatColored) b ).hsvFromState( ModUtil.getStateFromMeta( b, is ) );

			final int s = out >> 8 & 0xff;
			final int v = out & 0xff;

			if ( s < 200 || v < 140 || v > 170 )
			{
				list.remove( x );
				--x;
			}
		}

		if ( list.isEmpty() )
		{
			fill( list );
		}
	}

}
