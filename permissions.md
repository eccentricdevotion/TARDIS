---
layout: default
title: Permissions
---

# Permissions

> **Quote from eccentric\_nz:**
> 
> If you are getting the error:
> 
> > `Your exile status does not allow you to bring the TARDIS to this…`
> 
> or
> 
> > `Your travel has been restricted to the [] area!`
> 
> Update the plugin!

## Setting permissions for TARDIS worlds

To automatically set permissions for TARDIS worlds when they are created go read the [Add permissions page](add-permissions.html).

* * *

Tested with PermissionsBukkit, there are a number of self explanatory permissions:

## General

Allow players to use a TARDIS

    tardis.use

There are a number of (probably unnecessary and self explanatory) child nodes. This means that if you have given players `tardis.use` you _DON’T_ need to give any of the permissions below.

    tardis.create
    tardis.exterminate
    tardis.timetravel
    tardis.list
    tardis.save
    tardis.enter
    tardis.add
    tardis.find
    tardis.update
    tardis.rebuild
    tardis.help
    tardis.book
    tardis.advanced
    tardis.storage
    tardis.gravity
    tardis.texture
    tardis.temporal

## TARDIS build sizes

In order to be able to create a ‘bigger’, ‘deluxe’, ‘eleventh’, ‘twelfth’, ‘redstone’, ‘steampunk’, ‘ARS’, ‘tom’, ‘plank’, ‘war’, ‘master’, ‘pyramid’, ‘custom’TARDIS, or any of the other consoles players will need:

    tardis.bigger
    tardis.deluxe
    tardis.eleventh
    tardis.twelfth
    tardis.redstone
    tardis.steampunk
    tardis.ars
    tardis.tom
    tardis.plank
    tardis.war
    tardis.master
    tardis.pyramid
    tardis.coral
    tardis.ender
    tardis.legacy_bigger
    tardis.legacy_budget
    tardis.legacy_deluxe
    tardis.legacy_eleventh
    tardis.legacy_redstone
    tardis.custom

## TARDIS world creation

If a server is set up to use a default world (and `create_worlds_with_perms: true` is set on the config), players can be allowed to create a TARDIS in its own world if they have:

    tardis.create_world

## Junk TARDIS

If a server is set up to allow a Junk TARDIS (both `junk.enabled: true` and `creation.default_world: true` are set on the config), players can use the Junk TARDIS if they have:

    tardis.junk

## TARDIS rooms

To allow players to grow new TARDIS rooms, they will need the permission for each room type. To be able to grow all room types (including custom ones), use:

    tardis.room

Otherwise there are individual nodes for for each room:

    tardis.room.antigravity
    tardis.room.antigravity
    tardis.room.arboretum
    tardis.room.baker
    tardis.room.bedroom
    tardis.room.birdcage
    tardis.room.empty
    tardis.room.farm
    tardis.room.gravity
    tardis.room.greenhouse
    tardis.room.harmony
    tardis.room.hutch
    tardis.room.igloo
    tardis.room.kitchen
    tardis.room.lazarus
    tardis.room.library
    tardis.room.mushroom
    tardis.room.passage
    tardis.room.pool
    tardis.room.rail
    tardis.room.renderer
    tardis.room.stable
    tardis.room.trenzalore
    tardis.room.vault
    tardis.room.village
    tardis.room.wood
    tardis.room.workshop
    tardis.room.zero

To use the Architectural Reconfiguration System, pl;ayers will need:

    tardis.architectural

## Time travel

There are a some time travel specific nodes:

Allow travel to player locations

    tardis.timetravel.player

Allow travel to specified co-ordinates

    tardis.timetravel.location

Allow travel to a specified biome

    tardis.timetravel.biome

Allow travel to caves

    tardis.timetravel.cave

Allow travel to villages

    tardis.timetravel.village

## Per world travel

If the TARDIS config option `per_world_perms` is `true`, then players will need the appropriate permission to time travel to a world. The permission format is:

    tardis.travel.[world]

Where `[world]` is the name of the world they will have access to.

## Environment

There are a couple of world environment specific nodes:

Allow travel to **Nether** worlds

    tardis.nether

Allow travel to **The End** worlds

    tardis.end

## Kits

There are two TARDIS Item Kit specific nodes:

Allow players to recieve a kit when they **join**

    tardis.kit.join

Allow players to recieve a kit when they **create** a TARDIS

    tardis.kit.create

