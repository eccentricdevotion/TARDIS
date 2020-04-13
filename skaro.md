---
layout: default
title: Planet Skaro
---

# Planet Skaro

![planet skaro](images/docs/skaro.jpg)

- This world is limited to Desert, Mesa and Ocean biomes.
- If configured, all water is acid and will harm / kill the player (wearing armour reduces the effect). Use a boat!
- Dalek structures will generate automatically as you explore the world — there are currently two building types, a big main building and a smaller corridor type one.
- Daleks will spawn in the big main buildings — you will need to have TARDISWeepingAngels plugin installed and the TARDISWeepingAngels-Resource-Pack to see them properly. There is a 10% chance that a Dalek will spawn flying in the air.
- A random loot chest is located in the main Dalek building — wear plenty of amour if you want to raid these!
- You can fill Acid Buckets and Rust Buckets by using an empty bucket on water and lava. Acid Buckets can be used to make Acid Batteries, which in turn can be used to make [Rift Manipulators](rift-manipulator.html). Rust Buckets are used in the crafting recipe for the [Rust Plague](http://tardis.wikia.com/wiki/Rust_plague) Sword - this deals more damage to Daleks (Update the TARDIS-MCP resource pack to see the textures).

## Enabling Skaro

The new Skaro Dalek homeworld is disabled by default, here are some instructions on enabling it.

### Minimum requirements

- [Spigot 1.10](https://www.spigotmc.org/threads/spigot-craftbukkit-bungeecord-1-10.154136/)
- [TARDIS v3.6-beta-1](http://tardisjenkins.duckdns.org:8080/job/TARDIS/lastSuccessfulBuild/me.eccentric_nz.TARDIS%24TARDIS/) (build #1499 or higher)
- [Multiverse-Core v2.5](https://ci.onarandombox.com/view/Multiverse/job/Multiverse-Core/)
- [TerrainControl v2.8.2](http://build.mctcp.com/job/TerrainControl%20-%20Master%20-%20Gradle/default/)

### Optional (but recommended) requirements

- [TARDISWeepingAngels v2.3](http://tardisjenkins.duckdns.org:8080/job/TARDISWeepingAngels/lastSuccessfulBuild/me.eccentric_nz.tardisweepingangels%24TARDISWeepingAngels/) (build #71 or higher)
- [TARDISWeepingAngels-ResourcePack](https://github.com/eccentricdevotion/TARDISWeepingAngels-Resource-Pack)
- [LibsDisguises v9.0.7](https://www.spigotmc.org/resources/libs-disguises.81/) (required by TARDISWeepingAngels)
- [ProtocolLib v4.0.2](https://www.spigotmc.org/resources/protocollib.1997/) (required by LibsDisguises)

### Step-by-step instructions

1. Install the plugin versions above
2. Start the server so that the default plugin files are generated
3. Stop the server
4. Edit the TARDIS planets.yml file (found in the _plugins/TARDIS/_ folder). It looks like this:

```yaml
switch_resource_packs: false
set_pack_on_join: true
default_resource_pack: https://dl.dropboxusercontent.com/u/53758864/rp/Default.zip
planets:
    Skaro:
        enabled: false
        resource_pack: https://dl.dropboxusercontent.com/u/53758864/rp/Skaro.zip
        acid: true
        acid_damage: 5
        acid_potions:
        — WEAKNESS
        — POISON
        rust: true
    TARDIS_TimeVortex:
        resource_pack: default
```

`switch_resource_packs: [true|false]` — To make the Skaro world look a little different than a regular world, TARDIS can set a small (79KB) resource pack when you switch worlds (you must enable server resource packs in the Minecraft client). The resource pack changes the colour of the sky, clouds, water, lava and fog — you will need to install [Optifine](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1272953).

`set_pack_on_join: [true|false]` — if players join the server in the Skaro world, setting this to `true` will switch the resource pack for them.

`default_resource_pack: https://dl.dropboxusercontent.com/u/53758864/rp/Default.zip` — This is just an empty resource pack (954 bytes). It is needed to revert back to the default (or other installed) resource pack colours. You can either host your own, or use the one provided. Worlds not listed in the `planets` section will use this.

`planets` — This is a list of worlds on your server. If you want to use the world resource pack switching feature for other server worlds, you can add them here. Use the `TARDIS_TimeVortex` entry as an example.

`planets: Skaro` — this is the config section for the custom Skaro world. The options are explained below:

`enabled: [true|false]` — whether this world should be created and managed by TARDIS.

`resource_pack: https://dl.dropboxusercontent.com/u/53758864/rp/Skaro.zip` — this is where you specify the URL to the resource pack you want to switch to when entering the world. Host your own, or use the one provided.

`acid: [true|false]` — whether all the water in the world is acid (and harms the player).

`acid_damage: [amount]` — the amount of damage a player takes when in acid water.

`acid_potions: []` — optional list of potion effects to give the player when they are in acid water. You can add any potion types from the list here: [SPIGOT PotionType.java](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/potion/PotionType.java).

`rust: [true|false]` — whether rust is enabled (not actually used yet).

### Step-by-step instructions (continued...)

1. Set, at a minimum, `planets: Skaro: enabled: true`, to use resource pack switching set `switch_resource_packs: true`
2. Start the server

TARDIS will copy the required TerrainControl config files to the server and create a new world called ‘Skaro’ that uses TerrainControl as the world generator. The Skaro world will also be enabled for TARDIS travel.
