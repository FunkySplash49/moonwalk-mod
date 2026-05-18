package com.moonwalk.mod;

/**
 * Common constants shared across NeoForge and Fabric implementations.
 */
public final class MoonwalkModCommon {
    public static final String MOD_ID = "moonwalkmod";
    public static final String MOD_NAME = "MoonWalk Mod";

    /** Number of MJ sound variants available (mj1.ogg ... mjN.ogg). */
    public static final int MJ_SOUND_COUNT = 5;

    /** Damage dealt per tick to mobs that are looking at the player. */
    public static final float STARE_DAMAGE_PER_TICK = 0.5f;

    /** Max distance (blocks) at which the stare damage applies. */
    public static final double STARE_RADIUS = 16.0;

    /** Dot-product threshold: how directly a mob must be facing the player. */
    public static final double STARE_DOT_THRESHOLD = 0.95;

    /** Duration (ticks) of slow-falling moonwalk effect when key is pressed. */
    public static final int MOONWALK_DURATION_TICKS = 60;

    private MoonwalkModCommon() {}
}