## Advanced Console

There are two Advanced Console specific nodes:

Allow players to use the advanced console

    tardis.advanced

Allow players to store items in the Disk Storage Container

    tardis.storage

## Artron Energy Cells

Allow players to store Artron Energy in Artron Energy Cells:

    tardis.store

## Temporal Locator

Allow players to use the Temporal Locator:

    tardis.temporal

## Perception Filter

Allow players to use the Perception Filter:

    tardis.filter

## ARS

Allow players to use the Architectural Reconfiguration System:

    tardis.ars

## TARDIS autonomous homing function

Allow TARDISes to automatically return to the nearest of the ‘Home’ location or the nearby recharge area:

    tardis.autonomous

## TARDIS mob farming

To allow players to farm mobs:

    tardis.farm

## Entity ejection

To allow players to eject entities (players and mobs — except horses):

    tardis.eject

## Lazarus device / Genetic manipulator / Immortality Gate

To allow players to use the TARDIS Genetic Manipulator and reverse the polarity of the TARDIS Genetic Manipulator turning it into the Immortality Gate:

    tardis.lazarus
    tardis.themaster

## Chameleon Arch

To allow players to use the Chameleon Arch:

    tardis.chameleonarch

## TARDIS upgrades / desktop theme

To allow players to use the desktop themer / upgrade GUI:

    tardis.upgrade

## Vault sorter

To allow players to have an automatic vault room chest sorter:

    tardis.vault

## TARDIS back doors

To allow players to to add a TARDIS back door:

    tardis.backdoor

## Sonic Screwdrivers

Each sonic upgrade has its own permission:

    tardis.sonic.standard
    tardis.sonic.bio
    tardis.sonic.redstone
    tardis.sonic.emerald
    tardis.sonic.diamond
    tardis.sonic.paint
    tardis.sonic.ignite
    tardis.sonic.arrow
    tardis.sonic.admin

The Bio-scanner Sonic has an extra permission to allow player freezing:

    tardis.sonic.freeze

The Emerald Environment Sonic has an extra permission to allow automatic replanting of crops (if the player has the correct seed item in their inventory):

    tardis.sonic.plant

The Diamond Disruptor Sonic has an extra permission to allow silk touch block drops:

    tardis.sonic.silktouch

## TARDIS books and achievements

To allow players to get books and gain achievements:

    tardis.book

## TARDIS Universal Translator

To allow players to use the `/tardissay` command:

    tardis.translate

## TARDIS Artron Furnace

To allow players to place the TARDIS Artron Furnace:

    tardis.furnace

## Atmospheric Excitation

To allow players to initiate Atmospheric Excitation:

    tardis.atmospheric

## TARDIS texture pack switching

To allow players to use the `/tardistexture` command and switch texture packs when entering/exiting the TARDIS:

    tardis.texture

## TARDIS gravity wells

To allow players to use the `/tardisgravity` command:

    tardis.gravity

## TARDIS difficulty level

To allow players to use the `/tardisprefs difficulty [easy|hard]` command:

    tardis.difficulty

## TARDIS areas

To allow access to TARDIS areas, users/groups must be given the permission:

    tardis.area.[name_of_area]

eg. to give access to the “airport” area, the permission would be:

    tardis.area.airport

To restrict a player to a specific area, the player must have the permission:

    tardis.exile

AND the permission for the area they are restricted to.

## Bypass TARDIS pruning

To allow players to bypass the abandoned TARDIS pruning feature:

    tardis.prune.bypass

## Admin/moderator

Allow players to do administration stuff

    tardis.admin
    tardis.delete
    tardis.skeletonkey
    tardis.remote

### Deleting any TARDIS

The node `tardis.delete` will allow admin users to remove any TARDIS by breaking the sign on the front of the Police Box, or using the `/tardisadmin delete [player]` command.

When breaking the sign, the TARDIS must not be occupied (including offline players).

The permission is set to **false** by default, so will have to explicitly given!

### Entering any TARDIS

The node `tardis.skeletonkey` will allow admin users to enter and use any TARDIS (either via the door, or via the `/tardisadmin enter [player]` command.

The permission is set to **false** by default, so will have to explicitly given!

### Remote controlling a TARDIS

The node `tardis.remote` will allow admin users to remotely control a TARDIS.

* * *

## ???

Can't find the permission you’re looking for?

Check out the [Big list of permissions](permissions-table.html) taken directly from _plugin.yml_.

