---
layout: default
title: Creating a TARDIS
---

# Creating a TARDIS

[Jump to video](#video)

To create a TARDIS, you first need to craft a TARDIS seed block. The crafting recipe depends on which version of the
plugin you have installed.

To see the crafting recipes, use the command:

`/tardisrecipe seed [console type]`

or just type "how do I make a TARDIS?" into chat.

## TARDIS Seed Blocks

A seed block requires five different components as explained below. You can mix and match your own combination of
blocks (20,000+ different combinations with the default plugin configuration) to customise the appearance of your
TARDIS.

![Crafting a TARDIS seed block](images/docs/craftdefaultseed-v3.7.jpg)

The crafting grid above shows how to craft the default ‘budget’ TARDIS that starts off with the FACTORY chameleon preset
**(TARDIS v3.7 and higher)**:

- **IRON block** = TARDIS type, can be any of the current TARDIS ‘type’ blocks e.g. IRON, GOLD, DIAMOND, EMERALD
  etc ([see list below](#tardis-types))
- **LAPIS block** = required block
- **REDSTONE TORCH** = required block
- **LIGHT GREY WOOL** = TARDIS interior floor block, can be any
  valid [TARDIS block](https://github.com/eccentricdevotion/TARDIS/blob/v4.0/src/main/resources/blocks.yml#L4-L127)
- **ORANGE WOOL** = TARDIS interior wall block, can be any
  valid [TARDIS block](https://github.com/eccentricdevotion/TARDIS/blob/v4.0/src/main/resources/blocks.yml#L4-L127)

Once the required and chosen blocks are placed in the crafting grid, a TARDIS seed block is automatically generated.

Seed blocks can be placed, broken and dropped and still remain as a valid seed block.

**Seed block not crafting?** You might be using an ancient version of the plugin - check out the instructions
here: [the old way of crafting](creating-a-tardis-old)

## TARDIS types

You can make your TARDIS any type of valid TARDIS block you like, listed below.
The TARDIS type is the block that replaces the "Iron Block" in the above crafting grid.

| Name                               | Block Required         | Description                                             |
|------------------------------------|------------------------|---------------------------------------------------------|
| [ANCIENT](interiors#ancient)       | Sculk                  | Ancient city console                                    |
| [ARS](interiors#ars)               | Quartz Block           | The "ARS” TARDIS                                        |
| [BUDGET](interiors#budget)         | Iron Block             | The default “budget” TARDIS interior                    |
| [BIGGER](interiors#bigger)         | Gold Block             | A "bigger" version of the default TARDIS                |
| [CAVE](interiors#cave)             | Dripstone Block        | A cave-themed TARDIS interior                           |
| [COPPER](interiors#copper)         | Warped Planks          | A copper TARDIS interior (by vistaero)                  |
| [CORAL](interiors#coral)           | Nether Wart Block      | The 10th Doctor's TARDIS (by vistaero)                  |
| [DELTA](interiors#delta)           | Crying Obsidian        | The Nether Delta TARDIS interior                        |
| [DELUXE](interiors#deluxe)         | Diamond Block          | A “deluxe” TARDIS                                       |
| [DIVISION](interiors#division)     | Pink Glazed Terracotta | The “division” as a TARDIS console                      |
| [ELEVENTH](interiors#eleventh)     | Emerald Block          | The 11th Doctor's TARDIS interior                       |
| [ENDER](interiors#ender)           | Purpur Block           | An End-inspired TARDIS interior                         |
| [FACTORY](interiors#factory)       | Yellow Concrete Powder | 1st Doctor's Factory TARDIS (by Razihel)                |
| [FUGITIVE](interiors#fugitive)     | Polished Deepslate     | Ruth (The Fugitive Doctor) Clayton's Console (by DT10)  |
| [HOSPITAL](interiors#hospital)     | White Concrete         | St John's Hospital interior                             |
| [ORIGINAL](interiors#original)     | Packed Mud             | The original TARDIS plugin Console                      |
| [MASTER](interiors#master)         | Nether Bricks          | The Master's TARDIS interior (by ShadowAssociate)       |
| [MECHANICAL](interiors#mechanical) | Polished Andesite      | Mechanical TARDIS interior (by Plastic Straw)           |
| [PLANK](interiors#plank)           | Bookshelf              | A "wood plank" TARDIS interior                          |
| [PYRAMID](interiors#pyramid)       | Sandstone Stairs       | A sandstone pyramid TARDIS interior (by airomis)        |
| [REDSTONE](interiors#redstone)     | Redstone Block         | A “redstone” TARDIS                                     |
| [ROTOR](interiors#rotor)           | Honeycomb Block        | An animated time rotor TARDIS console                   |
| [STEAMPUNK](interiors#steampunk)   | Coal Block             | A “steampunk” TARDIS (designed by ToppanaFIN)           |
| [THIRTEENTH](interiors#thirteenth) | Orange Concrete        | The 13th Doctor's TARDIS interior (designed by Razihel) |
| [TOM](interiors#tom)               | Lapis Block            | The “Tom Baker” TARDIS interior                         |
| [TWELFTH](interiors#twelfth)       | Prismarine             | The 12th Doctor's TARDIS interior                       |
| [WAR](interiors#war)               | White Terracotta       | The War Doctor's TARDIS interior                        |
| [WEATHERED](interiors#weathered)   | Weathered Copper       | A “weathered” TARDIS interior                           |

### Legacy TARDIS types

| Name            | Block Required               |
|-----------------|------------------------------|
| LEGACY_BIGGER   | Orange Glazed Terracotta     |
| LEGACY_DELUXE   | Lime Glazed Terracotta       |
| LEGACY_ELEVENTH | Cyan Glazed Terracotta       |
| LEGACY_REDSTONE | Red Glazed Terracotta        |

[View a gallery of interiors](interiors)

## Growing the TARDIS from the seed

Your TARDIS Police Box requires a 3 x 4 x 4 (w x d x h) cuboid region. Where you place the TARDIS seed block will be the
centre of the Police Box, and will be saved as the TARDIS’s ‘Home’ location.

To create your TARDIS, right-click a placed seed block with the TARDIS key.

Once clicked, the seed block will change into a TARDIS builder block and the TARDIS creation will be displayed as a
progress bar with informative text.

## Entering the TARDIS

Your TARDIS requires a key (default is a gold nugget, configurable in the config file).

**RIGHT**-click the door with your “key” to open the door and then walk into your new TARDIS interior!

## Video

This video is now outdated, for TARDIS v3.7 and above **do NOT include the exterior wall or lamp blocks!**

<iframe src="https://player.vimeo.com/video/80702478" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>

&nbsp;

**Next:** [Artron Energy](artron-energy)
