# TODO


## Current version `4.14.0`

## Next version `4.15.0`

1. Convert consoles to use LIGHT blocks for light sources rather than REDSTONE_LAMPS

## Version 5.0.0

1. Merge TCG along TWA, TVM & TARDISShop into a Mega TARDIS plugin
   - Copy all config values from previous plugins to new files
   - Change generator settings in _planets.yml_ from `TARDISChunkGenerator:xxx` to `TARDIS:xxx`
   - Change messaging to `TARDISMessage.send()`, but configured with module prefix
   - Add all messages to _en.yml_
   - Add config options to enable/disable modules (include TARDISDynmap)
   - Read schematic files from JAR file
   - Update shop _items.yml_ with new TARDIS items

## Future versions

1. Cache TARDIS data to speed lookups and reduce database queries
2. Use the Vortex Manipulator to teleport past force fields
3. Optional world name in `/tardistravel` - [#306](https://github.com/eccentricdevotion/TARDIS/issues/306)
4. Time travel - [#305](https://github.com/eccentricdevotion/TARDIS/issues/305)
5. Charge players a fee (via Vault) for using the Junk TARDIS (buy ticket at the TARDISShop?)
6. All the things...

## Wiki Documentation

* Semi-random assigned parking - add GUI
* Add other area sub-commands

## Resource Pack / TARDISWeepingAngels

1. Re-skin mobs for Resource pack, and add new mob sounds
    * Davros
    * Whispermen
    * Clockwork Droid
    * Endermites -> Cybermats
    * Iron Golem -> Gunslinger
    * Slimes -> Adipose
    * Husk -> Sycorax
    * Stray -> Scarecrows
2. Animate models for player disguises (could be done in TARDIS instead?)
3. Fix laggy Silurian cave finder

## Minecraft Bedrock Edition?

1. Recreate everything :)
2. Create custom blocks for the TARDIS and console
3. Custom TARDIS items and crafting recipes for the same
4. A Sonic Screwdriver and a crafting recipe for it
5. GUIs
6. Make the TARDIS Police Box an entity
7. Custom monsters
