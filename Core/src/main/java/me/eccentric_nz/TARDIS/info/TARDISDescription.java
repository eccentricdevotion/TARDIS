/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.info;

/**
 * The Master had modified the Doctor's TARDIS using Block Transfer Computation as part of his plan to ensnare the
 * Doctor in Castrovalva. At least some of the TIS's contents, such as the entry on Castrovalva, would not otherwise
 * have existed.
 *
 * @author bootthanoo, eccentric_nz
 */
public enum TARDISDescription {

    KEY_INFO("The TARDIS key allows you to enter and exit the TARDIS."),
    SONIC_INFO("The Sonic Screwdriver comes with 11 different upgrades, and each has its own special uses. Check out the Sonic Screwdriver upgrades for further information."),
    SONIC_STANDARD_INFO("The Standard Sonic Screwdriver is the base model, and has no specific functions. It will open doors at a distance,  press buttons & levers at a distance and open the Player Preferences GUI."),
    SONIC_BIO_INFO("The Bio-scanner Sonic Screwdriver Upgrade is useful for scanning the health/hunger of players, and freezing players in place for a second or two."),
    SONIC_REDSTONE_INFO("The Redstone Sonic Screwdriver Upgrade is useful for building redstone contraptions. It will power on redstone by right-clicking, power pistons, lamps, and redstone tracks."),
    SONIC_DIAMOND_INFO("The Diamond Sonic Screwdriver Upgrade can be used for immediately breaking iron fences, glass, ice & packed ice, and cutting spider web."),
    SONIC_EMERALD_INFO("When you right-click the ground with the Emerald Sonic Screwdriver Upgrade, it tells you the same information as the TARDIS Scanner, but relative to you."),
    SONIC_ADMIN_INFO("The Admin Sonic Screwdriver Upgrade allows you to open the TARDIS Admin GUI, scan a player's inventory, or when right-clicking a TARDIS door, see who is inside a TARDIS."),
    SONIC_PAINTER_INFO("The Painter Sonic Screwdriver Upgrade can be used re-colour coloured blocks such as wool, stained glass and terracotta."),
    SONIC_BRUSH_INFO("The Brush Sonic Screwdriver Upgrade can be used instead of an archaeology brush to remove items from suspicious blocks."),
    SONIC_IGNITE_INFO("The Ignite Sonic Screwdriver Upgrade can be used for lighting blocks on fire."),
    SONIC_KNOCKBACK_INFO("The Knockback Sonic Screwdriver Upgrade can be used to knockback hostile mobs."),
    SONIC_PICKUP_ARROWS_INFO("The Pickup Arrows Sonic Screwdriver Upgrade can be used change the status of fired arrows so that the player can pick them up."),
    SONIC_CONVERSION_INFO("The Conversion Sonic Screwdriver Upgrade can be used to sonically transform blocks from one state to another - you can turn concrete powder into concrete, dirt into mud, and mud into clay."),
    LOCATOR_INFO("The TARDIS Locator is a craftable compass that will point the way towards a Time Lord's TARDIS."),
    STATTENHEIM_REMOTE_INFO("The Stattenheim Remote is a craftable item used to summon a Time Lord's TARDIS."),
    READER_INFO("The TARDIS Biome Reader is a craftable item used to quickly create Biome Storage Disks. It is only available in Medium and Easy difficulty modes."),
    REMOTE_KEY_INFO("The TARDIS Remote Key is a craftable item used to deadlock and unlock the Police Box door (LEFT-click air), and also show and hide the TARDIS (RIGHT-click air)."),
    LOCATOR_CIRCUIT_INFO("The Locator Circuit is used in crafting the TARDIS Locator."),
    MATERIALISATION_CIRCUIT_INFO("The Materialisation Circuit enables a TARDIS to dematerialize. It is used in crafting a Stattenheim Circuit, and is a component of the TARDIS Advanced Console."),
    STATTENHEIM_CIRCUIT_INFO("The Stattenheim Circuit is used in the crafting of a Stattenheim Remote. This circuit combines the Materialization Circuit and the Locator Circuit to allow the two to fit in the remote's casing."),
    SONIC_OSCILLATOR_INFO("The Oscillator Circuit is used in crafting the Sonic Screwdriver."),
    BIO_SCANNER_CIRCUIT_INFO("The Bio-scanner Circuit is used to upgrade the Sonic Screwdriver."),
    BRUSH_CIRCUIT_INFO("The Brush Circuit is used to upgrade the Sonic Screwdriver."),
    CONVERSION_CIRCUIT_INFO("The Conversion Circuit is used to upgrade the Sonic Screwdriver."),
    REDSTONE_ACTIVATOR_CIRCUIT_INFO("The Redstone Activator Circuit is used to upgrade the Sonic Screwdriver."),
    RANDOMISER_CIRCUIT_INFO("The Randomiser Circuit is used to generate random Time Travel destinations when placed in the Advanced Console."),
    RIFT_CIRCUIT_INFO("The Rift Circuit is an ingredient in crafting a Rift Manipulator."),
    RIFT_MANIPULATOR_INFO("The Rift Manipulator allows players to set up their own personal TARDIS rechargers."),
    ACID_BUCKET_INFO("Acid Buckets are an ingredient in crafting an Acid Battery. Acid Buckets can only be gained on the Skaro planet."),
    ACID_BUCKET_RECIPE("Fill an empty bucket with Acid water from the Skaro planet."),
    ACID_BATTERY_INFO("Acid Batteries are an ingredient in crafting the Rift Manipulator."),
    RUST_BUCKET_INFO("Rust Buckets are an ingredient in crafting the Rust Plague Sword. Rust Buckets can only be gained on the Skaro planet."),
    RUST_BUCKET_RECIPE("Fill an empty bucket with Rusty lava from the Skaro planet."),
    RUST_PLAGUE_SWORD_INFO("The Rust Plague Sword deals more damage to Daleks."),
    SKARO_INFO("The planet Skaro is a dry, barren place where the lava is rusty and the water acidic. Structures are plentiful and lootable, but beware of the natives!"),
    SKARO_MONSTERS("The native inhabitants of planet Skaro are Daleks."),
    SILURIA_INFO("Siluria is a lush, jungle planet where bamboo is plentiful. Structures can be found in and around water and are lootable, but beware of the natives!"),
    SILURIA_MONSTERS("The native inhabitants of Siluria are the lizard-like Silurians."),
    GALLIFREY_INFO("Gallifrey is a barren rust-coloured landscape, with brown lakes and grey clouds. Structures are plentiful and lootable, and look out for a good trade!"),
    GALLIFREY_MONSTERS("The native inhabitants of Gallifrey are Time Lords (who like to trade)."),
    SONIC_GENERATOR_INFO("The Sonic Generator allows players to configure and produce a new screwdriver from the console."),
    SONIC_DOCK_INFO("The Sonic Dock allows players to charge their Sonic Screwdriver and load the last scanned location or player as the TARDIS's next travel destination."),
    DIAMOND_DISRUPTOR_CIRCUIT_INFO("The Diamond Disruptor Circuit is used to upgrade the Sonic Screwdriver."),
    EMERALD_ENVIRONMENT_CIRCUIT_INFO("The Emerald Environment Circuit is used to upgrade the Sonic Screwdriver."),
    SERVER_ADMIN_CIRCUIT_INFO("The Server Admin Circuit is used to upgrade the Sonic Screwdriver."),
    CHAMELEON_CIRCUIT_INFO("The Chameleon Circuit is used in crafting the Preset Storage Disks and is a component of the TARDIS Advanced Console."),
    INPUT_CIRCUIT_INFO("The Input Circuit is a component of the TARDIS Advanced Console and is required to use the Destination Terminal and the TARDIS Keyboard."),
    IGNITE_CIRCUIT_INFO("The Ignite Circuit upgrades the Sonic Screwdriver so that you can ignite flammable blocks."),
    KNOCKBACK_CIRCUIT_INFO("The Knockback Circuit upgrades the Sonic Screwdriver so that you can repel monsters."),
    PICKUP_ARROWS_CIRCUIT_INFO("The Pickup Arrows Circuit upgrades the Sonic Screwdriver so that you can pickup any arrow."),
    INVISIBILITY_CIRCUIT_INFO("The Invisibility Circuit is a component of the TARDIS Advanced Console and is required to make the TARDIS invisible."),
    MEMORY_CIRCUIT_INFO("The Memory Circuit is a component of the TARDIS Advanced Console and is required to use the Save Sign."),
    TEMPORAL_CIRCUIT_INFO("The Temporal Circuit is a component of the TARDIS Advanced Console and is required to use the Temporal Locator."),
    TARDIS_ARS_CIRCUIT_INFO("The ARS Circuit is a component of the TARDIS Advanced Console and is required to use the Architectural Reconfiguration System."),
    SCANNER_CIRCUIT_INFO("The Scanner Circuit is a component of the TARDIS Advanced Console and is required to use the TARDIS Scanner and Exterior Rendering room."),
    PERCEPTION_CIRCUIT_INFO("The Perception Circuit is used in crafting the Perception Filter."),
    PAINTER_CIRCUIT_INFO("The Painter Circuit upgrades the Sonic Screwdriver so that you can change the colour of blocks."),
    PERCEPTION_FILTER_INFO("The Perception Filter is used to make yourself less noticeable."),
    AREA_DISK("An Area Storage Disk lets you travel to a TARDIS Area. These disks are not craftable, but are stored automatically in the Disk Storage Container for the areas you have permission to travel to."),
    BLANK_STORAGE_DISK_INFO("A Blank Storage Disk is a craftable disk that is the base disk for crafting into another sort of TARDIS disk."),
    BIOME_STORAGE_DISK_INFO("A Biome Storage Disk is a craftable disk that lets you travel to specific biomes."),
    AUTHORISED_CONTROL_DISK_INFO("An Authorised Control Disk is a craftable disk that lets companions travel to the Time Lord of the TARDIS it belongs to."),
    PLAYER_STORAGE_DISK_INFO("A Player Storage Disk is a craftable disk that lets you travel to players."),
    PRESET_STORAGE_DISK_INFO("A Preset Storage Disk is a craftable disk that lets you set the TARDIS exterior preset."),
    SAVE_STORAGE_DISK_INFO("A Save Storage Disk is a craftable disk that lets you travel to a saved location."),
    ARTRON_CAPACITOR_INFO("An Artron Capacitor lets you overcharge your TARDIS with Artron Energy."),
    ARTRON_CAPACITOR_STORAGE_INFO("An Artron Capacitor Storage block lets you charge an Artron Furnace using TARDIS energy. It is also found in the Eye of Harmony room, where you can store Artron Capacitors."),
    ARTRON_STORAGE_CELL_INFO("An Artron Storage Cell lets you store and transfer Artron Energy."),
    ARTRON_FURNACE_INFO("An Artron Furnace is a special furnace that can use Artron Storage Cells for fuel. The Artron Furnace cooks quickly but doesn't burn as long as a regular furnace."),
    BOW_TIE_INFO("Bow ties are cool."),
    SPACE_HELMET_INFO("A TARDIS Space Helmet - wear it to prevent player damage when inside the Eye of Harmony room."),
    CUSTARD_INFO("Part of the Eleventh Doctor's regeneration food, good for dipping fish fingers in."),
    CUSTARD_CREAM_INFO("The thirteenth Doctor thought that Custard Creams were 'the best' type of biscuit."),
    FISH_FINGER_INFO("One half of the Eleventh Doctor's regeneration food. Best eaten with custard."),
    THREE_D_GLASSES_INFO("The Tenth Doctor used a pair of 3-D glasses when observing the effects the Void had on people and objects. You can do the same! 3-D glasses give you night vision when worn."),
    JAMMY_DODGER_INFO("The eleventh Doctor fooled the Daleks into thinking a Jammy Dodger was a self-destruct button for the TARDIS."),
    JELLY_BABY_INFO("The fourth Doctor's favourite. Offer them to strangers in order to defuse tense situations."),
    FOB_WATCH_INFO("The Tenth Doctor used a Chameleon Arch to change himself into the human John Smith to elude the Family of Blood. You can craft a fob watch to change your self from a Time Lord in a regular player (a Steve - albeit with a random name)."),
    COMMUNICATOR_INFO("You can craft a communicator to talk to Handles when he is inside the TARDIS."),
    EXTERIOR_LAMP_LEVEL_SWITCH_INFO("Allows you to set the light level for the TARDIS's exterior lamp. Levels are 2, 4, 6, and 8. The Chameleon Preset must be using an armour stand model e.g. a coloured Police Box."),
    INTERIOR_LIGHT_LEVEL_SWITCH_INFO("Allows you to set the light level of the custom lights inside the TARDIS."),
    TARDIS_TELEVISION("Allows you to change your player skin to a character for the Doctor Who universe."),
    CHARGING_SENSOR("""
            INFO: The Charging Sensor changes to a Redstone block when the TARDIS is charging.
            USAGE: Automatic.
            UPDATING: /tardis update charging-sensor
            BLOCK: REDSTONE_BLOCK"""),
    FLIGHT_SENSOR("""
            INFO: The Flight Sensor changes to a Redstone block when the TARDIS is in flight.
            USAGE: Automatic.
            UPDATING: /tardis update flight-sensor
            BLOCK: REDSTONE_BLOCK"""),
    HANDBRAKE_SENSOR("""
            INFO: The Handbrake Sensor changes to a Redstone block when the TARDIS handbrake is released.
            USAGE: Automatic.
            UPDATING: /tardis update handbrake-sensor
            BLOCK: REDSTONE_BLOCK"""),
    MALFUNCTION_SENSOR("""
            INFO: The Malfunction Sensor changes to a Redstone block when the TARDIS malfunctions.
            USAGE: Automatic.
            UPDATING: /tardis update malfunction-sensor
            BLOCK: REDSTONE_BLOCK"""),
    POWER_SENSOR("""
            INFO: The Power Sensor changes to a Redstone block when the TARDIS is powered on.
            USAGE: Automatic.
            UPDATING: /tardis update power-sensor
            BLOCK: REDSTONE_BLOCK"""),
    TARDIS_MONITOR_INFO("""
            INFO: The TARDIS Monitor allows you to view the exterior of the TARDIS.
            USAGE: Right-click the levers on the Monitor Frame to update the exterior view.
            UPDATING: Place a monitor in an item frame and use /tardis update monitor
            BLOCK: ITEM_FRAME with placed TARDIS Monitor item"""),
    MONITOR_FRAME_INFO("""
            INFO: The Monitor Frame is part of the TARDIS Monitor setup and contains the control levers.
            USAGE: Right-click the levers to update the exterior view.
            UPDATING: Place a monitor frame in an item frame and use /tardis update monitor
            BLOCK: ITEM_FRAME with placed monitor frame item"""),
    RELATIVITY_DIFFERENTIATOR("""
            INFO: The Relativity Differentiator is a comparator that controls whether the TARDIS automatically enters exterior flight mode when the handbrake is released.
            USAGE: Set the comparator to "subtraction" mode to engage exterior flying mode.
            UPDATING: /tardis update relativity-differentiator
            BLOCK: COMPARATOR"""),
    HANDLES_INFO("You can craft and place Handles - a Cyberman head from the Maldovarium Market - so that he can interact with you and the TARDIS."),
    VORTEX_MANIPULATOR_INFO("Travel like Jack Harkness or River Song from Doctor Who! Have the Vortex Manipulator in your hand, put in your desired location, and GO!"),
    SONIC_BLASTER_INFO("A sonic blaster, or squareness gun, is a type of weapon available in the 51st century."),
    BUDGET("Small, but cosy - good if you don't like walking too far to the console :)"),
    BIGGER("Big enough to swing a cat - console design by killeratnight & L0rd Rahl."),
    DELUXE("Luxury in a box - console design by killeratnight & L0rd Rahl."),
    ELEVENTH("It's smaller on the outside... - console design by killeratnight & L0rd Rahl."),
    TWELFTH("The 12th Doctor's TARDIS - console design by killeratnight."),
    THIRTEENTH("The 13th Doctor's TARDIS - console design by Razihel."),
    FIFTEENTH("The 14/15th Doctor's TARDIS - console design by airomis"),
    FACTORY("The 1st Doctor's TARDIS; straight from the factory! - console design by Razihel."),
    FUGITIVE("The Fugitive Doctor's TARDIS; don't get captured by the Judoon! - based on design by DT10."),
    HOSPITAL("St John's Hospital TARDIS; for when you just need some rest."),
    REDSTONE("This one has some redstone in it - console design by killeratnight & L0rd Rahl."),
    STEAMPUNK("Small, but steamy."),
    PLANK("A wood based interior."),
    TOM("The 4th Doctor's TARDIS."),
    ARS("The Architectural Reconfiguration System TARDIS - console design by killeratnight & L0rd Rahl."),
    WAR("The War Doctor's TARDIS."),
    PYRAMID("A sandstone pyramid TARDIS - console design by airomis."),
    MASTER("The Master's TARDIS - console design by ShadowAssociate."),
    MECHANICAL("Mechanical TARDIS - adapted from design by Plastic Straw."),
    ENDER("An End Stone and Purpur TARDIS - console design by ToppanaFIN."),
    COPPER_11TH("The 11th Doctor's Copper TARDIS - console design by vistaero."),
    CORAL("The 10th Doctor's TARDIS - console design by vistaero."),
    CURSED("A dark, cursed TARDIS - console design by airomis"),
    DELTA("A Nether Delta TARDIS."),
    DIVISION("The Division Interuniverse Spaceship."),
    CAVE("A dripstone cave TARDIS."),
    WEATHERED("A weathered copper TARDIS."),
    ROTOR("A simple time rotor TARDIS."),
    RUSTIC("A rustic TARDIS."),
    ORIGINAL("The original v1.0 TARDIS."),
    ANCIENT("An ancient deep dark TARDIS."),
    BONE("An early era styled TARDIS."),
    CUSTOM("A custom designed server TARDIS"),
    ALLAY("An allay house, bring your friends."),
    ANTIGRAVITY("Going up..."),
    APIARY("Bees, make some honey."),
    AQUARIUM("Tanks for your fish."),
    ARBORETUM("A room full of plants."),
    BAKER("A secondary console room based on the 4th Doctor's TARDIS."),
    BAMBOO("A place for pandas."),
    BEDROOM("A king sized bed for two."),
    BIRDCAGE("A room for birds. Polly wants a cracker!"),
    CHEMISTRY("A scientific place of learning and making."),
    EMPTY("Not much in here."),
    EYE("The Eye of Harmony from the 11th Doctor's TARDIS."),
    FARM("You need this room to bring farm animals into the TARDIS."),
    GARDEN("Flowers, flowers, everywhere."),
    GEODE("Grow some amethyst, catch an axolotl."),
    GRAVITY("Going down..."),
    GREENHOUSE("Food grows here."),
    HARMONY("The Eye of Harmony from the 8th Doctor's TARDIS."),
    HUTCH("Bunnies!"),
    IGLOO("Cold storage for polar bears."),
    IISTUBIL("Bring your camels."),
    KITCHEN("Take a seat, relax, and have some chow."),
    LAVA("Stride though the lava pools"),
    LAZARUS("Modify your genetic makeup."),
    LIBRARY("Plenty to read and shelve in here."),
    MANGROVE("Mud, roots and frogs."),
    MAZE("Can you find your way through the maze?"),
    MUSHROOM("A small Mycellarium."),
    NETHER("Your own personal hell."),
    PASSAGE("To get from here to there."),
    PEN("A cosy room for sniffers."),
    POOL("Grab your togs!"),
    RAIL("You need this room to transport items into the TARDIS with a storage minecart."),
    RENDERER("This room shows you the environment outside the TARDIS's current location."),
    SHELL("The room in the TARDIS where the plasmic shells are created."),
    SMELTER("Efficiently smelt your items."),
    STABLE("Giddy-up, here come the horses..."),
    STALL("Llamas, watch for spit!"),
    SURGERY("In need of medical attention?"),
    TRENZALORE("The final resting place of the Doctor."),
    VAULT("Some extra storage space."),
    VILLAGE("A home for the weary trader."),
    WOOD("A secondary console room made of wood."),
    WORKSHOP("Craft, smelt, brew, enchant and repair."),
    ZERO("Recover your health in tranquility."),
    FLYING("You can fly the TARDIS exterior if it is set to an armour stand Chameleon preset. Use the relativity differentiator to make the TARDIS enter flying mode when the handbrake is released. Once flying use the WASD keys to navigate, and press the dismount key (default: shift) to land."),
    CAMERA("You can view the TARDIS exterior from an armour stand Chameleon preset by sneaking and clicking the TARDIS Monitor. Press the dismount key (default: shift) to return to the TARDIS interior."),
    ALT_CONTROLS("Please refer to the Advanced Console, the Save Sign, the Keyboard, the Destination Terminal and the travel commands."),
    MALFUNCTIONS("When the TARDIS travels, there is a chance of a malfunction happening that deposits the TARDIS in an unexpected location."),
    CONSOLE_ARS("""
            INFO: The Architectural Reconfiguration System allows the player to add and remove rooms in the TARDIS
            USAGE: Right-click.
            UPDATING: /tardis update ARS
            BLOCK: SIGN"""),
    ADVANCED("""
            The TARDIS Advanced Console is the TARDISes control centre, it enables TARDIS Circuits and processes location Storage Disks.
            USAGE: Right-click.
            UPDATING: /tardis update advanced
            BLOCK: JUKEBOX"""),
    ARTRON("""
            INFO: When pressed, it performs various Artron Energy functions.
            USAGE: Right-click with the TARDIS key to initialise the Artron Energy Capacitor.
            USAGE: Right-click while sneaking to transfer Time Lord energy.
            USAGE: Right-click with the full charge item (default: NETHER_STAR) to boost the Artron Energy Capacitor to 100%.
            USAGE: Right-click with any item other than the TARDIS key to view the reserve percentage.
            USAGE: Left-click while sneaking with the TARDIS key to respawn the charged Creeper.
            UPDATING: /tardis update artron
            BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"""),
    BACKDOOR("""
            INFO: The backdoor allows an extra point of entry/exit in the TARDIS
            USAGE: Place an IRON_DOOR inside the TARDIS and another IRON_DOOR outside the TARDIS. Right-click to enter/exit.
            UPDATING: /tardis update backdoor (on both IRON_DOORs)
            BLOCK: IRON_DOOR"""),
    BUTTON("""
            INFO: The button calculates a random destination to travel to based on the coordinate repeaters.
            USAGE: Right-click.
            UPDATING: /tardis update button
            BLOCK: LEVER, STONE_BUTTON or WOODEN_BUTTON"""),
    CHAMELEON("""
            INFO: The Chameleon sign allows the Chameleon Circuit to be turned on or off, and for presets to be selected. The Chameleon Circuit makes the TARDIS exterior become the block it lands on.
            USAGE: Right-click the sign.
            UPDATING: /tardis update chameleon
            BLOCK: SIGN"""),
    CONDENSER("""
            INFO: The condenser chest condenses materials into Artron Energy.
            USAGE: Right-click to open the chest. Place items in the chest.
            UPDATING: /tardis update condenser
            BLOCK: CHEST"""),
    CREEPER("""
            INFO: Updates the position where the charged Creeper spawns.
            UPDATING: /tardis update creeper
            BLOCK: any"""),
    DOOR("""
            INFO: This is the door used to enter and exit the TARDIS.
            USAGE: Right-click with the TARDIS key.
            UPDATING: /tardis update door
            BLOCK: IRON_DOOR"""),
    EPS("""
            INFO: Updates the block that the Emergency Programme One appears on when enacted.
            USAGE: Right-click the block to update
            UPDATING: /tardis update EPS
            BLOCK: any"""),
    UPDATEABLE_FARM("""
            INFO: Updates the block that animals spawn relative to when entering the TARDIS.
            USAGE: Right-click the block to update.
            UPDATING: /tardis update farm
            BLOCK: any"""),
    HANDBRAKE("""
            INFO: The Handbrake is used to start the dematerialization and rematerialization of the TARDIS.
            USAGE: Right-click to disengage, left-click do engage.
            UPDATING: /tardis update handbrake
            BLOCK: LEVER"""),
    INFO("""
            INFO: The TARDIS Information System is the TARDIS's computer and information database. It contains information and documentation about the plugin.
            USAGE: Right-click. Type the white letter into chat to select the menu item.
            UPDATING: /tardis update info
            BLOCK: SIGN"""),
    KEYBOARD("""
            INFO: The Keyboard allows the player to type in specific coordinate, the 'home' location, a saved location, a player, a TARDIS area or a biome to materialize to.
            USAGE: Right-click with a sign in hand.
            USAGE: Line 1: home, save name, player name, area name or biome type.
            or
            USAGE: Line 1: World.
            USAGE: Line 2: x coordinate.
            USAGE: Line 3: y coordinate
            USAGE: Line 4: z coordinate.
            UPDATING:
            BLOCK: SIGN"""),
    LIGHT("""
            INFO: When pressed, the lamps in the TARDIS control room are toggled on/off.
            USAGE: Right-click.
            UPDATING: /tardis update light
            BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"""),
    TOGGLE("""
            INFO: When pressed, the black wool blocks behind the TARDIS interior door are toggled AIR/WOOL to provide access to the Vortex.
            USAGE: Right-click.
            UPDATING: /tardis update toggle_wool
            BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"""),
    UPDATEABLE_RAIL("""
            INFO: Updates the block that storage minecarts spawn on when entering the TARDIS.
            USAGE: Attach a rail track to the front of the TARDIS and have a Minecart with Chest run into the TARDIS door.
            UPDATING: /tardis update rail
            BLOCK: FENCE"""),
    SAVE_SIGN("""
            INFO: The save sign can be used to quickly set the TARDIS destination. When Right-clicked, it opens up a GUI that allows the player to select a saved location to travel to.
            USAGE: Right-click.
            UPDATING: /tardis update save-sign
            BLOCK: SIGN"""),
    SCANNER("""
            INFO: The scanner will show information for the current location of the TARDIS Police Box, or, if a destination has been set, the next travel location.
            USAGE: Right-click.
            UPDATING: /tardis update scanner
            BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"""),
    UPDATEABLE_STABLE("""
            INFO: Updates the block that horses spawn on when entering the TARDIS.
            USAGE: Right-click the block that horses are to spawn on.
            UPDATING: /tardis update stable
            BLOCK: any"""),
    UPDATEABLE_STALL("""
            INFO: Updates the block that llamas spawn on when entering the TARDIS.
            USAGE: Right-click the block that llamas are to spawn on.
            UPDATING: /tardis update stall
            BLOCK: any"""),
    UPDATEABLE_ALLAY("""
            INFO: Updates the block that allays spawn on when entering the TARDIS.
            USAGE: Right-click the block that allays are to spawn on.
            UPDATING: /tardis update allay
            BLOCK: any"""),
    UPDATEABLE_BAMBOO("""
            INFO: Updates the block that pandas spawn on when entering the TARDIS.
            USAGE: Right-click the block that pandas are to spawn on.
            UPDATING: /tardis update bamboo
            BLOCK: any"""),
    UPDATEABLE_BIRDCAGE("""
            INFO: Updates the block that parrots spawn on when entering the TARDIS.
            USAGE: Right-click the block that parrots are to spawn on.
            UPDATING: /tardis update birdcage
            BLOCK: any"""),
    UPDATEABLE_FUEL("""
            INFO: Updates the chest that fuel items are dropped into in the Smelter room.
            USAGE: Right-click the chest that fuel items go in.
            UPDATING: /tardis update fuel
            BLOCK: any"""),
    UPDATEABLE_HUTCH("""
            INFO: Updates the block that rabbits spawn on when entering the TARDIS.
            USAGE: Right-click the block that rabbits are to spawn on.
            UPDATING: /tardis update hutch
            BLOCK: any"""),
    UPDATEABLE_IGLOO("""
            INFO: Updates the block that polar bears spawn on when entering the TARDIS.
            USAGE: Right-click the block that polar bears are to spawn on.
            UPDATING: /tardis update igloo
            BLOCK: any"""),
    UPDATEABLE_IISTUBIL("""
            INFO: Updates the block that camels spawn on when entering the TARDIS.
            USAGE: Right-click the block that camels are to spawn on.
            UPDATING: /tardis update iistabul
            BLOCK: any"""),
    UPDATEABLE_LAVA("""
            INFO: Updates the block that striders spawn on when entering the TARDIS.
            USAGE: Right-click the block that striders are to spawn on.
            UPDATING: /tardis update lava
            BLOCK: any"""),
    UPDATEABLE_PEN("""
            INFO: Updates the block that sniffers spawn on when entering the TARDIS.
            USAGE: Right-click the block that sniffers are to spawn on.
            UPDATING: /tardis update pen
            BLOCK: any"""),
    UPDATEABLE_SMELT("""
            INFO: Updates the chest that smeltable items are dropped into in the Smelter room.
            USAGE: Right-click the chest that smeltable items go in.
            UPDATING: /tardis update smelt
            BLOCK: CHEST"""),
    UPDATEABLE_VAULT("""
            INFO: Updates the drop chest that distributes items into the Vault room.
            USAGE: Right-click the chest to act as the drop chest.
            UPDATING: /tardis update vault
            BLOCK: CHEST, TRAPPED_CHEST"""),
    UPDATEABLE_VILLAGE("""
            INFO: Updates the block that villagers spawn on when entering the TARDIS.
            USAGE: Right-click the block that villagers are to spawn on.
            UPDATING: /tardis update village
            BLOCK: any"""),
    STORAGE("""
            The Disk Storage Container lets you store Storage Disks and Circuits.
            USAGE: Right-click.
            UPDATING: /tardis update storage
            BLOCK: NOTEBLOCK"""),
    TERMINAL("""
            INFO: The Terminal is the GUI alternative to setting TARDIS coordinates.
            USAGE: Right-click.
            UPDATING: /tardis update terminal
            BLOCK: SIGN"""),
    TEMPORAL("""
            INFO: The Temporal Locator sets the time the Time Lord will be in when exiting the TARDIS.
            USAGE: Right-click. Select the time of day you want to be in.
            UPDATING: /tardis update temporal
            BLOCK: SIGN"""),
    WORLD_REPEATER("""
            INFO: The World Repater controls the environment type of the world you will travel to.
            USAGE: 1-tick: Select a location within the current world.
            USAGE: 2-tick: Select a location from a random (Normal) Overworld.
            USAGE: 3-tick: Select a location from The Nether worlds.
            USAGE: 4-tick: Select a location from The End worlds.
            UPDATING: /tardis update world-repeater
            BLOCK: REPEATER"""),
    X_REPEATER("""
            INFO: The X Repeater controls the X coordinate. The more ticks, the greater distance travelled on the X axis.
            USAGE: Right-click the repeater to set the ticks.
            UPDATING: /tardis update x-repeater
            BLOCK: REPEATER"""),
    Y_REPEATER("""
             INFO: The Y Repeater is a multiplier for distance traveled. The more ticks, the greater distance travelled.
            USAGE: Right-click the repeater to set the ticks.
            UPDATING: /tardis update y-repeater
            BLOCK: REPEATER"""),
    Z_REPEATER("""
            INFO: The Z Repeater controls the Z coordinate. The more ticks, the greater distance travelled on the Z axis.
            USAGE: Right-click the repeater to set the ticks.
            UPDATING: /tardis update z-repeater
            BLOCK: REPEATER"""),
    CYBERMAN("Cybermen will upgrade villagers and players when they have killed them (a new Cyberman) spawns in their place."),
    DALEK("Daleks come in different colours, but mostly spawn in their typical bronze colour. Exterminate!"),
    DALEK_SEC("Dalek Sec just looks fabulous! He's passive unless you annoy him."),
    DAVROS("Davros doesn't do much yet, but probably best not to get in his way."),
    EMPTY_CHILD("If you are killed by an Empty Child you get a gas mask applied to your head when you respawn that you can't remove for 30 seconds."),
    HATH("Hath don't do much yet, but they look pretty cool."),
    HEADLESS_MONK("Headless Monks fire energy blasts from their hands as well as channelling that energy into their swords."),
    ICE_WARRIOR("Ice Warriors are really angry."),
    JUDOON("Judoon are the police force of the Whoniverse. Left-click a Judoon to claim it as your own."),
    K9("You can either craft a K-9 or tame a wolf to get a K-9! Clicking a K-9 will toggle whether he will follow you or stay put."),
    MIRE("The Mire will distill you down to your essential nectar."),
    OOD("Ood spawn randomly around villagers. Left-click an Ood to claim it as your own."),
    RACNOSS("Racnoss spawn randomly in the Nether."),
    SEA_DEVIL("Found in the sea and on land, best if they don't find you!"),
    SILENT("Silent spawn anywhere. Beware their deadly energy discharges!"),
    SILURIAN("Only spawn underground in caves. Watch out for their Silurian guns!"),
    SLITHEEN("A nasty flatulent surprise, best avoided if you want to survive."),
    SONTARAN("Sontarans will try to kill you (as any good Sontaran should). If you manage to right-click a Sontaran with a Weakness Potion before he kills you, he will transform into Strax.n"),
    STRAX("If you right-click Strax he'll talk to you, and if you right-click him with an empty bucket, you'll be able to milk him."),
    TOCLAFANE("Toclafane fly around passively until you hit them, then watch out as they get angry with you."),
    VASHTA_NERADA("Vashta Nerada have a random chance of spawning when a bookshelf is broken."),
    WEEPING_ANGEL("Weeping Angels can only be killed with the configured weapon - by default a DIAMOND_PICKAXE. You can freeze them in place by looking at them and quickly pressing the sneak key."),
    ZYGON("Zygons don't do much yet (except try to kill you), but they look pretty cool.");
    private final String desc;

    TARDISDescription(String desc) {
        this.desc = desc;
    }

    /**
     * Gets the text of this TARDISDescription
     *
     * @return name of this TARDISDescription
     */
    public String getDesc() {
        return desc;
    }
}
