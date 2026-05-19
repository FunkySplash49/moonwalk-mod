package com.moonwalk.mod.fabric.client;

import com.moonwalk.mod.fabric.MoonwalkModFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MoonwalkModFabricClient implements ClientModInitializer {

    public static KeyBinding moonwalkKey;

    @Override
    public void onInitializeClient() {
        moonwalkKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.moonwalkmod.moonwalk",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "key.categories.moonwalkmod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (moonwalkKey.wasPressed()) {
                // Tell the server we moonwalked. Server handles sound, effect, damage, knockback.
                ClientPlayNetworking.send(new MoonwalkModFabric.MoonwalkPayload());
            }
        });
    }
}
