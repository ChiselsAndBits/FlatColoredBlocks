package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.registries.deferred.IRegistrar;
import com.communi.suggestu.scena.core.registries.deferred.IRegistryObject;
import mod.flatcoloredblocks.core.block.ColoredConcreteBlock;
import mod.flatcoloredblocks.core.block.ColoredGlassBlock;
import mod.flatcoloredblocks.core.block.PaintBasinBlock;
import mod.flatcoloredblocks.core.block.PaintMixerBlock;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Blocks
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Blocks.class);
    private static final IRegistrar<Block> BLOCK_REGISTRAR = IRegistrar.create(Registry.BLOCK_REGISTRY, Constants.MOD_ID);

    public static final IRegistryObject<PaintMixerBlock> PAINT_MIXER = BLOCK_REGISTRAR.register("paint_mixer", () -> new PaintMixerBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final IRegistryObject<PaintBasinBlock> PAINT_BASIN = BLOCK_REGISTRAR.register("paint_basin", () -> new PaintBasinBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final IRegistryObject<ColoredConcreteBlock> COLORED_CONCRETE = BLOCK_REGISTRAR.register("colored_concrete", () -> new ColoredConcreteBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.8F)));
    public static final IRegistryObject<ColoredGlassBlock> COLORED_GLASS = BLOCK_REGISTRAR.register("colored_glass", () -> new ColoredGlassBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));

    private Blocks()
    {
        throw new IllegalStateException("Can not instantiate an instance of: Blocks. This is a utility class");
    }

    public static void onModConstruction()
    {
        LOGGER.info("Registering blocks");
    }

    private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
}
