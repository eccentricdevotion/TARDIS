---
layout: default
title: Room configuration
---

# Room configuration

These config options are found in the file: `rooms.yml`

The rooms configuration sets the seed block and Artron Energy costs for growing rooms. Each room type has its own
sub-section with two keys: the room `cost`, and the room `seed`.

The format is:

    rooms:
       [ROOMTYPE]:
          enabled: [true|false]
          cost: [amount]
          offset: [negative amount]
          seed: [MATERIAL]

All rooms are enabled by default, the rest of the settings are:

| ROOMTYPE    | cost | offset | seed block               |
|-------------|------|--------|--------------------------|
| PASSAGE     | 200  | -4     | CLAY                     |
| AQUARIUM    | 450  | -4     | TUBE_CORAL_BLOCK         |
| APIARY      | 450  | -4     | BEE_NEST                 |
| ARBORETUM   | 325  | -4     | OAK_LEAVES               |
| BAMBOO      | 475  | -4     | BAMBOO                   |
| BEDROOM     | 475  | -4     | GLOWSTONE                |
| BIRDCAGE    | 350  | -4     | YELLOW_GLAZED_TERRACOTTA |
| CHEMISTRY   | 550  | -4     | BLAST_FURNACE            |
| KITCHEN     | 450  | -4     | PUMPKIN                  |
| LAZARUS     | 750  | -4     | FURNACE                  |
| LIBRARY     | 550  | -4     | ENCHANTING_TABLE         |
| MANGROVE    | 450  | -4     | MUDDY_MANGROVE_ROOTS     |
| MAZE        | 650  | -4     | LODESTONE                |
| NETHER      | 450  | -4     | BLACKSTONE               |
| POOL        | 450  | -4     | SNOW_BLOCK               |
| VAULT       | 350  | -4     | DISPENSER                |
| WORKSHOP    | 400  | -4     | CRAFTING_TABLE           |
| EMPTY       | 250  | -4     | GLASS                    |
| GRAVITY     | 625  | -20    | MOSSY_COBBLESTONE        |
| ANTIGRAVITY | 625  | -4     | SANDSTONE                |
| HARMONY     | 450  | -4     | STONE_BRICK_STAIRS       |
| BAKER       | 350  | -4     | END_STONE                |
| WOOD        | 350  | -4     | OAK_PLANKS               |
| FARM        | 350  | -4     | DIRT                     |
| GREENHOUSE  | 450  | -4     | MELON                    |
| HUTCH       | 450  | -4     | ACACIA_LOG               |
| GEODE       | 650  | -4     | AMETHYST_BLOCK           |
| IGLOO       | 650  | -4     | PACKED_ICE               |
| MUSHROOM    | 350  | -4     | GRAVEL                   |
| SHELL       | 550  | -4     | DEAD_BRAIN_CORAL_BLOCK   |
| SMELTER     | 750  | -4     | CHEST                    |
| STABLE      | 350  | -4     | HAY_BLOCK                |
| STALL       | 350  | -4     | BROWN_GLAZED_TERRACOTTA  |
| RAIL        | 650  | -4     | HOPPER                   |
| VILLAGE     | 550  | -4     | OAK_LOG                  |
| TRENZALORE  | 550  | -4     | BRICKS                   |
| RENDERER    | 550  | -4     | TERRACOTTA               |
| ZERO        | 650  | -4     | OAK_BUTTON               |

## Room jettisons

**NB:** These config options are found in the file: `artron.yml`

Set the percentage of energy the TARDIS gets back when a room is jettisoned.

    jettison: [percentage]

The default is `75`.

Set the seed block for room jettisons.

    jettison_seed: [material]

The default is `TNT`.

[Back to main configuration page](configuration.html)

