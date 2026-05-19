package com.moonwalk.mod;

/**
 * Common constants shared across NeoForge and Fabric implementations.
 */
public final class MoonwalkModCommon {
    public static final String MOD_ID = "moonwalkmod";
    public static final String MOD_NAME = "MoonWalk Mod";

    /** Number of MJ sound variants available (mj1.ogg ... mjN.ogg). */
    public static final int MJ_SOUND_COUNT = 3;

    /** Damage dealt per stare-tick interval to entities staring at a moonwalker. */
    public static final float STARE_DAMAGE = 2.0f;

    /** Max distance (blocks) at which the stare damage/knockback applies. */
    public static final double STARE_RADIUS = 16.0;

    /** Dot-product threshold: how directly the witness must be facing the moonwalker. */
    public static final double STARE_DOT_THRESHOLD = 0.95;

    /** Duration (ticks) of slow-falling moonwalk effect when key is pressed. */
    public static final int MOONWALK_DURATION_TICKS = 60;

    /** Interval (ticks) between stare damage + knockback applications. */
    public static final int STARE_INTERVAL_TICKS = 14;

    /**
     * Knockback horizontal strength (blocks/tick velocity component).
     * Tuned to match Knockback II from a sword (~0.4 base + 0.4*level horizontal).
     */
    public static final double KNOCKBACK_HORIZONTAL = 1.2;
    public static final double KNOCKBACK_VERTICAL = 0.4;

    /** Network packet/channel id for moonwalk trigger. */
    public static final String MOONWALK_PACKET_ID = "moonwalk_trigger";

    private MoonwalkModCommon() {}
}
