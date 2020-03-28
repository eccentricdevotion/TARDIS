---
layout: default
title: Item Kits
---

# TARDIS Item Kits

Kits allow server operators to give TARDIS items to players.

You can configure the plugin to automatically give kits to players when two events occur — when a player joins the server and when a player creates a TARDIS. You can also give kits to players using the `/tardisgive` command (@see [TARDIS give commands](give-commands.html)).

## Configuring kits

All kits are configured in _kits.yml_ — the default configuation can be see here: [kits.yml](https://github.com/eccentricdevotion/TARDIS/blob/2.7/src/main/resources/kits.yml).

The kits file is split into two sections `give` and `kits`:

<style type="text/css">
			table, table code { font-size:85%; }
			td { vertical-align:top; }
			td.noborder { border-bottom: none; }
			tr.coption { background-color: #eee; }
		</style>

The `give` configuration section sets whether kits are given for either of the two events, and if configured, which kit to give.

| Option | Type | Default Value |
| --- | --- | --- |
| give: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`join:` | &nbsp; |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`enabled` | boolean | `false` |
| &nbsp; | Sets whether to give a kit to players when they join the server. Players must have the `tardis.kit.join` permission. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`kit` | string | `basic` |
| &nbsp; | Sets **which** kit to give to players when they join the server. |
| &nbsp;&nbsp;&nbsp;&nbsp;`create:` | &nbsp; |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`enabled` | boolean | `false` |
| &nbsp; | Sets whether to give a kit to players when they create a TARDIS. Players must have the `tardis.kit.create` permission. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`kit` | string | `circuits` |
| &nbsp; | Sets **which** kit to give to players when they create a TARDIS. |

The `kits` configuration section allows you to set up lists of TARDIS items. The format of a kit is shown below:

```yaml
kits:
  basic:
    - TARDIS Key
    - Sonic Screwdriver
  circuits:
    - TARDIS ARS Circuit
    - TARDIS Chameleon Circuit
    - TARDIS Input Circuit
    - TARDIS Materialisation Circuit
    - TARDIS Memory Circuit
    - TARDIS Scanner Circuit
    - TARDIS Temporal Circuit
```

| Option | Type |
| --- | --- |
| kits: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`[kit name]:` | string |
| &nbsp; | This is what you want to call the kit. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`[item]` | list item |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`[item]` | list item |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`[item]` | list item |
| &nbsp; | These are the items contained in the kit.  
The item names must be the same as configured in _recipes.yml_ — see [Recipes](recipes.html) |

## Kit events

### Player join

`give: join: enabled` must be set to `true`

If a player has the permission `tardis.kit.join`, then when they join the server, they will automatically be given the `join` configured kit. The kit is only ever given once, not every time they join.

### TARDIS creation

`give: create: enabled` must be set to `true`

If a player has the permission `tardis.kit.create`, then when they create a TARDIS for the first time, they will automatically be given the `create` configured kit. The kit is only ever given once, not every time they create a TARDIS.
