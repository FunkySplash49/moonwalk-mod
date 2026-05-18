package com.moonwalk.mod.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import com.moonwalk.mod.MoonwalkModCommon;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = MoonwalkModCommon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindings {

    public static final KeyMapping MOONWALK_KEY = new KeyMapping(
        "key.moonwalkmod.moonwalk",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_M,
        "key.categories.moonwalkmod"
    );

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(MOONWALK_KEY);
    }
}
