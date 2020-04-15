---
layout: default
title: Zero Room
---

# TARDIS Zero Room

The Zero Room is one of the most peaceful locations in existence. It is a place completely cut off from the rest of the universe, cut off even from the TARDIS interior, in a entirely separate dimension. As such, no voice can be heard within its walls, and no commands can be run. There is no communication with the outside world.

## Creation

The Zero room is not a room within the TARDIS interior, as most rooms are. Before any players can grow and use it, a server administrator must first enable `zero_room` in the TARDIS config, preferably using the command `/tardisadmin zero_room true`. TARDIS will then create the TARDIS\_Zero\_Room world, if it does not already exist. The server must then be restarted to enable its more advanced features.

Once the room is enabled, growing the Zero Room is a simple matter of following these steps:

1. Run the command `/tardis room ZERO`, normal room growing requirements apply (artron energy, required blocks, etc.)
2. Place a button, sign, lever or comparator, and run the command `/tardis update zero`
3. Click on the desired control to update the position of the Zero Room Transmat System
4. Once the Zero Room has grown, activate the control to teleport into the room

**Note:** The Zero room **must** be grown using the `/tardis room` command, as it is not part of the ARS.

## Capabilities

The Zero Room has several useful abilities, including:

- Players can levitate in the Zero Room — when you walk your feet do not touch the floor!
- Slow healing, default 200 ticks (1/2 heart every 10 seconds)
- Chat isolation &dash; Both chat and commands are disabled, and only server-wide broadcasts (the important things) can be heard
- Configurable artron cost to enter the room, default 250 (can be changed in _artron.yml_)

## Video
<iframe src="https://player.vimeo.com/video/87441837" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
