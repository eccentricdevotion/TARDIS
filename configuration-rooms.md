---
layout: default
title: Room configuration
---

# Room configuration

These config options are found in the file: `rooms.yml`

The rooms configuration sets the seed block and Artron Energy costs for growing rooms. Each room type has its own sub-section with two keys: the room `cost`, and the room `seed`.

The format is:

    rooms:
       [ROOMTYPE]:
          enabled: [true|false]
          cost: [amount]
          offset: [negative amount]
          seed: [MATERIAL]

All rooms are enabled by default, the rest of the settings are:

| ROOMTYPE | cost | offset | seed block |
| --- | --- | --- | --- |
| ARBORETUM | 325 | -4 | LEAVES |
| BAKER | 350 | -1 | ENDER\_STONE |
| BEDROOM | 475 | -1 | GLOWSTONE |
| CROSS | 350 | -1 | SOUL\_SAND |
| EMPTY | 250 | -1 | GLASS |
| FARM | 350 | -1 | DIRT |
| GRAVITY | 625 | -12 | MOSSY\_COBBLESTONE |
| ANTIGRAVITY | 625 | -2 | SANDSTONE |
| GREENHOUSE | 450 | -2 | MELON\_BLOCK |
| HARMONY | 450 | -1 | SMOOTH\_STAIRS |
| KITCHEN | 450 | -1 | PUMPKIN |
| LIBRARY | 550 | -1 | BOOKSHELF |
| LONG | 300 | -2 | NOTE\_BLOCK |
| MUSHROOM | 350 | -1 | GRAVEL |
| PASSAGE | 325 | -2 | LEAVES |
| POOL | 450 | -3 | SNOW\_BLOCK |
| VAULT | 350 | -1 | DISPENSER |
| WOOD | 350 | -1 | WOOD |
| WORKSHOP | 400 | -1 | NETHER\_BRICK |

## Room jettisons

**NB:** These config options are found in the file: `artron.yml`

Set the percentage of energy the TARDIS gets back when a room is jettisoned.

    jettison: [percentage]

The default is `75`.

Set the seed block for room jettisons.

    jettison_seed: [material]

The default is `TNT`.

[Back to main configuration page](configuration.html)

