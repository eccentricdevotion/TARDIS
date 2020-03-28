---
layout: default
title: Change log
---

# Change log

## What’s been changed?

### TARDIS v3.5.3

- **TARDIS requires TARDISChunkGenerator v2.3 and CraftBukkit 1.9.4**

#### Additions

- Grass path blocks are restored when the TARDIS leaves the affected location
- Added new sound effects 
  - a faster materialisation sound effect when the TARDIS rebuilds
  - a faster dematerialisation sound effect when the TARDIS hides
  - requires an update to the [TARDIS-SoundResourcePack](https://github.com/eccentricdevotion/TARDIS-SoundResourcePack)
- Added cooldown logic to the TARDIS remote key rebuild & hide feature
- Added an under door block to the INVISIBLE preset if the block under is AIR

#### Changes

- HADS no longer engages if the TARDIS is powered down
- The Remote Key no longer functions if the TARDIS is powered down
- The required server version is now explicitly specified in the plugin disable message
- Materialisation sound effects are now played for autonomous homing (inside and outside the TARDIS)
- Materialisation sound effects are now played when EP1 appears
- Players are now removed from the `travellers` table if they respawn in a non-TARDIS world after dying
- Doors are now closed (and portals removed) before autonomously returning home — may fix monsters spawning when the door is (supposedly) closed
- Monsters should no longer randomly spawn inside a TARDIS when set to the INVISIBLE preset
- The plugin now allows the `/tardis occupy` command for companions outside the TARDIS

#### Bug fixes

- Fixed the Police Box door falling off when landing on grass paths
- Materialisation sound effects are no longer played when `police_box.materialise` is `false`
- Companions can now rescue the Time Lord of a TARDIS using the telepathic circuit
- Fixed an error when showing the Artron Indicator and the player had already disconnected from the server
- Prevented rebuilding the TARDIS while it ’s still hiding
- Prevented companions opening the door when TARDIS autonomous homes and is still materialising
- The plugin now restores the biome at the Police Box ’s last location when leaving — even if the TARDIS was hidden
- Fixed a `null` message when there is malfunction with the INVISIBLE preset
- Fixed the TARDIS becoming unusable when autonomously returning with invisibility engaged
- The player ’s inventory is updated after cancelling the block place event in the renderer room (so the item appears back in the inventory without having to click on it)

### TARDIS v3.5.2

- TARDISChunkGenerator updated for CraftBukkit 1.9.4
- **TARDIS requires TARDISChunkGenerator v2.3 and CraftBukkit 1.9.4**

### TARDIS v3.5.1

#### Bug fixes

- Fixed the wrong chunk loading when the TARDIS materialises
- Fixed a bug with using TARDIS items when the off hand contains a usable item (like a shield)
- Fixed a nasty bug with HADS — DISPERSAL was running when the HADS type was set to DISPLACEMENT and a safe location wasn ’t found on the first try — this left the plugin thinking that the TARDIS was permanently in dispersed mode. See how to fix this below...

#### Additions

- Added a `/tardisadmin assemble [all|player]` command to unset the dispersed trackers for `all` dispersed TARDISes, or a specific `player` TARDIS

### TARDIS v3.5

#### Additions

- Added new 1.9 additions to condensable items
- Added support for MultiInv and PerWorldInventory plugins
- You can now use telepathic `/tardistravel` commands
- Made the maximum travel distance configurable 
  - plugin will use the maximum travel distance or the world border distance whichever is smaller
  - `travel.max_distance: [distance in blocks]`
  - default is `29999983`
  - the usual world border checks will still be run when checking the TARDIS’ destination
- Added new / missing Chameleon blocks to `blocks.yml`
- Added the `make_her_blue` command to the `/tardisbind cmd [command]` command
- Add a handbrake switch to the Sonic Prefs menu 
  - Useful for engaging the handbrake after a Time lord has died and may have respawned outside the TARDIS 
  - You can only _engage_ the handbrake, not disengage it
  - You cannot engage the handbrake if the TARDIS is travelling
- Added the Twelfth Doctor’s Sonic Screwdriver 
  - [TARDIS-MCP Resource Pack](https://github.com/eccentricdevotion/TARDIS-MCP) update required

#### Changes

- Made the default Police Box lamp `REDSTONE_LAMP_OFF`
- The Time Lord of the TARDIS must be online to use telepathic commands
- The plugin now uses `BARRIER` blocks for under door and platform blocks
- Signs are now part of the Sonic Screwdriver (un)interactables list
- The TARDIS can no longer be moved while it is dispersed
- The TARDIS will no longer autonomously return home while it is dispersed

#### Bug fixes

- Prevented double message for some TARDIS item interactions
- Fixed adding the Telepathic Circuit for the first time
- Fixed an error when removing players from the Companions GUI
- Fixed sticky pistons being able to retract TARDIS under-beacon blocks
- Fixed not being able to set a recharger surrounded by End rods
- Fixed the vanilla Minecraft world border check
- Fixed HADS numbers going negative when the TARDIS is dispersed
- Fixed carpet dropping when the TARDIS is dispersed
- Fixed being able to break TARDIS blocks if player has `tardis.sonic.admin` permission
- Fixed the config option `siege.butcher` not working
- Fixed a bug causing the door to drop when leaving the TARDIS ‘home’ location

#### Related stuff

- The [TARDIS-MCP Resource Pack](https://github.com/eccentricdevotion/TARDIS-MCP) has been updated to work with OptiFine 1.9.2_U_B2
- The [TARDISWeepingAngels Resource Pack](https://github.com/eccentricdevotion/TARDISWeepingAngels-Resource-Pack) has been updated to work with OptiFine 1.9.2_U_B2

### TARDIS v3.5-beta-1

#### Additions

- 

Updated for CraftBukkit / Spigot 1.9

  - Added new blocks
  - Added new Purpur chameleon preset
  - Used new Villager API additions
  - Used new Particle API
  - Used new BossBar API
  - Updated biomes and sounds
- 

Added world name tab completion to `/tardisadmin` and `/tardistravel` commands

- Added a Companion GUI for the `/tardis list companions` and `/tardis add` commands
- Added a `/tardisprefs policebox_textures [on|off]` command — setting this to `off` stops the plugin changing the biome (for MCPatcher texture support) when the Police Box materialises
- 

Implemented the Hostile Action Dispersal System

  - toggle it with the `/tardisprefs hads_type [DISPLACEMENT|DISPERSAL]` command (or use the sonic preferences menu)
  - to rebuild you must stand in the centre of the dispersed TARDIS (carpet) and right-click-air with the sonic screwdriver
- 

Added the TARDIS Telepathic Circuit

  - `/tardisrecipe telepathic`
  - `/tardisprefs telepathy [on|off]` (or use the sonic preferences menu)
  - `/tardis update telepathic`
  - Allows a companion to run any `/tardis` command for the TARDIS that they are in
- Added all door types to standard sonic function
- Added Junk mode to regular TARDISes 
  - `/tardisprefs junk [on|off]` (or use the sonic preferences menu)
  - You can only travel to saved locations while in Junk mode

#### Changes

- Carpet is no longer allowed in custom Chameleon presets
- Updated some dependent plugin versions — make sure you have the latest 1.9 versions
- The plugin now uses the new BossBar API instead of the BarAPI plugin for the TARDIS travel bar
- The plugin now uses the new Particle API for Artron Furnace and Junk TARDIS particle effects instead of the EffectLib plugin
- The plugin now checks that Multiverse-Inventories is the correct build (as well as version)
- You can now set a limit for the number of mobs that can randomly enter the TARDIS 
  - `preferences.spawn_limit: [max number of monsters]`
- The TARDIS will now throw a malfunction if no destination is found or the player is denied travel
- The plugin now checks a player is in their own TARDIS when using the `/tardis upgrade` command
- Protection is now removed from inner TARDIS blocks broken in CREATIVE gamemode
- Allow the desktop theme change if TARDIS Artron level is greater than or equal to (not just greater than) the Artron upgrade cost

#### Bug fixes

- Fixed players’ Police Box lamp preference not taking effect
- Fixed a possible NPE in the Zero room chat listener
- Fixed Junk TARDIS creation
- Fixed an issue with the Junk TARDIS not deleting
- Fixed getting the wrong world for deleting the vortex Junk TARDIS
- Fixed Junk TARDIS particle direction
- Fixed player permission checks when upgrading the TARDIS console
- Fixed not all console types being available when upgrading
- Fixed using the correct wall/floor type when doing the upgrade check
- Fixed `/tardisbook` usage (in help)
- Fixed ANDESITE, DIORITE and GRANITE in seed blocks
- Fixed the error message when adding a room and the file name is wrong
- Fixed a prefix mixup when updating the SQLite database
- Fixed getting the Factions plugin version when it contains ‘Beta’
- Fixed recipes with Potions in them
- Fixed `/ttravel area` tab completion

### TARDIS v3.4.2

#### Additions

- Added a config option `preferences.vortex_fall: [teleport|kill]` so that players that fall into the time vortex are not kicked for flying. This setting really only applies if the TARDIS WorldGuard region flag is set to `exit: deny`
  - `teleport` will send the players back into the TARDIS they were in
  - `kill` will set the players’ health to `0`, resulting in death
- Added a `/tardistravel cancel` command — cancels the last set destination
- Added `/ttravel costs` command — shows the Artron Energy costs for TARDIS travel

#### Changes

- If the `/tardistravel` command receives a world name and two coordinates, it now automatically determines the highest Y block
- When switching to the `exit: deny` flag on TARDIS WorldGuard regions, the `use: allow` flag is also now added so that players can interact with doors, buttons and pressure plates etc.
- The time travel cost is now shown in the Artron Energy HUD when a destination has been set
- In the TARDIS Saved Locations GUI you can now remove saves by dragging them to the trash (a bucket)
- When selecting the fast return location (`/tardistravel back`) the actual location is now shown in chat

#### Bug fixes

- Fixed a bug where players who were made companions inside the TARDIS could not move when the WorldGuard flag was set to `exit: deny`
- Fixed cave travel failing in some cases
- Fixed cave travel locking up the server when trying to find a cave on a HothGenerator world
- Fixed a bug where the `allow.perception_filter: [true|false]` config option was not added if missing
- Fixed a bug where players could not turn completely invisible, even though `allow.perception_filter` was set to `false`
- Fixed crops popping off when growing a GREEHOUSE room and `growth.room_speed` is set to default (`4`)
- Fixed the Chameleon Circuit sign not displaying the correct preset when the `default_preset` had been changed. This bug prevented the `NEW` preset from being applied unless it was changed to something else first

### TARDIS v3.4.1

#### Additions

- Added cave and village travel to the Keyboard

#### Bug fixes

- The Vortex Junk TARDIS now properly uses the configured wall & floor blocks
- Fixed double selecting a Chameleon preset messing up the dematerialisation sequence (causing block drops)

### TARDIS v3.4

#### Additions

- Added a bunch of missing TARDIS Information System entries (Remote Key, Artron Furnace, foods and accessories)
- Added a new MEDIUM difficulty mode — uses easy crafting recipes, automatically writes Save Storage Disks, has a new Biome Reader tool — see [http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1081-tardis-medium-difficulty/](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1081-tardis-medium-difficulty/)
- Added the TARDIS Biome Reader 
  - use the `/tardisrecipe reader` command to see how to craft it
  - clicking the Biome Reader on a block will detect the biome at the player’s current location and automatically write a Biome Storage Disk to the Disk Storage Container if there is sufficient room and the biome disk is not there already
  - cannot be used in the Nether or The End
- Added a config option to prevent condensing blocks and the use of the ‘full charge’ item if the TARDIS world is set to gamemode CREATIVE 
  - `preferences.no_creative_condense: [true|false]`
- Added an admin command for setting the `artron_furnace.particles` config option — `/tardisadmin particles [true|false]`
- Added commands to allow the Junk TARDIS wall and floor blocks to be set 
  - `/tardisjunk <wall|floor> [material]`
  - the `[material]` options are the same as when setting the wall or floor for TARDIS rooms — use tab completion!
- Added a new button to the Chameleon Construct GUI to load the last saved construct
- Added a config option to implement a TARDIS ‘open door policy’ 
  - config: `preferences.open_door_policy: [true|false]` — false by default — requires restart if enabled
  - command: `/tardisadmin region-flag [entry|exit]` — used to update all TARDIS regions in the default TARDIS world to add the specified flag (and remove its opposite). **You will need to change existing TARDIS regions** to use the `exit` flag if `open_door_policy` is `true`, but you only need to do this once

#### Changes

- The TARDISHelper and TARDISChunkGenerator plugins have been merged into one plugin — TARDISChunkGenerator v2.0, you will need to update TARDISChunkGenerator and can safely remove TARDISHelper
- Added some new API methods so that the [Dynmap-TARDIS](http://dev.bukkit.org/bukkit-plugins/dynmap-tardis/) plugin can display more information (console type etc) on maps — update Dynmap-TARDIS to v1.2 to see the results
- The plugin now gets and sets Villager career levels when farming Villagers — requires TARDISChunkGenerator v2.0
- Updated the Junk TARDIS to use red stained clay under the console and cobblestone above it
- You can no longer build door and lamp only Chameleon constructs
- When building a Chameleon construct, if there is no block under the lamp, and the lamp is a torch or redstone torch, then there will be no lamp in the resulting construct
- The DESERT Chameleon preset now uses orange stained clay instead of wool to reflect the change made to desert temples in Minecraft
- The TARDIS ‘umbrella’ now uses invisible BARRIER blocks instead of glass
- New TARDISes now have their region set to `exit: deny` instead of `entry:deny` by default
- Players can no longer place blocks inside the Police Box
- HADS can no longer be engaged if the TARDIS has not been intialised
- You can no longer change the desktop theme from someone else’s TARDIS

#### Bug fixes

- The plugin now properly updates the Junk TARDIS’ last time used
- Fixed Artron Furnace particles filling the server log with errors
- Fixed a bug trying to detect a missing BEACON block
- Fixed a bug that allowed Storage Disks to be duplicated in the Disk Storage Container
- Fixed Gravity Wells growing 1 block too high
- Fixed Gravity and Antigravity Wells not being jettisoned correctly if they were on the top or bottom level and extending beyond the ARS area
- Fixed the chameleon table not being cleaned up when a TARDIS is deleted
- Fixed a bug where the Advanced Console would destroy Save Storage Disks when circuit damage was enabled
- Fixed circuits being damaged even though the TARDIS hadn’t actually travelled yet
- Fixed saving protected blocks for the Junk TARDIS
- Fixed a bug where the correct biome was not restored when a TARDIS left a location
- Fixed the `/tardistravel biome [biome]` command crashing the server when the x or z coordinate was greater than +/- 30,000 blocks
- Added a work around for when Bukkit stores very far locations using exponential numbers
- Fixed the limits of the Junk TARDIS dangerous area, so players don’t die as often :)
- The plugin no longer messages players inside the TARDIS about releasing the handbrake when the Police box is rebuilt
- The plugin no longer sends HADS messages if the player’s HADS preference is OFF
- Fixed not being able to break the ANGEL and JAIL preset signs to exterminate the TARDIS
- Fixed the Control Centre sign showing the TARDIS Saved Locations GUI if the player updated the Chameleon Circuit sign to the Control Centre
- Fixed Chameleon presets with trapdoors dropping trapdoors when materialising, changing direction or hiding
- Fixed toggling the trapdoor on presets with a trapdoor
- Fixed a bug allowing players to unsiege inside another player’s TARDIS

### TARDIS v3.4-beta-2

#### Additions

- Added a `/tardisjunk time` command to see how much time is left before the Junk TARDIS automatically returns home

#### Changes

- The plugin now loads chunks before returning the Junk TARDIS
- The plugin now loads chunks before teleporting player to the Vortex Junk TARDIS
- The handbrake lever now toggles when travelling in Junk TARDIS
- The Resdtone Torch now flashes when flying the Junk TARDIS
- If the Junk TARDIS destination sign is blank, then clicking the handbrake lever now returns the console to the ‘home’ location (if away)

##### Bug fixes

- Fixed a bug where EffectLib needed to be installed even if particle effects were disabled
- Fixed lamp scanning using the `/tardis lamps` command after upgrading to a larger console size
- Fixed lava flowing over rooms below the console after upgrading from MASTER console
- Fixed the upgrade block scanner bugging out when upgrading to the REDSTONE console
- Fixed redstone torches popping off when upgrading to the MASTER’s console
- Fixed permissions for the `/tardisjunk find` command
- Fixed the Junk TARDIS return check logic
- Fixed the `/tardisadmin delete junk` and `/tardisadmin purge junk` commands not working for the Junk TARDIS
- Fixed a NulPointerException when checking if a block is an Artron Furnace

### TARDIS v3.4-beta-1

#### Additions

- Added a Junk TARDIS 
  - The Junk TARDIS is a public use TARDIS that any player with the correct permission can use on the server. It only travels in overworlds (no Nether or The End) and doesn’t require the player to have an Artron Energy supply
  - Video here: [https://vimeo.com/139185257](https://vimeo.com/139185257)
  - **permissions:** `tardis.admin` to create, delete and return the Junk TARDIS — `tardis.junk` to fly and find the Junk TARDIS
  - **commands:** `/tardisjunk create` — target the centre block of a flat 6 x 6 area — this becomes the Junk TARDIS ‘home’ location
  - `/tardisjunk delete` — self explanatory
  - `/tardisjunk return` — returns the Junk TARDIS to its ‘home’ location (bringing along any players who may be onboard)
  - `/tardisjunk find` — find the current location of the Junk TARDIS
  - **config options:** `junk.enabled: [true|false]` — whether the Junk TARDIS is available on the server — **NOTE:** The Junk TARDIS will be disabled if `creation.default_world` is `false`
  - `junk.particles: [true|false]` — whether the Junk TARDIS displays a particle effect when travelling — requires [EffectLib 3.4](http://dev.bukkit.org/bukkit-plugins/effectlib/) or higher
  - `junk.return: [time in seconds]` — if `[time in seconds]` is greater than `0` and the Junk TARDIS has not been used for longer than the specified period, it will automatically return to its ‘home’ location
  - **controls:** sign — shows the travel destination, handbrake — starts the TARDIS travelling, tripwire hook — selects the world to travel to, stone button — sets the x coordinate, wood button — sets the z coordinate, repeater — determines the amount added or subtracted from the x and z coordinates when the buttons are clicked, comparator — sets whether the buttons add or subtract
  - players moving off the Junk TARDIS while it is travelling are killed instantly
  - update the TARDIS-Sounds resource pack for the new sfx
- You can now give a full Artron Storage Cell with:  
`/tardisgive [player] cell [amount] full`
- Added The Master’s TARDIS 
  - designed by shadowhawk14269
  - seed block is nether brick
  - permission is `tardis.master`
- Added a Pyramid TARDIS console 
  - many thanks to Airomis for the design
  - seed block is sandstone stairs
  - permission is `tardis.pyramid`
- Added a particle effect to Artron Furnaces — requires [EffectLib 3.4](http://dev.bukkit.org/bukkit-plugins/effectlib/) or higher
- Added an Ignite Circuit to the Sonic Screwdriver 
  - view the recipe with `/tardisrecipe ignite-circuit`
  - permission: `tardis.sonic.ignite`
  - don’t forget to move the new map file into its correct location!
  - flammable blocks need to be clicked on the side
  - a flint & steel is actually more useful / powerful in-game
- Added a Chameleon preset construction GUI 
  - update the TARDIS-MCP resource pack for the new textures
  - accessed via the third page of the Chameleon presets GUI
  - help & information on use is built into the GUI
  - all text can be translated using the _plugins/TARDIS/language/chameleon\_guis.yml_ file
  - if `allow.all_blocks` is `false`, precious blocks cannot be used in the GUI
- Added a few more interactable blocks for the environment sonic to ignore
- Added a `/tardis update beacon` command
- Added a config option to disable random spawning in TARDIS if door is open — `preferences.spawn_random_monsters: [true|false]` — or use the admin command `/tardisadmin spawn_random_monsters [true|false]`
- Added more checks and warnings for missing custom console upgrade entries
- Added a `getOverWorlds()` method to TARDIS API
- Added a player configurable choice of (2) blocks that are changed during siege mode engagement (only applies if `siege.textures` is `true` in the config)
- Added config options to limit desktop upgrades by counting the percentage of blocks that have changed from the original schematic 
  - must be enabled in the config — `desktop.check_blocks_before_upgrade: [true|false]` and `desktop.block_change_percent: [max percent changed blocks allowed]`
  - Defaults are `false` and `25`

#### Changes

- The plugin no longer lists items if the add-on plugin isn’t enabled
- Added a check for malformed plugin versions when checking plugin dependencies
- The allowable lamps list (`lamp_blocks` in blocks.yml) now requires Bukkit Material names rather than IDs 
  - existing IDs will be automatically updated
- The plugin now allows all door types for minecart entry
- The beacon toggler block (used when powering up & down) is now a redstone block 
  - adds the ability to trigger redstone contraptions when the TARDIS is powered down
- The space between TARDISes parked in TARDIS areas is now configurable 
  - default distance can be set in the config — `creation.parking_distance: [distance]` — or use the admin command `/tardisadmin parking_distance [distance in blocks between TARDII]`
  - each TARDIS area can be set to different distances — `/tardisarea parking [area name] [distance]`
- The `tardis.ars` permission is now solely used for the ARS console — permission to use the Architectural Reconfiguration System to grow rooms now requires `tardis.architectural` — update your permissions!
- If you’re changing the desktop theme using the same console type you now only have to have 1/2 the upgrade cost in stored Artron Energy

#### Bug fixes

- Fixed the SQLite -\> MySQL database converter tool
- Fixed the second Lazarus easter egg
- Fixed an error in TARDIS Information System for the Painter Circuit
- Fixed saving the location of a secondary console handbrake
- Fixed Antigravity / Gravity Wells growing in the wrong location
- The plugin now prevents double-clicking items to the cursor in all TARDIS GUIs
- Fixed the beacon not turning off when powering down
- Malfunctions now respect a player’s lamps / lanterns preference
- Fixed a bug where some language files were not copied when doing a fresh install because the _plugins/TARDIS/language/_ directory hadn’t been created yet

### TARDIS v3.3

Requires _CraftBukkit/Spigot 1.8.7, TARDISHelper 1.7.3_ (included in the TARDIS ZIP file download), and if installed, _TARDISWeepingAngels v2.0.7_

ALWAYS run BuildTools to get the latest server JARs before installing/updating TARDIS!

#### Additions

- Added the Sonic Blaster and Blaster Battery to the `/tardisgive` command
- Made perception filter configurable — `allow.perception_filter`
- Made TARDIS sign text configurable
- Added a configuration option for a MySQL database prefix — `storage.mysql.prefix`
- Added the ability to Get/Set a Villager’s willingness to breed/trade — requires TARDISHelper v1.7.3
- Add messages when modifying genetic material using the Lazarus Room
- Made the Artron HUD localisable — edit the `ARTRON_XXXX` entries in your language file

#### Changes

- Updated Bukkit dependency to 1.8.7
- Mobs can now spawn inside the TARDIS via dispensers
- The TARDIS will not auto power down if the handbrake is off
- Use translation file for ‘ON’ and ‘OFF’ where used throughout the plugin
- The plugin now runs the `/twad` command via the console when genetically modifying players — requires TARDISWeepingAngels v2.0.7
- Allow the sonic to work on players with both the bio-scanner and admin upgrades — the Admin upgrade to view a player’s inventory requires the admin to be sneaking while clicking the player

#### Bug fixes

- Fixed a spelling mistake in the recipe lister
- Fixed `/tardis rebuild` block duplication exploit
- Fix seed block crafting duplication exploit
- Fixed doors not opening on 2x2x2 TARDIS interiors
- Load Multiverse before checking for the default world creation — fixes NPE on fresh installs when trying to enter TARDIS
- Only rotate the TARDIS direction frame in 90 deg angles
- Now checking the LANTERN player preference when using the `/tardis lamps` command

#### Dependent plugin versions

If running any of these plugins with TARDIS they must be equal to or _greater than_ the versions listed here:

- BarAPI — v3.3
- Citizens — v2.0.16
- Factions — v2.7.4
- GriefPrevention — v10
- LibsDisguises — v8.5.1
- MultiWorld — v5.2
- Multiverse-Adventure — v2.5
- Multiverse-Core — v2.5
- Multiverse-Inventories — v2.5
- My Worlds — v1.67
- ProtocolLib — v3.6.3
- TARDISHelper — v1.7.3
- TARDISWeepingAngels — v2.0.7
- Towny — v0.89
- WorldBorder — v1.8.1
- WorldGuard — v6.0.0

### v3.2.11

#### Additions

- Added upcoming TARDIS Sonic Blaster recipes to `/tardisrecipe` command
- Added `/tardistravel village` command 
  - Config: `allow.village_travel: [true|false]`
  - Permission: `tardis.timetravel.village`
- Added a room cost message to the `/tardisroom required` command
- Added a `/tardisadmin arch [player] force` command to allow an admin to toggle a player’s ‘arched’ status
- Added a player preference for whether consoles use lamps or lanterns for console lights 
  - `/tardisprefs lanterns [on|off]`
  - The player preference is `off` by default, but will be automatically set to `true` when creating or upgrading to an ELEVENTH or TWELFTH console
  - It is best left **OFF** for _existing consoles_ as the levers that power lamps cannot be placed on lanterns — therefore breaking the lights if they are then switched back to lamps — to get around this, up-, down- or cross-grade your console first — this version contains new schematics to rectify the problem

#### Changes

- By default the config option `default_world` is now **true** for new installs — this will automatically create the _TARDIS\_TimeVortex_ world — `create_worlds` is now defaulted to false
- The plugin now persists Villager careers when mob farming — **requires TARDISHelper v1.4**
- The plugin now checks that any dependent plugins that are on the server are the correct version for _this version of TARDIS_ — if not, TARDIS is disabled until they are updated
- TARDIS WorldGuard regions are updated if the player name has changed
- The plugin now uses new `player.getTargetBlock()` and `itemMeta.addItemFlags()` methods — requires an updated CraftBukkit / Spigot build
- Enchantment text is now hidden on Sonic Screwdrivers, Artron Storage Cells and the arrows in the Chameleon GUI 
  - Requires the latest CraftBukkit / Spigot build
  - Not compatible with Multiverse-Inventories (they need to update) — change will not be applied if Multiverse-Inventories is enabled
- Updated the ELEVENTH and TWELFTH console schematics to use sea lanterns 
  - Existing consoles can also use lanterns if you set the player preference for lanterns (see above)
- Custom consoles now have a `has_lanterns: [true|false]` config option\* 
- Sea lantern can now be used as the Police Box lamp
- The plugin now sets the `CookTimeTotal` furnace entity tag in Artron Furnaces (for better burn animations) — **requires TARDISHelper v1.4**

#### Bug fixes

- Fixed a TARDIS Remote Key message
- Fixed a misspelled ZERO room message
- Removed an extra word ‘command’ in the `/tardisarea` command message
- Fix anti/gravity room growing 
  - Anti-Gravity / Gravity rooms now properly grow outside of the ARS grid
  - Gravity rooms will now grow under rooms in the level above 
- The plugin now makes sure the furnace is powered by Artron Energy before altering the cook time
- Fixed the ARS GUI showing a different rooms after the room scroll buttons were clicked

### v3.2.10

### Additions

- Added a `/tardis?` command (alias `/t?`) to replace the old outdated help messages
- Added Hopper Minecarts to the RAIL room
- Added progress messages when growing a room

### Changes

- Updated the achievement XP rewarder to cope with huge amounts of XP
- Made it so disabling HADS doesn’t require a restart to take effect
- Vastly improved the `/tardis help` command — it now shows pages and includes links to online documentation
- The plugin now uses Multiverse world name aliases (if they are set) when showing the scanner results, find location, and random destination messages
- Player permissions are now checked before the grace period on hard mode
- When accessing ARS the plugin always gets the ARS Map of the TARDIS the player is in, but only processes it if they are the Time Lord of that TARDIS
- Minor changes to the TARDIS API — this means that you will need to update the TARDISVortexMainpulator plugin as well (if you use it)

### Bug fixes

- The plugin is now disabled at startup if the installed WorldGuard plugin is the wrong version
- Fixed Guardians spawning in the TARDIS when there is no POOL room
- Fixed the minecart not returning from the RAIL room because the chunk unloads

### v3.2.9

[View requirements for this version](#reqs)

### Additions

- Added a console warning if Remote Key ingredient does not = TARDIS Key result
- Added Siege Cube protection when dropping the cube from the player’s inventory (as well as the existing case of when it is mined)
- 

Added a config option to allow Guardians to be pulled into the TARDIS if the door is left open — it’s `false` by default:

    allow:
        guardians: [true|false]       

  - regardless of the setting, guardians will not be spawned in the TARDIS if there is _no POOL room_
  - if set to `true` — any guardians within a 16 blocks radius of the open TARDIS door will be sucked into the pool
  - if set to `false` — there is still a 25% chance that a random monster will spawn in the TARDIS, and a 1 in 12 chance that it will be a guardian
  - toggle the setting with `/tardisadmin guardians [true|false]`

### Changes

- Daleks (from the TARDISWeepingAngels plugin) spawned in the TARDIS (when the door is open) will now be redisguised after teleportation
- If a world is deleted from the server, the plugin now attempts to clean the database of invalid block and portal records

### Bug fixes

- Added missing return statement that caused the TARDIS to unsiege inside itself
- Fixed some NPEs with the Artron Furnace
- Fixed repeaters not being reset after manual flight
- Blocks and portals that belong to non-existent worlds are no longer loaded

### v3.2.8

[View requirements for this version](#reqs)

### Additions

- Added a `/tardisadmin desiege [player]` command 
  - Stop tracking the siege cube
  - Set siege mode to OFF
  - Rebuild the TARDIS at the home location
- Added a `/tardis cube` command to see which player is carrying the Siege Cube
- Added a `/tardisadmin list portals` command (primarily for debugging)
- Added a check / warning if GOLD\_NUGGET ingredient in the Remote Key recipe is different from the result of crafting the TARDIS Key — _if you have changed the recipe result of the TARDIS Key to something other than a GOLD\_NUGGET, then you will also need to change the matching ingredient in the Remote Key recipe!_

### Changes

- Swamp Hut preset now has a walk in portal
- You can no longer power down using the Control Centre sign if `allow.power_down` is `false` in the config
- Added `tardis.budget` as a child node of the `tardis.use` permission
- A TARDIS Key is now shown in the TARDIS Remote Key recipe (`/tardisrecipe r-key`) — _if you have changed the recipe result of the TARDIS Key to something other than a GOLD\_NUGGET, then you will also need to change the matching ingredient in the Remote Key recipe!_

### Bug fixes

- Fixed rabbit ejecting — Rabbits are not Sheep!
- Temporary fix for Swamp Hut preset popping the door off, waiting on CraftBukkit/Spigot for bugs fixed in Minecraft itself (toggable blocks poppng off half slabs)
- Fixed some cases where the world name was not being read/set correctly
- Fixed / added some missing default config options
- Fixed the permissions check when crafting a BUDGET seed block
- Fixed the 3-D Glasses not being white anymore — update your TARDIS resource pack

### v3.2.7

[View requirements for this version](#reqs)

#### Additions

- 

Added a TARDIS Artron Furnace — requires TARDISHelper v1.2 (included in ZIP file)

  - `/tardisrecipe furnace`
  - permission to place a furnace: `tardis.furnace`
  - Custom texture — update the [TARDIS-MCP](https://github.com/eccentricdevotion/TARDIS-MCP) resource pack
  - Custom sound when burning starts — update the [TARDISSound](https://github.com/eccentricdevotion/TARDIS-SoundResourcePack) resource pack
  - set config options in **artron.yml** :

        artron_furnace:
            burn_limit: 100000
            burn_time: 0.5
            cook_time: 0.5
            set_biome: true

  - `set_biome [true|false]` — whether to set the biome so that custom textures work on the furnace block
  - `cook_time: [time ratio]` — sets the cook time for items smelted with Artron Storage Cells — `0.5` is half the normal time, `2.0` would be twice normal speed (longer), 1 = normal cook time (same as coal)
  - `burn_time: [time ratio]` — sets the length of time an Artron Storage Cell will burn for — as above fractions (`0.5`) will give shorter life to the cells, \> 1 will extend the burn time, 1 = normal burn time (same as coal)
  - `burn_limit: [amount]` — the maximum amount of burn time a fully charged Artron Storage Cell can have.
  - The actual maximum amount given is `burn_limit * burn_time * (cell_charge_level / full_charge)`
  - default settings mean that the Artron Furnace cooks quickly but doesn’t burn as long.
- Added support for banner colours and patterns in TARDIS schematics

#### Bug fixes

- Fixed item duplication exploit when crafting seed blocks (again)
- Fixed invisibility potions being removed when not temporally located
- Fixed rabbit farming (and [found a CraftBukkit bug](https://hub.spigotmc.org/jira/browse/SPIGOT-392)!)
- Fixed some missing arguments to the `/tardisrecipe` command tab completion
- Fixed missing 1.8 mobs in the Monster runnable

### v3.2.6

[View requirements for this version](#reqs)

- Fixed spamming chat with plugin.yml defaults when the player rescue method returns false
- Added the ability to render entities in the Render Room 
  - `preferences.render_entities: [true|false]`
  - `/tardisadmin render_entities [true|false]`
  - Requires Citizens 2.0.14
- Fixed a long standing bug where rooms could be grown manually in the ARS grid for bigger sized and custom consoles
- Fixed a bug when using the `/tardis room [room]` command to check the blocks required to grow the room, and then using ARS, resulted in the plugin thinking you wanted to manually grow the room 
  - Players should use the command `/tardisroom required [room]` instead
- Added tab completion to the `/tardisroom` command
- Added a player preference for whether TARDIS will automatically enter siege mode if there is insufficient energy to travel home 
  - `/tardisprefs auto_siege [on|off]` or use the sonic prefs menu
- Added a **“how to”** GUI 
  - if a player asks in chat _“How do I build/create/make/get a TARDIS?”_, a GUI opens showing them the seed blocks they have access to
  - if they click a seed block, it shows them the recipe
  - if they click the interior wall or floor blocks, it will show them the blocks they can use
  - if they click the police box wall block, it will show them the chameleon blocks they can use
- Added rabbits, guardians and endermites to the genetic manipulator
- Fixed more item duplication exploits when crafting seed blocks

### v3.2.5

[View requirements for this version](#reqs)

- Added an option to allow TARDIS owners to see their door when it is INVISIBLE — must be wearing 3D Glasses and looking at the block that the door would normally be placed on — `/tardisadmin 3d_doors [true|false]`
- Allow the Remote Key to toggle the door open and closed when TARDIS is invisible — RIGHT-click air
- Fixed TWELFTH schematic sponge blocks interfering with door circuits when upgrading
- Fixed the Upgrade GUI showing incorrect lore for current console
- Removed duplicate blocks in wall / floor upgrade GUI
- Fixed monsters not entering TARDIS if the door is left open
- Fixed door and sign popping off when autonomous homing
- Fixed exceptions in the 3D Glasses Listener

### v3.2.4

[View requirements for this version](#reqs)

- Prevented disabling siege mode when the siege cube is inside a TARDIS
- Added a rabbit HUTCH room
- Added rabbits to TARDIS mob farming — you must grow a HUTCH room first
- Added a mob farming player preference — must be on to farm mobs — `/tardisprefs farm [on|off]` or use the sonic prefs menu
- Villagers now keep their trades when farming them — requires TARDISHelper v1.2 or higher
- You can now eject rabbits from the TARDIS
- The Chameleon circuit now uses red sandstone when landing on red sand
- You can now power up and check energy levels from the Control Centre GUI when the TARDIS is powered down
- Fixed levers popping off when growing consoles and rooms
- Fixed pressure plate locations and some minor inconsistencies in TWELFTH console schematic
- Fixed a seed crafting duplication exploit

### v3.2.3

[View requirements for this version](#reqs)

- Fixes for desktop theme up/downgrading: 
  - downgrading to TWELFTH console  no longer jettisons 3/4 of the console
  - the REDSTONE console now builds at the correct level
  - better dropped item removal
  - setting torch blocks correctly
  - always teleport player to safe location
  - add control centre sign if in schematic
- Added ability to allow a `~` shortcut for current world in the `/tardistravel [world] [x] [y] [z]` command
- Changes to autonomous homing feature: 
  - don’t autonomous home if the TARDIS is in siege mode
  - if insufficient energy to go home, enter siege mode instead

### v3.2.2

[View requirements for this version](#reqs)

- Fixed a Police Box duplication bug
- Added a `/tardisadmin remove_flag` command to remove the mob-spawning flag in **old** TARDIS WorldGuard regions (newer ones won’t have the flag set) — once the command is run, this should fix the Farm, Village and Stable rooms not transferring mobs

### v3.2.1

[View requirements for this version](#reqs)

- The plugin now checks to see if the server has the (vanilla) WorldBorder class and will disable itself if it is not found
- Fixed stone pressure plate locations in TWELFTH schematic
- Fixed red zeroes showing in the `/tardisrecipe` command

### v3.2

[View requirements for this version](#reqs)

#### Additions

- Added new fence gates to the Sonic Screwdriver interactable block list
- Added COARSE\_DIRT and PODZOL to wall / floor blocks
- Added basic seed blocks to the `/tardisgive` command — `/tardisgive [player] seed [type]`
- Added an upgrade warning when changing TARDIS console types (chests and items will be deleted!)
- 

You can now redefine the End and Nether buttons in the Destination Terminal to specific worlds when travel to those environments is disabled

    travel:
        the_end: false
        nether: false
        terminal:
            redefine: [true|false]
            nether: [world]
            end: [world]

#### Changes

- Autonomous homing is disabled if the TARDIS is powered down
- The TARDIS will power down after returning home autonomously (and `allow.power_down: true`)

#### Bug fixes

- Added missing ‘e’ in upgrade GUI
- Fixed not being able to place armour stands in TARDIS worlds
- Fixed a few misspellings in _en.yml_
- Fixed ProtocolLib Keyboard packet listener not working and throwing errors on sign placement
- Fixed the redstone upgraded Sonic Screwdriver not powering LAMPS
- Fixed the seed block not recognising ANDESITE, DIORITE, GRANITE and their POLISHED variants as valid Chameleon blocks when crafted
- Fixed grid not clearing when crafting a seed block

### v3.2-beta-1

#### Requirements

Do not bother posting comments / tickets if you are not running the appropriate server and plugin versions — they will be deleted immediately!

##### Server

A new WorldBorder API was addedd to CraftBukkit / Spigot on the 13 December 2014. This means if your server JAR was built before then it **WILL NOT WORK** with TARDIS. You should use Spigot’s [BuildTools](https://hub.spigotmc.org/jenkins/job/BuildTools/) to compile a new server JAR — instructions for doing this can be found here: [How to use BuildTools](build-tools.html).

- [CraftBukkit](http://tardisjenkins.duckdns.org:8080/job/BuildTools/lastSuccessfulBuild/artifact/craftbukkit-1.11.2.jar) — version git-Bukkit-1092acb (MC: 1.8) or newer

_or_

- [Spigot](http://tardisjenkins.duckdns.org:8080/job/BuildTools/lastSuccessfulBuild/artifact/spigot-1.11.2.jar) — version git-Spigot-b1e6da1-1092acb (MC: 1.8) or newer

##### Plugins

If running the following plugins the **minimum** version required is shown.

- [WorldGuard](http://dev.bukkit.org/bukkit-plugins/worldguard/files/45-world-guard-6-0-beta-5/) — version 6.0 beta 5
- [ProtocolLib](http://ci.shadowvolt.com/job/ProtocolLib/) — version 3.6.3-SNAPSHOT build #50
- [LibsDisguises](http://ci.md-5.net/job/LibsDisguises/) — version 8.2.6-SNAPSHOT build #363
- [Factions](http://dev.bukkit.org/bukkit-plugins/factions/) — version 2.7.4 (+ MassiveCore 2.7.4)
- [Citizens](http://ci.citizensnpcs.co/job/Citizens2/) — version 2.0.14-SNAPSHOT build #1194
- [BarAPI](https://dev.bukkit.org/projects/bar-api) — version 3.3-SNAPSHOT

TARDISHorseSpeed is now obsolete, you need to use TARDISHelper v1.2 instead (included in the TARDIS ZIP file download).

#### Additions

- Added a config option to set the default preset on TARDIS creation — `/tardisadmin default_preset [preset]`
- Added a Randomiser Circuit: 
  - to see the recipe use the command `/tardisrecipe random-circuit`
  - to give the circuit use the command `/tardisgive [player] random-circuit [amount]`
  - put it in the Advanced Console, a random location will be chosen when the GUI is closed
  - the scanner is disabled until the random destination has been travelled to
  - the Artron cost is configurable in _artron.yml_ — `random_circuit: 150`
- Added an SQLite to MySQL conversion tool. Double-clicking the TARDIS JAR file opens the GUI.
- Added Siege Mode — see the [Siege Mode](siege-mode.html) page for more info
- Added PACKED\_ICE and new 1.8 blocks to artron condensables
- Added new 1.8 blocks to TARDIS wall / floor choices
- Added tab completion to `/tardisbook` command
- Added a cooldown to the `/tardis hide` command
- Added the ability to add multiple custom TARDIS consoles — see the [Custom consoles](custom-consoles.html) page
- Added a TARDIS API for other plugin developers to hook into — for example [TARDISVortexManipulator](http://dev.bukkit.org/bukkit-plugins/tardisvortexmanipulator/) — see the [Java Doc](http://thenosefairy.co.nz/TARDIS_java_docs/me/eccentric_nz/TARDIS/api/TardisAPI.html) and [API](api.html) pages for more info
- Added the Vortex Manipulator to `/tardisrecipe` and `/tardisgive` commands — `/tardisrecipe vortex` and `/tardisgive [player] vortex [amount]`
- Added Vortex Manipulator tachyon energy to the give command — `/tardisgive [player] tachyon [amount]`
- Added 4 new presets: PRISMARINE, ANDESITE, DIORITE and GRANITE
- Added an invisibility circuit to enable invisible landing — see the [Invisibility](invisibility.html) page for more info 
  - to see the recipe use the command `/tardisrecipe invisible`
  - to give the circuit use the command `/tardisgive [player] invisible [amount]`
  - put it in the Advanced Console to enable the INVISIBLE chameleon preset
  - choose the preset to land invisibly on the next time travel
  - the circuit can be damaged after each use — set the number of uses in the config — see the [Circuit use and repair](circuit-use.html) page for more info
- Added a `/tardis make_her_blue` command to disengage invisibility
- Added a control panel GUI — lots of buttons in one place — use the `/tardis update control` command to add it to your console
- Added the new 1.8 doors to Sonic interactable blocks
- Added a new Twelfth Doctor’s console — use the `/tardisrecipe twelfth` command to see the recipe — permission `tardis.twelfth` — once again thanks go to **killeratnight** for the outstanding design :)

#### Changes

- The plugin only changes the wall and floor blocks when running the Desktop Theme changer and the TARDIS type is not being changed
- Changed the default LIBRARY room seed block to match the one in ARS — this fixes a conflict with the PLANK console seed block
- Prevented door opening when the TARDIS is materialising
- The Portal persister now checks for ‘null’ world entries — this fixes start-up errors for corrupt database records
- Players are added to a TARDIS’ WorldGuard region membership if they have the `tardis.skeletonkey` permission node
- The Chameleon GUI now has three pages
- The Admin config GUI now has two pages
- The biome search now starts offset from the current location so that it is possible to find a Deep Ocean biome
- If `respect_worldborder` is true the plugin now checks both vanilla and plugin world borders — the WorldBorder plugin takes precedence over vanilla
- The `/tardis make_her_blue`, `/tardis hide` and `/tardis rebuild` now have a short materialisation
- All circuits can be configured to be damagable — see the [Circuit use and repair](circuit-use.html) page for more info

#### Bug fixes

- Fixed redstone torches popping off when creating the console and growing rooms
- The plugin now prevents click-dragging in all TARDIS GUIs (fixes a duplicate items exploit)
- Fixed Redstone TARDIS schematic so walk in/out portals work
- Fixed a `/tardis eject` exploit
- Fixed having to load the map even if just closing the Map/ARS GUI
- Fixed the broken `/tardisbook` command
- Fixed broken achievements
- Fixed an incorrect ARS slot location
- Fixed broken `/tardis update` chat interface
- Fixed TARDIS book instructions, been a long time coming lol
- Fixed incorrect default `respect_worldguard` config option
- Fixed breaking the Police Box sign to remove TARDIS

### v3.1

There are some breaking changes — please read _everything_ carefully!

#### Additions

- Added the ability to prevent piston harvesting of TARDIS Police Box and other precious blocks

    preferences:
                nerf_pistons:
                enabled: [true|false]
                only_tardis_worlds: [true|false]

- Added a cooldown to the `/tardis rebuild` command

#### Changes

- The Vault room sorter now recognises ItemStack data values
- Now compiled against Spigot-API in preparation for the upcoming Spigot 1.8 release
- Updated WorldGuard dependency! If installed, TARDIS now requires WorldGuard to be v6 or higher

#### Bug fixes

- Fixed an `ArrayIndexOutOfBoundsException` if there is no rail room
- Fixed ICE not being changed to water when growing a room
- We’re now checking to make sure it is a TARDIS JSON schematic being added to custom rooms — in case the user hasn’t read the documentation
- The whole stack of Artron Cells is transferred to the Artron capacitor, not just one
- Fixed Grief Prevention claim checking

### 3.1-beta-1

Requires CraftBukkit version git-Bukkit-1.7.9-R0.2-24-g07d4558-b3116jnks (MC: 1.7.10) (Implementing API version 1.7.10-R0.1-SNAPSHOT) or the Spigot equivalent.

#### Additions

- Added the ability to change the TARDIS desktop theme — see: [Desktop Theme](desktop-theme.html)
- Added `/tardis eject` command to eject farmed mobs, villagers and companions — see: [Mob farming](farming.html)
- Added time rotor glass colouring, use the `/tardis colourise` or `/tardis colorize` command, then click the bottom glass block in the time rotor with a dye
- Added a clickable chat interface for the `/tardis update` command (just do a /tardis update with no further arguments) — requires ProtocolLib — the command remains essentially the same otherwise
- Updated the VAULT room to automatically sort items into the chests: 
  - see: [Vault room](vault.html)
  - one chest is set as the drop chest
  - all other chests in the 16x16x16 chunk are set as storage chests
  - place at least 1 block/item into the storage chests
  - blocks/items placed in the drop chest will automatically be sent to the chest with the same block/item in it
  - add a sorter chest to existing VAULT rooms with the `/tardis update
                        vault` command

#### Changes

- Updated the creation help message to use Seed blocks rather than block stacks
- Updated the Lazarus Device to support TARDISWeepingAngels v2.0.2
- The Emergency Programme One message can now be set to the contents of the book in the player’s hand (the command remains essentially the same)
- There is now a 15 million block limit to coordinates travel
- Saved biome disks can now be extended by combining them with a ladder

#### Bug fixes

- Prevented a Lockette exploit
- Prevented autonomous travel to TARDIS creation area
- An incorrect world argument given to ~relative travel command is now caught instead of throwing an error

### v3.0.1

#### Bug fixes

- Fixed broken `/tardisroom add` command

### v3.0

#### Changes

- Added a door lock message when using the Remote Key to lock/unlock the TARDIS
- Custom ItemStack attributes are saved and restored when switching inventories while ‘fobbing’

#### Bug fixes

- Fixed door lock message when not using the ‘walk in’ method of entry
- Caught a couple of errors when when players incorrectly update their console repeaters
- Fixed the ‘stone column’ preset disc being uncraftable
- Fixed a bug when removing and re-adding the ARS sign
- Removed the CUSTOM console from the ARS GUI

### v3.0-beta-3

#### Changes

- Factions and MassiveCore dependencies updated (minimum versions required are Factions 2.5 and MassiveCore 7.3)
- When disguising a ‘fobbed’ player, we now always set `HideHeldItemFromSelf` and `ViewSelfDisguise` to false (Chameleon Arch)

#### Bug fixes

- Fixed LibsDisguises support not being loaded at all (Chameleon Arch, Genetic Manipulator)
- Fixed Fireworks metadata being removed when switching inventories (Chameleon Arch)
- Fixed an exploit that could be used to generate fob watches (Chameleon Arch)
- Fixed losing inventories if Multiverse-Inventories or GameMode Inventories is on the server (Chameleon Arch)
- Fixed annoying messaging when TARDIS mob farming and Multiverse-Inventories is on the server and no mobs are actually being farmed
- Fixed an NPE in `/tardis arch_time` command and after using the `/kill` command

### v3.0-beta-2

#### Additions

- Added a `/tardisnetherportal` command, or `/tnp` for short. Use it to calculate the position to place a portal in the NETHER / OVERWORLD in order to link them. Inspired by Nether Portal Calculator v1.0 by D3Phoenix, see [http://ilurker.rooms.cwal.net/portal.html](http://ilurker.rooms.cwal.net/portal.html)

#### Bug fixes

- Fixed TARDIS Remote Key recipe in `easy` difficulty mode
- Fixed trying to parse a string float as an integer when determining the location of the console BEDROCK block, and also now getting the correct location for the BEDROCK block in the new schematics
- Fixed trying to toggle the beacon on/off in the PLANK and TOM TARDISes (there isn’t one)
- Fixed a NPE in the Chameleon Arch
- Fixed grace period count records not being created when needed
- Fixed player difficulty settings not taking affect because of the bug above
- Fixed a messaging bug when trying to overcharge an Artron cell
- Fixed plugin enable and disable errors if LibsDisguises is not installed

### TARDIS v3.0-beta-1

- CraftBukkit 1.7.10-R0.1 **recommended**
- [LibsDisguises](http://ci.md-5.net/job/LibsDisguises/) build #312 or higher **required** (if enabling the Genetic Manipulator and Chameleon Arch features)
- [Sound](https://github.com/eccentricdevotion/TARDIS-SoundResourcePack) and [MCP](https://github.com/eccentricdevotion/TARDIS-MCP) resource packs updated
- TARDISHorseSpeed updated for CraftBukkit 1.7.10-R0.1

#### Additions

- Added a `/tardisschematic` command (`/ts` for short) — use it to create the new TARDIS schematics (see Changes below) and [http://eccentricdevotion.github.io/TARDIS/schematic-commands.html](http://eccentricdevotion.github.io/TARDIS/schematic-commands.html)
- Added craftable bow ties and 3D-glasses (requires MCPatcher and the TARDIS-MCP Resource Pack). See [http://eccentricdevotion.github.io/TARDIS/accessories.html](http://eccentricdevotion.github.io/TARDIS/accessories.html) for recipes and functionality.
- Added a config option to restrict the number of rooms that can be reconfigured at one time with ARS. See [http://eccentricdevotion.github.io/TARDIS/configuration-growth.html](http://eccentricdevotion.github.io/TARDIS/configuration-growth.html#alimit)
- Added a craftable Fob Watch with a Chameleon Arch ability — [http://eccentricdevotion.github.io/TARDIS/chameleon-arch.html](http://eccentricdevotion.github.io/TARDIS/chameleon-arch.html)
- Added a TARDIS creation area config option, see: [http://eccentricdevotion.github.io/TARDIS/configuration-creation.html](http://eccentricdevotion.github.io/TARDIS/configuration-creation.html#carea) and [http://eccentricdevotion.github.io/TARDIS/tardis-areas.html](http://eccentricdevotion.github.io/TARDIS/tardis-areas.html#forcec)
- Added a `/tardisarea yard [area name] [material] [material]` command to visually set up parking spots in the specified TARDIS area, see: [http://eccentricdevotion.github.io/TARDIS/area-commands.html](http://eccentricdevotion.github.io/TARDIS/area-commands.html#yard)
- Added a `/tardistravel player ?` command to request travel to a player’s protected region/claim. Can also use `/tardistravel player tpa`.
- Added a config option to allow players to use ‘easy’ mode while within the configured grace period (and the server is on ‘hard’). See [http://eccentricdevotion.github.io/TARDIS/configuration-travel.html](http://eccentricdevotion.github.io/TARDIS/configuration-travel.html#grace)
- Added a [config option](http://eccentricdevotion.github.io/TARDIS/configuration-allow.html#diff), [permission](http://eccentricdevotion.github.io/TARDIS/permissions.html) and [command](http://eccentricdevotion.github.io/TARDIS/player-preferences.html#diff) to allow the personal setting of difficulty preference by players.
- Added a public server condenser. To add a server condenser, place a chest, target it, then run the command `/tardisadmin condenser`
- Added remote deadlocking, unlocking, hide and rebuild via a new craftable remote key, see [http://eccentricdevotion.github.io/TARDIS/remote-key.html](http://eccentricdevotion.github.io/TARDIS/remote-key.html)

#### Changes

- The plugin no longer uses MCEdit/WorldEdit schematics, instead, a new TARDIS JSON format is used. All the default plugin schematics will be updated automatically, but custom consoles and rooms will need to be remade. See [http://eccentricdevotion.github.io/TARDIS/schematics.html](http://eccentricdevotion.github.io/TARDIS/schematics.html)
  - As the plugin now has a bit more control over the schematics, the CUSTOM console can now be used with ARS
- Project Rassilon plugin support was removed, as it was reported to not work anyway
- The Diamond Disruptor Sonic upgrade can now break snow
- Only allow `/tardis update` command in player’s own TARDIS

#### Bug fixes

- Fixed the isomorphic preference message
- Fixed a NPE when messaging ops if the TARDIS map files were not found
- The plugin now properly checks player permission for creating the war TARDIS
- Fixed the Zero room chat listener sometimes throwing an error when other plugins send empty quotes (`""`) in chat
- Fixed the e-circuit in the TARDIS Information System
- Fixed GriefPrevention support not actually loading
- Fixed walking into TARDIS not setting ‘occupied’ when a player has `tardis.create_world` permission
- Fixed the condenser not returning items with no Artron value

[Even older releases](change-log-older.html)

