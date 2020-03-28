---
layout: default
title: Change log
---

# Change log

## What’s been changed?

### TARDIS v4.1.0

Updated to Spigot 1.14.4-R0.1

Requires TARDISChunkGenerator 4.1.0 (included in ZIP file)

#### Additions

- Added new biomes to Biome disks
- Added new items and blocks to condenser
- Added world management features to support Whovian worlds. Multiverse (or other world managers) are no longer required (but still compatible). Please let TARDIS manage its own worlds!
- Added a `/tardisworld [load|unload] [world] [type] [environment][generator]`command
- Added a command to change the gamemode of a world `/tardisworld gm [gamemode]`
- Added a command to rename a world `/tardisworld rename [old name] [new name]`
- Added a `/tardisteleport [world]` command
- Added a `/tardisgamemode [gamemode] [player]` command
- Added a `tardis.gamemode.bypass` permission to bypass gamemode switching when changing worlds
- Added a Pickup Arrows sonic upgrade
- Added Custard Cream recipe and a Custard Cream dispenser
- Added the Berry Bush to Sonic replanter
- Added some fireworks to repeaters when there is a malfunction
- Added the 13th Doctor’s console
- Added the Factory console

#### Changes

Lots of code changes made to support Spigot 1.14.x!

- Updated all item and block names to 1.14.x
- Tamed cats can be farmed, but not wild ocelots
- MushroomCow variants are now saved when mob farming and ejecting
- Updated to the latest WorldGuard / WorldEdit APIs
- The plugin no longer relies on the TerrainControl / OpenTerrainGenerator plugin to generate custom Whovian worlds (Skaro, Gallifrey, Siluria)
- Moved all planet / world configuration to _planets.yml_
- Removed the WorldEdit version check so servers can use FAWE instead
- Now checking for Handles when changing the Desktop Theme (and cancelling if he is found so he can be removed)
- There is now increased damage to circuits if a malfunction occurs (and difficulty is set to hard)
- Increased the size of the Desktop Theme GUI so it will fit in more custom consoles

#### Bug fixes

- Fixed random location repeater SQL queries for servers using MySQL
- Fixed the AQUARIUM schematic with missing repeater
- Fixed problems with the `/tardissay` command
- Fixed doing TARDIS achievements even if they are disabled in the config
- Make sure a TARDIS’ home location isn’t in another Time Lord’s home location
- Fixed the Desktop theme not using a player’s wall and floor block preferences
- Fixed falling into the void when changing desktop theme to the BIGGER console
- Fixed a NPE in constructs converter

### TARDIS v4.0.6

#### Additions

- Added API to get door open/closed status for Dynmap-TARDIS
- Now rotating Directional blocks in Chameleon Constructs for MCDW Models
- Implemented a TARDIS Chameleon item frame — /tardis update frame — will show the associated Chameleon preset block in the item frame

#### Changes

- Updated Multiverse dependency to v3.0

#### Bug fixes

- Fixed the `open_door_policy` so it actually works
- Fixed Junk TARDIS not being created properly
- Fixed opening keyboard sign GUI
- Fixed TOPSYTUVEY and JAIL presets popping off doors
- Fixed door direction in REDSTONE console
- Fixed trapdoor direction on lamp post preset
- Fixed claiming an abandoned TARDIS
- Added a random UUID for spawned abandoned TARDISes to fix some issues
- Fixed Multiverse world aliases
- Added some checks to/for TARDIS\_Zero\_Room so falling into the void works correctly according to the TARDIS feature

### TARDIS v4.0.5

Requires TARDISChunkGenerator v4.0.5 — included in the zip file

If using WorldGuard 7 you need build #1755 or higher. If using WorldEdit 7 you need build #3930 or higher

#### Additions

- Added some missing entities to the TARDIS scanner feature

#### Changes

- No longer runs on CraftBukkit/Spigot 1.13.1 — update...
- Updated to latest WorldGuard/WorldEdit API changes
- Clarified usage of most /handles commands
- The plugin now ignores underwater plants when finding submarine location
- The plugin now checks plugin protection for all sonic-able blocks

#### Bug fixes

- Replaced a missed block ID with its proper BlockData value
- Fixed a bug where Banner JSON could potentially be null + banner base colour is now set by block type
- Fixed being able to sonic doors and other blocks protected by the Towny plugin
- Fixed the TORCH chameleon preset
- Fixed a bug where villagers lost their attributes when ejecting them
- Fixed a bug with handles commands when used with Paper
- Fixed the ZERO room location not being added to the database

