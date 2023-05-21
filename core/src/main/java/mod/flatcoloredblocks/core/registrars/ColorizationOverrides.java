package mod.flatcoloredblocks.core.registrars;

import mod.flatcoloredblocks.core.block.ColoredBlock;
import mod.flatcoloredblocks.core.registry.ColorizationRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class ColorizationOverrides
{
    private static final Logger LOGGER    = LogManager.getLogger();

    private ColorizationOverrides()
    {
        throw new IllegalStateException("Can not instantiate an instance of: ColorizationOverrides. This is a utility class");
    }

    public static void onModConstruction()
    {
        LOGGER.info("Loaded colorization overrides configuration.");

        final Supplier<ColoredBlock> CONCRETE_SUPPLIER = Blocks.COLORED_CONCRETE::get;
        final Supplier<ColoredBlock> WOOL_SUPPLIER = Blocks.COLORED_WOOL::get;
        final Supplier<ColoredBlock> CARPET_SUPPLIER = Blocks.COLORED_WOOL_CARPET::get;
        final Supplier<ColoredBlock> STAINED_GLASS_SUPPLIER = Blocks.COLORED_GLASS::get;

        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.WHITE_CONCRETE, CONCRETE_SUPPLIER, 0xced3d4);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.ORANGE_CONCRETE, CONCRETE_SUPPLIER, 0xdf6000);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.MAGENTA_CONCRETE, CONCRETE_SUPPLIER, 0xa72f9d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_BLUE_CONCRETE, CONCRETE_SUPPLIER, 0x248ac8);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.YELLOW_CONCRETE, CONCRETE_SUPPLIER, 0xf2b015);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIME_CONCRETE, CONCRETE_SUPPLIER, 0x60aa1a);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PINK_CONCRETE, CONCRETE_SUPPLIER, 0xd66690);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GRAY_CONCRETE, CONCRETE_SUPPLIER, 0x373a3e);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_GRAY_CONCRETE, CONCRETE_SUPPLIER, 0x7a7d82);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.CYAN_CONCRETE, CONCRETE_SUPPLIER, 0x157788);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PURPLE_CONCRETE, CONCRETE_SUPPLIER, 0x65209d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLUE_CONCRETE, CONCRETE_SUPPLIER, 0x2d2f90);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BROWN_CONCRETE, CONCRETE_SUPPLIER, 0x613d21);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GREEN_CONCRETE, CONCRETE_SUPPLIER, 0x495b24);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.RED_CONCRETE, CONCRETE_SUPPLIER, 0x8f2222);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLACK_CONCRETE, CONCRETE_SUPPLIER, 0x090b10);

        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.WHITE_WOOL, WOOL_SUPPLIER, 0xced3d4);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.ORANGE_WOOL, WOOL_SUPPLIER, 0xdf6000);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.MAGENTA_WOOL, WOOL_SUPPLIER, 0xa72f9d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_BLUE_WOOL, WOOL_SUPPLIER, 0x248ac8);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.YELLOW_WOOL, WOOL_SUPPLIER, 0xf2b015);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIME_WOOL, WOOL_SUPPLIER, 0x60aa1a);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PINK_WOOL, WOOL_SUPPLIER, 0xd66690);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GRAY_WOOL, WOOL_SUPPLIER, 0x373a3e);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_GRAY_WOOL, WOOL_SUPPLIER, 0x7a7d82);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.CYAN_WOOL, WOOL_SUPPLIER, 0x157788);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PURPLE_WOOL, WOOL_SUPPLIER, 0x65209d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLUE_WOOL, WOOL_SUPPLIER, 0x2d2f90);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BROWN_WOOL, WOOL_SUPPLIER, 0x613d21);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GREEN_WOOL, WOOL_SUPPLIER, 0x495b24);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.RED_WOOL, WOOL_SUPPLIER, 0x8f2222);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLACK_WOOL, WOOL_SUPPLIER, 0x090b10);

        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.WHITE_CARPET, CARPET_SUPPLIER, 0xced3d4);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.ORANGE_CARPET, CARPET_SUPPLIER, 0xdf6000);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.MAGENTA_CARPET, CARPET_SUPPLIER, 0xa72f9d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_BLUE_CARPET, CARPET_SUPPLIER, 0x248ac8);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.YELLOW_CARPET, CARPET_SUPPLIER, 0xf2b015);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIME_CARPET, CARPET_SUPPLIER, 0x60aa1a);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PINK_CARPET, CARPET_SUPPLIER, 0xd66690);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GRAY_CARPET, CARPET_SUPPLIER, 0x373a3e);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_GRAY_CARPET, CARPET_SUPPLIER, 0x7a7d82);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.CYAN_CARPET, CARPET_SUPPLIER, 0x157788);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PURPLE_CARPET, CARPET_SUPPLIER, 0x65209d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLUE_CARPET, CARPET_SUPPLIER, 0x2d2f90);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BROWN_CARPET, CARPET_SUPPLIER, 0x613d21);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GREEN_CARPET, CARPET_SUPPLIER, 0x495b24);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.RED_CARPET, CARPET_SUPPLIER, 0x8f2222);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLACK_CARPET, CARPET_SUPPLIER, 0x090b10);

        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GLASS, STAINED_GLASS_SUPPLIER, 0xced3d4);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.WHITE_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0xced3d4);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.ORANGE_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0xdf6000);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.MAGENTA_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0xa72f9d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_BLUE_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x248ac8);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.YELLOW_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0xf2b015);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIME_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x60aa1a);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PINK_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0xd66690);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GRAY_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x373a3e);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.LIGHT_GRAY_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x7a7d82);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.CYAN_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x157788);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.PURPLE_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x65209d);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLUE_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x2d2f90);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BROWN_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x613d21);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.GREEN_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x495b24);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.RED_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x8f2222);
        ColorizationRegistry.getInstance().registerConversion(net.minecraft.world.level.block.Blocks.BLACK_STAINED_GLASS, STAINED_GLASS_SUPPLIER, 0x090b10);
    }
}
