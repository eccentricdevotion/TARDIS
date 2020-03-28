---
layout: default
title: Malfunctions
---

# Malfunctions

You can configure the TARDIS plugin so that when a player time travels, there is a chance of a malfunction happening that deposits the TARDIS in an unexpected location. There is also a (configurable) chance the location may be the Nether or The End, even if they are disabled in the config or the player doesn’t have permission.

When the TARDIS malfunctions, there is an emergency landing sound effect (requires the [TARDIS Resource Pack](https://github.com/eccentricdevotion/TARDIS/blob/2.7) and Minecraft version 1.7.2 or higher). Lights will flicker and there will be smoke and nausea effects. **Note:** Effects will only be present if the TARDIS was created with **[TARDIS v2.2-beta-2](http://dev.bukkit.org/bukkit-plugins/tardis/files)** or later.

### Config options

There are three options that affect TARDIS malfunctions:

- `malfunction: [percent]` — sets the percentage chance of a malfunction. Default: `3`
- `malfunction_nether: [percent]` — sets the percentage chance of a malfunction landing in the Nether. Default: `3`
- `malfunction_end: [percent]` — sets the percentage chance of a malfunction landing in The End. Default: `3`

With the default settings, there is a 3% chance of a TARDIS malfunction. If a malfunction does occur, then there is a 94% chance it will land in a NORMAL environment (overworld).

### Video
<iframe src="https://player.vimeo.com/video/64443843" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>