### TARDIS v4.0.4

#### Additions

- Added the Thirteenth Doctor’s (Jodie Whittiaker) Sonic
- Added occupy to the /tardisbind command
- Added a /tardis construct asymmetric command

#### Changes

- Changed the TARDIS police telephone block from QUARTZ\_PILLAR to OBSERVER — you’ll need to update the TARDIS-MCP resource pack
- The TARDIS is now set to hidden if the world the TARDIS is currently in no longer exists
- Old Chameleon Construct data is now converted to the new BlockData format

#### Bug fixes

- Re-enabled the custom Chameleon preset
- Fixed not being able to craft the MASTER seed block
- Fixed the ELEVENTH console failing to build when the seed block is clicked
- Removed a duplicate key in TARDIS condensable list
- Fixed the blocks table erroring when writing data to the time field
- Fixed fence blocks and other problems in Swamp preset
- Fixed asymmetric Chameleon presets and bed directions
- Fixed Weeping Angel Chameleon preset head direction
- Fixed a bug writing "Current location" to preset field in tardis table
- Fixed an `IllegalArgumentException` when trying to get Chameleon preset
- Fixed a bug with the Handles sound effects not playing when he is clicked

### TARDIS v4.0.3

Requires TARDISChunkGenerator 4.0.3 and Spigot 1.13.1-R0.1-SNAPSHOT

#### Changes

- Updated for CraftBukkit/Spigot 1.13.1
- The ‘back’ destination is now always set when dematerialising (instead of just for `/tardis comehere` and the Stattenheim Remote)
- You can no longer land on the end portal in The End when using the Destination Terminal GUI

#### Bug fixes

- Fixed an `ArrayIndexOutOfBoundsException` in TARDISRecordingTask
- Fixed a bug with Chameleon presets not loading properly from Save Sign GUI
- Fixed BEDS not being set properly in Chameleon presets
- Fixed the wall and floor block from the TARDIS seed not being set correctly on TARDIS creation
- Fixed a NPE loading protected blocks from the database
- Fixed mob farming not working (you’ll need to regrow the farm room if you have an existing one that was created in 1.13)
- Fixed the renderer room not working (you’ll need to regrow the room if you have an existing one that was created in 1.13)
- Fixed the greenhouse room losing crops and levers, and missing sugar cane (probably best to grow a new one of these too)
- Fixed Chamelon constructs and the stained glass cycle when dematerialising
- Prevented some items dropping when materialising
- Sonic Screwdriver upgrades work again
- Fixed the Redstone upgraded Sonic not turning on redstone lamps
- Fixed the Admin Sonic on TARDIS doors
- Added two missing room permissions — `tardis.room.aquarium` and `tardis.room.shell`
- Fixed the TARDIS exterior lamp not being lit
- Fixed the /tardisrecipe cell command
- Fixed players still being killed when falling into the void in the Time Vortex world and `preferences.vortex_fall` is set to teleport
- Fixed TARDIS farmed Villager trades being overwritten when their career is set
- Fixed Villager careers being randomly set for their profession e.g. librarians sometimes becoming cartographers
- Fixed case sensitive getWorld() method in TARDISMultiverseHelper
- Fixed TARDIS door knocking throwing an `ArrayIndexOutOfBoundsException` when the TARDIS key is AIR

### TARDIS v4.0.2

Requires TARDISChunkGenerator v4.0.2

#### Changes

- Custom room schematics are now disabled if they contain bogus data — old custom rooms will need their schematics updated
- The TARDIS Schematic Wand is now a TARDIS recipe (you can edit the recipe result and therefore the tool it uses in recipes.yml)
- The TARDIS cave finder command (/tardistravel cave) uses a new method to find caves
- Updated to the latest WorldGuard v7.0.0 API changes
- Bow Tie and Jelly Baby recipe variants are now grouped together

#### Bug fixes

- Updated default Disk Storage inventory serialization so that the TARDIS Disk Storage GUI displays correctly
- Fixed an error opening the Area Disk storage when there are no defined TARDIS areas
- Fixed TARDIS Biome Reader recipe
- Fixed the cave finder timing out and crashing the server due to a new type of "cave air" found in 1.13
- Fixed a bug where the Rift Circuit & Manipulator recipes didn’t get added properly
- Fixed the default config being cleared on first run
- Fixed a typo in default config that led to two options for the default TARDIS lamp block
- Fixed a bug where using the /tardisadmin command to set Artron values could overwrite the config file instead of artron.yml

