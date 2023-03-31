---
layout: default
title: Vortex Manipulator
---

[Jump to videos](#videos)

# Vortex Manipulator

> "Vortex manipulator. Cheap and nasty time travel. Very bad for you. I'm trying to give it up."

TARDISVortexManipulator is one of the TARDIS plugin modules that brings
the [Vortex Manipulator](https://tardis.fandom.com/wiki/Vortex_manipulator) to Minecraft. Travel like Jack Harkness or
River Song from Doctor Who! Have the Vortex Manipulator in your hand, put in your desired location, and GO!

> __Note__ For TARDIS version 4.13.0 and below you can download the separate TARDISVortexManipulator plugin JAR
> from [http://tardisjenkins.duckdns.org:8080/job/TARDISVortexManipulator/](http://tardisjenkins.duckdns.org:8080/job/TARDISVortexManipulator/) -
  _The instructions below are for the TARDIS module, but are pretty much the same ,with some extra configuration options (
  database / plugin respect), and separate commands instead of sub-commands._

## Enabling the module

To use the Weeping Angels module, it must be enabled in the TARDIS config.

- Type `/tardisconfig vortex_manipulator [true|false]` to enable or disable the feature.
- A server restart is required when changing the config value.



The TARDISVortexManipulator models and textures are part of
the [TARDIS-Resource-Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/)

## Requirements

For the full experience, you'll need
the [TARDIS-Resource-Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/)

## Crafting

Crafting is a little complicated, as it's an advanced piece of technology. Requirements for crafting are (left to right,
top row first): stone button, another stone button, glass; clock, gold, compass; iron ingot, iron ingot, iron ingot.
Custom graphics for the crafted item and GUI are available as part of
the [TARDIS-Resource-Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/).

![Crafting](images/docs/vortex_manipulator.jpg)

![Item](images/docs/manipulator.png)

## Multi-world support

The plugin works with Multiverse (or other multi-word plugin), remembering personally saved locations and allowing for
direct teleporting to any available world and coordinate. Also having respect for World Borders (plugin and the 1.8
default if possible). Towny/Faction/WorldGuard/GriefPrevention suport can be enabled via the config.

## Commands

| Command                              | Description                                                                                                                                       |
|--------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| __Help__                             |                                                                                                                                                   |
| `/vm help {topic}`                   | Display help and commands for the plugin                                                                                                          |        
| __Activate__                         |                                                                                                                                                   |
| `/vm activate {player}`              | Activate a Vortex Manipulator that has been given with the /tardisgive command                                                                    |        
| __Travel__                           |                                                                                                                                                   |
| `/vm go {worldname}`                 | Teleport to a random location in the specified world                                                                                              |
| `/vm go {worldname} {X} {Y} {Z}`     | Teleport to a specific location in the specified world                                                                                            |
| `/vm go`                             | Teleport to random world, random location. If another player is standing on the same block as you, they will teleport with you, using more energy |
| `/vm go {custom}`                    | Teleport to a saved location                                                                                                                      |
| __Saves__                            |                                                                                                                                                   |
| `/vm save {custom}`                  | Save a specific location                                                                                                                          |
| `/vm remove {custom}`                | Remove a saved location                                                                                                                           |
| __Messaging__                        |                                                                                                                                                   |
| `/vm message msg {player} {message}` | Send a message to another user with a device                                                                                                      |
| `/vm message list {in/out} [page]`   | See a list of received or sent messages                                                                                                           |
| `/vm message read {#}`               | Read a specific message                                                                                                                           |
| `/vm message delete {#}`             | Delete a specific message                                                                                                                         |
| `/vm message clear {in/out}`         | Clear the specified mail box                                                                                                                      |
| __Lifesigns__                        |                                                                                                                                                   |
| `/vm lifesigns`                      | List entities nearby, mobs and players                                                                                                            |
| `/vm lifesigns {player}`             | Gets the health, hunger and oxygen level of a player                                                                                              |
| __Beacon__                           |                                                                                                                                                   |
| `/vm beacon`                         | Send out a beacon signal that lasts until the player moves                                                                                        |
| __GUI__                              |                                                                                                                                                   | 
| `/vm gui`                            | Open the Vortex Manipulator GUI                                                                                                                   | 
| __Give__                             |                                                                                                                                                   | 
| `/vm give {player} {amount}`         | Give Tachyon energy to a player's Vortex Manipulator                                                                                              | 

## Permissions

* `vm.teleport` - allow crafting and using of a Vortex Manipulator
* `vm.message` - for messaging system
* `vm.lifesigns` - detect lifesigns and info on players
* `vm.beacon` - activate the beacon feature

## Configuration

The _vortex_manipulator.yml_ config is shown below:

```yaml
allow:
  teleport: true
  messaging: true
  lifesigns: true
  beacon: true
  multiple: true
  look_at_block: true
recipe:
  shape: BBG,WOC,III
  ingredients:
    B: STONE_BUTTON
    G: GLASS
    W: WATCH
    O: GOLD_INGOT
    C: COMPASS
    I: IRON_INGOT
  result: WATCH
  amount: 1
  lore: "Cheap and nasty time travel"
tachyon_use:
  max: 1000
  recharge: 25
  recharge_interval: 600
  travel:
    random: 100
    world: 150
    coords: 200
    saved: 50
  lifesigns: 15
  beacon: 1000
  message: 5
date_format: dd/MM/YY HH:mm
lifesign_scan_distance: 16
max_look_at_distance: 50
block_travel_malfunction_chance: 0
```

### Video

<iframe width="600" height="366" src="https://www.youtube.com/embed/broKxj7z3cI?rel=0" frameborder="0" allowfullscreen></iframe>

<iframe width="600" height="366" src="https://www.youtube.com/embed/mYq9hOWmVS8?rel=0" frameborder="0" allowfullscreen></iframe>
