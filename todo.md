# TODO

## Current bugfix version `3.8.2`
1. Fix bugs!

## Next version `4.0-beta-1`
1. Update to Minecraft/Spigot 1.13 - _started_
   * Update ARS records at first run - _started_
   * Update configs - _started_
   * Tidy up database creation (remove unused fields where necessary)
   * Update Lazarus GUI with new spawn eggs, remove deprecated data value use when running genetic change
   * Remove deprecated data value use in gravity wells
   * Remove deprecated data value use in chest, smelter and vault sorters
   * Use BlockData for `isDoorOpen()` check, getting lower door blocks and opening/closing doors/trapdoors
   * Use BlockData to set Junk TARDIS sign
   * Find alternative to MaterialData for Enderman block carrying
   * `sendBlockChange(Location loc, Material material, byte data)` is deprecated in 3-D spectacles code...
2. Add glazed terracotta chameleon presets?
3. Fix Biome adaptive presets dropping flowers & seeds (properly)

## Future version `?+`
* TARDIS Junk Mode - `/tprefs junk [wall|floor] [block_type]`?
* TARDISSonicBlaster addon? - _unlikely_
* SpongeAPI - _unlikely_
* Forge mod for custom items and blocks (to tie in with Sponge version) - _maybe_
* Slimmer TARDIS option? - _unlikely_
* Add more API Events that other plugins can listen for?
   * Sonic Screwdriver events
   * Artron Energy event
   * Mob farming / ejection events

## Resource Pack / TARDISWeepingAngels
0. Custom Entity & Item models using Optifine
1. Rare armour drops
2. Something with an invisible Shulker passenger - or an invisible bat with a Shulker passenger (Toclafane?)
3. Re-skin mobs for Resource pack, and add new mob sounds
   * Villagers -> Ood - _started texture_
   * Villagers -> Hath
   * Endermen -> The Silence - _implemented, but texture needs to be finished_
   * Endermites -> Cybermats
   * Iron Golem -> Gunslinger
   * Witches -> Slitheen / Whispermen?
   * Wolves -> K-9? (Make new model using OptiFine Custom Entity Model mod) _done_
   * Slimes -> Adipose
   * Husk -> Sycorax
   * Stray -> Scarecrows
   * Judoon
   * Clockwork Droid
4. Custom GUI textures for TARDIS GUIs
 
## TIPS visulisation tool
* Use WorldGuard region data

## Waiting on Bukkit / SpigotAPI
0. IDs/data are soon to be history... so convert all ID/data use to block data - _started_ 
1. Get/set horse speed (Attribute API)

## Minecraft API if ever available
1. Recreate everything with the new API :)
2. Create custom blocks for the TARDIS and console
3. Custom TARDIS items and crafting recipes for the same
4. A Sonic Screwdriver and a crafting recipe for it
5. GUIs
6. Make the TARDIS Police Box an entity
7. Custom advancements
8. Custom monsters
