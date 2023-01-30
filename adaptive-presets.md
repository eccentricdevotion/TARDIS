---
layout: default
title: Biome Adaptive Chameleon Presets
---

# Biome Adaptive Chameleon Presets

When the Chameleon Circuit is set to Adaptive Biome mode, the TARDIS exterior preset is selected based on the biome that the TARDIS travels to.

You can configure which preset is used for which biome by editing _adaptive.yml_.
The file uses YAML aliases - for example:

```yaml
# snowy biomes
SNOWY_TAIGA: &snowy COLD_TAIGA
SNOWY_SLOPES: *snowy
```

`&snowy` defines an _alias_ to the `COLD_TAIGA` preset
`*snowy` is an _instance_ of the alias

You can either change the alias `&snowy` to another Chameleon preset e.g. `&snowy PINE`, and the other instances will use that as well, or set a preset for each individual biome.

You can use any Chameleon block preset as well as the biome presets shown below.

> __Note:__ You can only use an `&`alias if it has been defined in the file above where you want to use the `*`instance - so to change _every_ snowy biome you would need to set the alias up in the `# frozen biomes` section first.


## Biomes and presets

By default the plugin maps biomes to presets in the follwing way:

### Beach, frozen river, river, snowy beach, stony shore
BOAT

![Boat](images/docs/adaptive_beach.jpg)

### Cold ocean, deep cold ocean, deep lukewarm ocean, deep ocean, lukewarm ocean, ocean, warm ocean
YELLOW

![Yellow Submarine](images/docs/adaptive_sub.jpg)

### Desert
DESERT

![Desert](images/docs/adaptive_desert.jpg)

### Windswept gravelly hills, windswept hills, windswept forest
EXTREME_HILLS

![Extreme Hills](images/docs/adaptive_hills.jpg)

### Birch forest, forest, old growth birch forest
FOREST

![Forest](images/docs/adaptive_forest.jpg)

### Nether wastes, soul sand valley, crimson forest, warped forest, basalt deltas
NETHER

![Nether](images/docs/adaptive_nether.jpg)

### Snowy plains, deep frozen ocean, frozen ocean jagged peaks, snowy peaks
ICE_FLATS

![Ice Flats](images/docs/adaptive_ice_flats.jpg)

### Ice spikes
ICE_SPIKES

![Ice Spikes](images/docs/adaptive_spikes.jpg)

### Jungle, sparse jungle, bamboo jungle
JUNGLE

![Jungle](images/docs/adaptive_jungle.jpg)

### Badlands, wooded badlands, eroded badlands
MESA

![Mesa](images/docs/adaptive_mesa.jpg)

### Mushroom fields
SHROOM

![Shroom](images/docs/adaptive_shroom.jpg)

### Plains, sunflower plains, meadow
PLAINS

![Plains](images/docs/adaptive_plains.jpg)

### Dark forest, flower forest
ROOFED_FOREST

![Roofed Forest](images/docs/adaptive_dark.jpg)

### Savanna, windswept savanna, savanna plateau
SAVANNA

![Savanna](images/docs/adaptive_savanna.jpg)

### Swamp, mangrove swamp
SWAMP

![Swamp](images/docs/adaptive_swamp.jpg)

### End barrens, End highlands, End midlands, small End islands, The End
THEEND

![The End](images/docs/adaptive_the_end.jpg)

### OLd growth spruce taiga, taiga, old growth pine taiga
TAIGA

![Taiga](images/docs/adaptive_taiga.jpg)

### Snowy taiga, snowy slopes, grove
COLD_TAIGA

![Cold Taiga](images/docs/adaptive_cold_taiga.jpg)

