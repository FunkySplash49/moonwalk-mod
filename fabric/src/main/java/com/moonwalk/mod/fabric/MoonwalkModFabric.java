package com.moonwalk.mod.fabric;

import com.moonwalk.mod.MoonwalkModCommon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class MoonwalkModFabric implements ModInitializer {

    public static final SoundEvent[] MJ_SOUNDS = new SoundEvent[MoonwalkModCommon.MJ_SOUND_COUNT];

    public static final Identifier PACKET_ID =
        Identifier.of(MoonwalkModCommon.MOD_ID, MoonwalkModCommon.MOONWALK_PACKET_ID);

    /** Empty C2S payload: client tells server "I pressed moonwalk". */
    public record MoonwalkPayload() implements CustomPayload {
        public static final CustomPayload.Id<MoonwalkPayload> ID = new CustomPayload.Id<>(PACKET_ID);
        public static final PacketCodec<PacketByteBuf, MoonwalkPayload> CODEC =
            PacketCodec.unit(new MoonwalkPayload());
        @Override public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    @Override
    public void onInitialize() {
        for (int i = 0; i < MoonwalkModCommon.MJ_SOUND_COUNT; i++) {
            Identifier id = Identifier.of(MoonwalkModCommon.MOD_ID, "mj" + (i + 1));
            MJ_SOUNDS[i] = SoundEvent.of(id);
            Registry.register(Registries.SOUND_EVENT, id, MJ_SOUNDS[i]);
        }

        PayloadTypeRegistry.playC2S().register(MoonwalkPayload.ID, MoonwalkPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MoonwalkPayload.ID, (payload, context) -> {
            context.server().execute(() -> ServerMoonwalkHandler.onMoonwalkTriggered(context.player()));
        });

        ServerTickEvents.END_SERVER_TICK.register(ServerMoonwalkHandler::onServerTick);
    }
}
