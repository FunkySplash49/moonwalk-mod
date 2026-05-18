package com.moonwalk.mod.neoforge;

import com.moonwalk.mod.MoonwalkModCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = MoonwalkModCommon.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) return;

        while (KeyBindings.MOONWALK_KEY.consumeClick()) {
            int idx = RANDOM.nextInt(MoonwalkModCommon.MJ_SOUND_COUNT);
            player.playSound(MoonwalkMod.MJ_SOUNDS[idx].get(), 1.0f, 1.0f);
            player.addEffect(new MobEffectInstance(
                MobEffects.SLOW_FALLING,
                MoonwalkModCommon.MOONWALK_DURATION_TICKS, 0, false, true));
        }

        Vec3 playerPos = player.position().add(0, player.getEyeHeight(), 0);
        List<LivingEntity> nearby = mc.level.getEntitiesOfClass(
            LivingEntity.class,
            player.getBoundingBox().inflate(MoonwalkModCommon.STARE_RADIUS),
            e -> e != player && e.isAlive()
        );

        for (LivingEntity entity : nearby) {
            Vec3 look = entity.getLookAngle().normalize();
            Vec3 toPlayer = playerPos.subtract(entity.getEyePosition()).normalize();
            if (look.dot(toPlayer) > MoonwalkModCommon.STARE_DOT_THRESHOLD) {
                entity.hurt(entity.damageSources().magic(), MoonwalkModCommon.STARE_DAMAGE_PER_TICK);
            }
        }
    }
}
