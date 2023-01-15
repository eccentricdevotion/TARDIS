---
layout: default
title: Dynmap-TARDIS
---

# Dynmap-TARDIS

If you have Dynmap installed on your server, you can use the Dynmap-TARDIS add-on to display the real-time locations of all TARDISes.

As of TARDIS v4.6.2 the Dynmap-TARDIS add-on has been integrated into the main TARDIS plugin. To enable/disable TARDISes in dynmap, set `dynmap.enabled: true\false` in the TARDIS config or run the command `/tardisconfig dynmap true|false`.

### Configuration options

| Option                                     | Type                                                                                                        | Default Value |
|--------------------------------------------|-------------------------------------------------------------------------------------------------------------|---------------|
| dynmap:                                    |
| ---                                        |
| &nbsp;&nbsp;&nbsp;&nbsp;`enabled`          | boolean                                                                                                     | `true`        |
| &nbsp;                                     | Sets whether to display TARDISes in Dynmap (requires Dynmap to be installed on the server).                 |
| &nbsp;&nbsp;&nbsp;&nbsp;`update_period`    | integer                                                                                                     | `30`          |
| &nbsp;                                     | Sets the interval in seconds between TARDIS marker updates, try setting this higher if you experience lag.  |
| &nbsp;&nbsp;&nbsp;&nbsp;`updates_per_tick` | integer                                                                                                     | `10`          |
| &nbsp;                                     | Sets the maximum number of TARDIS markers to update per tick, try setting this lower if you experience lag. |

## Dynmap-TARDIS plugin (for TARDIS version below 4.6.2)

You can download the Dynmap-TARDIS plugin from here: [http://dev.bukkit.org/bukkit-plugins/dynmap-tardis/](http://dev.bukkit.org/bukkit-plugins/dynmap-tardis/)

### Requirements

- TARDIS version 2.8-beta-1 build #654 or higher
- Dynmap (Dynmap-TARDIS is compiled against Dynmap 1.9.2)

### Installation

1. Install Dynmap as per the instructions found on the [Dynmap](http://dev.bukkit.org/bukkit-plugins/dynmap/) page. Run the server once to generate the default Dynmap files and folders.
2. Drop the _Dynmap-TARDIS.jar_ file in the server plugins folder.
3. Put the [tardis.png](https://github.com/eccentricdevotion/Dynmap-Tardis/blob/master/tardis.png?raw=true%0A) icon file ( ![tardis icon](https://github.com/eccentricdevotion/Dynmap-Tardis/blob/master/tardis.png?raw=true)) into the _plugins/dynmap/web/tiles/\_markers\_/_ folder.
4. Run the command `/dmarker addicon id:tardis newlabel:tardis file:plugins/dynmap/web/tiles/_markers_/tardis.png`
5. Stop and start the server.
6. Render the map.

![Dynmap-TARDIS](images/docs/dynmap-tardis.jpg)

