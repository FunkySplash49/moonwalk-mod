package com.moonwalk.mod.neoforge;

import com.moonwalk.mod.MoonwalkModCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(MoonwalkModCommon.MOD_ID)
public class MoonwalkMod {

    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(Registries.SOUND_EVENT, MoonwalkModCommon.MOD_ID);

    @SuppressWarnings("unchecked")
    public static final DeferredHolder<SoundEvent, SoundEvent>[] MJ_SOUNDS =
        new DeferredHolder[MoonwalkModCommon.MJ_SOUND_COUNT];

    public MoonwalkMod(IEventBus modBus) {
        for (int i = 0; i < MoonwalkModCommon.MJ_SOUND_COUNT; i++) {
            final int idx = i + 1;
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                MoonwalkModCommon.MOD_ID, "mj" + idx);
            MJ_SOUNDS[i] = SOUNDS.register("mj" + idx, () -> SoundEvent.createVariableRangeEvent(id));
        }
        SOUNDS.register(modBus);
    }
}
