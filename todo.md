# TODO

## Current bugfix version `3.8.2`
1. Fix bugs!

## Next version `4.0-beta-1`
1. Update to Minecraft/Spigot 1.13 - _started_
   * Update ARS records at first run - _started_
   * Update configs - _started_
   * Update / test schematic creation / pasting - _started_
   * Update Lazarus GUI with new spawn eggs (drowned, turtle, phantom, cod, salmon, pufferfish, tropical fish - waiting on LibsDisguises update) - _started_
   * Check sonic replanter / chest sorter features still work
   * Add new biomes (end and ocean)
   * Correctly set BlockData in `sendBlockChange(Location loc, BlockData data)` in TARDISSpectaclesRunnable
2. Shell Room - [https://dev.bukkit.org/projects/tardis/issues/1536](https://dev.bukkit.org/projects/tardis/issues/1536)
3. Aquarium room, maybe a Turtle Beach room?
4. Check database updater (Material) changes
5. Fix Biome adaptive presets dropping flowers & seeds (properly)

## TARDIS data pack
* Move custom advancements to here
* Add TARDIS recipes (where possible)
* Add TARDIS tags

## Future version `?+`
* TARDIS Junk Mode - `/tprefs junk [wall|floor] [block_type]`?
* TARDISSonicBlaster addon? - _unlikely_
* Slimmer TARDIS option via Optifine's CEM mod? - _maybe_
* Add more API Events that other plugins can listen for? - _unlikely_
   * Sonic Screwdriver events
   * Artron Energy event
   * Mob farming / ejection events

## Resource Pack / TARDISWeepingAngels
0. Custom Entity & Item models using Optifine (when they can be applied to named entities)
1. Rare armour drops
2. Something with an invisible Shulker passenger - or an invisible bat with a Shulker passenger (Toclafane?)
3. Re-skin mobs for Resource pack, and add new mob sounds
   * Villagers -> Ood - _started texture_
   * Villagers -> Hath
   * Illagers -> Red-eyed Ood - _started_
   * Endermen -> The Silence - _implemented, but texture needs to be finished_
   * Endermites -> Cybermats
   * Iron Golem -> Gunslinger
   * Witches -> Slitheen / Whispermen?
   * Wolves -> K-9 (Make Custom Entity Model apply to named wolves only) _waiting on Optifine_
   * Slimes -> Adipose
   * Husk -> Sycorax
   * Stray -> Scarecrows
   * Judoon
   * Clockwork Droid
4. Custom GUI textures for TARDIS GUIs - _now possible with Optifine_
5. Move all files in assets/minecraft/mcpatcher to assets/minecraft/optifine
6. Rename resource pack to 'TARDIS-Optifine'
 
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
7. Custom advancements - _done_
8. Custom monsters
