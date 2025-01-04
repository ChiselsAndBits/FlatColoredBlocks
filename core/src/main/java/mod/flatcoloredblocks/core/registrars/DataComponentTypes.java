package mod.flatcoloredblocks.core.registrars;

import com.communi.suggestu.scena.core.registries.deferred.IRegistrar;
import com.communi.suggestu.scena.core.registries.deferred.IRegistryObject;
import com.mojang.serialization.Codec;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataComponentTypes {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final IRegistrar<DataComponentType<?>> REGISTRAR = IRegistrar.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    private DataComponentTypes() {
        throw new IllegalStateException("Can not instantiate an instance of: DataComponentTypes. This is a utility class");
    }

    public static void onModConstruction() {
        LOGGER.info("Loaded data component type configuration.");
    }

    public static IRegistryObject<DataComponentType<Integer>> COLOR = REGISTRAR.register(
            "color",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static IRegistryObject<DataComponentType<Integer>> AMOUNT = REGISTRAR.register(
            "amount",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build()
    );
}
