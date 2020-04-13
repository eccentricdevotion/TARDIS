---
layout: default
title: Schematics
---

[Jump to video](#video)

# Schematics

## New schematic format

As of version 3.0-beta-1, TARDIS now uses its own JSON schematic format. The method for creating schematics remains basically the same, with the following differences:

- The schematic region start and end points must be defined with the TARDIS schematic wand (`/tardisgive [player] wand 1`)
- RIGHT-clicking with the wand sets the start point
- LEFT-clicking with the wand sets the end point
- Once the region is defined, use the `/tardisschematic` command (`/ts` for short) to save the schematic — see the [TARDIS Schematic commands](schematic-commands.html) page for more details
- You will be warned if the schematic region is not square and a multiple of 16 blocks
- TARDIS schematic files have the extension `.tschm` and are saved in the _plugins/TARDIS/user\_schematics_ folder

## Guidelines for custom TARDIS schematics

You can create a custom schematic for the inner TARDIS, but there are a few things you need to know before you jump right in!

1. In order to be loaded by the plugin, the schematic file must be named _custom.tschm_ (_custom.schematic_ for pre-v3.0 versions), and should be located in the _plugins/TARDIS/user\_schematics_ folder.
2. The custom TARDIS can only have 1 iron door, 1 stone button, and 1 wood button in its initial design (in addition to the placeholder blocks as listed below).
3. The TARDIS should be sized in multiples of 16 blocks (1 chunk) — the chunks they use are reserved and cannot be used by other TARDISes - but be aware that big TARDISs will lag the server!
4. TARDISes should be squares i.e. that same width (x) and length (z) dimensions. A 16 x 16 block TARDIS is _good_, a 24 x 18 block TARDIS would be **bad**.
5. Custom consoles are always grown starting at the same Y level (Y=64), this means that — the level of the floor where the doors are — needs to be at a certain height for the doors to line up with any rooms that are grown. You should be able to determine the floor level by basing your design on any console schematic except the REDSTONE one.
6. When you are facing the outside of the TARDIS door, you should be pointing SOUTH.
7. The are some special placeholder blocks that are needed so the that plugin will record the location of the TARDIS controls. They are:

| `cake block` | handbrake |
| `monster stone (97:0)` | Save Sign |
| `97:1` | Destination Terminal |
| `97:2` | Architectural Reconfiguration System |
| `97:3` | TARDIS Information System |
| `97:4` | Temporal Locator |
| `97:5` | Keyboard |
| `Mushroom stem (100:15)` | All 4 repeaters |
| `Sign` | Chameleon Circuit / Preset selector |
| `A second sign` | Control centre (requires TARDIS v3.2 or higher) |
| `Wood button` | Artron Energy Button |
| `Stone button` | Random Destination Button |
| `Chest` | Artron Energy Condenser |
| `Iron door` | TARDIS interior door |
| `Spawner` | Scanner |
| `Bedrock` | To block off the Beacon beam (used when turning off the power) |

## Making the schematics (pre-v3.0)

The WorldEdit plugin was used to create the default set of TARDIS schematics.

So you could:

1. Build a TARDIS
2. Make a cuboid WorldEdit selection e.g. with the commands `//hpos1` and `//hpos2`, or using the WorldEdit wand (wood axe)
3. `//copy`
4. `//schematic save custom`
5. Move the schematic file into the _plugins/TARDIS/user\_schematics_ folder and start up your server.

### Video
<iframe src="https://player.vimeo.com/video/52214021" width="600" height="375" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
