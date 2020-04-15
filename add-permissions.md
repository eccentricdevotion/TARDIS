---
layout: default
title: Add permissions
---

Add permissions
===============

If you are using a permissions manager that has per world config files, you would normally need to manually add permissions for players when they create a TARDIS in its own world. By setting `add_perms: true` in the TARDIS config file, you can get the plugin to automatically add permissions groups with your desired permissions attached to them.

To do this:

1.  With TARDIS installed, run the server once so that it creates its default files
2.  Stop the server and edit _plugins/TARDIS/permissions.txt_
3.  Use the example (in file and) below:

### Example permissions.txt

    #first_group
    tardis.use
    tardis.room
    tardis.timetravel.player
    tardis.timetravel.location
    -tardis.exile
    #second_group
    tardis.enter


*   Groups names **MUST** start with a `#`
*   The first line is the name of the permissions group the player should be added to, so it would be something like: _Timelord_ or _Donor_
*   Then it's just one permission per line (any permission, not just TARDIS ones) that you want to give this group
*   Add a minus in front of the permissions to make it false.
*   If you want to add other groups, put the group name (with a `#` at the start) and add the permissions for the players in that group

In the example above the `first_group` is the one that the TARDIS owner will be added to. The `second_group` would be used for any companions that the `first_group` player might want to tag along (they can’t build TARDISes, but can enter anyone else's if they are a companion).

This feature supports Essentials GroupManager and bPermissions only at this stage.
