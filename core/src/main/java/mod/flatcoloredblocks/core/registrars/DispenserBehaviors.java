package mod.flatcoloredblocks.core.registrars;

import mod.flatcoloredblocks.core.dispensor.PaintBrushDispenseBehavior;
import mod.flatcoloredblocks.core.dispensor.PaintBucketDispenseBehavior;
import net.minecraft.world.level.block.DispenserBlock;

public final class DispenserBehaviors
{

    private DispenserBehaviors()
    {
        throw new IllegalStateException("Can not instantiate an instance of: DispenserBehaviors. This is a utility class");
    }

    public static void onModInitialization() {
        DispenserBlock.registerBehavior(Items.PAINT_BUCKET.get(), new PaintBucketDispenseBehavior());
        DispenserBlock.registerBehavior(Items.PAINT_BRUSH.get(), new PaintBrushDispenseBehavior());
    }
}
