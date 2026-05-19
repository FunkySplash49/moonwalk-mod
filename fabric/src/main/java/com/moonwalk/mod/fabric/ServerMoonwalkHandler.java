package com.moonwalk.mod.fabric;

import com.moonwalk.mod.MoonwalkModCommon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Server-side moonwalk logic for Fabric.
 * Mirrors the NeoForge ServerMoonwalkHandler.
 */
public class ServerMoonwalkHandler {

    private static final Random RANDOM = new Random();
    private static final Map<UUID, Integer> ACTIVE = new HashMap<>();
    private static int tickCounter = 0;

    public static void onMoonwalkTriggered(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        int idx = RANDOM.nextInt(MoonwalkModCommon.MJ_SOUND_COUNT);
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
            MoonwalkModFabric.MJ_SOUNDS[idx], SoundCategory.PLAYERS, 1.0f, 1.0f);

        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.SLOW_FALLING,
            MoonwalkModCommon.MOONWALK_DURATION_TICKS, 0, false, true));

        ACTIVE.put(player.getUuid(), MoonwalkModCommon.MOONWALK_DURATION_TICKS);
    }

    public static void onServerTick(MinecraftServer server) {
        if (ACTIVE.isEmpty()) return;
        tickCounter++;

        ACTIVE.entrySet().removeIf(e -> {
            int t = e.getValue() - 1;
            if (t <= 0) return true;
            e.setValue(t);
            return false;
        });

        if (tickCounter % MoonwalkModCommon.STARE_INTERVAL_TICKS != 0) return;

        for (ServerWorld world : server.getWorlds()) {
            for (ServerPlayerEntity moonwalker : world.getPlayers()) {
                if (!ACTIVE.containsKey(moonwalker.getUuid())) continue;
                applyStarePunish(world, moonwalker);
            }
        }
    }

    private static void applyStarePunish(ServerWorld world, ServerPlayerEntity moonwalker) {
        Vec3d mwEye = moonwalker.getEyePos();
        List<LivingEntity> nearby = world.getEntitiesByClass(
            LivingEntity.class,
            moonwalker.getBoundingBox().expand(MoonwalkModCommon.STARE_RADIUS),
            e -> e != moonwalker && e.isAlive()
        );

        for (LivingEntity witness : nearby) {
            Vec3d look = witness.getRotationVec(1.0f).normalize();
            Vec3d toMoonwalker = mwEye.subtract(witness.getEyePos()).normalize();
            if (look.dotProduct(toMoonwalker) <= MoonwalkModCommon.STARE_DOT_THRESHOLD) continue;

            witness.damage(witness.getDamageSources().magic(), MoonwalkModCommon.STARE_DAMAGE);

            Vec3d back = look.multiply(-1);
            double h = MoonwalkModCommon.KNOCKBACK_HORIZONTAL;
            double v = MoonwalkModCommon.KNOCKBACK_VERTICAL;
            witness.setVelocity(witness.getVelocity().add(back.x * h, v, back.z * h));
            witness.velocityModified = true;
            if (witness instanceof ServerPlayerEntity sp) {
                sp.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(sp));
            }
        }
    }
}
