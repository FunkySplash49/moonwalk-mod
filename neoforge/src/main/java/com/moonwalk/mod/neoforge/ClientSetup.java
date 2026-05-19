package com.moonwalk.mod.neoforge;

import com.moonwalk.mod.MoonwalkModCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = MoonwalkModCommon.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) return;

        while (KeyBindings.MOONWALK_KEY.consumeClick()) {
            // Tell the server we moonwalked. Server handles sound, effect, damage, knockback.
            PacketDistributor.sendToServer(new MoonwalkMod.MoonwalkPayload());
        }
    }
}
