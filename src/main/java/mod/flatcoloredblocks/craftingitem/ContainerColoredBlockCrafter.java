package mod.flatcoloredblocks.craftingitem;

import mod.flatcoloredblocks.ModUtil;
import mod.flatcoloredblocks.network.NetworkRouter;
import mod.flatcoloredblocks.network.packets.ScrolingGuiPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Container for for crafting item's gui, manages scroll communication slots,
 * and shift clicking.
 */
public class ContainerColoredBlockCrafter extends Container
{

	final EntityPlayer thePlayer;
	final InventoryColoredBlockCrafter craftinginv;

	public ContainerColoredBlockCrafter(
			final EntityPlayer player,
			final World world,
			final int x,
			final int y,
			final int z )
	{
		thePlayer = player;
		craftinginv = new InventoryColoredBlockCrafter( thePlayer, this );
		craftinginv.updateContents();

		final IInventory playerInventory = player.inventory;
		final int i = ( 7 - 4 ) * 18;

		for ( int j = 0; j < 7; ++j )
		{
			for ( int k = 0; k < 9; ++k )
			{
				addSlot( new SlotColoredBlockCrafter( craftinginv, craftinginv, k + j * 9, 8 + k * 18, 18 + j * 18 ) );
			}
		}

		for ( int l = 0; l < 3; ++l )
		{
			for ( int j1 = 0; j1 < 9; ++j1 )
			{
				addSlot( new SlotChangeDetect( playerInventory, craftinginv, j1 + l * 9 + 9, 8 + j1 * 18, 104 + l * 18 + i ) );
			}
		}

		for ( int i1 = 0; i1 < 9; ++i1 )
		{
			addSlot( new SlotChangeDetect( playerInventory, craftinginv, i1, 8 + i1 * 18, 162 + i ) );
		}
	}

	@Override
	public boolean canInteractWith(
			final EntityPlayer playerIn )
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(
			final EntityPlayer playerIn,
			final int index )
	{
		int emptySlots = 0;

		for ( final Slot s : inventorySlots )
		{
			if ( !( s instanceof SlotColoredBlockCrafter ) )
			{
				if ( !s.getHasStack() )
				{
					emptySlots++;
				}
			}
		}

		if ( emptySlots > 0 )
		{
			final Slot s = inventorySlots.get( index );
			if ( s instanceof SlotColoredBlockCrafter )
			{
				final ItemStack which = s.getStack();
				final ItemStack out = craftinginv.craftItem( which, 64, false );

				mergeItemStack( out, 7 * 9, inventorySlots.size(), true );
			}
		}

		return ModUtil.getEmptyStack();
	}

	float scrollPercent = 0;
	float originalScroll = 0;

	public void setScroll(
			final float currentScroll )
	{
		scrollPercent = currentScroll;

		final int rowsOfScrolling = Math.max( ( craftinginv.getSizeInventory() + 8 ) / 9 - 7, 0 );
		craftinginv.offset = Math.round( rowsOfScrolling * currentScroll ) * 9;

		if ( Math.abs( originalScroll - currentScroll ) > 0.00001 )
		{
			if ( thePlayer.world.isRemote )
			{
				final ScrolingGuiPacket sgp = new ScrolingGuiPacket();
				originalScroll = sgp.scroll = scrollPercent;

				// send...
				NetworkRouter.instance.sendToServer( sgp );
			}
		}
	}

	public int getItemCount()
	{
		return craftinginv.getSizeInventory();
	}

	@OnlyIn( Dist.CLIENT )
	public static Object getGuiClass()
	{
		return GuiColoredBlockCrafter.class;
	}

}
