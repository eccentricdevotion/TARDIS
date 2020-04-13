---
layout: default
title: Restrictions configuration options
---

# TARDIS Restrictions configuration options

These are the plugin restriction configuration options.

<style type="text/css">
			table, table code { font-size:85%; }
			td { vertical-align:top; }
			td.noborder { border-bottom: none; }
			tr.coption { background-color: #eee; }
		</style>

| Option | Type | Default Value |
| --- | --- | --- |
| allow: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`mob_farming` | boolean | `true` |
| &nbsp; | Sets whether players can [farm mobs](farming.html) when entering the TARDIS. |
| &nbsp;&nbsp;&nbsp;&nbsp;`spawn_eggs` | boolean | `true` |
| &nbsp; | Sets whether the player receives mob spawn eggs when TARDIS mob farming (and there is no farm room grown). |
| &nbsp;&nbsp;&nbsp;&nbsp;`emergency_npc` | boolean | `true` |
| &nbsp; | Sets whether the [Emergency Program One](emergency-program-one.html) feature is enabled. This setting will be disabled automatically if the Citizens plugin is not found. |
| &nbsp;&nbsp;&nbsp;&nbsp;`external_gravity` | boolean | `false` |
| &nbsp; | Sets whether the [Gravity Wells](gravity-wells.html) can be set up outside of the TARDIS. |
| &nbsp;&nbsp;&nbsp;&nbsp;`achievements` | boolean | `true` |
| &nbsp; | Sets whether players can gain TARDIS achievements. |
| &nbsp;&nbsp;&nbsp;&nbsp;`autonomous` | boolean | `true` |
| &nbsp; | Sets whether players can use the [autonomous function](autonomous.html). |
| &nbsp;&nbsp;&nbsp;&nbsp;`hads` | boolean | `true` |
| &nbsp; | Sets whether the TARDIS can [escape hostile](hads.html) actions. |
| &nbsp;&nbsp;&nbsp;&nbsp;`tp_switch` | boolean | `true` |
| &nbsp; | Sets whether players can set and switch resource packs when entering and exiting the TARDIS. |
| &nbsp;&nbsp;&nbsp;&nbsp;`all_blocks` | boolean | `false` |
| &nbsp; | Sets whether the chameleon circuit is allowed to use precious blocks such as GOLD and DIAMOND. This also affects whether players are allowed to change the TARDIS Key to any item — if set to `false`, then keys are restricted to the items listed under `keys:` in _blocks.yml_. |
| &nbsp;&nbsp;&nbsp;&nbsp;`sfx` | boolean | `true` |
| &nbsp; | Sets whether sound effects are played in the TARDIS interior — this overrides user preferences if turned off. |
| &nbsp;&nbsp;&nbsp;&nbsp;`wg_flag_set` | boolean | `true` |
| &nbsp; | Sets whether players can set the build flag (allow, deny) in their TARDIS region. |
| &nbsp;&nbsp;&nbsp;&nbsp;`zero_room` | boolean | `false` |
| &nbsp; | Sets whether players can grow a Zero room. If this is set to true using the `/tardisadmin zero_room true` command, the plugin wil attempt to create the `TARDIS_Zero_Room` world if it doesn’t yet exist. |
| &nbsp;&nbsp;&nbsp;&nbsp;`power_down` | boolean | `false` |
| &nbsp; | Sets whether players can power down their TARDIS. |
| &nbsp;&nbsp;&nbsp;&nbsp;`power_down_on_quit` | boolean | `false` |
| &nbsp; | Sets whether a player’s TARDIS automatically powers down when the player leaves the server. |
| &nbsp;&nbsp;&nbsp;&nbsp;`player_difficulty` | boolean | `true` |
| &nbsp; | Sets whether players can use the `/tardisprefs difficulty [easy|hard]` command to change their personal difficulty setting. See [http://dev.bukkit.org/bukkit-plugins/tardis/tickets/689-tardis-console-modes/](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/689-tardis-console-modes/) for the reasoning behind it all. |
| &nbsp;&nbsp;&nbsp;&nbsp;`invisibility` | boolean | `true` |
| &nbsp; | Sets whether players can use the invisibility Chameleon preset. See [Invisibility](invisibility.html) for more info. |
| &nbsp;&nbsp;&nbsp;&nbsp;`guardians` | boolean | `false` |
| &nbsp; | Sets whether players can farm guardians when entering the TARDIS, and whether guardians will randomly spawn in the pool room if the door is left open. |
| &nbsp;&nbsp;&nbsp;&nbsp;`village_travel` | boolean | `false` |
| &nbsp; | Sets whether players can use the `/tardistravel village` command to time travel to a random village. |
| &nbsp;&nbsp;&nbsp;&nbsp;`3d_doors` | boolean | `true` |
| &nbsp; | Sets whether TARDIS owners can see their door when the exterior is set to the INVISIBLE preset. They must be wearing 3D Glasses and looking at the block that the door would normally be placed on. |
| &nbsp;&nbsp;&nbsp;&nbsp;`repair` | boolean | `true` |
| &nbsp; | Sets whether players can repair a damaged TARDIS interior. |

[Back to main configuration page](configuration.html)
