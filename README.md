# MoonWalk Mod (Minecraft 1.21.11)

A Minecraft mod with these features:
1. Press **M** to "moonwalk": a random Michael Jackson sound plays for **everyone nearby**, and you gain a short Slow Falling effect.
2. Any living entity that looks directly at an actively-moonwalking player takes damage **and Knockback II–style knockback** in the OPPOSITE direction of their own gaze, every 14 ticks (synced with the damage tick).
3. Works on both **NeoForge** and **Fabric** loaders, in **singleplayer AND multiplayer**.
4. The mod is required on **both the server and every client** that wants to use it.

## Multiplayer / Server install
Put the appropriate JAR in your **server's** `mods/` folder AND every player's `mods/` folder.
A NeoForge server needs the NeoForge build; a Fabric server needs the Fabric build (plus Fabric API).
The two loaders are not cross-compatible on the same server, but each loader build is fully server-aware.

## How to build

You need:
- JDK 21
- Internet (Gradle downloads dependencies on first build)

### NeoForge
```
cd neoforge
./gradlew build
```
Output: `neoforge/build/libs/moonwalkmod-neoforge-1.21.11-1.0.0.jar`

### Fabric
```
cd fabric
./gradlew build
```
Output: `fabric/build/libs/moonwalkmod-fabric-1.21.11-1.0.0.jar`

## Install
Drop the built .jar into your `.minecraft/mods/` folder of a matching NeoForge or Fabric installation for Minecraft 1.21.11.
Fabric also requires **Fabric API**.

## Add your MJ sounds
See `AUDIO_README.md`. You need 3 `.ogg` files in:
`src/main/resources/assets/moonwalkmod/sounds/mj1.ogg` ... `mj3.ogg`
in **each** loader directory.

## Controls
- **M** — Moonwalk (configurable in Controls > MoonWalk Mod)
