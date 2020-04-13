---
layout: default
title: Abandon a TARDIS
---

Abandon a TARDIS
================

You can abandon a TARDIS if you have permission and the TARDIS is stored in a [T.I.P.S](tips.html) world (e.g. TARIS \_TimeVortex). When you do this, the TARDIS powers down automatically, you will no longer be the owner of the TARDIS, and it won’t respond to your commands. You will be able to grow a new one, and any person will be able to enter the abandoned TARDIS — the first player that turns on the Artron Capacitor, will have 'claimed' the TARDIS and will become the new owner.

Abandoning a TARDIS is one way of transferring TARDIS ownership to another player.

### Config options

    abandon:
        enabled: [true|false]
        reduce_count: [true|false]

Both options are `true` by default. reduce\_count only applies if `creation.count` is set to > `0`. `allow.power_down` must also be `true` for abandonment to take place.

### Commands

`/tardis abandon` — abandons your TARDIS — the TARDIS must be initialised, not travelling or in the time vortex, and not set to Junk mode. Players must have `tardis.abandon` permission.

`/tardis abandon list` or `/tardisadmin list abandoned` — lists all abandoned TARDISes. If ProtocolLib is installed you can click < Enter > in the list to teleport into that TARDIS (otherwise you’ll need to walk there like everyone else :P ). You can also delete an abandoned TARDIS by clicking < Delete >.

Spawning abandoned TARDISes
---------------------------

You can spawn abandoned TARDISes automatically with a command:

    /tardisadmin spawn_abandoned [SCHEMATIC] [PRESET] [DIRECTION] world x y z

Where `[SCHEMATIC]` is the interior console type of the TARDIS — see [list\_of\_interiors.html](list_of_interiors.html)

And `[PRESET]` is the exterior preset type of the TARDIS — see [PRESET](http://www.thenosefairy.co.nz/TARDIS_java_docs/me/eccentric_nz/TARDIS/enumeration/PRESET.html)

And `[DIRECTION]` is the direction the player would be facing if they are looking at the exterior preset door — one of: NORTH, SOUTH, EAST, WEST

And `world x y z` are the coordinates of the TARDIS exterior’s bottom centre.
