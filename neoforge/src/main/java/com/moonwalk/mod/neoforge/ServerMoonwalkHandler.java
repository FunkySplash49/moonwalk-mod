package com.moonwalk.mod.neoforge;

import com.moonwalk.mod.MoonwalkModCommon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Server-side moonwalk logic. Runs on dedicated server and integrated server.
 * - Plays a random MJ sound to all nearby players (everyone hears it).
 * - Gives the moonwalker Slow Falling (server effects auto-sync to clients).
 * - Every STARE_INTERVAL_TICKS, damages + knocks back any LivingEntity staring
 *   at an actively-moonwalking player. Knockback is opposite the witness's look.
 */
@EventBusSubscriber(modid = MoonwalkModCommon.MOD_ID)
public class ServerMoonwalkHandler {

    private static final Random RANDOM = new Random();
    /** moonwalker UUID -> ticks remaining */
    private static final Map<UUID, Integer> ACTIVE = new HashMap<>();
    private static int tickCounter = 0;

    public static void onMoonwalkTriggered(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        int idx = RANDOM.nextInt(MoonwalkModCommon.MJ_SOUND_COUNT);
        // Play at the player's position so all nearby clients hear it.
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
            MoonwalkMod.MJ_SOUNDS[idx].get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        player.addEffect(new MobEffectInstance(
            MobEffects.SLOW_FALLING,
            MoonwalkModCommon.MOONWALK_DURATION_TICKS, 0, false, true));

        ACTIVE.put(player.getUUID(), MoonwalkModCommon.MOONWALK_DURATION_TICKS);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (ACTIVE.isEmpty()) return;
        tickCounter++;

        // Decrement all timers each tick; remove expired.
        ACTIVE.entrySet().removeIf(e -> {
            int t = e.getValue() - 1;
            if (t <= 0) return true;
            e.setValue(t);
            return false;
        });

        if (tickCounter % MoonwalkModCommon.STARE_INTERVAL_TICKS != 0) return;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (ServerPlayer moonwalker : level.players()) {
                if (!ACTIVE.containsKey(moonwalker.getUUID())) continue;
                applyStarePunish(level, moonwalker);
            }
        }
    }

    private static void applyStarePunish(ServerLevel level, ServerPlayer moonwalker) {
        Vec3 mwEye = moonwalker.getEyePosition();
        List<LivingEntity> nearby = level.getEntitiesOfClass(
            LivingEntity.class,
            moonwalker.getBoundingBox().inflate(MoonwalkModCommon.STARE_RADIUS),
            e -> e != moonwalker && e.isAlive()
        );

        for (LivingEntity witness : nearby) {
            Vec3 look = witness.getLookAngle().normalize();
            Vec3 toMoonwalker = mwEye.subtract(witness.getEyePosition()).normalize();
            if (look.dot(toMoonwalker) <= MoonwalkModCommon.STARE_DOT_THRESHOLD) continue;

            // Damage
            witness.hurt(witness.damageSources().magic(), MoonwalkModCommon.STARE_DAMAGE);

            // Knockback OPPOSITE to where the witness is looking (so they get shoved backwards).
            Vec3 back = look.scale(-1);
            double h = MoonwalkModCommon.KNOCKBACK_HORIZONTAL;
            double v = MoonwalkModCommon.KNOCKBACK_VERTICAL;
            witness.setDeltaMovement(
                witness.getDeltaMovement().add(back.x * h, v, back.z * h)
            );
            witness.hurtMarked = true;
            if (witness instanceof ServerPlayer sp) {
                sp.hurtMarked = true;
                sp.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(sp));
            }
        }
    }
}
