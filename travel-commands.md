---
layout: default
title: Time travel commands
---

# Time travel commands

**Note:** if the plugin difficulty is set to `hard` you will need to use the TARDIS [Advanced Console](advanced-console.html) instead of these commands.

Some of these commands also require the appropriate permission before players can use them. See the [Permissions](permissions.html#ttperms) page for more information.

### `/tardistravel`

Set the destination location when exiting the TARDIS. You can either time travel to specific co-ordinates, to a player’s location, a saved destination, a specified biome, the TARDIS ‘home’ location, an underground cave, or an admin preset area. Type:

    /tardistravel [world] [x] [y] [z]

eg. `/tardistravel New_New_York -319 64 277` — to travel to co-ordinates in the _specified world_

eg. `/tardistravel ~ -319 64 277` — to travel to co-ordinates in the _world that the TARDIS is currently in_

    /tardistravel ~[x] ~[y] ~[z]

eg. `/tardistravel ~20 ~ ~-35` — to travel to relative co-ordinates. **Note** all coordinates must be proceeded by the tilde character (`~`). In this case the TARDIS would travel `20` blocks in the x-direction (EAST), `0` blocks in the y-direction (UP/DOWN) and negative `35` blocks in the z-direction (NORTH) from its current location.

    /tardistravel [player]

eg. `/tardistravel eccentric_nz` — to travel to a player.

A player can prevent other players from travelling to them by setting the player preference to ‘Do Not Disturb’ — to do this type: `/tardisprefs dnd [on|off]` or use the Sonic Prefs Menu.

To ask a player if you can travel to their protected region / claim, use the command:

    /tardistravel [player] ?

eg. `/tardistravel eccentric_nz ?`  
or you can also use `/tardistravel eccentric_nz tpa`

To accept a travel request, type **tardis request accept** in chat.

    /tardistravel dest [name]

eg. `/tardistravel dest mycoolsavedplace` — to travel to a saved destination

    /tardistravel area [name]

eg. `/tardistravel area airport` — to travel to a server defined TARDIS area

    /tardistravel biome [biome|list]

eg. `/tardistravel biome DESERT` — to travel to the nearest desert biome

eg. `/tardistravel biome list` — to list all available biome types

    /tardistravel home

Travel to where the TARDIS was first created

    /tardistravel cave

Travel underground to the nearest cave in a random direction from the TARDISes current location.

    /tardistravel village

Travel to the nearest village in a random direction from the TARDISes current location

    /tardistravel costs

Display a list of Artron Energy travel costs

    /tardistravel cancel

Removes the currently set travel destination

    /tardistravel stop

Stops travelling / materialising and returns to the home location. _Use in an emergency only!_
