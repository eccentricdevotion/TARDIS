---
layout: default
title: Autonomous homing function
---

# Autonomous homing function

Should you, as a Time Lord, die in game, you can set a preference to have the TARDIS automatically fly to a recharge point or the TARDIS home location (whichever is closest to where you die).

To enable/disable the autonomous homing function, use the command:

    /tardisprefs auto [on|off]

The following applies to the use of the autonomous homing function:

- The `auto` player preference must `on`
- You must have the permission: `tardis.autonomous`
- The function must be enabled in the TARDIS config: `allow_autonomous: true`
- The TARDIS must have sufficient Artron Energy (set by the config option `autonomous`)
- The TARDIS must not be in Siege Mode
- If their is insufficient Artron Energy to return home, and Siege Mode is enabled on the server, then the TARDIS will enter Siege Mode
