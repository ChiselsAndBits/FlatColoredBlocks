package mod.flatcoloredblocks.core.fluid;

import com.communi.suggestu.scena.core.fluid.FluidInformation;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
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

    public boolean isEmpty() {
        return amount <= 0 || unitContents == null;
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
            final Fluid fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(fluidName));
            final int amount = pTag.getInt("amount");

            final DataComponentPatch patch;
            if (pTag.contains("data")) {
                final CompoundTag data = pTag.getCompound("data");


                patch = DataComponentPatch.CODEC.parse(NbtOps.INSTANCE, data)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to parse data component patch"));
            } else {
                patch = DataComponentPatch.EMPTY;
            }


            setContents(new FluidInformation(fluid, amount, patch));
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
            pTag.putString("fluid", BuiltInRegistries.FLUID.getKey(getContents().get().fluid()).toString());
            pTag.putInt("amount", (int) getContents().get().amount());
            if (getContents().get().data() != null)
                pTag.put("data", DataComponentPatch.CODEC.encodeStart(NbtOps.INSTANCE, getContents().get().data()).result().orElseThrow());
        }
        return pTag;
    }
}
