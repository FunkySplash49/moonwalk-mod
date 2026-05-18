package com.moonwalk.mod.fabric;

import com.moonwalk.mod.MoonwalkModCommon;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class MoonwalkModFabric implements ModInitializer {

    public static final SoundEvent[] MJ_SOUNDS = new SoundEvent[MoonwalkModCommon.MJ_SOUND_COUNT];

    @Override
    public void onInitialize() {
        for (int i = 0; i < MoonwalkModCommon.MJ_SOUND_COUNT; i++) {
            Identifier id = Identifier.of(MoonwalkModCommon.MOD_ID, "mj" + (i + 1));
            MJ_SOUNDS[i] = SoundEvent.of(id);
            Registry.register(Registries.SOUND_EVENT, id, MJ_SOUNDS[i]);
        }
    }
}
