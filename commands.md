---
layout: default
title: Commands
---

# Commands

All commands are _case-insensitive_.

Only the admin commands are available via the console.

You can view descriptions, usage and permissions for all commands in-game using the TARDIS help system. Type `/tardis help` to list all the commands, then use `/tardis? [command]` to view all the relevant subcommands. Use `/tardis? [command] [subcommand]` when necessary to view the command information.

### There are lots of commands:

`/tardis` - [Do stuff with your TARDIS](tardis-commands.html)

`/tardistravel` - [Go somewhere in your TARDIS](travel-commands.html)

`/tardisprefs` - [Change your TARDIS preferences](player-preferences.html)

`/tardisbind` - [Bind destinations to buttons](bind-commands.html)

`/tardisbook` - [Get TARDIS timelore books and start achievements](books.html)

`/tardisgravity` - [Create TARDIS gravity wells in any direction](gravity-wells.html)

`/tardisrecipe` - [View recipes for TARDIS items](recipe-commands.html)

`/tardisadmin` - [Change the TARDIS plugin configuration](admin-commands.html)

`/tardisgive` - [Give TARDIS items to players](give-commands.html)

`/tardisarea` - [Set up predefined TARDIS travel areas](area-commands.html)

`/tardisroom` - [Add custom rooms and view room costs](custom-rooms.html)

`/tardistexture` - [Switch texture packs when entering/exiting the TARDIS](texture-commands.html)

`/tardisremote` - [Remotely control any TARDIS](remote-commands.html)

`/tardisschematic` - [Make TARDIS schematics](schematic-commands.html)

`/tardisnetherportal` - [Get coordinates for linking Nether Portals](netherportal-command.html)

`/tardisjunk` - [Junk TARDIS related commands](junk-tardis.html)

`/handles` - [Handles related commands](handles.html)

There are multiple command arguments for each command.

### Command aliases

| Command | Aliases |
| --- | --- |
| `/tardis` | `/tt`<br>`/ttardis`<br>`//tardis`<br>`/timelord` |
| `/tardistravel` | `/ttravel` |
| `/tardisadmin` | `/tadmin` |
| `/tardisroom` | `/troom` |
| `/tardisprefs` | `/tprefs` |
| `/tardisarea` | `/tarea` |
| `/tardisbind` | `/tbind` |
| `/tardisgravity` | `/tgravity` |
| `/tardisbook` | `/tbook` |
| `/tardistexture` | `/tardisresource`<br>`/tresource`<br>`/ttexture`<br>`/trp`<br>`/ttp` |
| `/tardisremote` | `/tremote`<br>`/trem` |
| `/tardisschematic` | `/tschematic`<br>`/ts` |

## Basic travel commands:

**Note:** Some of these commands do not work in `hard` difficulty mode. See the [Advanced Console](advanced-console.html) page for more information.

### Returning home

To return home simply use the command:

    /tardistravel home

### Setting a new home

To move your TARDIS home location, simply look at the block you wish to make the new home location, and use the command:

    /tardis home

### Saving destinations

To save the destination where your TARDIS currently is, use the command:

    /tardis save [name]

To save the location your player is looking at for future TARDIS travel use the command:

    /tardis setdest [name]

### Travelling to destinations & areas

To travel to a saved destination use the command:

    /tardistravel dest [name]

To travel to a saved area (such as a recharging point) use the command:

    /tardistravel area [name]

### Calling your TARDIS

**Note:** In `hard` difficulty mode you maust use the Stattenheim Remote.

To call your TARDIS to you, simply use the command:

    /tardis comehere
