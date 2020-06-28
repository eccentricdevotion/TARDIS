---
layout: default
title: Space Time Throttle
---

# Space Time Throttle

The Space Time Thottle is a TARDIS control that sets the speed of flight through the Time Vortex i.e. how long flights take.

In game, the Space Time Thottle is a locked redstone repeater &mdash; each tick setting determines a different speed, and a corresponding increase in the amount of Artron energy consumed:

| Setting  | Speed  | Artron energy requirements |
| -------- | ------ | -------------------------- |
| 1 tick   | Normal | Normal |
| 2 ticks  | Faster | 1.5 x normal |
| 3 ticks  | Rapid  | 2 x normal |
| 4 ticks  | Warp   | 3 x  normal |

### Adding the Space Time Thottle

Place a redstone repeater, then use the command:
```
/tardis update throttle
```

## Important note!

The Space Time Thottle has no effect under the following circumstances:

* The TARDIS flight mode is set to `MANUAL` or `REGULATOR`
* The TARDIS isn&rsquo;t actually travelling e.g. rebuilding, hiding, Siege mode
* Hostile Action Displacement (HADS) &mdash; the TARDIS will travel at normal speed
* Time travel malfunctions &mdash; the TARDIS will travel at normal speed
* Junk TARDIS / Junk Mode &mdash; the travel speed is the regular Junk speed
