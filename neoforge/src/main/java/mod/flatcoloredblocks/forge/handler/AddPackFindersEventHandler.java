package mod.flatcoloredblocks.forge.handler;

import com.mojang.logging.LogUtils;
import mod.flatcoloredblocks.core.FlatColoredBlocks;
import mod.flatcoloredblocks.core.util.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddPackFindersEventHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        event.addRepositorySource(registrar -> {
            try {
                Path coreJarPath = Path.of(FlatColoredBlocks.class.getProtectionDomain().getCodeSource().getLocation().toURI());

                final PackResources packResources = new PathPackResources(
                        "flatcoloredblocks-core",
                        coreJarPath,
                        true
                );

                final Pack corePack = Pack.readMetaAndCreate(
                        "flatcoloredblocks-core",
                        Component.literal("FlatColoredBlocks Core"),
                        true,
                        new SinglePackResourceResourcesSupplier(packResources),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.BOTTOM,
                        PackSource.BUILT_IN
                );

                registrar.accept(corePack);
            } catch (URISyntaxException e) {
                LOGGER.error("Failed to inject Core Resource Pack. FCB Assets will not be loaded!", e);
            }
        });
    }

    private record SinglePackResourceResourcesSupplier(PackResources packResources) implements Pack.ResourcesSupplier {

        @Override
        public @NotNull PackResources openPrimary(@NotNull String s) {
            return packResources();
        }

        @Override
        public @NotNull PackResources openFull(@NotNull String s, Pack.@NotNull Info info) {
            return packResources();
        }
    }
}
