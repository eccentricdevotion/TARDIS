---
layout: default
title: creation configuration options
---

# TARDIS creation configuration options

These are the creation configuration options.

<style type="text/css">
			table, table code { font-size:85%; }
			td { vertical-align:top; }
			td.noborder { border-bottom: none; }
			tr.coption { background-color: #eee; }
		</style>

| Option | Type | Default Value |
| --- | --- | --- |
| creation: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`create_worlds` | boolean | `false` |
| &nbsp; | It is **NOT recommended** setting this to true! Sets whether TARDISes are created in their own separate worlds. Requires the TARDISChunkGenerator plugin and a multi-world plugin. |
| &nbsp;&nbsp;&nbsp;&nbsp;`create_worlds_with_perms` | boolean | `false` |
| &nbsp; | Sets whether TARDISes are created in their own separate worlds for players with the appropriate permission — `tardis.create_world`. |
| &nbsp;&nbsp;&nbsp;&nbsp;`default_world` | boolean | `true` |
| &nbsp; | Sets whether TARDISes are created in a shared world. Also required to be true to enable the Junk TARDIS and the abilty to abandon TARDISes. |
| &nbsp;&nbsp;&nbsp;&nbsp;`default_world_name` | string | `TARDIS_TimeVortex` |
| &nbsp; | Sets the name of the default world. This is only used if `default_world` is true. |
| &nbsp;&nbsp;&nbsp;&nbsp;`border_radius` | integer | `256` |
| &nbsp; | Sets the distance of the WorldBorder barrier (only used if the plugin is installed on the server). |
| &nbsp;&nbsp;&nbsp;&nbsp;`gamemode` | string | `suvival` |
| &nbsp; | Sets the gamemode of TARDIS worlds when they are created. |
| &nbsp;&nbsp;&nbsp;&nbsp;`keep_night` | boolean | `true` |
| &nbsp; | Sets whether to keep the TARDIS world in perpetual night. |
| &nbsp;&nbsp;&nbsp;&nbsp;`inventory_group` | string | `'0'` |
| &nbsp; | If the Multiverse-Inventories plugin is enabled on the server, you can set the group that TARDIS worlds are added to when they are created. |
| &nbsp;&nbsp;&nbsp;&nbsp;`add_perms` | boolean | `true` |
| &nbsp; | Sets whether a TARDIS world is assigned permissions when the server uses a permissions plugin that has per-world configuration — see [Add permissions](add-permissions.html). |
| &nbsp;&nbsp;&nbsp;&nbsp;`custom_schematic` | boolean | `false` |
| &nbsp; | Sets whether the server will use a custom TARDIS console schematic — see [Schematics](schematics.html). |
| &nbsp;&nbsp;&nbsp;&nbsp;`custom_schematic_seed` | string | `OBSIDIAN` |
| &nbsp; | Sets the material for crafting the custom TARDIS console seed block. |
| &nbsp;&nbsp;&nbsp;&nbsp;`custom_creeper_id` | string | `BEACON` |
| &nbsp; | Sets the block material that the plugin should look for in the custom schematic to spawn the Artron Capacitor charged creeper on. |
| &nbsp;&nbsp;&nbsp;&nbsp;`use_block_stack` | boolean | `false` |
| &nbsp; | Sets whether players can use the legacy method of creating a TARDIS. |
| &nbsp;&nbsp;&nbsp;&nbsp;`use_clay` | string | `WOOL` |
| &nbsp; | Sets whether the coloured wool in TARDIS console and room schematics is switched to stained terracotta or concrete instead. Valid options are WOOL, TERRACOTTA, and CONCRETE. |
| &nbsp;&nbsp;&nbsp;&nbsp;`count` | integer | `0` |
| &nbsp; | Sets the maximum number of times a player can build and destroy a TARDIS. If set to `0` there is NO maximum. |
| &nbsp;&nbsp;&nbsp;&nbsp;`tips_limit` | integer | `400` |
| &nbsp; | Sets the number of T.I.P.S slots to use. Must be one of 400, 800, 1200 or 1600. |
| &nbsp;&nbsp;&nbsp;&nbsp;`sky_biome` | boolean | `true` |
| &nbsp; | Whether to set the TARDIS interior console and rooms to SKY biome when they are created. This setting allows the custom TARDIS block textures to work. |
| &nbsp;&nbsp;&nbsp;&nbsp;`area` | string | `none` |
| &nbsp; | If set to a pre-defined TARDIS area, it will set that area as the only place on the server that TARDISes can be created in. |
| &nbsp;&nbsp;&nbsp;&nbsp;`enable_legacy` | boolean | `true` |
| &nbsp; | Sets whether the legacy console schematics are available to build TARDIS interiors with. |

[Back to main configuration page](configuration.html)

