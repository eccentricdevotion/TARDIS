---
layout: default
title: Updating from v4.x.x to v5.x.x
---

# Updating from v4.x.x to v5.x.x

Version 5 of the TARDIS plugin combines all the separate TARDIS plugins (_TARDISChunkGenerator, TARDISVortexManipulator,
TARDISWeepingAngels, TARDISShop_ and _TARDISSonicBlaster_) into a modularised all-in-one plugin. The relevant configuration section is shown below:

```yaml
# modules
# are disabled by default
# https://eccentricdevotion.github.io/TARDIS/modules.html
modules:
  weeping_angels: false
  vortex_manipulator: false
  dynmap: false
  shop: false
  sonic_blaster: false
  blueprints: false
```

If you are updating from a previous version and have been using any of the other separate plugins,
you will need to transfer configuration settings and database records to the unified plugin.

To do this:

1. Uninstall the separate standalone plugins, but leave their data folders in the _plugins_ folder.
2. Install and run TARDIS v5.x.x once, to allow the plugin to generate the necessary config files and database entries.
3. Stop the server.
4. Edit the TARDIS config file - enable the modules that you require.
5. Restart the server, TARDIS will automatically transfer configuration settings and database records.
