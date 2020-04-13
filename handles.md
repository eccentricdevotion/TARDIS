---
layout: default
title: Handles
---

[Jump to video](#video)

# Handles

Handles is the name given by the Eleventh Doctor to a Cyberman head that he obtained from the Maldovarium Market, and subsequently repaired.

You can craft and place Handles so that he can interact with you and the TARDIS.

To see the Handles crafting recipe, use the command `/tardisrecipe handles`. Alternatively, you can give yourself or other players a Handles with the command `/tardisgive [player] handles 1`

Handles can only be placed inside the TARDIS

Outside the TARDIS you can only communicate with Handles if he is in your inventory, or you have a communicator (and Handles is placed in the TARDIS) — use the `/tardisrecipe communicator` to view the recipe, or give a communicator with the `/tardisgive [player] communicator 1` command.

### Talking to Handles

Use chat to talk to Handles. All communication must be proceeded by a prefix — by default this is: “Hey Handles” — followed by one or more keywords.

Keywords for chat processing are: _“takeoff”, “land”, “scan”, “lock”, “unlock”, “remind”, “say”, “name”, “time”, “travel”_ — travel can be to _“home”, “save” [name], “player” [name], “area” [name]_, _“biome” [name]_ — normal TARDIS permissions apply.

### Examples

    hey handles takeoff
    hey handles land
    hey handles scan the exterior
    hey handles lock the door
    hey handles unlock the door
    hey handles remind me to get some wood in 5
    hey handles say something funny
    hey handles what is my name
    hey handles what time is it

Reminders need to have the number of minutes (until the reminder) as the last part of the handles request e.g. `hey handles remind me to [do something] in 5 `(reminder times will not be 100% accurate, because the plugin only checks for reminders every minute — can be changed in the config)

### Commands

Name a Handles Program Disk

    /handles disk [name]

Remove the Handles record from the TARDIS database. Use this if Handles is destroyed accidentally.

    /handles remove

### Config options:
<style type="text/css">
table, table code { font-size: 85%; }
td { vertical-align: top; }
td.noborder { border-bottom: none; }
tr.coption { background-color: #eee; }
th.wide { width: 33%; }
</style>

| Option | Type | Default Value |
| --- | --- | --- |
| allow: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`handles` | boolean | `true` |
| &nbsp; | Whether Handles is enabled on the server (requires restart if changed) |
| handles: |
| --- |
| &nbsp;&nbsp;&nbsp;&nbsp;`prefix` | string | `Hey Handles` |
| &nbsp; | The key word to trigger Handles chat processing |
| &nbsp;&nbsp;&nbsp;&nbsp;`reminders:` |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`enabled` | boolean | `true` |
| &nbsp; | Whether reminders are enabled |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`schedule` | integer | `1200` |
| &nbsp; | Number of ticks between reminder checks (1200 = 1 minute) |

Clicking on a placed handles plays a Handles voice snippet ([TARDIS-SoundResourcePack](https://github.com/eccentricdevotion/TARDIS-SoundResourcePack/) required)

The [TARDIS-Resource-Pack](https://github.com/eccentricdevotion/TARDIS-Resource-Pack) has been updated with the Handles model and textures — the Handles block is a BIRCH\_BUTTON.

Clicking on Handles while sneaking opens the Handles Programming GUI where you can create program disks, _documentation yet to come_, disks can be renamed with a `/handles disk [name]` command). More on this later, but it should extend Handles’ functionality a bit with events and more actions and will work with Advanced Console Disks.

### Permissions

The is one parent permission, with three children for finer control that allow players to use the Handles companion.

    tardis.handles

And the children:

    tardis.handles.use
    tardis.handles.communicator
    tardis.handles.program

### Video
<iframe width="600" height="366" src="https://www.youtube.com/embed/pyJQHvxqpA8?rel=0" frameborder="0" allowfullscreen></iframe>

Original Handles request thread: [https://dev.bukkit.org/projects/tardis/issues/1549](https://dev.bukkit.org/projects/tardis/issues/1549)
