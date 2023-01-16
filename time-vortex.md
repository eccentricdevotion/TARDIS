---
layout: default
title: TARDIS_TimeVortex world
---

# TARDIS\_TimeVortex world

By default the plugin will set up a special world that will allow you to store all the TARDISes in it and still be able 
to grow rooms, abandon TARDISes, be easier on server resources and make management of TARDISes easier. The world is called 
**TARDIS\_TimeVortex** and uses TARDISChunkGenerator to create a void type environment.

The plugin will automatically try to create the world when it first starts up.

### If you need to set up the world manually:

* Set TARDIS config options as shown below:

```
  /tardisconfig default_world true
  /tardisconfig default_world_name TARDIS_TimeVortex
  /tardisconfig include_default_world false
  /tardisconfig create_worlds false
```

## Notes
__Do NOT__ use Multiverse or another world management plugin to create and load the TARDIS_TimeVortex world. Doing so 
will cause players to receive the message `You cannot grow rooms unless your TARDIS was created in its own world`!

As of TARDIS v2.7-beta-1 the plugin now uses a system called [T.I.P.S](tips.html) (TARDIS Interior Positioning System). 
This system places new TARDISes in a specific ‘plot’ in the TARDIS_TimeVortex World. This means that TARDISes will no 
longer overlap other TARDISes if they are created close together in the overworld.

If you are using a pre-TARDIS 2.7 version, you will have to educate your players to create their TARDIS home locations 
far apart as their position in the TimeVortex relates to the position they were created in the server world.
