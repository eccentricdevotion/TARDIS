---
layout: default
title: Monsters configuration
---

# Monsters configuration

The default _monsters.yml_ configuration is shown below:

```yaml
config_version: 2.0
spawn_rate:
  how_many: 2
  how_often: 400
  default_max: 0
angels:
  worlds:
    world: 10
  teleport_to_location: false
  teleport_locations:
    - world,0,64,0
  teleport_worlds:
    - world
  freeze_time: 100
  weapon: DIAMOND_PICKAXE
  drops:
    - COBBLESTONE
    - STONE
  angels_can_steal: true
  can_build: true
  spawn_from_chat:
    enabled: true
    chance: 50
    distance_from_player: 10
cybermen:
  worlds:
    world: 10
  drops:
    - REDSTONE
    - STONE_BUTTON
  can_upgrade: true
daleks:
  worlds:
    world: 10
  drops:
    - SLIME_BALL
    - ROTTEN_FLESH
  dalek_sec_chance: 5
  dalek_sec_drops:
    - VERDANT_FROGLIGHT
    - LEATHER_CHESTPLATE
  davros_chance: 5
  davros_drops:
    - CHAIN
    - CRIMSON_BUTTON
empty_child:
  worlds:
    world: 10
  drops:
    - COOKED_BEEF
    - SUGAR
hath:
  worlds:
    world: 10
  drops:
    - SALMON
    - STONE_PICKAXE
headless_monks:
  worlds:
    world: 10
  drops:
    - BOOK
    - RED_CANDLE
  projectile: SMALL_FIREBALL
  particles: true
ice_warriors:
  worlds:
    world: 10
  drops:
    - ICE
    - PACKED_ICE
    - SNOW_BLOCK
judoon:
  worlds:
    world: 10
  guards: true
  can_build: true
  ammunition: 25
  damage: 4
k9:
  by_taming: true
  can_build: true
  worlds:
    world: true
ood:
  worlds:
    world: true
  drops:
    - NAME_TAG
  spawn_from_villager: 20
  spawn_from_cured: 5
racnoss:
  worlds:
    world_nether: 5
  drops:
    - NETHERITE_INGOT
    - ECHO_SHARD
sea_devils:
  worlds:
    world: 10
  drops:
    - COD
    - KELP
silent:
  worlds:
    world: 10
  drops:
    - INK_SAC
    - FLOWER_POT
silurians:
  worlds:
    world: 10
  drops:
    - GOLD_NUGGET
    - FEATHER
slitheen:
  worlds:
    world: 10
  drops:
    - RABBIT_HIDE
    - PHANTOM_MEMBRANE
sontarans:
  worlds:
    world: 10
  drops:
    - POTATO
    - POISONOUS_POTATO
  can_tame: true
the_mire:
  worlds:
    world: 10
  drops:
    - POTION
    - HONEY_BOTTLE
toclafane:
  worlds:
    world: 10
  spawn_from_bee: 5
  destroy_blocks: true
  drops:
    - GUNPOWDER
    - HONEYCOMB
vashta_nerada:
  worlds:
    world: 10
  drops:
    - BONE
    - LEATHER
zygons:
  worlds:
    world: 10
  drops:
    - PAINTING
    - SAND
```
 ## Spawn rates

The `spawn_rate` section sets monster spawning options

- `how_many` sets how many monsters to spawn each time.
- `how_often` is the time period (in server ticks — _20 ticks = 1 second_) between spawn attempts.
- `max_per_world` is the maximum number of monsters can be alive at one time.

## For each monster type

The `worlds` section allows you to list the worlds you want the monsters to spawn in.

`drops` sets a list of items that drop when the monster is killed.

## Angel specific

`freeze_time` is the length of time (in server ticks) that the Angels remain motionless for.

`weapon` sets the item that will kill a Weeping Angel.

`angels_can_steal` sets whether the Angels can steal your TARDIS Key — requires the TARDIS plugin to be installed.

`teleport_to_location` sets whether angles teleport players to random worlds or specific locations.

`teleport_locations` a list of one or more specific teleport locations to use.

`teleport_worlds` a list of one or more worlds to for random teleport locations.

`spawn_from_chat` sets whether mentioning _weeping angels_ in chat has a chance to spwan an angel nearby.

`can_build` sets whether angels can be built using a specific sequence of blocks (like a snow golem).

## Cybermen specific

`can_upgrade` sets whether the Cybermen can upgrade players and villagers.

## Dalek specific

`dalek_sec_chance` sets the chance that Dalek Sec will spawn instead of a regular Dalek.

`davros_chance` sets the chance that Davros will spawn instead of a regular Dalek.

## Headless Monk specific

`projectile` sets the item used as the monks projectile.

`particles` sets whether monks have a flaming sword.

## Judoon specific

`guards` sets whether Judoon can serve as personal bodyguards.

`can_build` sets whether Judoon can be built using a specific sequence of blocks (like a snow golem).

`ammunition` sets the default amount of ammunition Judoon can carry.

`damage` sets the amount of damage a Judoon projectile will cause when hitting entities.

## K-9 specific

`by_taming` sets the chance that K-9 will appear instead of a tamed wolf.

`can_build` sets whether K-9 can be built using a specific sequence of blocks (like a snow golem).

## Ood specific

`spawn_from_villager` sets the chance that an Ood will spawn instead of a villager.

`spawn_from_cured` sets the chance that an Ood will spawn instead of a cured villager.

## Sontaran specific

`can_tame` sets whether sontarans can to tamed into Strax.

## Toclafane specific

`spawn_from_bee` sets the chance that Toclaface will spawn instead of a bee.

`destroy_blocks` sets whether toclafane explosions will destroy blocks.
