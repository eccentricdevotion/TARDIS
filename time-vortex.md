---
layout: default
title: TARDIS_TimeVortex world
---

# TARDIS\_TimeVortex world

By default the plugin will set up a special world that will allow you to store all the TARDISes in it and still be able to grow rooms, abandon TARDISes, be easier on server resources and make management of TARDISes easier. The world is called **TARDIS\_TimeVortex** and uses TARDISChunkGenerator to create a void type environment.

The plugin will automatically try to create the world when it first starts up (as long as a multi-world plugin is found).

### If you need to set up the world manually:

1. Create a new world with your multi-world plugin
  - For [Multiverse-Core](https://github.com/Multiverse/Multiverse-Core/wiki/Command-Reference#wiki-create) use the command:  
`/mv create TARDIS_TimeVortex normal -g TARDISChunkGenerator`
  - For Multiword use the command:  
`/mw create TARDIS_TimeVortex plugin:TARDISChunkGenerator`
2. Set up any world flags as you see fit
  - For [Multiverse-Core](https://github.com/Multiverse/Multiverse-Core/wiki/Command-Reference#wiki-modify_set) use the command(s):  
`/mv modify set monstersrate 0 TARDIS_TimeVortex
                        /mv modify set hidden true TARDIS_TimeVortex`  
 etc
  - For [Multiword](http://dev.bukkit.org/bukkit-plugins/multiworld-v-2-0/pages/flags/) use the command(s):  
`/mw setflag TARDIS_TimeVortex SpawnMonster false
                        /mw setflag TARDIS_TimeVortex PvP false`  
 etc
3. Set TARDIS config options as shown below:
```
    /tardisadmin default_world true
    /tardisadmin default_world_name TARDIS_TimeVortex
    /tardisadmin include_default_world false
    /tardisadmin create_worlds false
```
As of TARDIS v2.7-beta-1 the plugin now uses a system called [T.I.P.S](tips.html) (TARDIS Interior Positioning System). This system places new TARDISes in a specific ‘plot’ in the TARDIS\_TimeVortex World. This means that TARDISes will no longer overlap other TARDISes if they are created close together in the overworld.

If you are using a pre-TARDIS 2.7 version, you will have to educate your players to create their TARDIS home locations far apart as their position in the TimeVortex relates to the position they were created in the server world.
