package mod.flatcoloredblocks.core.client.registrars;

import com.communi.suggestu.scena.core.client.event.IClientEvents;
import mod.flatcoloredblocks.core.client.keys.KeyBindingManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class EventHandlers {

    private static final Logger LOGGER    = LogManager.getLogger();

    private EventHandlers() {
        throw new IllegalStateException("Can not instantiate an instance of: EventHandler. This is a utility class");
    }

    public static void onClientConstruction() {
        LOGGER.info("Registering Client Event Handlers");

        IClientEvents.getInstance().getClientTickStartedEvent().register(() -> {
            KeyBindingManager.getInstance().handleKeyPresses();
        });
    }
}
