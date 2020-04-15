---
layout: default
title: Achievements
---

Achievements
============

Players can gain rewards for TARDIS achievements. The achievement system is mostly automatic, and works in conjunction with TARDIS [books](books.html). If using a Spout enabled server/client combination, then an actual Minecraft achievement popup is used :)

Players can get the following achievements.

*   **tardis** — awarded when they create their TARDIS
*   **travel** — awarded for travelling the configured distance in their TARDIS (Note: only distances travelled from point A to point B **in the same world** count towards this achievement)
*   **rooms** — awarded after growing one of each enabled room type
*   **farm** — awarded after farming one of each mob type
*   **energy** — awarded for overcharging the Artron Energy Capacitor by the configured amount
*   **friends** — awarded for adding the configured amount of companions
*   **kill** — awarded for killing a charged creeper

Players can list achievements with the `/tardisbook list` command. This shows the name of the achievement/book and the reward type and amount.

Customising achievements
------------------------

You can edit the _plugins/TARDIS/achievements.yml_ file to change the attributes of each achievement. An achievement looks like this:

    rooms:
      name: Room freak
      description: Grow all room types
      required: 1
      reward_type: XP
      reward_amount: 50
      enabled: true
      repeatable: false
      auto: true
      message: Grew a truckload of rooms
      icon: BOOKSHELF

*   The achievement (in this example `rooms`) matches the name of the book the player gets to read about the achievement
*   `name:` is used as the title of the book
*   `description:` is not currently used
*   `required:` is the amount of the goal the player needs to gain e.g. for the ‘travel’ achievement this is set to 100000 — the number of blocks they need to travel
*   `reward_type:` what sort of reward to give the player when they reach the achievement goal. This can be:
    *   `XP`
    *   An item specified by the [Bukkit material enum](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/Material.java) name
*   `reward_amount:` the amount of the reward type the player gets
*   `enabled:` a true of false value determining whether the achievement is available to players
*   `repeatable:` a true of false value determining whether the achievement can be repeated
*   `auto:` if `true` the player does not have to manually initiate the start of the achievement with the `/tardisbook start` command
*   `message:` is the second line of the achievement notification
*   `icon:` is material icon used in the achievement notification
