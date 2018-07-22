# TODO

## Current bugfix version `3.8.3`
1. Fix bugs!

## Next version `4.0-beta-1`
1. Convert maps to new NBT format - when checking maps check the NBT tag (TARDISHelper?)
2. Update to Minecraft/Spigot 1.13 - _started_
   * **All we need is BlockData!** - _started_
   * Update Lazarus GUI with new spawn eggs (drowned, turtle, phantom, cod, salmon, pufferfish, tropical fish - waiting on LibsDisguises update) - _started_
3. Shell Room - [https://dev.bukkit.org/projects/tardis/issues/1536](https://dev.bukkit.org/projects/tardis/issues/1536) - _started_
5. Test player preference to bypass `tardis rescue accept` - _started_
6. Test `invisibility` setting to TARDIS areas
7. Test Transmat signs to non-console destinations
   * Add a way to remove the transmat binding
   * Test Handles transmat requests
8. Test `/tardisbind chameleon [off|adapt|invisible|PRESET]` - _started_
9. Test Tardis biome travel world selection - `/ttravel biome [biome] [world]`
10. Fix Biome adaptive presets dropping flowers & seeds (properly)
11. Test test test

## TARDIS data pack?
* Move custom advancements to here
* Add TARDIS recipes (where possible)
* Add TARDIS tags

## Future version `?+`
* TARDIS Junk Mode - `/tprefs junk [wall|floor] [block_type]`?
* TARDISSonicBlaster addon? - _unlikely_
* Slimmer TARDIS option via Optifine's CEM mod? - _maybe_
* Add more API Events that other plugins can listen for? - _unlikely_
   * Sonic Screwdriver events
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
4. Custom GUI textures for TARDIS GUIs - _now possible with Optifine, but don't get applied to Spigot generated GUIs :(_
5. Rename resource pack to 'TARDIS-Optifine'
 
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
