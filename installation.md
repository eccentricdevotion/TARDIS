---
layout: default
title: Installation
---

[Jump to video](#video)

# Installation

Get the TARDIS plugin files from: [http://dev.bukkit.org/bukkit-plugins/tardis](http://dev.bukkit.org/bukkit-plugins/tardis)

* * *

You install TARDIS just like you would any other Bukkit plugin, by dropping it into the Bukkit plugins folder and starting the server. TARDIS depends on a small library of code that is found in another plugin: TARDISChunkGenerator.jar — you can find this under _Additional Files_ on the TARDIS download page. The TARDIS database, configuration, schematic and map files will be created automatically.

Some versions of the TARDIS plugin are backwards compatible with previous versions of CraftBukkit (version 2.5.8 is compatible with Tekkit Classic Server), for the plugin to have full functionality and work correctly you should have CraftBukkit / Spigot / Paper version `1.15.2-R0.1` or higher.

The TARDIS plugin has evolved into a complex beast, with configurable recipes, and other things that unexperienced server administrators may have trouble with. If you have difficulties, create a ticket on the [plugin page](http://dev.bukkit.org/bukkit-plugins/tardis/tickets).

### See also:

TARDIS [Time Vortex](time-vortex.html) shared world setup.

### Installation:

Place the following jar files in your plugins folder:

    TARDIS.jar
    TARDISChunkGenerator.jar

You will most probably need to run a permissions plugin for players to actually use the TARDIS (it’s possible without one, but players will need to be opped!). As a minimum players will generally need the `tardis.use` permission to build a TARDIS, and `tardis.enter` to be a companion.

If this is your first time using the plugin, you may want to set the TARDIS difficulty mode to _easy_. To do this use the in-game command: `/tardisadmin difficulty easy`.

#### Upgrade notes:

It is recommended to delete the TARDIS `schematics` folder and rooms.yml before an upgrade!

New configuration options and database fields will be added automatically, and if any major changes occur, TARDIS will backup the relevant files before attempting to upgrade them.

## Enhancing the TARDIS plugin

While not essential to make the TARDIS plugin work, the following plugins will make your Time Travelling experience even better.

1. Install [WorldGuard](http://dev.bukkit.org/bukkit-plugins/worldguard) to automatically protect your TARDIS from griefing.
2. Install a permissions plugin, so that you can restrict or grant players the rights to use TARDIS’ different features. We like [PermissionsBukkit](http://dev.bukkit.org/bukkit-plugins/permbukkit/).
3. Install [TARDISWeepingAngels](weeping-angels.html) for a more Who-like experience!
4. Install [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) so that the TARDIS Zero Room is completely dettached from the universe. Also required by LibsDisguises below.
5. Install [LibsDisguises](http://www.spigotmc.org/resources/libs-disguises.81/) so that you can use the Genetic Manipulator / Immortality Gate.
6. Install [Dynmap-TARDIS](http://dev.bukkit.org/bukkit-plugins/dynmap-tardis/) so that you show real-time TARDIS locations on the Dynmap plugin map.

### Video
<iframe src="https://player.vimeo.com/video/58356201" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>

&nbsp;

## Install notes:

If upgrading from a previous version of TARDIS that did not have generated worlds, or enabling them for the first time, please delete your previously created TARDISes and recreate them.

If upgrading from a version without an Artron Energy button or handbrake, please place these items, and use the `/tardis update` command to add them to your TARDIS. See [Commands](tardis-commands.html#update) for more information.
