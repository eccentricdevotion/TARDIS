---
layout: default
title: Blueprints
---

# Blueprints

TARDIS blueprints are a way for players to earn or purchase TARDIS features. If enabled, blueprints suppliment server permissions &mdash; if a player attempts to use a TARDIS feature, the following occurs:

1. A reglular server permission check is performed:
   * if the player has the correct permission they can use it as normal
   * if the player doesn't have the correct permission, proceed to step 2
2. A blue print check is performed:
   * if the player has condensed the blueprint they can use the feature
   * otherwise the player is denied use of the feature
   
### Enabling blueprints

To enable blueprints on your server, use the command:
```
/tardisadmin blueprints true
```
To disable, run the command again with the last argument set to `false`. 

### Obtaining blueprints

Blueprints are a form of TARDIS disk.

![Blueprint disk](images/docs/blueprint_disk.png)

_Blueprints are keyed to a specific player &mdash; only the player who obtained the blueprint can use it to upgrade their TARDIS with the blueprinted feature the disk contains._

Players can obtain blueprints in a few ways:

1. By purchasing them from a TARDIS Shop. See the instructions on the [TARDIS Shop](tardis-shop.html) page for how to set this up.
2. Using the `/tardisadmin give [player] [blueprint]` command.
3. Using a system that can run the command above when certain conditions are met &mdash; for example a &ldquo;rank up&rdquo; plugin (or a command block &mdash; use the @p selector).

### Using blueprints

Once a player has obtained a blueprint they need to condense it in the TARDIS Artron condenser. Once condensed the blueprint will be stored in the TARDIS&rsquo; memory core.

### Removing a blueprint

To remove a player&rsquo;s ability to use a blueprint feature use the following command:
```
/tardisadmin revoke [player] [permission]
```
To see a list of blueprint permissions use the command:
```
/tardisadmin list blueprints
```
