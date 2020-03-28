---
layout: default
title: Gravity Wells
---

[Jump to videos](#video)

# Gravity Wells

Gravity wells let you move around the TARDIS interior without having to walk or use ladders. They let you create multiple levels so you can expand the TARDIS as much as you want.

There are three types of gravity wells:

1. The _gravity_ room type grows gravity wells that go **down** a level
2. The _antigravity_ room type grows gravity wells that go **up** a level
3. The `/tardisgravity` command lets you create **sideways** gravity wells (or inertial dampened passages)

## Growing a Gravity Well Room

Gravity Wells can be grown [in the same manner as any other room](rooms.html#growing), though with certain other requirements. The _gravity_ room requires an additional empty ARS slot **below** the selected slot, while the _antigravity_ room requires an additional empty ARS slot **above**.

For TARDIS versions prior to `2.6-beta-1`, gravity wells are best grown in the middle of an empty room.

1. Use the `/tardis room [gravity|antigravity]` command to get started
2. Place the seed block (default MOSSY COBBLESTONE for gravity, and SANDSTONE for antigravity) down into the centre of the floor (don’t sit it on top)
3. Click the seed block with the TARDIS key and sit back while you watch it grow

The PINK side of the gravity well allows you fall down it without taking any damage, while the GREEN side will lift you up (without any need for flying).

## The `/tardisgravity` command

This command will let you create either sideways gravity wells, or up and down ones of any height (though you’ll need to build your own gravity shaft).

The command syntax is:

    /tardisgravity [direction] [distance] [velocity]

Where `[direction]` is one of:

    up
    down
    north
    west
    south
    east

`[distance]` is the number of blocks you want to move

`[velocity]` is how fast you want to move. As a guide, normal Minecraft walking speed is 0.37, and a standard (grown) TARDIS gravity well is 0.5.

You will find that the higher you set the velocity, the less you will have to set the distance. For example, a standard (grown) TARDIS gravity well uses `distance = 11`, `velocity = 0.5`. If we change the velocity to `1.5`, then the distance will need to be lowered to `2` to stop players hitting their heads on the ceiling.

After running the command you will be prompted to click the wool block that becomes the trigger for the gravity well.

### Wool colours

The following table sets out the colour of the wool block needed for each gravity well direction.

| Direction | Wool colour |
| --- | --- |
| Up | Light Green |
| Down | Pink |
| North | Black |
| West | Purple |
| South | Red |
| East | Yellow |

## Removing gravity well blocks

You cannot jettison gravity wells, but sometimes you may want to remove them manually. To get rid of the gravity well trigger block, you can use the command:

    /tardisgravity remove

You will be prompted to click on the block to remove the reference to it in the TARDIS database.

### Videos
<iframe src="https://player.vimeo.com/video/58275849" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe><iframe src="https://player.vimeo.com/video/61447553" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>

&nbsp;

[Back to the Rooms page](rooms.html)

