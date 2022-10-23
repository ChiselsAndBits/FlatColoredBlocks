package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.entity.block.IBlockEntityManager;
import com.communi.suggestu.scena.core.registries.deferred.IRegistrar;
import com.communi.suggestu.scena.core.registries.deferred.IRegistryObject;
import mod.flatcoloredblocks.core.block.entity.ColoredBlockEntity;
import mod.flatcoloredblocks.core.block.entity.PaintBasinBlockEntity;
import mod.flatcoloredblocks.core.block.entity.PaintContainingBlockEntity;
import mod.flatcoloredblocks.core.block.entity.PaintMixerBlockEntity;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class BlockEntityTypes
{

    private static final Logger LOGGER    = LogManager.getLogger();
    private static final IRegistrar<BlockEntityType<?>> REGISTRAR = IRegistrar.create(Registry.BLOCK_ENTITY_TYPE_REGISTRY, Constants.MOD_ID);

    private BlockEntityTypes()
    {
        throw new IllegalStateException("Can not instantiate an instance of: BlockEntityTypes. This is a utility class");
    }

    public static void onModConstruction()
    {
        LOGGER.info("Loaded block entity configuration.");
    }

    public static final IRegistryObject<BlockEntityType<PaintContainingBlockEntity>> PAINT_MIXER = REGISTRAR.register(
      "paint_mixer",
      IBlockEntityManager.getInstance().createType(
              PaintMixerBlockEntity::new,
              builder -> builder.withValidBlock(Blocks.PAINT_MIXER::get)
      )
    );

    public static final IRegistryObject<BlockEntityType<PaintContainingBlockEntity>> PAINT_BASIN = REGISTRAR.register(
            "paint_basin",
            IBlockEntityManager.getInstance().createType(
                    PaintBasinBlockEntity::new,
                    builder -> builder.withValidBlock(Blocks.PAINT_BASIN::get)
            )
    );

    public static final IRegistryObject<BlockEntityType<ColoredBlockEntity>> COLORED_BLOCK = REGISTRAR.register(
            "colored_block",
            IBlockEntityManager.getInstance().createType(
                    ColoredBlockEntity::new,
                    builder -> builder.withValidBlock(Blocks.COLORED_CONCRETE::get)
                                       .withValidBlock(Blocks.COLORED_GLASS::get)
            )
    );
}
