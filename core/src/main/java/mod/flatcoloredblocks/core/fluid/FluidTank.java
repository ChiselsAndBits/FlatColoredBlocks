package mod.flatcoloredblocks.core.fluid;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.Validate;

import java.util.Optional;

public class FluidTank
{
    private final long maximalAmount;

    private FluidInformation unitContents;
    private long amount;

    public FluidTank(final long maximalAmount) {
        this.maximalAmount = maximalAmount;
    }

    public Optional<FluidInformation> getContents() {
        return Optional.ofNullable(unitContents)
                       .map(contents -> contents.withAmount(amount));
    }

    public void setContents(final FluidInformation contents) {
        Validate.notNull(contents);
        this.unitContents = contents.withAmount(1);
        this.amount = contents.amount();
    }

    public long getMaximalAmount()
    {
        return maximalAmount;
    }

    public void clear() {
        this.unitContents = null;
        this.amount = 0;
    }

    public void setAmount(final long amount) {
        this.amount = amount;
        if (this.amount <= 0)
            clear();
    }

    public long getAmount() {
        return amount;
    }

    public void readFromNBT(final CompoundTag pTag)
    {
        if (pTag.contains("fluid"))
        {
            final String fluidName = pTag.getString("fluid");
            final Fluid fluid = Registry.FLUID.get(new ResourceLocation(fluidName));
            final int amount = pTag.getInt("amount");
            final CompoundTag data = pTag.getCompound("data");

            setContents(new FluidInformation(fluid, amount, data));
        }
        else
        {
            clear();
        }
    }

    public CompoundTag writeToNBT(final CompoundTag pTag)
    {
        if (getContents().isPresent())
        {
            pTag.putString("fluid", Registry.FLUID.getKey(getContents().get().fluid()).toString());
            pTag.putInt("amount", (int) getContents().get().amount());
            pTag.put("data", getContents().get().data());
        }
        return pTag;
    }
}
