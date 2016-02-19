package mod.flatcoloredblocks.craftingitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.gui.ModGuiTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemColoredBlockCrafter extends Item
{

	int scrollIndex=-1;
	List<ItemStack> options = new ArrayList<ItemStack>();
	Stopwatch stopWatch;
	
	public ItemColoredBlockCrafter()
	{
		setCreativeTab( FlatColoredBlocks.instance.creativeTab );
		setUnlocalizedName( "flatcoloredblocks.coloredcraftingitem" );
	}

	@Override
	public ItemStack onItemRightClick(
			final ItemStack itemStackIn,
			final World worldIn,
			final EntityPlayer playerIn )
	{
		if ( worldIn.isRemote )
		{
			return itemStackIn;
		}

		playerIn.openGui( FlatColoredBlocks.instance, ModGuiTypes.ColoredCrafter.ordinal(), worldIn, 0, 0, 0 );
		return itemStackIn;
	}

	@Override
	public void addInformation(
			final ItemStack stack,
			final EntityPlayer playerIn,
			final List<String> tooltip,
			final boolean advanced )
	{
		if ( scrollIndex == -1 )
		{
			scrollIndex = 0;
			stopWatch = Stopwatch.createStarted();
			
			options.clear();			
			options.addAll( OreDictionary.getOres( FlatColoredBlocks.instance.config.solidCraftingBlock ));
			options.addAll( OreDictionary.getOres( FlatColoredBlocks.instance.config.glowingCraftingBlock ));
			options.addAll( OreDictionary.getOres( FlatColoredBlocks.instance.config.transparentCraftingBlock ));
			
			// remove extra stuff...
			Iterator<ItemStack> i = options.iterator();
			while ( i.hasNext() )
			{
				ItemStack is = i.next();
				if ( is.getMetadata() == OreDictionary.WILDCARD_VALUE )
					i.remove();
			}
		}
		
		if ( ! options.isEmpty() )
		{
			if ( stopWatch.elapsed( TimeUnit.SECONDS ) >= 1.2 )
			{
				scrollIndex = ++scrollIndex % options.size();
				stopWatch = Stopwatch.createStarted();
			}
			
			String tip = StatCollector.translateToLocal( "item.flatcoloredblocks.coloredcraftingitem.tip1" );
			tip = tip.replace( "%%", options.get( scrollIndex ).getDisplayName() );
			tooltip.add( tip );
		}
		
		tooltip.add( StatCollector.translateToLocal( "item.flatcoloredblocks.coloredcraftingitem.tip2" ) );

		super.addInformation( stack, playerIn, tooltip, advanced );
	}

}
