#TODO

##Current version `2.9.2`

###Bugs
? probably ...

##Future version `3.0+`
1. Console modes / grace period on hard mode?
2. ARS room limit - can only reconfigure x rooms at a time (and cannot open ARS until reconfiguration is complete) - configurable option
3. Slimmer TARDIS option?
4. Chameleon Arch / Fob watch
5. Remote locking, advanced hide and rebuild via remote key
6. TARDIS creation area config
7. More MCPatcher texture support
8. Add snow to Diamond Disruptor Sonic Screwdriver
9. Server condenser
10. Add `/tpa` equivalent to travel to player's WorldGuard/Towny/Factions/GriefPrevention region
11. Create a TARDIS `json` schematic format, convert current code to use the new format - _started_
    * fix repeater control positions in console
    * fix bedrock in doorway not being replaced with wall block (or AIR if a room is next door) _started_
    * region selection via wand - __done__
    * record dimensions - __done__
    * record block type and data - __done__
    * record player's relative location - __done__
    * schematic creation and compression/decompression - __done__
    * load/paste schematics - __done__
    * add checks to make sure region is square, a multiple of 16 - __done__
    * add check to make sure player is facing the correct direction?
    * allow custom schematic to be ARS compatible

###I couldn't really be bothered
1. HADS + explosions + fireballs + lava
2. Put farmland, crops, buttons, ladders, levers and signs on last when growing rooms
3. TARDIS Information System auto log out?
4. Check interior SFX don't play in the TARDIS of the player whose preference is off

##Resource Pack / TARDISWeepingAngels

1. Texture the top and bottom of white stained clay roundel

Re-skin mobs for Resource pack, and add new mob sounds

* (Wither?) Skeletons -> Vashta Nerada (breaking a bookcase gives a random chance of one spawning at that location)
* Villagers -> Ood - _started_
* Villagers -> Hath
* Endermen -> The Silence
* Iron Golem -> Gunslinger
* Witches -> ?
* Wolves -> K-9
* Slimes -> Adipose
* PigZombies -> Strax / Butler (allow milking) __done__
* Skeletons -> Weeping Angels - __done__
* Skeletons disguised as Snow Golems -> Daleks - __done__
* Skeletons -> Silurians - __done__
* PigZombies -> Ice Warriors - __done__
* Zombie babies -> Empty Child - __done__
* Zombies -> Zygons - __done__
* Zombies -> Cybermen - __done__
* Zombies -> Sontarans - __done__

##Waiting on Minecraft/Bukkit update

1. TARDIS invisibility - 1.8 barrier blocks?
2. Add new blocks / items

##Waiting on Bukkit API
0. IDs are soon to be history... so convert all ID use to entity names
1. Get/set horse speed (Attribute API)
2. Achievements API is now available, but doesn't allow custom achievements :(

##Minecraft API when available
1. Recreate everything with the new API :)
2. Create custom blocks for the TARDIS and console.
3. If using the LAPIS block have the walls a special TARDIS wall block.
4. Custom TARDIS items and crafting recipes for the same.
5. A Sonic Screwdriver and a crafting recipe for it.
6. GUIs (if allowed)
7. Make the TARDIS Police Box an entity (if allowed)
8. Custom achievements
9. Custom monsters
