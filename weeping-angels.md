---
layout: default
title: Weeping Angels
---

[Jump to video](#video)

# Weeping Angels

TARDISWeepingAngels is a TARDIS plugin module that transforms Minecraft mobs into terrifying
Whovian [monsters](https://www.bbc.co.uk/programmes/articles/4tDN85fyxYXTtVPrCql8jB/monsters).

> __Note__ For TARDIS version 4.13.0 and below you can download the separate TARDISWeepingAngels plugin JAR from
>
here: [http://tardisjenkins.duckdns.org:8080/job/TARDISWeepingAngels/](http://tardisjenkins.duckdns.org:8080/job/TARDISWeepingAngels/) -
_The instructions below are for the TARDIS module, but are pretty much the same, with some extra configuration options (
database / plugin respect), and separate commands instead of sub-commands._

## Enabling the module

To use the Weeping Angels module, it must be enabled in the TARDIS config.

- Type `/tardisconfig weeping_angels [true|false]` to enable or disable the feature.
- A server restart is required when changing the config value.

The TARDISWeepingAngel models and textures are part of
the [TARDIS-Resource-Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/)

## Requirements

**From version 4.0.0 this plugin/module requires Paper server or a compatible fork.**

For the full experience (textures and sounds), you'll need
the [TARDIS-Resource-Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/)

## Monsters

* Weeping Angels
* Cybermen
* Daleks
* Empty Children
* Hath
* Headless Monks
* Ice Warriors
* Judoon
* K9
* Mire
* Ood
* Racnoss
* Sea Devils
* Silent
* Silurians
* Slitheen
* Sontarans
* Strax
* Toclafane
* Vashta Nerada
* Zygons

## Features

#### For each monster:

- Configurable spawn rate
- Configurable drop on death
- Only spawn them in the worlds you want

#### Angels:

- Configurable killing item
- Can be frozen in place for a configurable time
- Configurable TARDIS Key stealing

#### Cybermen:

- Can upgrade villagers and players

## Information

### Weeping Angels

Weeping Angels only spawn at night in loaded chunks. They may also spawn when metioned in chat (if configured) or by building them with blocks.

To build an angel, place four cobblestone walls in a 'T' shape, then place a skeleton skull on the top. If the builder has the permission `tardisweepingangels.build.angel` an angel will spawn.

```
 S
CCC
 C
```

Weeping Angels can only be killed with the configured weapon - by default a DIAMOND_PICKAXE - hitting them with anything
else has no effect. When they die they drop a random (1-3) amount of STONE.

The angels move pretty fast, but you can freeze them in place by looking at them and quickly pressing the sneak key.
Better arm yourself or flee quickly though, as they'll be after you again in a snap - and if they touch you, you'll be
teleported away to a random location. If the TARDIS plugin is also installed, your TARDIS Key will be stolen.

![Weeping Angel](images/docs/weeping_angel.jpg)

Random teleport locations can be specified by world, or you can set specific locations to teleport players to.

* Random teleports are set in the `angels.teleport_worlds` section in _monsters.yml_.
  Add all the worlds you want to allow teleports to. A random location will be generating from loaded chunks in a randomly selected world.
* Specific teleport locations require `angels.teleport_to_location` to be set to `true`. You can then use the `/twa teleport [replace|true|false]` command.
  * You can list multiple locations to teleport to in the `angels.teleport_locations` section in _monsters.yml_.
  * By default one location exists - the spawn location of the server's main world.
  * To add a location, stand in the place you want to add and use the `/twa teleport` command.
  * To replace all previously stored locations, repeat the above step, but use the `/twa teleport replace` command.
  * To toggle between random world and specific location teleporting use the command `/twa teleport [true|false]` - where `true` is for specific locations.

### Cybermen

Cybermen can spawn at anytime. If configured, Cybermen will upgrade villagers and players when they have killed them (a
new Cyberman) spawns in their place. If the upgraded entity was a player, the new Cyberman displays the player's name
above its head.

![Cyberman](images/docs/cyberman.jpg)

### Ice Warriors

Ice Warriors are really angry. They can spawn at anytime, but only spawn in snowy, icy or cold biomes. They carry an ice
dagger. Did I mention they're angry!

![Ice Warrior](images/docs/ice_warrior.jpg)

### Daleks

Daleks come in different colours, but mostly spawn in their typical bronze colour. Exterminate!

![Dalek](images/docs/dalek.jpg)

### Dalek Sec

Dalek Sec just looks fabulous! He's passive unless you annoy him.

![Dalek](images/docs/dalek_sec.jpg)

### Davros

Davros doesn't do much yet, but probably best not to get in his way.

![Dalek](images/docs/davros.jpg)

### Empty Children

Empty Children spawn anytime, and are of course child size. If you are killed by an Empty Child you get a gas mask
applied to your head when you respawn that you can't remove for 30 seconds.

![Empty child](images/docs/empty_child.jpg)

### Hath

Hath don't do much yet, but they look pretty cool.

![Hath](images/docs/hath.jpg)

### Headless Monk

Headless Monks fire energy blasts from their hands as well as channelling that energy into their swords.

![Headless Monk](images/docs/headless_monk.jpg)

![Headless Monk](images/docs/headless_monk_2.jpg)

### Judoon

Judoon are the police force of the Whoniverse. Click an Judoon to claim it as your own. You can equip Judoon with
ammunition (craft with arrows and gunpowder and put into a shulker box, then right click the Judoon with the box).
Judoon can then be toggled to be in guard mode and will shoot any hostile mobs nearby. Use the `/twa follow` command to
make the Judoon follow you around.

![Judoon](images/docs/judoon.jpg)

### K-9

You can either craft a K-9 or tame a wolf to get a K-9! Clicking a K-9 will toggle whether he will follow you or stay
put. The crafting recipe is 3 iron ingots, 3 redstone, and 3 bones in the crafting grid:

```
III
RRR
BBB
```

![K9](images/docs/k9.jpg)

### Mire

The Mire will distill you down to your essential nectar.

![The Mire](images/docs/mire.jpg)

![The Mire](images/docs/mire_helmetless.jpg)

### Ood

Ood spawn randomly around villagers. Click an Ood to claim it as your own. Use the `/twa follow` command to make the Ood
follow you around.

![Ood](images/docs/ood.jpg)

### Racnoss

Racnoss spawn randomly in the Nether.

![Racnoss](images/docs/racnoss.jpg)

### Sea Devils

Found in the sea and on land, best if they don't find you!

![Sea Devil](images/docs/sea_devil.jpg)

### Silurians

Only spawn underground in caves. Watch out for their Silurian guns!

![Silurian](images/docs/silurian.jpg)

### Slitheen

A nasty flatulent surprise, best avoided if you want to survive.

![Slitheen](images/docs/slitheen.jpg)

![Slitheen](images/docs/slitheen_suit.jpg)

### Sontarans

Sontarans will try to kill you (as any good Sontaran should). If you manage to right-click a Sontaran with a Weakness
Potion before he kills you, he will transform into Strax.

![Sontaran](images/docs/sontaran.jpg)

### Strax

If you right-click Strax he'll talk to you, and if you right-click him with an empty bucket, you'll be able to milk him.
Yum, yum Sontaran lactic fluid :) Be careful not to anger him though as he'll go rabid on you and let his killer
Sontaran instincts get the better of him!

![Strax](images/docs/strax.jpg)

### Toclafane

Toclafane fly around passively until you hit them, then watch out as they get angry with you. They explode when they
die.

![Toclafane](images/docs/toclafane.jpg)

### Vashta Nerada

Vashta Nerada have a random (configurable) chance of spawning when a bookshelf is broken, say "Hey who turned out the
lights?" and of course try to eat you!

![Vashta Nerada](images/docs/vashta_nerada.jpg)

### Zygons

Zygons don't do much yet (except try to kill you), but they look pretty cool.

![Zygon](images/docs/zygon.jpg)

## Commands

| Command | Arguments                             | Description                                                                 |
|---------|---------------------------------------|-----------------------------------------------------------------------------|
| `/twa`  | `spawn [monster type]`                | Spawn a monster on the block you are looking at                             |  
|         | `disguise [monster type] [on:off]`    | Disguise yourself as a TWA monster                                          |
|         | `equip [monster type]`                | Equip an armor stand with a TWA monster                                     |
|         | `count [monster type] [world]`        | Reports the current number of monsters in the specified world               |
|         | `kill [monster type] [world]`         | Kills all of the monsters in the specified world                            |
|         | `set [monster type] [world] [amount]` | Sets the maximum number of the monsters that are allowed to spawn the world |
|         | `follow`                              | Makes the Ood/Judoon/K9 you are targeting follow you                        |
|         | `stay`                                | Makes the Ood/Judoon/K9 you are targeting _stop_ following you              |
|         | `remove`                              | Removes the Ood/Judoon/K9 you are targeting                                 |
|         | `give [player] [monster type]`        | Gives a player a monster head to display in an item frame                   |

## Configuration

The default config is shown below:

```
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
  dalek_sec_chance: 5
  davros_chance: 5
daleks:
  worlds:
    world: 10
  drops:
    - SLIME_BALL
    - ROTTEN_FLESH
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

The `spawn_rate` section sets monster spawning options

- `how_many` sets how many monsters to spawn each time.
- `how_often` is the time period (in server ticks — _20 ticks = 1 second_) between spawn attempts.
- `max_per_world` is the maximum number of monsters can be alive at one time.

The `worlds` section allows you to list the worlds you want the monsters to spawn in.

`drops` sets a list of items that drop when the monster is killed.

#### Angel specific

`freeze_time` is the length of time (in server ticks) that the Angels remain motionless for.

`weapon` sets the item that will kill a Weeping Angel.

`angels_can_steal` sets whether the Angels can steal your TARDIS Key — requires the TARDIS plugin to be installed.

`teleport_to_location` sets whether angles teleport players to random worlds or specific locations.

`teleport_locations` a list of one or more specific teleport locations to use.

`teleport_worlds` a list of one or more worlds to for random teleport locations.

`spawn_from_chat` sets whether mentioning _weeping angels_ in chat has a chance to spwan an angel nearby.

`can_build` sets whether angels can be built using a specific sequence of blocks (like a snow golem).

#### Cybermen specific

`can_upgrade` sets whether the Cybermen can upgrade players and villagers.

`dalek_sec_chance` sets the chance that Dalek sec will spawn instead of a regular Dalek

`davros_chance` sets the chance that Davros will spawn instead of a regular Dalek

#### Headless Monk specific

`projectile` sets the item used a the monks projectile

`particles` sets whether monks have a flaming sword

#### Judoon specific

`guards` sets whether Judoon can serve as personal body guards

`can_build` sets whether Judoon can be built using a specific sequence of blocks (like a snow golem).

`ammunition` sets the default amount of ammunition Judoon can carry

`damage` sets the amount of damage a Judoon projectile will cause when hitting entities

#### K-9 specific

`by_taming` sets the chance that K-9 will appear instead of a tamed wolf

`can_build` sets whether K-9 can be built using a specific sequence of blocks (like a snow golem).

#### Ood specific

`spawn_from_villager` sets the chance that an Ood will spawn instead of a villager

`spawn_from_cured` sets the chance that an Ood will spawn instead of a cured villager

#### Sontaran specific

`can_tame` sets whether sontarans can to tamed into Strax

#### Toclafane specific

`spawn_from_bee` sets the chance that Toclaface will spawn instead of a bee
  
`destroy_blocks` sets whether toclafane explosions will destroy blocks

### Video

<iframe width="600" height="366" src="https://www.youtube.com/embed/Ybpo4KQZpF4?rel=0" frameborder="0" allowfullscreen></iframe>
