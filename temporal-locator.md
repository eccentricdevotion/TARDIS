---
layout: default
title: Temporal Locator
---

# Temporal Locator

The Temporal Locator lets players set their time relative to the server time. The Temporal Locator is automatically added to new TARDISes. You can destroy and place it at a new location by using the `/tardis update temporal` command.

To use the Temporal Locator, right-click it to open the inventory based GUI. You will see a screen like the one below.

- Click a clock to set your time to the one displayed
- Time is set when exiting the TARDIS and reset back to server time when re-entering the TARDIS
- When players are temporally located, the perception filter is added to them
- Player time can be reset at any time by right-clicking air with a clock
- Players need `tardis.temporal` permission to use it

![Temporal Locator](images/docs/temporal1.png)

### Huh, what?

This doesn’t change the server time, that runs as normal, this just sets the player’s time on the client, e.g.:

- server time is 7000 ticks (morning)
- `eccentric_nz` has Temporal Location of 20000 ticks he will see the world at night
- `eccentric_nz` will NOT see zombie’s spawning because the server time is morning
- `thenosefairy` is standing next to `eccentric_nz` and is NOT temporally located
- `thenosefairy` will see the world in the same time as the server i.e. morning

![Temporal Locator](images/docs/temporal2.png)

