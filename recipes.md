---
layout: default
title: Recipes
---

# Recipes

Each of the TARDIS items has a configurable recipe. There are both shaped and shapeless recipes — generally the shapeless ones are used when turning (or upgrading) one TARDIS item into another.

The plugin relies on the items having a certain name, so you shouldn’t change the recipe names, but the ingredients, amount and sometimes the result can be configured.

If after editing the recipes, things stop working, you will need to delete the edited file to revert it to the default recipes on the next server restart.

### Shaped recipes

A shaped TARDIS recipe looks like this:

    shaped:
        TARDIS Locator:
            easy_shape: OIO,ICI,OIO
            easy_ingredients:
                O: GRAVEL
                I: IRON_INGOT
                C: 'WOOL:14'
            hard_shape: OIO,ICI,OIO
            hard_ingredients:
                O: OBSIDIAN
                I: IRON_INGOT
                C: 'MAP:1965'
            result: COMPASS
            amount: 1
            lore: ""

<style type="text/css">
			table, table code { font-size:85%; }
			td { vertical-align:top; }
			td.noborder { border-bottom: none; }
			tr.coption { background-color: #eee; }
		</style>

Options (below) surrounded by square brackets `[]` are areas that you can change. Do **NOT** type the square brackets.

| Option | Type | Value |
| --- | --- | --- |
| shaped: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`item name` | string | &nbsp; |
| &nbsp; | You **shouldn’t** change this or the recipe will break. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`easy_shape` | string | `AAA,BBB,CCC` |
| &nbsp; | The recipe shape is the same as the workbench crafting area — a 3x3 grid — where `AAA` is the 3 top slots, `BBB` is the 3 middle slots, and `CCC` is the 3 bottom slots. The letters you type here matter, and relate to the ingredients that are used in the recipe. If a slot should be left _empty_, then use a dash `-` instead of a letter. You specify the ingredients in the next section. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`easy_ingredients` | &nbsp; | &nbsp; |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`A` | string | `[material name]` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`B` | string | `[material name]` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`C` | string:integer | `'[material name:damage value]'` |
| &nbsp; | The number of ingredients listed here must be the same as the number of different letters in the recipe shape. For each different letter, you will specify a new ingredient. Ingredients should be specified using the [Bukkit Material ENUM](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html). If you need to specify a damage value (for example to set the colour of wool) then you **NEED** to separate the values with a colon `:` **AND** surround the values in single quotes `''`. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`hard_shape` | string | `AAA,BBB,CCC` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`hard_ingredients` | &nbsp; | &nbsp; |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`A` | string | `[material name]` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`B` | string | `[material name]` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`C` | string:integer | `'[material name:damage value]'` |
| &nbsp; | The hard shape and ingredients are specified in the same way as for the easy ones, but are used when the TARDIS difficulty level is set to `hard` in the TARDIS config. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`result` | string:integer | `'[material name:damage value]'` |
| &nbsp; | This is the `material name`, or `'material name:damage value'`, of the item that the player receives from crafting the recipe. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`amount` | integer | `[amount]` |
| &nbsp; | The amount of the item the player gets from this recipe. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`lore` | string:integer | `"[lines separated by \n]"` |
| &nbsp; | The TARDIS item name will be set automatically from the name used in the recipe configuration section. If you also want the item to display custom lore, then you can specify that here. To set multiple lines, separate the different lines with `\n`. All the text you enter here must ne surrounded with double quotes `""`. |

### Shapeless recipes

A shaped TARDIS recipe looks like this:

    shapeless:
        Save Storage Disk:
            recipe: RECORD_9,REDSTONE
            result: RECORD_4
            amount: 1
            lore: "Blank"

Options (below) surrounded by square brackets `[]` are areas that you can change. Do **NOT** type the square brackets.

| Option | Type | Default Value |
| --- | --- | --- |
| shapeless: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`item name` | string | &nbsp; |
| &nbsp; | You **shouldn’t** change this or the recipe will break. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`recipe` | 'string:integer,string' | `'[material,material name:damage value]'` |
| &nbsp; | A shapeless recipe is just a comma separated list of ingredients. If you specify any damage values, then the whole list should be enclosed in single quotes `''`. |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`result` | string:integer | `'[material name:damage value]'` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`amount` | integer | `[amount]` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`lore` | string:integer | `"[lines separated by \n]"` |
| &nbsp; | The `result`, `amount` and `lore` are set the same as for shaped recipes. |

