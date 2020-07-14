---
layout: default
title: Planet Gallifrey
---

# Planet Gallifrey

- This world is limited to Badlands.
- Time Lord structures will generate automatically as you explore the world — there is currently one building type.
- Time Lords will spawn in these structures — you will need to have TARDISWeepingAngels plugin installed and the TARDISWeepingAngels-Resource-Pack to see them properly.
- A random loot chest is also located in the structures.

## Enabling Gallifrey

Gallifrey is disabled by default, here are some instructions on enabling it.

### Minimum requirements

- [Spigot 1.10](https://www.spigotmc.org/threads/spigot-craftbukkit-bungeecord-1-10.154136/)
- [TARDIS v3.6-beta-1](http://tardisjenkins.duckdns.org:8080/job/TARDIS/lastSuccessfulBuild/me.eccentric_nz.TARDIS%24TARDIS/) (build #1499 or higher)
- [Multiverse-Core v2.5](https://ci.onarandombox.com/view/Multiverse/job/Multiverse-Core/)

### Optional (but recommended) requirements

- [TARDISWeepingAngels v2.3](http://tardisjenkins.duckdns.org:8080/job/TARDISWeepingAngels/lastSuccessfulBuild/me.eccentric_nz.tardisweepingangels%24TARDISWeepingAngels/) (build #71 or higher)
- [TARDISWeepingAngels-ResourcePack](https://github.com/eccentricdevotion/TARDISWeepingAngels-Resource-Pack)
- [LibsDisguises v9.0.7](https://www.spigotmc.org/resources/libs-disguises.81/) (required by TARDISWeepingAngels)
- [ProtocolLib v4.0.2](https://www.spigotmc.org/resources/protocollib.1997/) (required by LibsDisguises)

### Step-by-step instructions

1. Install the plugin versions above
2. Start the server so that the default plugin files are generated
3. Stop the server
4. Edit the "Skaro" Planet in the TARDIS planets.yml file (found in the _plugins/TARDIS/_ folder). Below is the default config (as of build #2168)

```yaml
# other config #
Gallifrey:
  enabled: false
  resource_pack: https://www.dropbox.com/s/i7bpjju9jrgclq7/Gallifrey.zip?dl=1
  gamemode: SURVIVAL
  time_travel: true
  world_type: BUFFET
  environment: NORMAL
  generator: DEFAULT
  keep_spawn_in_memory: false
  spawn_other_mobs: true
# other config #
```

### Configuration Breakdown

`enabled: [true|false]` — whether this world should be created and managed by TARDIS.

`resource_pack: https://www.dropbox.com/s/i7bpjju9jrgclq7/Gallifrey.zip?dl=1` — this is where you specify the URL to the resource pack you want to switch to when entering the world. Host your own, or use the one provided.

`spawn_other_mobs: [true|false]` - as of Build #2168, you can disable non-Time Lord mobs. See [GH-299](https://github.com/eccentricdevotion/TARDIS/issues/299)

All other config options are generic options configurable per-planet, you can view those on the [configuring planets.yml](configuration-planets) page.

### Step-by-step instructions (continued...)

1. Set, at a minimum, `planets: Gallifrey: enabled: true`, to use resource pack switching set `switch_resource_packs: true`
2. Start the server
