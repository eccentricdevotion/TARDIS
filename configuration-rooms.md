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
          user: [true|false]

All rooms are enabled by default, the rest of the settings are:

| ROOMTYPE    | cost | offset | seed block               |
|-------------|------|--------|--------------------------|
| ANTIGRAVITY | 625  | -4     | SANDSTONE                |
| APIARY      | 450  | -4     | BEE_NEST                 |
| AQUARIUM    | 450  | -4     | TUBE_CORAL_BLOCK         |
| ARBORETUM   | 325  | -4     | OAK_LEAVES               |
| BAKER       | 350  | -4     | END_STONE                |
| BAMBOO      | 475  | -4     | BAMBOO                   |
| BEDROOM     | 475  | -4     | GLOWSTONE                |
| BIRDCAGE    | 350  | -4     | YELLOW_GLAZED_TERRACOTTA |
| CHEMISTRY   | 550  | -4     | BLAST_FURNACE            |
| EMPTY       | 250  | -4     | GLASS                    |
| FARM        | 350  | -4     | DIRT                     |
| GEODE       | 650  | -4     | AMETHYST_BLOCK           |
| GRAVITY     | 625  | -20    | MOSSY_COBBLESTONE        |
| GREENHOUSE  | 450  | -4     | MELON                    |
| HARMONY     | 450  | -4     | STONE_BRICK_STAIRS       |
| HUTCH       | 450  | -4     | ACACIA_LOG               |
| IGLOO       | 650  | -4     | PACKED_ICE               |
| KITCHEN     | 450  | -4     | PUMPKIN                  |
| LAZARUS     | 750  | -4     | FURNACE                  |
| LIBRARY     | 550  | -4     | ENCHANTING_TABLE         |
| MANGROVE    | 450  | -4     | MUDDY_MANGROVE_ROOTS     |
| MAZE        | 650  | -4     | LODESTONE                |
| MUSHROOM    | 350  | -4     | GRAVEL                   |
| NETHER      | 450  | -4     | BLACKSTONE               |
| PASSAGE     | 200  | -4     | CLAY                     |
| POOL        | 450  | -4     | SNOW_BLOCK               |
| RAIL        | 650  | -4     | HOPPER                   |
| RENDERER    | 550  | -4     | TERRACOTTA               |
| SHELL       | 550  | -4     | DEAD_BRAIN_CORAL_BLOCK   |
| SMELTER     | 750  | -4     | CHEST                    |
| STABLE      | 350  | -4     | HAY_BLOCK                |
| STALL       | 350  | -4     | BROWN_GLAZED_TERRACOTTA  |
| TRENZALORE  | 550  | -4     | BRICKS                   |
| VAULT       | 350  | -4     | DISPENSER                |
| VILLAGE     | 550  | -4     | OAK_LOG                  |
| WOOD        | 350  | -4     | OAK_PLANKS               |
| WORKSHOP    | 400  | -4     | CRAFTING_TABLE           |
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

