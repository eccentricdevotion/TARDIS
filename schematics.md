---
layout: default
title: Schematics
---

# Schematics

## Schematic format

As of version 3.0-beta-1, TARDIS now uses its own JSON schematic format. The method for creating schematics remains
basically the same, with the following differences:

- The schematic region start and end points must be defined with the TARDIS schematic
  wand (`/tardisgive [player] schematic_wand 1`)
- RIGHT-clicking with the wand sets the start point
- LEFT-clicking with the wand sets the end point
- Once the region is defined, use the `/tardisschematic` command (`/ts` for short) to save the schematic — see
  the [TARDIS Schematic commands](schematic-commands.html) page for more details
- You will be warned if the schematic region is not square and a multiple of 16 blocks
- TARDIS schematic files have the extension `.tschm` and are saved in the _plugins/TARDIS/user\_schematics_ folder

## Guidelines for custom TARDIS schematics

You can create multiple custom schematics for the inner TARDIS, but there are a few things you need to know before you
jump
right in!

1. In order to be loaded by the plugin, the schematic file must be located in the _plugins/TARDIS/user\_schematics_
   folder.
2. There must be an entry for the console in _custom_consoles.yml_ and _artron.yml_ — see
   [Custom Consoles](custom_consoles.html) for more information.
3. The custom TARDIS can only have 1 iron door, 1 stone button, and 1 wood button in its initial design (in addition to
   the placeholder blocks as listed below).
4. The TARDIS should be sized in multiples of 16 blocks (1 chunk). Schematics can be a maximum 3x3 chunks in size.
5. TARDISes should be squares i.e. that same width (x) and length (z) dimensions. A 16 x 16 block TARDIS is _good_, a 24
   x 18 block TARDIS would be **bad**.
6. Custom consoles are always grown starting at the same Y level (Y=64), this means that — the level of the floor where
   the doors are — needs to be at a certain height for the doors to line up with any rooms that are grown. You should be
   able to determine the floor level by basing your design on one of the template consoles (small, medium, tall).
7. When you are facing the outside of the TARDIS door, you should be pointing SOUTH.
8. The are some special placeholder blocks that are needed so the that plugin will record the location of the TARDIS
   controls. They are:

| Block               | Control                                                        |
|---------------------|----------------------------------------------------------------|
| `Cake block`        | handbrake                                                      |
| `Mushroom stem`     | All 4 repeaters                                                |
| `Oak Sign`          | Control centre                                                 |
| `Wood button`       | Artron Energy Button                                           |
| `Stone button`      | Random Destination Button                                      |
| `Chest`             | Artron Energy Condenser                                        |
| `Iron door`         | TARDIS interior door                                           |
| `Spawner`           | Scanner                                                        |
| `Bedrock`           | To block off the Beacon beam (used when turning off the power) |
| `Jukebox`           | Advanced Sonsole                                               |
| `Note block`        | Disk Storage                                                   |
| `Command block`     | Artron charged creeper spawn location                          |
| `Daylight detector` | Telepathic Circuit                                             |
