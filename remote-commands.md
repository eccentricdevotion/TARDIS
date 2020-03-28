---
layout: default
title: The Remote command
---

[Jump to video](#video)

# The Remote command

This command lets you remotely control a TARDIS, and is useful for server admins and command blocks. To use the command players need the permission: `tardis.remote`

    /tardisremote [player] [action] [travel options]

The currently supported actions are:

- travel
- comehere
- chameleon
- hide
- rebuild
- back

If the action equals `travel`, travel options are:

- home
- area [area name]
- coords e.g. world x y z

### Notes

- can be run from command blocks, but the specified player must be online
- if the player who is running the command does **NOT** have `tardis.admin` permission, then the normal checks are performed (energy/permissions/circuits/plugin respect etc for the specified player)
- use via Command Block will fail silently if above checks fail, as we can’t message a command block…
- if player has `tardis.admin` permission then no energy is used / only basic checks performed (so we’re not griefing anything)
- `comehere` comes to the currently targeted block (only available to TARDIS admin players)

### Video
<iframe width="600" height="366" src="https://www.youtube.com/embed/VypqSYls1QM0" frameborder="0" allowfullscreen></iframe>