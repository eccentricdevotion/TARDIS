---
layout: default
title: areas
---

[Jump to video](#video)

# TARDIS areas

You can define _flat_ rectangular regions so that TARDISs can either be blocked from appearing in, or allowed/forced to travel to. This can be useful for creating TARDIS parking lots/airports, preventing players from travelling into towns or restricting players to particular locations.

A TARDIS area is perfect when setting up a TARDIS recharge beacon.

From TARDIS v3.0-beta-1, an area can be configured as the only place on the server that TARDISes can be created. To set this up follow the instructions below.

## How to define TARDIS areas

There are 2 commands used to set up TARDIS areas:

    /tardisarea start [name]
    /tardisarea end

After typing `/tardisarea start [name]` you will be prompted to define the first corner of the TARDIS area by clicking on the block where you want it to start. Change `[name]` to something suitable (between 2 and 16 characters, no spaces).

You will then have 60 seconds to run the second command `/tardisarea end`. Once again you will be prompted to click on the block (diagonally opposite the start block…) where you want the TARDIS area to end. The end block must be at the same Y-level coordinate as the start block

TARDIS areas cannot overlap, and ideally should be nice flat areas.

See also: the [Area commands](area-commands.html) page

### Setting the parking distance of a TARDIS area

By default TARDIS areas default to using 2 blocks of space between parked TARDISes, you can change this setting on a per area basis by typing:

    /tardisarea parking [name] [distance]

Where `[name]` is the name you gave the area when you created it, and `[distance]` is the number of blocks between TARDISes.

### Showing a TARDIS area

To temporarily show markers on the four corners of a defined TARDIS area, type:

    /tardisarea show [name]

Where `[name]` is the name you gave the area when you created it. A SNOW block will appear on each corner, then disappear after 15 seconds.

### Removing a TARDIS area

To delete an area, type:

    /tardisarea remove [name]

Where `[name]` is the name you gave the area when you created it.

## Restricting/Allowing access to TARDIS areas

Access to TARDIS areas is blocked by default. All access is controlled by permission nodes.

To give a player/group access to a TARDIS area they must have the permission:

    tardis.area.<area_name>

Where `<area_name>` is the name you gave the area when you created it.

## Forcing TARDIS creation to one area

1. Set up an area as described above
2. In the TARDIS config set `creation.area` to the name of the area defined in step 1
3. Don’t give players permission to travel to the area!

## Forcing a player to use only a specific area

If you want to punish a player or do some Timelord exile role playing (thanks to **PiP69** and **TKR101010** for the ideas) you can give the player the permission node for the TARDIS area you want to restrict them to, AND the permission:

    tardis.exile

This will restrict all TARDIS travel to the specified area.  
_Note:_ `allow.exile` must be `true` in the config.

### Video

**Note** The video shows the old command syntax, the process is the same, but the commands have changed from `/tardis area` to just `/tardisarea`

See also: the [Artron Energy](artron-energy.html#video) page for another video.

<iframe src="https://player.vimeo.com/video/52724961" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>