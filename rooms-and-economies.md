---
layout: default
title: Rooms and server economies
---

# Rooms and server economies

TARDIS has the potential to disturb the fragile balance of a server’s economy, by allowing the cheap growing of rooms which can potentially be harvested for their blocks.

TARDIS has a configuration option — `rooms_require_blocks` — that enables server owners to balance the cost of a room against their server economies. When set to true, it only allows a room to be grown if the player has collected and condensed the blocks needed to grow it.

The following table illustrates the actual cost of the default TARDIS room based on the lookup tables currently used to convert blocks to Artron Energy if you were to put them in the TARDIS energy condenser chest.

| Room | Actual cost to grow | Default cost in config |
| --- | --- | --- |
| Antigravity | 15379 | 625 |
| Arboretum | 8680 | 325 |
| Baker | 7392 | 350 |
| Bedroom | 8915 | 475 |
| Empty | 8403 | 250 |
| Farm | 7435 | 350 |
| Gravity | 17192 | 625 |
| Greenhouse | 7790 | 450 |
| Harmony | 8963 | 450 |
| Kitchen | 8562 | 450 |
| Lazarus | 8537 | 750 |
| Library | 12318 | 550 |
| Mushroom | 5147 | 350 |
| Passage | 4979 | 200 |
| Pool | 8796 | 450 |
| Renderer | 16572 | 550 |
| Stable | 8215 | 350 |
| Trenzalore | 9969 | 550 |
| Vault | 8557 | 350 |
| Village | 6641 | 550 |
| Wood | 3758 | 350 |
| Workshop | 11508 | 400 |
| Zero | 6506 | 650 |

## A choice

Server admins can either adjust, the config values for room costs, based on the table above (or by using the `/tardisroom blocks save` command to generate per room text files that list the required blocks and the actual room cost — good if you have [custom rooms](custom-rooms.html)). This makes things better, but Artron Energy can still be gained for free using a recharge beacon.

## A better choice

On SURVIVAL servers, the better choice is to use the `rooms_require_blocks: true` option. Rather than using just the Artron Energy cost, this makes sure that players have gathered the resources they need first. This can be balanced with the `rooms_condenser_percent` option. Setting this to less than 100% means that players need only gather a portion of the required blocks, and the rest can be made up of Artron Energy. There is plenty of flexibility to get the balance just right :)

