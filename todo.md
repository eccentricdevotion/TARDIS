#TODO

##Current bugfix version `3.6-beta-1`
1. Fix bugs!
   * Siege mode wall/floor changes are not reverted when disengaging siege mode from outside the TARDIS
   * Disengaging siege mode in the BUDGET console TARDIS changes the floor to orange wool
   * You can get stuck in the Renderer room if you exit the TARDIS while the scanner is still displaying its data

##Next minor version `3.6-beta-1`
1. TARDIS Junk Mode - `/tprefs junk [wall|floor] [block_type]`?
2. TIPS visulisation tool (using WorldGuard region data)
3. [Auto powerup](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1209-auto-powerup/)
4. [Red time rotor](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1216-red-time-rotor/)
5. [TARDISDalekCities](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1168-a-dalek-conquest/) addon?
    * Custom TerrainControl world (_done_)
    * Custom resource pack and resource pack switching (_done_)
    * Acid water (_done_)
    * Custom acid damage amount (_done_)
    * Optional custom acid potion effects (_done_)
    * Automatically generating Dalek cities
6. [Abandoned TARDISes](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1277-possibility-to-abandon-a-tardis/)
7. [Destination 'vortex'](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1023-set-the-tardis-inflight-without-setting-a-destination/) - dematerialise without setting a destination.
8. VOID biome for TARDIS interior
9. [Sonic screwdriver [flower] pot](http://dev.bukkit.org/bukkit-plugins/tardis/tickets/1239-new-screwdriver/)
10. Ice/igloo room - if TARDISWeepingAngels plugin is enabled, make a 3% chance a Dalek will form when building a snowman in the room. Should have a pen ready for polar bears

##Upcoming minor version `3.7`
1. Update for Minecraft 1.10
   * Add new blocks
   * Add new mobs
   * Use new spawn eggs (farming & Lazarus GUI)
   * Structure blocks?
   * Polar bear farming
2. TARDISSonicBlaster addon?

?

##Future version `4+`
* SpongeAPI
* Forge mod for custom items and blocks (to tie in with Sponge version)
* Slimmer TARDIS option?

##Resource Pack / TARDISWeepingAngels

1. Re-skin mobs for Resource pack, and add new mob sounds

   * Villagers -> Ood - _started texture_
   * Villagers -> Hath
   * Endermen -> The Silence - _implemented, but texture needs to be finished_
   * Endermites -> Cybermats
   * Iron Golem -> Gunslinger
   * Witches -> Slitheen / Whispermen?
   * Wolves -> K-9? (All the textures I seen so far just don't work - you shouldn't see the legs...)
   * Slimes -> Adipose
   * Something with a Shulker passenger?

##Waiting on Bukkit / SpigotAPI
0. IDs/data are soon to be history... so convert all ID/data use to block states 
1. Get/set horse speed (Attribute API)
2. Achievements API is now available, but doesn't allow custom achievements :( [https://bukkit.atlassian.net/browse/BUKKIT-5672](https://bukkit.atlassian.net/browse/BUKKIT-5672)

##Minecraft API when available
1. Recreate everything with the new API :)
2. Create custom blocks for the TARDIS and console.
3. Custom TARDIS items and crafting recipes for the same.
4. A Sonic Screwdriver and a crafting recipe for it.
5. GUIs (if allowed)
6. Make the TARDIS Police Box an entity (if allowed)
7. Custom achievements
8. Custom monsters
