---
layout: default
title: Schematic command
---

# TARDIS Schematic command

As of version 3.0-beta-1, TARDIS now uses its own JSON schematic format. To create a TARDIS schematic (.tschm) file, you
use the `/tardisschematic` command.

## Command use

    /tardisschematic [save|load|paste] [name]

#### Saving

In order to use the `save` argument, you must first define the schematic region start and end points with the TARDIS
schematic wand (`/tardisgive [player] schematic_wand 1`).

    /tardisschematic save custom

#### Loading

To use the `load` argument, you must specify the folder and file `[name]` of the schematic you want to use. You don’t need to add the file extension.

    /tardisschematic load [folder] [schematic]
    
There are four `[folder]` locations:

* `user` - this is the loaction for user made schematics found at _plugins/TARDIS/user\_schematcis_.
* `console` - this contains all the plugin's interior schematics.
* `room` - this contains all the plugin's room schematics.
* `structure` - this contains all the plugin's structure schematics used in TARDIS worlds.

#### Pasting

Once loaded the schematic can be pasted back into the world with the `paste` argument.

    /tardisschematic paste

Pasting should be relatively lag free, as the number of blocks being placed per tick is restricted. A progress bar will
appear to give you an indication of how long the schematic will take to process.

#### Miscellaneous

There are two sub-commands to deal with custom display item light blocks:

    /tardisschematic convert [light] [material]
    
eg use ` /tardisschematic convert TENTH REDSTONE_LAMP` to convert all redstone lamps in the selected area to Tenth TARDIS lights.

    /tardisschematic remove
    
Removes TARDIS light blocks and replaces them with redstone lamps.

---

> **Note:** While it is possible to paste schematics, it is still recommended to use a plugin such as WorldEdit to do this
sort of work.

