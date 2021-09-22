---
layout: default
title: Schematic command
---

# TARDIS Schematic command

As of version 3.0-beta-1, TARDIS now uses its own JSON schematic format. To create a TARDIS schematic (.tschm) file, you use the `/tardisschematic` command.

## Command use

    /tardisschematic [save|load|paste] [name]

#### Saving

In order to use the `save` argument, you must first define the schematic region start and end points with the TARDIS schematic wand (`/tardisgive [player] schematic_wand 1`).

    /tardisschematic save custom

#### Loading

To use the `load` argument, the specified file `[name]` must be in the _plugins/TARDIS/user\_schematics_ folder. You don’t need to add the file extension.

    /tardisschematic load custom

#### Pasting

Once loaded the schematic can be pasted back into the world with the `paste` argument.

    /tardisschematic paste

Pasting should be relatively lag free, as the number of blocks being placed per tick is restricted. A progress bar will appear to give you an indication of how long the schematic will take to process.

**Note:** While it is possible to paste schematics, it is still recommended to use a plugin such as WorldEdit to do this sort of work.

