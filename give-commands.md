---
layout: default
title: give commands
---

# TARDIS give commands

TARDIS admins (players with the permission `tardis.admin`) can use this command to give items and energy to players.

    /tardisgive [player] artron [full|empty|amount]

This will alter the specified player’s TARDIS Artron Energy level.

`full` will set the level to the amount specified by `full_charge` in the Artron config file.

`empty` will set the level to `0`.

`amount` can be either positive or negative, but the final level will never be less than `0` or higher than `full_charge`.

    /tardisgive [player] kit [kit]

This will give the a player the specified TARDIS item kit. Kits can be configured in [kits.yml](kits.html)

    /tardisgive [player] [item] [amount]

`item` is any valid TARDIS item available in [recipes.yml](recipes.html).

`amount` determines how many of the item to give the player.

