# MoonWalk Mod (Minecraft 1.21.1)

A Minecraft mod with three features:
1. Press **M** to "moonwalk": play a random Michael Jackson sound and gain a short Slow Falling effect.
2. Any living entity that looks directly at you takes continuous magic damage.
3. Works on both **NeoForge** and **Fabric** loaders.

## How to build

You need:
- JDK 21
- Internet (Gradle downloads dependencies on first build)

### NeoForge
```
cd neoforge
./gradlew build
```
Output: `neoforge/build/libs/moonwalkmod-neoforge-1.21.1-1.0.0.jar`

### Fabric
```
cd fabric
./gradlew build
```
Output: `fabric/build/libs/moonwalkmod-fabric-1.21.1-1.0.0.jar`

## Install
Drop the built .jar into your `.minecraft/mods/` folder of a matching NeoForge or Fabric installation for Minecraft 1.21.1.
Fabric also requires **Fabric API**.

## Add your MJ sounds
See `AUDIO_README.md`. You need 5 `.ogg` files in:
`src/main/resources/assets/moonwalkmod/sounds/mj1.ogg` ... `mj5.ogg`
in **each** loader directory.

## Controls
- **M** — Moonwalk (configurable in Controls > MoonWalk Mod)