### TARDIS v4.0.1

Requires TARDISChunkGenerator v4.0.1

#### Additions

- Now includes a world loader — if a multi-world plugin is not installed on the server, TARDIS will load any worlds found in its config
- Added a Paper Bag to hold Jelly Babies — [https://github.com/eccentricdevotion/TARDIS/issues/74](https://github.com/eccentricdevotion/TARDIS/issues/74)
- /tardisrecipe paper-bag
- SHIFT-click the bag in inventory to store up to 4 stacks of Jelly Babies in it
- RIGHT-click the bag in inventory to get a random Jelly Baby out of the bag

#### Changes

 Updated WorldGuard and WorldEdit dependencies to v7.0.0 
#### Bug fixes

- Fixed data conversion errors when using MySQL database or updating from an early version of the plugin
- Fixed Trapped Chests not being sorted correctly with the Sonic Screwdriver

### TARDIS v4.0.0

For Spigot 1.13

This release is not recommended for production servers — use at your own risk!

Initial update to 1.13. While most TARDIS functions are working, we are still waiting for some dependent plugins to update (LibsDisguises, WorldGuard, TerrainControl/OpenTerrainGenerator etc). There may also be changes to the Spigot API, so thorough testing is impossible at this stage...

### TARDIS v3.8.3

For Spigot 1.12.2

#### Additions

- Added Handles — [https://dev.bukkit.org/projects/tardis/issues/1549](https://dev.bukkit.org/projects/tardis/issues/1549)
- Add an Artron energy event to the TARDIS API

#### Changes

- Use Essentials nickname on sign if available
- Made in-game command help better (esp. for `/tardisadmin`)
- Return blocks to player if they close the Chameleon Constructor without saving the construct
- Disable abandoned TARDISes if `create_worlds` is true
- Update to Terrain Control 2.9.0-SNAPSHOT
- If Project Rassilon is enabled and player regenerates in siege mode, then transfer Time Lord energy to TARDIS
- You can no longer SHIFT-click to exit if using the INVISIBLE preset

#### Bug fixes

- Reduced poor timings caused by TARDIS Hum lambda
- Reduced poor timings caused by explosion listener
- Fixed `ArrayIndexOutOfBoundsException` in `/tadmin delete [player] command`
- Fixed broken `/tardisadmin list command`
- Fixed broken `/tardisrecipe command`
- Don’t allow hiding if the TARDIS is dispersed
- Don’t set a cooldown unless actually doing a rebuild
- Fixed "How do I make a TARDIS"
- Apply Isomorphic controls to all controls...
- NamespacedKeys must be lower case

### TARDIS v3.8.2

#### Changes

- The plugin now requires TARDISChunkGenerator v2.5.5
- The required build of Multiverse-Inventories is now v2.5.0-SNAPSHOT-b431 or higher
- Spawned abandoned TARDISes as now listed as being owned by TARDIS Admin
- You can no longer use the TARDIS back door in junk and siege mode
- Removed the Villager Career getting/setting methods from TARDISChunkGenerator (they’re now included in Bukkit API)

#### Additions

- Added a config option for `preferences.no_coords`
- Defaults to false — when set to true, will not display the random destination coordinates in chat when the random button is pressed
- Added a false\_nether key to planets.yml
- Added support for LockettePro
- Added the vault chest to the `/tardis update` lister
- Added Stained Clay (Terracotta) to the blocks than can be smelted in the Smelter room

#### Bug fixes

- Fixed the Lazarus Device pressure plate location not being updated when the room was grown a second or more times
- Fixed "false" NETHER world random location finding
- Fixed a bug in the Multiverse-Inventories version check
- Fixed a `NullPointerException` using the Admin sonic when TARDIS travellers are offline
- Fixed spawned abandoned TARDISes throwing a `NullPointerException` because they have no associated UUID
- Fixed setting the TARDIS exterior direction when spawning abandoned TARDISes
- Fixed deleting abandoned TARDISes
- Fixed an error with ProtocolLib sending JSON chat (we now use our own TARDISChunkGenerator method instead)
- Resolved a class cast exception in Chameleon Arch inventory restoration

### TARDIS v3.8.1

#### Additions

- Added the legacy consoles to `/trecipe tardis [console]` command
- Added ARS methods messages to language localisations (_en.yml_)
- Added the Illusioner to the Genetic Manipulator

#### Changes

- Multiverse world aliases are now shown in all instances in the Destination terminal
- Update to SpigotAPI 1.12.1-R0.1-SNAPSHOT
- All players are now teleported to safe location when upgrading the TARDIS console
- Optimised the SQL that populates the control centre sign updates so servers with hundreds of TARDISes perform better
- Converted TARDIS achievements to custom Advancements (complete with toasts)

#### Bug fixes

- Fixed ARS records sometimes not being created
- Fixed an SQL error at startup when using MySQL
- Fixed the `give_key` config option giving a key when the player already had a key in their off hand slot
- Fixed a bug on PaperSpigot where commands were being run from an async thread
- Fixed the WorldGuard version check when it had an odd version number string e.g. `6.2.1;84bc322`
- Fixed checking the creeper location throwing an error in custom consoles
- Fixed the looping time rotor sfx when in the time vortex not stopping when the TARDIS materialises and the player left the TARDIS by non-standard means e.g. not by the door

### TARDIS v3.8

#### Additions

- Added new 1.12 blocks
- Added BIRDCAGE room — permission: `tardis.room.birdcage`
- Added the ability to enter the TARDIS with a parrot on your shoulder(s)
- Added Parrots to Genetic Manipulation (Lazarus) Device
- Added Atmospheric Excitation — use the command `/tardis excite` or right-click the exterior TARDIS sign with your Sonic Screwdriver — permission: `tardis.atmospheric` — [https://www.youtube.com/watch?v=vFGJkrnTTfI](https://www.youtube.com/watch?v=vFGJkrnTTfI)
- Added the ability to give Knowledge books with TARDIS recipes — doesn’t do anything yet… you get a knowledge book, but the recipe doesn’t display in the recipe book so pretty useless… waiting for Minecraft 1.13... — `/tardis give [tardis item] knowledge`
- (Re)added the original old consoles to desktop themes / seed blocks
- Added a Silurian Underworld and Gallifrey — home world of the Time Lords 
  - as with the Skaro world it requires TerrainControl to be enabled on the server.
  - enable it in planets.yml and restart the server (might need to run the plugin once to update the planets config file).
  - Gallifrey has a world specific resource pack to change a few colours for a more Whovian look (according to the [TARDIS Wikia](http://tardis.wikia.com/wiki/Gallifrey) _"From the planet’s surface, it boasted an orange sky at night, snow-capped mountains, fields of red grass, and trees with bright silver leaves."_) — enable resource pack switching in planets.yml (or check it out here: [https://github.com/eccentricdevotion/Gallifrey](https://github.com/eccentricdevotion/Gallifrey))

#### Changes

- The TARDIS can now land a lot closer to the world border (used to be 16 blocks, now down to 1 block)
- The plugin now prevents sleeping in TARDIS if the TARDIS world environment is The End
- Mob farming is now turned on when growing a stable, village, hutch, stall, igloo or birdcage room
- Changed the `creation.use_clay` config option to allow ‘WOOL’, ‘TERRACOTTA’ or ‘CONCRETE’
- Updated the Master’s TARDIS schematic — thanks to&nbsp;ShadowAssociate

#### Bug fixes

- Fixed a crafting guide duplication bug when using the `/tardisrecipe` command
- Fixed the version number display in the error message when the server is outdated
- Fixed farming Mules & Donkeys
- Added missing room and legacy console perms to plugin.yml
- Fixed duplicate lime blocks in desktop theme wall GUI
- Fixed duplicate seed blocks for STALL room / CORAL console & WORKSHOP / MASTER console

### TARDIS v3.7

#### Additions

- Updated for Spigot 1.11 
  - Added new blocks
  - Added new mobs
- Added more biome adaptive presets
- Added new Chameleon Circuit GUI — see [http://eccentricdevotion.github.io/TARDIS/chameleon-circuit.html](http://eccentricdevotion.github.io/TARDIS/chameleon-circuit.html)
- Added the ability to archive a TARDIS console 
  - `/tardis archive` command
  - TARDIS Archive GUI (can be accessed from the Desktop Theme GUI)
  - Added 3 different sized cobblestone template consoles
  - See [http://eccentricdevotion.github.io/TARDIS/archive.html](http://eccentricdevotion.github.io/TARDIS/archive.html)
- Added room costs to the ARS GUI
- Added a `/tardis construct [line] [text with colour codes]` command to set the text on the TARDIS’ exterior sign (when using the CONSTRUCT preset)
- Added a SMELTER room
- Added a Llama STALL room
- Added a `/tardisadmin repair [player] [number]` command
- Added clean & repair buttons to the Desktop Theme GUI
- Added an admin command `/tardisadmin set_size [player] [size]`
- Add Polar Bears and Llamas to `/tardis eject` command
- Added a companions menu button to control panel

#### Changes

- Removed the `/tardis chameleon` commands
- Converted NEW / OLD preset TARDISes with non blue wool walls to CONSTRUCT presets (due to new Chameleon Circuit GUI functionality)
- The default preset for new installs is now set to FACTORY on TARDIS creation
- TARDIS seed blocks no longer have a Chameleon or lamp block (use the Chameleon Construct GUI)
- Use save-disks when in Junk Mode on `hard` difficulty
- Use Multiverse world name alias if available
- Changed the jettison permission to `tardis.jettison`
- Allow rebuilding to the INVISIBLE preset if the previous preset was visible
- You can no longer use biome travel in the Skaro world
- Improved the SQLite to MySQL database converter
- To access TARDISWeepingAngels monsters in the Genetic Manipulator, trigger the pressure plate while sneaking
- Condenser records are now purged when deleting a TARDIS (as the records are tied to the `tardis_id`)
- Updated the Universal Translator to use new Microsoft Azure Portal

#### Bugfixes

- Fixed ridiculously slow Ender Dragon movement
- Fixed companion anti-build
- Update the WorldGuard region when claiming an abandoned TARDIS
- Fixed timeout problems with the cave finder feature
- Fixed ENDER console entry in TARDIS Information System
- Fixed breaking BEACON blocks
- Fixed the handbrake not being able to be engaged if the Invisibility Circuit is engaged while floating in the vortex
- Check the player has condensed blocks for all rooms
- Fixed the RAIL room dropping items before teleporting the cart
- Disallow the `/tardis comehere` command while the TARDIS is materialising
- Prevent un-sieging in excluded worlds
- Fixed the TARDIS umbrella being added twice and not being removed
- Fixed wrong stained glass colour in dematerialisation animation
- Fixed being able to jettison the console in the ARS GUI
- Fixed Siege Mode block not appearing
- Fixed setting the Universal Translator language preference
- Fixed opening player inventories with the Admin Sonic
- Fixed the permission for bypassing acid damage
- Don’t go into an endless loop if the TARDIS console beacon has been moved / removed
- Fixed skeletons not spawning in the nether
- Fixed the SQLite to MySQL database converter
- Fixed most seeds and flowers dropping from Biome presets
- Lots of other small bugfixes...

### TARDIS v3.6.2

#### Additions

- Use Multiverse aliases if possible for the `/tardistravel` command and listing rechargers

#### Changes

- The Architectural Reconfiguration GUI now shows the Artron Energy cost for rooms

#### Bug fixes

- Fixed breaking BEACON blocks when they weren’t Rift Manipulators
- Fixed setting invisibility while drifting in vortex breaking the handbrake

### TARDIS v3.6.1

#### Additions

- Added a config option to _planets.yml_ to set whether the world is an empty “void” world — `void: [true|false]` — if not set, will default to `false`. This will speed up random location finding considerably.

#### Changes

- Falling in to the Time Vortex in TARDIS worlds now uses the `preferences.vortex_fall` config option regardless of whether the player was kicked.
- The plugin now allows the `/tardis make_her_blue` command to turn off Junk Mode
- When claiming an abandoned TARDIS the WorldGuard region is updated with the the claiming player’s UUID
- When the plugin difficulty is set to hard, you can now use Save Storage Disks to set the Junk Mode destination

#### Bug fixes

- Fixed blocks required for IGLOO room when `growth.rooms_require_blocks` is `true`
- Made sure Junk Mode is ON before allowing it to be OFF (and vice versa)
- Fixed a `NullPointerException` related to the perception filter when disabling the plugin
- Added more world checks to cave finder top prevent server crashes when running the `/tardistravel cave` command in empty “void” worlds
- Fixed the ENDER console entry in TARDIS Information System

### TARDIS v3.6

#### Additions

- Updated for Minecraft 1.10 
  - Added new blocks
  - Added new mobs
  - Fixed spawn eggs due Minecraft changes
- Added the ability to create a Skaro world — see more details here: [Skaro](skaro.html)
- Added the ability to have per-world resource packs — see [Planet configuration](configuration-planets.html)
- Added beetroot to the Sonic replanter feature
- Added the ENDER console designed by ToppanaFIN — seed block: PURPUR\_BLOCK
- Added CORAL console designed by vistaero — seed block: NETHER_WART_BLOCK
- Added the Sonic Generator
- Added an Igloo room and the ability to farm Polar Bears
- Added a 3% chance a Dalek will form when building a snowman in the TARDIS
- Added `auto_powerup_on` to player prefs to make the TARDIS power up when entering
- Added the ability to dematerialise without setting a destination
- If a player tries to toggle the handbrake when the door is open, closing the door within 30 seconds now starts dematerialisation
- Added the ability to abandon a TARDIS 
  - config — `abandon.enabled: [true|false]`, `abandon.reduce_count: [true|false]`
  - permission — `tardis.abandon`
  - commands — `/tardis abandon`, `/tardisadmin list abandoned` — TARDIS admins can click an entry in the abandoned list to enter or delete that TARDIS (requires ProtocolLib)
  - players can claim an abandoned TARDIS by being the first person to power it up
  - the config option `allow.power_down` must be `true`
- Added `hum` player preference and GUI to set the TARDIS interior hum sound effect preference — use the Sonic Preferences GUI or the `/tardisprefs hum [sound name]` command
- Added Acid Batteries, the Rift Circuit, [Rift Manipulator](rift-manipulator.html) and the Rust Plague Sword 
  - `/tardisrecipe [acid-battery|rift-circuit|rift-manipulator|rust]`
  - Acid Batteries and the Rift Circuit are ingredients in crafting the Rift Manipulator
  - The Rift Manipulator allows players to create personal TARDIS rechargers — permission `tardis.rift`

#### Changes

- New sound effects for quick materialisations (hide/rebuild)
- Lots of other sound effect changes — update the [TARDIS-Sounds resource pack](https://github.com/eccentricdevotion/TARDIS-SoundResourcePack)!
- The interior hum when entering TARDIS now only plays if player has SFX pref ON
- The remote key hide and rebuild feature now has a cooldown period
- The remote key no longer functions if the TARDIS is powered down
- Sound effects now play for TARDIS autonomous homing
- Companions can now perform rescues via the telepathic circuit
- The language localisation files are now easier to translate
- Refresh the chunk after changing the biome — `world.refeshChunk()` is now deprecated because it doesn’t work reliably. TARDISChunkGenerator now has a method to send a `PacketPlayOutMapChunk` packet to the client to achieve this
- Use the new VOID biome for the TARDIS interior — update the [TARDIS-MCP resource pack](https://github.com/eccentricdevotion/TARDIS-MCP)! — existing TARDIS interior biomes will be updated to use VOID instead of SKY, this should stop pesky Endermen spawning
- The control Centre has been updated with all TARDIS functions, and now displays relevant TARDIS information. Clicking the Control Centre sign while sneaking will open the keyboard
- If TARDISWeepingAngels is enabled Whovian monsters now show in the scanner results and the messages when they enter the TARDIS
- Zombie Villagers that enter the TARDIS now keep their profession
- The TARDIS beacon will now turn red if there is a malfunction
- Removed the `police_box.materialise` config option
- New TARDISes will only have the new Control Centre sign and not all the others (they can still be added if desired using the `/tardis update` command)
- Added more opportunity for `no_destination_malfunctions`
- Toggling Junk Mode now respects the rebuild cooldown period

#### Bug fixes

- Don’t engage HADS if the TARDIS is powered down
- Explicitly specify the required server version in the plugin disable message
- Remove players from the `travellers` table if they respawned in a non-TARDIS world
- Restore grass paths after the TARDIS has landed on them
- Set an under door block if the TARDIS materialises in the air
- Fixed a null message when the TARDIS malfunctions
- Update the player’s inventory after they try (and are denied) placing a siege cube in the render room
- Fixed the database `prefix` option on first run
- Only transmat a player to the Renderer room if they are still in the TARDIS world — they may have disconnected from the server or exited the TARDIS
- Revert Siege mode wall/floor changes when disengaging from outside the TARDIS
- Change the SQLite siege wall & floor defaults to be the same as MySQL
- Add missing 1.9 attributes to inventory saving
- Prevented use of more controls when in Siege Mode
- Fixed the MySQL database converter
- Added missing [help|tab completion] entries for commands
- Fixed spelling mistakes in some TARDIS messages
- Fixed the Junk Mode save sign not working after a new TARDIS was created, and persist Junk Mode players between server restarts
- Fixed the INVISIBLE chameleon preset not updating the exterior door location

[Older releases](change-log-not-so-older.html)

