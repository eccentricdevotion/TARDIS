# TODO

## Next version `4.1`
0. Update to Spigot 1.14
   * Add new mob types to farming and genetic manipulator (waiting for ProtocolLib/LibsDisguises update)
   * Foxes?
   * Test villager farming - biome specific / professions not persistent?
   * Remove / alter the Artron furnace now that there is a Smoker / Blast Furnace?
   * Make the TARDIS Condenser a composter?
   * Bamboo room to farm Pandas?
1. Test player preference to bypass `tardis rescue accept`
2. Test `invisibility` setting in TARDIS areas
3. Test Handles transmat requests
4. Test Tardis biome travel world selection - `/ttravel biome [biome] [world]`
5. Actually use `TARDISArtronLevels#checkLevel()`
6. Re-enable Siluria, Skaro and Gallifrey worlds
   * For TARDIS worlds, use PlayerChangedWorldEvent to switch GameModes depending on config setting
   * Make sure spawners work
7. Update documentation...
8. Add a check to make sure a TARDIS can't be created in someone elses home location


## TARDIS data pack
* Add TARDIS recipes? _probably not as you can't set the display name_

## Future version `?+`
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
5. Rename resource pack to 'TARDIS-Optifine'?

## Waiting on Bukkit / SpigotAPI
1. Get/set Villager career level and willingness

## Minecraft API if ever available
1. Recreate everything with the new API :)
2. Create custom blocks for the TARDIS and console
3. Custom TARDIS items and crafting recipes for the same
4. A Sonic Screwdriver and a crafting recipe for it
5. GUIs
6. Make the TARDIS Police Box an entity
7. Custom monsters
