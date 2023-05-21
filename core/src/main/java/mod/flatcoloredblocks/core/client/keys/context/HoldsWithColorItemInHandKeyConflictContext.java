package mod.flatcoloredblocks.core.client.keys.context;

import com.communi.suggestu.scena.core.client.key.IKeyConflictContext;
import mod.flatcoloredblocks.core.item.IWithColorItem;
import net.minecraft.client.Minecraft;

public final class HoldsWithColorItemInHandKeyConflictContext implements IKeyConflictContext
{
    private static final HoldsWithColorItemInHandKeyConflictContext INSTANCE = new HoldsWithColorItemInHandKeyConflictContext();

    public static HoldsWithColorItemInHandKeyConflictContext getInstance()
    {
        return INSTANCE;
    }

    private HoldsWithColorItemInHandKeyConflictContext()
    {
    }

    /**
     * @return true if conditions are met to activate keybindings with this context
     */
    @Override
    public boolean isActive()
    {
        return Minecraft.getInstance().player != null &&
                 (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof IWithColorItem ||
                    Minecraft.getInstance().player.getOffhandItem().getItem() instanceof IWithColorItem);
    }

    /**
     * @param other The other context.
     * @return true if the other context can have keybindings conflicts with this one. This will be called on both contexts to check for conflicts.
     */
    @Override
    public boolean conflicts(final IKeyConflictContext other)
    {
        return other == this;
    }
}
