package mod.flatcoloredblocks.craftingitem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotColoredBlockCrafter extends Slot
{

	InventoryColoredBlockCrafter secondInv;

	public SlotColoredBlockCrafter(
			final IInventory inv,
			final InventoryColoredBlockCrafter secondInv,
			final int index,
			final int x,
			final int y )
	{
		super( inv, index, x, y );
		this.secondInv = secondInv;
	}

	@Override
	public boolean isItemValid(
			final ItemStack stack )
	{
		return false;
	}

	@Override
	public boolean canTakeStack(
			final EntityPlayer playerIn )
	{
		return secondInv.craftItem( getStack(), 1, true ) != null;
	}

	@Override
	public void onPickupFromSlot(
			final EntityPlayer playerIn,
			final ItemStack stack )
	{
		secondInv.craftItem( stack, 1, false );
	}

}
