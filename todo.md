#TODO

##Current bugfix version `3.6.1`
1. Fix bugs!

##Next minor version `3.7`  
1. [Spawn abandoned TARDISes](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1277-possibility-to-abandon-a-tardis/?comment=6)
2. Add other worlds (Silurian cave world, Gallifrey)?
3. Add [API Events](http://wiki.bukkit.org/Event_API_Reference#Creating_Custom_Events) that other plugins can listen for
   * ~~Chameleon Arch events~~
   * ~~Desktop Theme event~~
   * ~~Zero Room events~~
   * ~~Materialisation events~~
   * Sonic Screwdriver events?
   * ~~Lazarus Device events~~
   * ~~Malfunction event~~
   * Artron Energy event?
   * ~~TARDIS creation / destruction events~~
   * ~~TARDIS enter / exit events~~
   * ~~HADS event~~
   * Mob farming / ejection events?
   * ~~ARS / room growing events~~
   * ~~Siege / desiege events~~
   * ~~Abandon / claim events~~
4. Add more API methods
   * Get a `Tardis` object from `UUID`, `tardis_id`, `Player`
   * Set a TARDISes next destination
   * Set a TARDISes next destination and travel to it
   * Initiate a desktop theme change
   * Change the Chameleon circuit
   * Spawn an abandoned TARDIS at a Bukkit Location
4. Update console schematics to remove MONSTER_EGG blocks, except for the ARS one (data = 2), as it is needed for initially setting up the ARS data and updating it when using the Desktop Theme
5. Update [Chameleon Circuit GUI](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1341-new-chameleon-circuit-gui/)
6. [Player console archiving](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1201-player-console-archive-feature/)
7. TARDIS Junk Mode - `/tprefs junk [wall|floor] [block_type]`?
8. TARDISSonicBlaster addon?

##Future version `4+`
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
