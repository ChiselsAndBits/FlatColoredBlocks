package mod.flatcoloredblocks.craftingitem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotChangeDetect extends Slot
{

	InventoryColoredBlockCrafter secondInv;

	public SlotChangeDetect(
			final IInventory inv,
			final InventoryColoredBlockCrafter secondInv,
			final int index,
			final int xPosition,
			final int yPosition )
	{
		super( inv, index, xPosition, yPosition );
		this.secondInv = secondInv;
	}

	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
		secondInv.updateContents();
	}

}
