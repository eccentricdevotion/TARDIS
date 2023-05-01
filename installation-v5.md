---
layout: default
title: Installation
---

[Jump to video](#video)

# Installation

Get the latest TARDIS plugin file
from: [http://tardisjenkins.duckdns.org:8080/view/TARDIS/](http://tardisjenkins.duckdns.org:8080/view/TARDIS/)

* * *

You install TARDIS just like you would any other Spigot/Paper plugin, by dropping it into the Bukkit plugins folder and starting the server.

> For versions before 5.0.0, see [Legacy installation](installation-old.html)

The TARDIS database, configuration, schematic and advancement files will be created automatically.

For the plugin to have full functionality and work correctly you should have the latest Spigot / Paper server (currently version `1.19.4-R0.1`).

The TARDIS plugin has evolved into a complex beast, with configurable recipes, and other things that unexperienced server administrators may have trouble with. If you have difficulties, create a ticket on the [issues page](https://github.com/eccentricdevotion/TARDIS/issues).

### Installation:

Place the following `TARDIS.jar` file in your plugins folder:


You will most probably need to run a permissions plugin for players to actually use the TARDIS (it’s possible without one, but players will need to be opped!). As a minimum players will generally need the `tardis.use` permission to build a TARDIS, and `tardis.enter` to be a companion.

If this is your first time using the plugin, you may want to set the TARDIS difficulty mode to _easy_. To do this use the in-game command: `/tardisconfig difficulty easy`.

### Enhancing the TARDIS plugin

While not essential to make the TARDIS plugin work, the following plugins will make your Time Travelling experience even better.

1. Install [WorldGuard](https://enginehub.org/worldguard) to automatically protect your TARDIS from griefing.
2. Install a permissions plugin, so that you can restrict or grant players the rights to use TARDIS’ different features.
   We like [LuckPerms](https://luckperms.net/).
3. (Optional **§**) Install [LibsDisguises](https://www.spigotmc.org/resources/libs-disguises-free.81/) so that you can use the Genetic Manipulator / Immortality Gate. **§ Note** _TARDIS can now create its own disguises, but LibsDisguises does a much better job of it_!
4. Install [TARDIS-Resource-Pack](resource-packs.html) so that you see re-textured TARDIS blocks and items, and hear TARDIS sounds.


### See also:

* If upgrading from a previous version of TARDIS, see [Updating from v4.x.x to v5.x.x](updating-to-unified-plugin.html) for more information.
* TARDIS [Time Vortex](time-vortex.html) shared world setup.