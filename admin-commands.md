---
layout: default
title: API
---
# Admin commands
### `/tardisadmin`
Enter a player’s TARDIS (by direct teleport instead of the door).
```
/tardisadmin enter [player]
```
Set the beacon block you are targeting in game, as a TARDIS recharge station.
```
/tardisadmin recharger [name]
```
Remove the specified beacon recharger from the config.
```
/tardisadmin decharge [name]
```
Set whether a specified world is included or excluded from random time travel destinations.
```
/tardisadmin include [world]
/tardisadmin exclude [world]
```
List all TARDISs and their locations.
```
/tardisadmin list
```
Remove a specified player’s TARDIS.
```
/tardisadmin delete [player]
```
List TARDISes that haven’t been used for an extended period.
```
/tardisadmin prunelist [number of days]
```
Remove TARDISes that haven’t been used for an extended period. See the [Prune](prune.html) page for more details.
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
List chunks that are being kept loaded by TARDIS &mdash; either for room growing or Police Box locations.
```
/tardisadmin chunks
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
