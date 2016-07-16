#TODO

##Current bugfix version `3.6.1`
1. Fix bugs!

##Next minor version `3.7`  
1. Add other worlds (Silurian cave world, Gallifrey)?
2. Add more API Events that other plugins can listen for?
   * Sonic Screwdriver events
   * Artron Energy event
   * Mob farming / ejection events
3. Add more API methods
   * Initiate a desktop theme change - _this is problematic as the desktop could only be changed to one that is the same size (as there is no player interaction to decide if rooms should be jettisoned)_
4. Update console schematics to remove MONSTER_EGG blocks, except for the ARS one (data = 2), as it is needed for initially setting up the ARS data and updating it when using the Desktop Theme
5. [Player console archiving](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1201-player-console-archive-feature/)
   * `/tardis archive [scan|add|remove|description?] [name]` command
   * `tardis.archive` permission
   * _archive.yml?_ - `enabled`, `limit`, `block-limit`, `prohibited blocks`
   * own database? `CREATE TABLE IF NOT EXISTS " + prefix + "archive (archive_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT, name TEXT, small INTEGER, tall INTEGER, beacon INTEGER, lanterns INTEGER, data TEXT)`
   * scan for prohibited blocks
   * only scan in the default sizes - 16x16x16, 32x32x16, 32x32x32 - find a way to minimize height
   * look up control locations, substitute control blocks eg random repeaters -> `HUGE_MUSHROOM:15`, handbrake -> `CAKE_BLOCK`, beacon block off -> `BEDROCK` etc


##Future version `?+`
* TARDIS Junk Mode - `/tprefs junk [wall|floor] [block_type]`?
* TARDISSonicBlaster addon?
* SpongeAPI
* Forge mod for custom items and blocks (to tie in with Sponge version)
* Slimmer TARDIS option?

##Resource Pack / TARDISWeepingAngels
1. Something with an invisible Shulker passenger - or an invisible bat with a Shulker passemger (Toclafane?)
2. Re-skin mobs for Resource pack, and add new mob sounds
   * Villagers -> Ood - _started texture_
   * Villagers -> Hath
   * Endermen -> The Silence - _implemented, but texture needs to be finished_
   * Endermites -> Cybermats
   * Iron Golem -> Gunslinger
   * Witches -> Slitheen / Whispermen?
   * Wolves -> K-9? (All the textures I seen so far just don't work - you shouldn't see the legs...)
   * Slimes -> Adipose
 3. Sycorax
 4. Scarecrows
 
##TIPS visulisation tool
* Use WorldGuard region data

##Waiting on Bukkit / SpigotAPI
0. IDs/data are soon to be history... so convert all ID/data use to block states 
1. Get/set horse speed (Attribute API)
2. Achievements API is now available, but doesn't allow custom achievements :(

##Minecraft API if ever available
1. Recreate everything with the new API :)
2. Create custom blocks for the TARDIS and console.
3. Custom TARDIS items and crafting recipes for the same.
4. A Sonic Screwdriver and a crafting recipe for it.
5. GUIs (if allowed)
6. Make the TARDIS Police Box an entity (if allowed)
7. Custom achievements
8. Custom monsters
