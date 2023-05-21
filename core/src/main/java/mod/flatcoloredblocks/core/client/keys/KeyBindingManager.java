package mod.flatcoloredblocks.core.client.keys;

import com.communi.suggestu.scena.core.client.key.IKeyBindingManager;
import com.communi.suggestu.scena.core.client.key.KeyModifier;
import com.mojang.blaze3d.platform.InputConstants;
import mod.flatcoloredblocks.core.client.keys.context.HoldsWithColorItemInHandKeyConflictContext;
import mod.flatcoloredblocks.core.client.screen.ColorSelectionScreen;
import mod.flatcoloredblocks.core.item.IWithColorItem;
import mod.flatcoloredblocks.core.util.ItemStackUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class KeyBindingManager {
    private static final KeyBindingManager INSTANCE = new KeyBindingManager();
    private KeyMapping openSelectColorMenuKeyBinding = null;
    private boolean selectColorMenuKeyWasDown = false;
    private boolean initialized = false;

    private KeyBindingManager() {
    }

    public static KeyBindingManager getInstance() {
        return INSTANCE;
    }

    public void onClientConstruction() {
        IKeyBindingManager.getInstance().register(openSelectColorMenuKeyBinding =
                IKeyBindingManager.getInstance().createNew("key.flatcoloredblocks.select_color_menu",
                        HoldsWithColorItemInHandKeyConflictContext.getInstance(),
                        KeyModifier.SHIFT,
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_C,
                        "key_categories.flatcoloredblocks"));


        initialized = true;
    }

    public void handleKeyPresses() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.screen == null) {
            boolean toolMenuKeyIsDown = isOpenToolMenuKeyPressed();
            if (toolMenuKeyIsDown && !selectColorMenuKeyWasDown) {
                if (isOpenToolMenuKeyPressed()) {
                    if (mc.screen == null && mc.player != null && mc.player.isCreative()) {
                        ItemStack inHand = ItemStackUtils.getWithColorItemStackFromPlayer(mc.player);
                        if (!inHand.isEmpty() && inHand.getItem() instanceof IWithColorItem withColorItem) {
                            try {
                                mc.setScreen(ColorSelectionScreen.create(inHand, withColorItem));
                            } catch (ClassCastException ignored) {
                            }
                        }
                    }
                }
            }
            selectColorMenuKeyWasDown = toolMenuKeyIsDown;
        } else {
            selectColorMenuKeyWasDown = true;
        }
    }

    public boolean hasBeenInitialized() {
        return initialized;
    }

    public boolean isOpenToolMenuKeyPressed() {
        return isKeyDown(getOpenSelectColorMenuKeyBinding());
    }

    public boolean isKeyDown(KeyMapping keybinding) {
        if (keybinding.isUnbound()) {
            return false;
        }

        boolean isDown = switch (keybinding.key.getType()) {
            case KEYSYM ->
                    InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keybinding.key.getValue());
            case MOUSE ->
                    GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keybinding.key.getValue()) == GLFW.GLFW_PRESS;
            default -> false;
        };
        return (isDown || keybinding.isDown()) && IKeyBindingManager.getInstance().isKeyConflictOfActive(keybinding) &&
                IKeyBindingManager.getInstance().isKeyModifierActive(keybinding);
    }

    public KeyMapping getOpenSelectColorMenuKeyBinding() {
        if (openSelectColorMenuKeyBinding == null) {
            throw new IllegalStateException("Keybindings have not been initialized.");
        }

        return openSelectColorMenuKeyBinding;
    }
}
