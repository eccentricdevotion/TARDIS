---
layout: default
title: Admin commands
---

# Admin commands

### `/tardisadmin`

Enter a player’s TARDIS (by direct teleport instead of the door).

```
/tardisadmin enter [player]
```

{ #recharger } Set the beacon block you are targeting in game, as a TARDIS recharge station.

```
/tardisadmin recharger [name]
```

Remove the specified beacon recharger from the config.

```
/tardisadmin decharge [name]
```

Set the chest you are targeting in game, as the server's community Artron Condenser - see [Server condenser](condenser.html#server-condenser).

```
/tardisadmin condenser
```

List all TARDISs and their locations.

```
/tardisadmin list
```

Remove a specified player’s TARDIS.

```
/tardisadmin delete [player]
```

Create a TARDIS for a player. The TARDIS exterior will appear on the block that
the command sender is targeting.

```
/tadmin create [player] [schematic] <wall> <floor>
```

List TARDISes that haven’t been used for an extended period.

```
/tardisadmin prunelist [number of days]
```

Remove TARDISes that haven’t been used for an extended period. See the
[Prune](prune.html) page for more details.

```
/tardisadmin prune [number of days]
```

Check or set a player’s TARDIS count.

```
/tardisadmin playercount [player]
/tardisadmin playercount [player] [count]
```

Delete a player’s TARDIS database records (will not remove any blocks in game).

```
/tardisadmin purge [player]
```

List chunks that are being kept loaded by TARDIS &mdash; either for room growing
or Police Box locations.

```
/tardisadmin chunks
```

Convert the SQLite database records to a MySQL import file.

```
/tardisadmin convert_database
```

Download the latest TARDIS plugin from the Jenkins server. Restart the server to update the plugin.

```
/tardisadmin update_plugins
```

Create a custom chameleon preset file - see [Custom presets](custom-preset.html).

```
/tardisadmin make_preset [name] [asymmetric:true|false]
```

Spawn an abandoned TARDIS - see [Spawning abandoned TARDISes](abandon.html#spawning-abandoned-tardises).

```
/tardisadmin spawn_abandoned [SCHEMATIC] [PRESET] [DIRECTION] world x y z
```

Clear all data associated with walking into the TARDIS (they’ll still work, just toggle open the door again)

```
/tardisadmin purge_portals
```

Import Multiverse worlds into TARDIS's _planets.yml_.

```
/tardisadmin mvimport
```

Create a 11 x 11 block maze starting at the targeted block.

```
/tardisadmin maze
```

Clear the dispersed status of all TARDISes, or list which TARDISes are currently dispersed by HADS.

```
/tardisadmin dispersed [clear|list]
```

Sets the WorldGuard entry or exit flags for all regions in the TARDIS `default_world`. 

```
/tardisadmin region_flag [entry|exit]
```

The actual commands run for `[entry]` are:

```
/rg flag [region_id] exit -w [world_name]
/rg flag [region_id] entry -w [world_name] -g nonmembers deny
```

And `[exit]`:

```
/rg flag [region_id]entry -w [world_name]
/rg flag [region_id] exit -w [world_name] -g everyone deny
/rg flag [region_id] use -w [world_name] allow
/rg flag [region_id] chest-access -w [world_name]
```

Set a player’s TARDIS repair count.

```
/tardisadmin repair [player] [amount]
```

Remove a blueprint permission from a player.

```
/tardisadmin revoke [player] [permission]
```

Set a player's console size/type in the database.

```
/tadmin set_size [player] [size]
```

## Chameleon Arch commands

_See also the [Chameleon Arch](chameleon-arch.html) page._
View an ‘arched’ player’s real name.

```
/tardisadmin arch [player]
```

Force toggle a player’s ‘arched’ status.

```
/tardisadmin arch [player] force
```

## Disguise commands

Disguise a player as the specified entity type.

```
/tardisadmin disguise [player] [entity type]
```

Remove a disguise from a player.

```
/tardisadmin undisguise [player]
```
