---
layout: default
title: Room growth configuration options
---

# Room growth configuration options

These are the room growth configuration options.

<style type="text/css">
			table, table code { font-size:85%; }
			td { vertical-align:top; }
			td.noborder { border-bottom: none; }
			tr.coption { background-color: #eee; }
		</style>

| Option | Type | Default Value |
| --- | --- | --- |
| growth: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`gravity_max_distance` | integer | `16` |
| &nbsp;&nbsp;&nbsp;&nbsp;`gravity_max_velocity` | integer | `5` |
| &nbsp; | Sets sets the maximum distance and velocity a gravity well can be set to. |
| &nbsp;&nbsp;&nbsp;&nbsp;`rooms_require_blocks` | boolean | `true` |
| &nbsp; | Sets whether growing a room requires the player to condense the materials required to grow the room in the TARDIS condenser. |
| &nbsp;&nbsp;&nbsp;&nbsp;`rooms_condenser_percent` | integer | `100` |
| &nbsp; | Sets the percentage of the actual blocks that a player needs to condense if `rooms_require_blocks: true` |
| &nbsp;&nbsp;&nbsp;&nbsp;`return_room_seed` | boolean | `true` |
| &nbsp; | Sets whether the room seed block is returned to the player when they jettison the room. |
| &nbsp;&nbsp;&nbsp;&nbsp;`room_speed` | integer | `4` |
| &nbsp; | Sets the speed (in blocks per second) at which rooms are grown. Maximum speed is `20` which is one block per tick. |
| &nbsp;&nbsp;&nbsp;&nbsp;`ars_limit` | integer | `1` |
| &nbsp; | Sets the maximum number of rooms that can be reconfigured at a time. |

[Back to main configuration page](configuration.html)

