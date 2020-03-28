---
layout: default
title: World management
---

# World management

From TARDIS v4.1.0 the plugin can now manage any worlds on the server and no longer relies on a separate multi-world plugin such as Multiverse.

Configuration for world management is handled by [planets.yml](configuration-planets.html).

## Commands

The main world management command is `/tardisworld`

<style type="text/css">
			table, table code { font-size:85%; }
			td { vertical-align:top; }
			td.noborder { border-bottom: none; }
			tr.coption { background-color: #eee; }
		</style>

| Aliases: | `tardisw`, `tw` |
| Permission: | `tardis.admin` |
| Command | Argument(s) | Function |
| `/tardisworld load` | `[world] [worldType] [environment] [generator]` | Loads a world for TARDIS to manage.<br>`world` should be the name of the world folder. <br>`worldType` should be one of: NORMAL, FLAT, or BUFFET.  <br>`environment` should be one of: NORMAL, NETHER, or THE\_END.  <br>`generator` should be the name of the plugin that will generate the world terrain e.g. TARDISChunkGenerator. |
| `/tardisworld unload` | `[world]` | Unloads a world from the server. `world` should be the name of the world folder. |
| `/tardisworld gm` | `[world] [gamemode]` | Sets the gamemode for a world. `world` should be the name of the world folder. `gamemode` should be one of: SURVIVAL, CREATIVE, ADVENTURE, or SPECTATOR. |
| `/tardisworld rename` | `[old name] [new name]` | Renames the world folder and the record in the level.dat file. |

Teleporting between worlds is handled by the `/tardisteleport` command.

| Aliases: | `tardistp`, `ttp` |
| Permission: | `tardis.admin` |
| Command | Argument(s) | Function |
| `/tardisteleport` | `[world]` | Teleports the player running the command to the specified world’s spawn point. |
