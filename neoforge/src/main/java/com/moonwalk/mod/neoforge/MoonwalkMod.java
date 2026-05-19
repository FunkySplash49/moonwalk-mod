package com.moonwalk.mod.neoforge;

import com.moonwalk.mod.MoonwalkModCommon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(MoonwalkModCommon.MOD_ID)
public class MoonwalkMod {

    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(Registries.SOUND_EVENT, MoonwalkModCommon.MOD_ID);

    @SuppressWarnings("unchecked")
    public static final DeferredHolder<SoundEvent, SoundEvent>[] MJ_SOUNDS =
        new DeferredHolder[MoonwalkModCommon.MJ_SOUND_COUNT];

    public static final ResourceLocation PACKET_ID =
        ResourceLocation.fromNamespaceAndPath(MoonwalkModCommon.MOD_ID, MoonwalkModCommon.MOONWALK_PACKET_ID);

    /** Empty C2S payload: client tells server "I pressed moonwalk". */
    public record MoonwalkPayload() implements CustomPacketPayload {
        public static final Type<MoonwalkPayload> TYPE = new Type<>(PACKET_ID);
        public static final StreamCodec<ByteBuf, MoonwalkPayload> CODEC =
            StreamCodec.unit(new MoonwalkPayload());
        @Override public Type<MoonwalkPayload> type() { return TYPE; }
    }

    public MoonwalkMod(IEventBus modBus) {
        for (int i = 0; i < MoonwalkModCommon.MJ_SOUND_COUNT; i++) {
            final int idx = i + 1;
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                MoonwalkModCommon.MOD_ID, "mj" + idx);
            MJ_SOUNDS[i] = SOUNDS.register("mj" + idx, () -> SoundEvent.createVariableRangeEvent(id));
        }
        SOUNDS.register(modBus);
        modBus.addListener(MoonwalkMod::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
            MoonwalkPayload.TYPE,
            MoonwalkPayload.CODEC,
            (payload, context) -> context.enqueueWork(() -> {
                if (context.player() instanceof net.minecraft.server.level.ServerPlayer sp) {
                    ServerMoonwalkHandler.onMoonwalkTriggered(sp);
                }
            })
        );
    }
}
