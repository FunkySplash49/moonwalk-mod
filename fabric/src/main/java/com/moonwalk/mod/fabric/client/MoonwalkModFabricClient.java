package com.moonwalk.mod.fabric.client;

import com.moonwalk.mod.MoonwalkModCommon;
import com.moonwalk.mod.fabric.MoonwalkModFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Random;

public class MoonwalkModFabricClient implements ClientModInitializer {

    private static final Random RANDOM = new Random();

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
            if (client.player == null || client.world == null) return;

            while (moonwalkKey.wasPressed()) {
                int idx = RANDOM.nextInt(MoonwalkModCommon.MJ_SOUND_COUNT);
                client.player.playSound(MoonwalkModFabric.MJ_SOUNDS[idx], 1.0f, 1.0f);
                client.player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOW_FALLING,
                    MoonwalkModCommon.MOONWALK_DURATION_TICKS, 0, false, true));
            }

            damageStaringMobs(client);
        });
    }

    private static void damageStaringMobs(MinecraftClient client) {
        Vec3d playerEye = client.player.getEyePos();
        List<LivingEntity> nearby = client.world.getEntitiesByClass(
            LivingEntity.class,
            client.player.getBoundingBox().expand(MoonwalkModCommon.STARE_RADIUS),
            e -> e != client.player && e.isAlive()
        );
        for (LivingEntity entity : nearby) {
            Vec3d look = entity.getRotationVec(1.0f).normalize();
            Vec3d toPlayer = playerEye.subtract(entity.getEyePos()).normalize();
            if (look.dotProduct(toPlayer) > MoonwalkModCommon.STARE_DOT_THRESHOLD) {
                entity.damage(entity.getDamageSources().magic(), MoonwalkModCommon.STARE_DAMAGE_PER_TICK);
            }
        }
    }
}
