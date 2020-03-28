---
layout: default
title: Travel configuration options
---

# Travel configuration options

These are the travel configuration options.

<style type="text/css">
			table, table code { font-size:85%; }
			td { vertical-align:top; }
			td.noborder { border-bottom: none; }
			tr.coption { background-color: #eee; }
		</style>

| Option | Type | Default Value |
| --- | --- | --- |
| travel: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`include_default_world` | boolean | `false` |
| &nbsp; | Sets whether the default world is included in time travel destinations. |
| &nbsp;&nbsp;&nbsp;&nbsp;`tp_radius` | integer | `500` |
| &nbsp; | Sets the maximum distance (in blocks) you can randomly time travel in the TARDIS. **Note** The actual values is about 4 times this (due to the multiplier repeater). |
| &nbsp;&nbsp;&nbsp;&nbsp;`max_distance` | integer | `29999983` |
| &nbsp; | Sets the maximum distance (in blocks) that can be used with the `/tardistravel` command. If the (vanilla) World Border distance is smaller this will be used instead. |
| &nbsp;&nbsp;&nbsp;&nbsp;`chameleon` | boolean | `true` |
| &nbsp; | Sets whether the TARDIS police box can change its appearance to match its surroundings. |
| &nbsp;&nbsp;&nbsp;&nbsp;`give_key` | boolean | `false` |
| &nbsp; | Sets whether the TARDIS key is given when changing worlds (and using a multi-world inventory plugin). |
| &nbsp;&nbsp;&nbsp;&nbsp;`the_end` | boolean | `false` |
| &nbsp; | Sets whether the TARDIS is allowed to travel to The End worlds. |
| &nbsp;&nbsp;&nbsp;&nbsp;`nether` | boolean | `false` |
| &nbsp; | Sets whether the TARDIS is allowed to travel to Nether worlds. |
| &nbsp;&nbsp;&nbsp;&nbsp;`land_on_water` | boolean | `true` |
| &nbsp; | Sets whether the TARDIS will land on water in the Overworld. |
| &nbsp;&nbsp;&nbsp;&nbsp;`timeout` | integer | `5` |
| &nbsp;&nbsp;&nbsp;&nbsp;`timeout_height` | integer | `135` |
| &nbsp; | Sets the maximum time in seconds a random location task can take. If the task times out then the Police Box location is set at the `timeout_height` value. This prevents the plugin crashing when using skyblock type worlds. |
| &nbsp;&nbsp;&nbsp;&nbsp;`random_attempts` | integer | `30` |
| &nbsp; | Sets the maximum number of attempts to find a random location. This prevents the plugin crashing under certain circumstances. |
| &nbsp;&nbsp;&nbsp;&nbsp;`exile` | boolean | `false` |
| &nbsp; | Sets whether the TARDIS exile feature is enabled. |
| &nbsp;&nbsp;&nbsp;&nbsp;`per_world_perms` | boolean | `false` |
| &nbsp; | Sets whether players require the `tardis.travel.[world]` permission to travel to a world. |
| &nbsp;&nbsp;&nbsp;&nbsp;`terminal_step` | integer | `1` |
| &nbsp; | Sets the 4 step values that the TARDIS Destination Terminal GUI uses, where the steps are `10 * step` and `25 * step` etc. |
| &nbsp;&nbsp;&nbsp;&nbsp;`terminal` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`redefine` | boolean | `true` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`the_end` | string | `world` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`nether` | string | `world` |
| &nbsp; | Allows you to redefine the End and Nether buttons in the Destination Terminal to specific worlds when travel to those environments is disabled. |
| &nbsp;&nbsp;&nbsp;&nbsp;`manual_flight_delay` | integer | `60` |
| &nbsp; | Sets the server tick delay between having to click the repeaters in [manual flight mode](flight-modes.html). |
| &nbsp;&nbsp;&nbsp;&nbsp;`random_circuit` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`x` | integer | `5000` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`y` | integer | `5000` |
| &nbsp; | Maximum +/- number of blocks the random location search will look between in x and z directions, so default will be -5000 to 5000 in both x and z directions. If the WorldBorder plugin is installed or a vaniila world border is in use, then those settings will be used instead. |
| &nbsp;&nbsp;&nbsp;&nbsp;`no_destination_malfunctions` | boolean | `true` |
| &nbsp; | Sets whether the TARDIS will malfunction if no valid random destination can be found with the current console settings. |

[Back to main configuration page](configuration.html)

