---
layout: default
title: World Generators
---

# World Generators

The TARDISChunkGenerator module includes world generators for more than just the TARDIS void worlds (
TARDIS_TimeVortex, TARDIS_Zero_Room).

You can use the generators for any world, just specify the `generator` setting in _planets.yml_.

## Void

`TARDISChunkGenerator:void`

An empty world.

![Void world](images/docs/void_world.jpg)

## Gallifrey

`TARDISChunkGenerator:gallifrey`

Badlands biomes with custom sky colour, trees and Time Lord structures.

![Gallifrey world](images/docs/gallifrey.jpg)

### Siluria

`TARDISChunkGenerator:siluria`

Bamboo & sparse jungle biomes with custom Silurian structures.

![Siluria world](images/docs/siluria.jpg)

### Skaro

`TARDISChunkGenerator:skaro`

Desert biome with custom sky colour, trees and Dalek structures.

![Skaro world](images/docs/skaro.jpg)

## Configurable flat world

`TARDISChunkGenerator:flat`

Set block types in **TARDISChunkGenerator**'s _config.yml_

```yaml
## Blocks to use in the Flat World Generator
# Use Spigot Material names
# bottom has 1 layer
bottom: BEDROCK
# rock has ~60 layers
rock: STONE
# middle has 3 layers
middle: DIRT
# surface has 1 layer
surface: GRASS_BLOCK
```

![Flat world](images/docs/flat_world.jpg)

## Water world

`TARDISChunkGenerator:water`

Ocean biomes and a small island at spawn, plus a 1/1000 chance an island will generate in a chunk.

![Water world](images/docs/water_world_1.jpg)

![Water world](images/docs/water_world_2.jpg)