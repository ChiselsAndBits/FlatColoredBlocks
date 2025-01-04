package mod.flatcoloredblocks.forge.handler;

import com.mojang.logging.LogUtils;
import mod.flatcoloredblocks.core.FlatColoredBlocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AddPackFindersEventHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        event.addRepositorySource(registrar -> {
            try {
                Path coreJarPath = Path.of(FlatColoredBlocks.class.getProtectionDomain().getCodeSource().getLocation().toURI());

                final PackLocationInfo packLocationInfo = new PackLocationInfo(
                        "flatcoloredblocks-core",
                        Component.literal("Flat Colored Blocks - Core"),
                        PackSource.BUILT_IN,
                        Optional.of(
                                new KnownPack(Constants.MOD_ID, "core", ModList.get().getModFileById(Constants.MOD_ID).versionString())
                        )
                );

                final PackResources packResources = new PathPackResources(
                        packLocationInfo,
                        coreJarPath
                );

                final Pack pack = Pack.readMetaAndCreate(
                        packLocationInfo,
                        new SinglePackResourceResourcesSupplier(packResources),
                        event.getPackType(),
                        new PackSelectionConfig(true, Pack.Position.TOP, true)
                );

                registrar.accept(pack);
            } catch (URISyntaxException e) {
                LOGGER.error("Failed to inject Core Resource Pack. FCB Assets will not be loaded!", e);
            }
        });
    }

    private record SinglePackResourceResourcesSupplier(PackResources packResources) implements Pack.ResourcesSupplier {

        @Override
        public @NotNull PackResources openPrimary(@NotNull PackLocationInfo packLocationInfo) {
            return packResources();
        }

        @Override
        public @NotNull PackResources openFull(@NotNull PackLocationInfo packLocationInfo, Pack.@NotNull Metadata metadata) {
            return packResources();
        }
    }
}
