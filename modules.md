---
layout: default
title: Modules
---

# Modules

Version 5 of the TARDIS plugin combines all the separate TARDIS plugins (_TARDISChunkGenerator, TARDISVortexManipulator,
TARDISWeepingAngels, TARDISShop_ and _TARDISSonicBlaster_) into a modularised all-in-one plugin. The relevant
configuration section is shown below:

```yaml
# modules
# are disabled by default
modules:
  weeping_angels: false
  vortex_manipulator: false
  dynmap: false
  shop: false
  sonic_blaster: false
```

__Note__ The Chunk Generator module is always enabled.

You can enable modules with the `/tardisconfig` command:

```bash
/tardisconfig weeping_angels [true|false]
/tardisconfig vortex_manipulator [true|false]
/tardisconfig dynmap [true|false]
/tardisconfig shop [true|false]
/tardisconfig sonic_blaster [true|false]
```

To see what each module does, visit the appropriate page:

- [Chunk Generator](generators.html)
- [Weeping Angels](weeping-angels.html)
- [Vortex Maipulator](vortex-manipulator.html)
- [Dynmap](dynmap-tardis.html)
- [Shop](tardis-shop.html)
- [Sonic Blaster](sonic-blaster.html)
