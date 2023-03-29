---
layout: default
title: Installation
---

[Jump to video](#video)

# Installation

Get the latest TARDIS plugin files
from: [http://tardisjenkins.duckdns.org:8080/view/TARDIS/](http://tardisjenkins.duckdns.org:8080/view/TARDIS/)

* * *

You install TARDIS just like you would any other Spigot/Paper plugin, by dropping it into the Bukkit plugins folder and
starting the server. TARDIS depends on a small library of code that is found in another plugin: TARDISChunkGenerator.jar
— you can find this on the
TARDISChunkGenerator [download page](http://tardisjenkins.duckdns.org:8080/view/TARDISChunkGenerator/).
The TARDIS database, configuration, schematic and advancement files will be created automatically.

{#tekkit}
Some versions of the TARDIS plugin are backwards compatible with previous versions of Spigot (version 2.5.8 is
compatible
with Tekkit Classic Server), for the plugin to have full functionality and work correctly you should have Spigot / Paper
version `1.19.3-R0.1` or higher.

The TARDIS plugin has evolved into a complex beast, with configurable recipes, and other things that unexperienced
server
administrators may have trouble with. If you have difficulties, create a ticket on
the [issues page](https://github.com/eccentricdevotion/TARDIS/issues).

### See also:

TARDIS [Time Vortex](time-vortex.html) shared world setup.

### Installation:

Place the following jar files in your plugins folder:

    TARDIS.jar
    TARDISChunkGenerator.jar

You will most probably need to run a permissions plugin for players to actually use the TARDIS (it’s possible without
one,
but players will need to be opped!). As a minimum players will generally need the `tardis.use` permission to build a
TARDIS,
and `tardis.enter` to be a companion.

If this is your first time using the plugin, you may want to set the TARDIS difficulty mode to _easy_. To do this use
the
in-game command: `/tardisconfig difficulty easy`.

## Enhancing the TARDIS plugin

While not essential to make the TARDIS plugin work, the following plugins will make your Time Travelling experience even
better.

1. Install [WorldGuard](https://enginehub.org/worldguard) to automatically protect your TARDIS from griefing.
2. Install a permissions plugin, so that you can restrict or grant players the rights to use TARDIS’ different features.
   We like [LuckPerms](https://luckperms.net/).
3. Install [TARDISWeepingAngels](weeping-angels.html) for a more Who-like experience!
4. (Optional **§**) Install [LibsDisguises](https://www.spigotmc.org/resources/libs-disguises-free.81/) so that you can
   use the Genetic Manipulator / Immortality Gate. **§ Note** _TARDIS can now create its own disguises, but
   LibsDisguises
   does a much better job of it_.
5. Install [TARDIS-Resource-Pack](resource-packs.html) so that you see re-textured TARDIS blocks and items, and hear
   TARDIS sounds.

### Video

<iframe src="https://player.vimeo.com/video/58356201" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>

&nbsp;

## Install notes:

If upgrading from a previous version of TARDIS that did not have generated worlds, or enabling them for the first time,
please delete your previously created TARDISes and recreate them.

If upgrading from a version without an Artron Energy button or handbrake, please place these items, and use
the `/tardis update`
command to add them to your TARDIS. See [Commands](tardis-commands.html#update) for more information.
