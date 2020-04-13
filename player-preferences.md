---
layout: default
title: Player Preferences
---

# Player Preferences

There are a number of specific player preferences that you can change:

- TARDIS key
- Sound effects
- Who quotes
- Wall material
- Floor material
- Siege Wall material
- Siege Floor material
- Isomorphic controls
- Autonomous homing function
- HADS
- Emergency Program One
- Police Box sign (not available in TARDIS v2.6 to v2.8)
- Police Box lamp (only available if the TARDIS preset is a Police Box)
- Beacon
- Do Not Disturb (DND)
- Build flag
- Minecart sounds
- Exterior Rendering Room
- Wool Lights
- Connected Textures
- Travel Bar
- Difficulty
- Farming
- Lanterns
- Junk Mode
- Interior hum sound effects

Each preference can be set with a command, and some of them can be set via an inventory based GUI (if the player has a Sonic Screwdriver, sneaking and right-clicking air will open the Player Preferences GUI).

## Player preference commands

### `/tardisprefs`

To change the TARDIS Key item, type:

    /tardisprefs key [item]

The server admin may have restricted the list of items that may be used for the TARDIS Key. Use auto-completion to see a list of available items e.g. type `/tardisprefs key` then press the TAB key.

To change the look of your TARDIS Key, type:

    /tardisprefs key_menu

This requires the [TARDIS Resource Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/) to see the different key textures. Use the command to open the TARDIS Key GUI.

To change the look of your Sonic Screwdriver, type:

    /tardisprefs sonic

This requires the [TARDIS Resource Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/) to see the different sonic textures. Use the command to open the Sonic Screwdriver GUI.

To toggle the TARDIS sound effects on or off, type:

    /tardisprefs sfx [on|off]

To toggle the Emergency Program One on or off, type:

    /tardisprefs eps [on|off]

To set the message that Emergency Program One gives, type:

    /tardisprefs eps_message [message]

To choose the TARDIS interior hum sound effect that you want to hear, type:

    /tardisprefs hum [sound]

The sounds are called:

- alien
- atmosphere
- computer
- copper
- coral
- galaxy
- learning
- mind
- neon
- sleeping
- void
- random

If the server admin has disabled sound effects in the plugin config, these last two commands will have no effect.

To toggle the Who quotes on or off, type:

    /tardisprefs quotes [on|off]

To change the block room walls and floors are grown with, type:

    /tardisprefs [wall|floor] [material]

To change the block type that changes when a TARDIS enters siege mode, type:

    /tardisprefs [siege_wall|siege_floor] [material]

Where `[material]` is a block from the list on the [Wall materials](walls.html) page. This setting has no effect if `siege.textures: false` is set in the config.

To change whether the TARDIS Autonomous homing function takes the Police Box to the nearest recharge point or the Tardis ‘home’ (whichever is closest) when the Time Lord owner dies, type:

    /tardisprefs auto [on|off]

If the server admin has disabled the autonomous homing function in the plugin config, this command will have no effect.

To toggle HADS on or off, type:

    /tardisprefs hads [on|off]

To set the HADS type, use:

    /tardisprefs hads_type [dispersal|displacement]

To toggle the Isomorphic controls on or off, type:

    /tardisprefs isomorphic [on|off]

To toggle whether the TARDIS console beacon is on or off during time travel, type:

    /tardisprefs beacon [on|off]

Annoyed by other players time travelling to your location? To toggle whether you want to allow other players to time travel to you, type:

    /tardisprefs dnd [on|off]

To toggle the Police Box sign on or off, type:

    /tardisprefs sign [on|off]

To toggle whether you enter the Exterior Rendering Room after a scan is completed, type:

    /tardisprefs renderer [on|off]

To set the type of block used as the Police Box lamp, type:

    /tardisprefs lamp [material]

The server admin may have restricted the blocks that can be used as a lamp.

### `/tardisprefs build`

You can toggle the whether companions can build inside your TARDIS with this command, type:

    /tardis build [on|off]

### `/tardisprefs minecart`

You can choose whether to use TARDIS Resource Pack sounds or default Minecraft sounds for TARDIS doors and materialisation with this command, type:

    /tardisprefs minecart [on|off]

### `/tardisprefs wool_lights`

You can choose whether to use black wool or sponge (re-textured with the TARDIS Resource Pack to look like Redstone Lamp Off blocks) for the off state of the TARDIS lights with this command, type:

    /tardisprefs wool_lights [on|off]

### `/tardisprefs ctm`

You can choose whether your Police Box preset uses the Connected Textures Mod to change the block to the left of the door to a quartz pillar that is retextured to the Police Box sign (players without the mod installed will see a plain quartz pillar instead of the regular blue wool when this is `on`) with this command, type:

    /tardisprefs ctm [on|off]

### `/tardisprefs travelbar`

You can choose whether to show a _travel time remaining_ status bar with this command, type:

    /tardisprefs travelbar [on|off]

### `/tardisprefs difficulty`

If allowed by the plugin configuration (and the server difficulty is set to `hard`), you can choose a TARDIS difficulty level with this command, type:

    /tardisprefs difficulty [easy|hard]

### `/tardisprefs farm`

Turn on TARDIS mob farming (requires the appropriate room, or `allow.spawn_eggs: true` in the config) with this command, type:

    /tardisprefs farm [on|off]

### `/tardisprefs auto_powerup`

To toggle automatically powering up the TARDIS when entering (if it is powered down), type:

    /tardisprefs auto_powerup [on|off]

### `/tardisprefs lanterns`

Set whether your TARDIS console uses lanterns or redstone lamps with this command, type:

    /tardisprefs lanterns [on|off]

ON means that the console will use sea lanterns, toggle the lights off and on to see the change.

## Other preferences

You can toggle Junk Mode on or off with this command, type:

    /tardisprefs junk [on|off]

To toggle the plain TARDIS on and off (no sign or lamp), type:

    /tardisprefs plain [on|off]

To toggle submarine landings on and off, type:

    /tardisprefs submarine [on|off]

To toggle automatic Siege Mode on and off (when a Time Lord dies), type:

    /tardisprefs auto_siege [on|off]

To set the TARDIS flight mode, type:

    /tardisprefs flight [normal|regulator|manual]

To set the language used in the '/tardis say' command, type (with auto-complete):

    /tardisprefs language [language]

To toggle Police Box textures on and off (biome for CTM is set / not set at the TARDIS' location), type:

    /tardisprefs policebox_textures [on|off]
