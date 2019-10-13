# Modern Minecraft Modding with Forge

## What do we want to build?

- When a player
  - hits a monster
  - with a golden axe
  - named Stormbreaker,
  - strike the monster with lightning and
  - make an explosion.

- When a player
  - uses the `/thunder` command
  - summon lightning around the player

## Download Forge

Using 1.13.2 because it's more mature. 1.14 is still under active development.

1. <https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.13.2.html>
2. `unzip forge-1.13.2-25.0.219-mdk.zip -d storm-hammer`
3. `cd storm-hammer`

## Set up dev environment

Must use Java 8. Java 9+ not supported. `jenv local 1.8`

1. `./gradlew genIntellijRuns`
2. Open Intellij Idea
3. Import project > select directory
4. Import as gradle project
5. Execute `tasks > fg_runs > runClient`
6. Verify example mod
7. Create new singleplayer world
8. See logs in Idea `:runClient` task

## Set up mod metadata

1. Edit `src > main > resources > META-INF > mods.toml`
2. Set `modId`, `displayName`, and `description`
3. Edit `ExampleMod.java`
4. Set `@Mod("stormhammer")`
5. Delete unused lifecycle event handlers

## Create first event handler

```java
@SubscribeEvent
public void onHurt(LivingHurtEvent event) {
    LOGGER.info(">>> " + event.getSource().getImmediateSource().getName().getString() + " hurt " +
            event.getEntity().getName().getString());
}
```

## Summon lightning

```java
World world = event.getEntityLiving().getEntityWorld();

// Summon lightning
world.addWeatherEffect(new EntityLightningBolt(
        world,
        event.getEntity().posX,
        event.getEntity().posY,
        event.getEntity().posZ,
        false));
```

## Create an explosion

```java
World world = event.getEntityLiving().getEntityWorld();

// Make an explosion
world.createExplosion(
        event.getEntity(),
        event.getEntity().posX,
        event.getEntity().posY,
        event.getEntity().posZ,
        8,
        true);
```

## Package jar

1. Set `group` and `archivesBaseName` in `build.gradle`
2. Execute `tasks > build > build` (don't use `jar`)
3. Extract built jar from `build/libs` directory

## Run with Forge

1. Copy mod into Forge mods directory
2. Start Forge from Minecraft launcher

