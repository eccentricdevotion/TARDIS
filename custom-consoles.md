---
layout: default
title: Custom consoles
---

# Custom consoles

TARDIS has the ability to add your own custom consoles using TARDIS schematic files (.tschm) and the _custom\_consoles.yml_ and _artron.yml_ configuration files.

### custom\_consoles.yml

After running the plugin for the first time, a configuration file called _custom\_consoles.yml_ will be created in the _plugins/TARDIS_ folder.

By default it contains one entry called ‘CUSTOM’ which is initially disabled.

It looks like this:

    # custom console names should preferably be a single word,
    # and cannot contain spaces (use underscores)
    CUSTOM:
        enabled: false
        schematic: custom
        seed: OBSIDIAN
        has_beacon: true
        has_lanterns: false
        description: Super-duper Custom Console

You can add as many custom consoles as you wish, all you need to do is duplicate the CUSTOM entry and then change it to suit as needed. The settings are explained below:

- `enabled: [true|false]` — pretty self-explanatory, whether to load this console when the plugin starts up
- `schematic: [sometext]` — this affects the schematic filename, the permission node and the artron upgrade cost — best shown by example — if this is set to `rani`, then the schematic filename needs to _‘rani.tschm’_, the permission to create this TARDIS will be _‘tardis.rani’_ and there needs to be a corresponding entry in _artron.yml_ in the `upgrades` section e.g. `rani: 8000` - it should be a lowercase version of the schematic name.
- `seed: [Material]` — this will determine the type of seed block this TARDIS uses — `[Material]` must be a valid Bukkit [Material](https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/Material.java) enum
- `has_beacon: [true|false]` — again pretty self-explanatory, whether this TARDIS has a beacon in it
- `has_lanterns: [true|false]` — again pretty self-explanatory, whether this TARDIS has a sea lanterns for lights
- `description: [some other text]` — this is the display name that appears when you hover over the block in the ARS and Desktop Theme GUIs

### artron.yml

The custom console schematic will NOT be enabled until an Artron Energy upgrade cost is entered into _artron.yml_. Add the schematic name (as entered into custom\_consoles.yml) to the `upgrades` config section along withe Artron cost.

    upgrades:
        custom: 10000
        rani: 8000

### The schematic file

When you have created your TARDIS schematic, you will need to put it in the _plugins/TARDIS/user\_schematics_ folder, and then restart the server.

You can learn about creating custom console schematics on the [Schematics](schematics.html) page.

