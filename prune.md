---
layout: default
title: pruning
---

# TARDIS pruning

**Pruning is a potentially hazardous activity — make sure you have backed up your server BEFORE running this command!**

You can list and remove TARDISes that have not been used for an extended period using the `/tardisadmin [prunelist|prune]` commands.

Each time a player logs in to the server, their last use time is recorded in the database. You can use this data to determine if a TARDIS has been abandoned.

### List

To list TARDISes that have not been used for a number of days, use the command:

    /tardisadmin prunelist [days]

Change `[days]` to the minimum number of days the TARDISes have been inactive for. The command will output to the screen/console and also save a text file called _TARDIS\_Prune\_List.txt_ to the TARDIS folder.

### Prune

If you are happy that the TARDISes in the list are OK to be pruned, use the command:

    /tardisadmin prune [days]

### Bypassing a prune

You can allow players to be excluded from pruning by giving them the permission `tardis.prune.bypass`. This will ensure that the players’ “last use” is always set to time in the distant future.

