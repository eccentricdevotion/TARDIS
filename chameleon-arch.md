---
layout: default
title: Chameleon Arch
---

# Chameleon Arch

The Tenth Doctor used a Chameleon Arch to change himself into the human John Smith to elude the Family of Blood. You can craft a fob watch to change your self from a Time Lord in a regular player (a Steve — albeit with a random name).

- To see the custom fob watch texture you need to use the TARDIS-MCP resource pack, and an MCPatcher modded client.
- To use the Chameleon Arch feature, the server must have [LibsDisguises](http://ci.md-5.net/job/LibsDisguises/) installed.
- Players must have the `tardis.chameleonarch` permission.

![Fob watch](images/docs/fob_watch.jpg)

To view the fob watch recipe type:

    /tardisrecipe watch

### Chameleon Arch use

Once you have crafted the fob watch, you can put it to use!

1. Right-click-AIR with the watch
  - Your health is reduced to 10% — _when Martha Jones asked if the transformation into a human would be painful, the Tenth Doctor simply responded “Oh yeah; it hurts”, implying any change in species caused a great deal of pain_
  - You are given random name
  - The Steve skin is applied
  - Your current Time Lord inventory and armour is saved, and you are given a new empty inventory
2. Do stuff… like trick people, become a farmer or join a village…
3. Try to pilot your TARDIS :(
  - Unfortunately the TARDIS no longer recognises that you are a Time Lord so denies you access to the controls
4. Try to select fob watch in hotbar, even if you can when you try to…
5. Right-click-AIR to ‘de-fob’
  - You can’t until the configured minimum ‘fobbed’ time requirement has been met

When ‘de-fobbing’ your Time Lord inventory and armour is restored.

If configured (see below), a player may lose both ‘fobbed’ and time Lord inventories when they die.

If you log off while ‘fobbed’ the amount of time you have been ‘fobbed’ for is recorded, when you log back in you will still need to meet the minimum time requirement.

### Chameleon Arch commands

There are two commands associated with ‘fobbing’.

    /tardis arch_time

Shows the player how much time left they have before they can ‘de-fob’

    /tardisadmin arch [fake arch name]

Admins can use this to show the ‘fobbed’ player’s real name  
**Note:** command usage is still logged with the player’s real name.

### Chameleon Arch configuration

Server admins can change the Chameleon Arch options.

    arch:
        enabled: [true|false]
        min_time: [real time in minutes]
        switch_inventory: [true|false]
        clear_inv_on_death: [true|false]

`enabled` — whether the feature is even enabled — default `true`, restart required for change to take effect

`min_time` — how long a player must remain ‘fobbed’ — default `20` minutes (1 Minecraft day)

`switch_inventory` — whether to switch/clear the player’s inventory — default `true` (be careful enabling this if using other inventory management plugins…)

`clear_inv_on_death` — whether to clear (both) ‘fobbed’ and saved Time Lord inventories on death — default `false`
