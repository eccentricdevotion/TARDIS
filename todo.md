# TODO

## Current version `2.6-beta-1`
1. Test `platform_id`, Temporal Invisibility
2. Jettison the whole room (including walls), will require new room schematics... (in progress - working with ARS :)
3. Architectural Reconfiguration System (almost there)
    * Doorways still. If a room is growing and there isn't one next to it - _but there will be_ - the doorway blocks will need to be removed. Could do a check right at the end, but it might just be easier to leave the doorway open…
4. Remove the TARDIS creation block stack, and make a special seed block that determines the type of TARDIS that will be grown. Use custom configurable recipes - the recipes will still include precious block/lapis block/redstone torch, but 3 other blocks will set:
    * TARDIS wall block
    * TARDIS floor block
    * Police Box preset block
    * Seed blocks when broken, should drop themselves
    * Use dyes, wool, and stained clay in recipes - each data value represents a different block type, but brings down the number of recipes needed, _the data value determines the seed_ (or just restrict it blocks of those colours? _Does it matter? There are 20,846,397,359,932,200 unique rocket combinations..._)
5. While doing [4], add one slot for a custom console (slot added, just need a command)
6. HADS + explosions + fireballs + lava
7. Put farmland, crops, buttons, levers and signs on last when growing rooms
8. Sonic Screwdrivers
9. TARDIS Information System (in progress)
10. Chameleon presets (in progress)
    * Grass mound?
    * Allow wood doors to be exterior TARDIS doors

### Waiting on Bukkit API
2. Get/set horse speed (Attribute API)

## Next version `2.7+`
1. TARDIS invisiblity
2. Advanced TARDIS console
3. Artron storage blocks
4. T.I.P.S. - TARDIS Interior Positioning System

## Minecraft API when available `3.0?`
1. Recreate everything with the new API :)
2. Create custom blocks for the TARDIS and console.
3. If using the LAPIS block have the walls a special TARDIS wall block.
4. A TARDIS key and a crafting recipe for that item.
5. A Sonic Screwdriver and a crafting recipe for that item.
6. GUI (if allowed)
7. Add sounds
8. Make the TARDIS Police Box an entity (if allowed)
