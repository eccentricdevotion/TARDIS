/*
 * Copyright (C) 2014 eccentric_nz
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
 *
 * The Master had modified the Doctor's TARDIS using Block Transfer Computation
 * as part of his plan to ensnare the Doctor in Castrovalva. At least some of
 * the TIS's contents, such as the entry on Castrovalva, would not otherwise
 * have existed.
 *
 * @author bootthanoo, eccentric_nz
 */
public enum TARDISDescription {

    KEY_INFO("The TARDIS key allows you to enter and exit the TARDIS."),
    SONIC_INFO("The Sonic Screwdriver comes with 5 different upgrades, and each has its own special uses. Check out the Sonic Screwdriver upgrades for further information."),
    SONIC_Q_INFO("The Quartz Sonic Screwdriver is the base model, and has no specific functions. It will open doors at a distance,  press buttons & levers at a distance and open the Player Preferences GUI."),
    SONIC_B_INFO("The Bio-scanner Sonic Screwdriver Upgrade is useful for scanning the health/hunger of players, and freezing players in place for a second or two."),
    SONIC_R_INFO("The Redstone Sonic Screwdriver Upgrade is useful for building redstone contraptions. It will power on redstone by right-clicking, power pistons, lamps, and redstone tracks."),
    SONIC_D_INFO("The Diamond Sonic Screwdriver Upgrade can be used for immediately breaking iron fences, glass, ice & packed ice, and cutting spider web."),
    SONIC_E_INFO("When you right-click the ground with a The Emerald Sonic Screwdriver Upgrade, it tells you the same information as the TARDIS Scanner, but relative to you."),
    SONIC_A_INFO("The Admin Sonic Screwdriver Upgrade allows you to open the TARDIS Admin GUI, scan a player's inventory, or when right-clicking a TARDIS door, see who is inside a TARDIS."),
    LOCATOR_INFO("The TARDIS Locator is a craftable compass that will point the way towards a Time Lord's TARDIS."),
    REMOTE_INFO("The Stattenheim Remote is a craftable item used to summon a Time Lord's TARDIS."),
    L_CIRCUIT_INFO("The Locator Circuit is used in crafting the TARDIS Locator."),
    M_CIRCUIT_INFO("The Materialisation Circuit enables a TARDIS to dematerialize. It is used in crafting a Stattenhiem Circuit, and is a component of the TARDIS Advanced Console."),
    S_CIRCUIT_INFO("The Stattenheim Circuit is used in the crafting of a Stattenhiem Remote. This circuit combines the Materialization Circuit and the Locator Circuit to allow the two to fit in the remote's casing."),
    OSCILLATOR_INFO("The Oscillator Circuit is used in crafting the Sonic Screwdriver."),
    BIO_CIRCUIT_INFO("The Bio-scanner Circuit is used to upgrade the Sonic Screwdriver."),
    R_CIRCUIT_INFO("The Redstone Activator Circuit is used to upgrade the Sonic Screwdriver."),
    RANDOMISER_CIRCUIT_INFO("The Randomiser Circuit is used to generate random Time Travel destinations when placed in the Advanced Console."),
    D_CIRCUIT_INFO("The Diamond Disruptor Circuit is used to upgrade the Sonic Screwdriver."),
    E_CIRCUIT_INFO("The Emerald Environment Circuit is used to upgrade the Sonic Screwdriver."),
    A_CIRCUIT_INFO("The Server Admin Circuit is used to upgrade the Sonic Screwdriver."),
    C_CIRCUIT_INFO("The Chamelon Circuit is used in crafting the Preset Storage Disks and is a component of the TARDIS Advanced Console."),
    I_CIRCUIT_INFO("The Input Circuit is a component of the TARDIS Advanced Console and is required to use the Destination Terminal and the TARDIS Keyboard."),
    IGNITE_CIRCUIT_INFO("The Ignite Circuit upgrades the Sonic Screwdriver so that you can ignite flammable blocks."),
    INVISIBLE_INFO("The Invisibility Circuit is a component of the TARDIS Advanced Console and is required to make the TARDIS invisible."),
    MEMORY_CIRCUIT_INFO("The Memory Circuit is a component of the TARDIS Advanced Console and is required to use the Save Sign."),
    T_CIRCUIT_INFO("The Temporal Circuit is a component of the TARDIS Advanced Console and is required to use the Temporal Locator."),
    ARS_CIRCUIT_INFO("The ARS Circuit is a component of the TARDIS Advanced Console and is required to use the Architectural Reconfiguration System."),
    SCANNER_CIRCUIT_INFO("The Scanner Circuit is a component of the TARDIS Advanced Console and is required to use the TARDIS Scanner and Exterior Rendering room."),
    P_CIRCUIT_INFO("The Perception Circuit is used in crafting the Perception Filter."),
    PAINTER_INFO("The Painter Circuit upgrades the Sonic Screwdriver so that you can change the colour of blocks."),
    FILTER_INFO("The Perception Filter is used to make yourself less noticeable."),
    AREA_DISK("An Area Storage Disk lets you travel to a TARDIS Area. These disks are not craftable, but are stored automatically in the Disk Storage Container for the areas you have permission to travel to."),
    BLANK_INFO("A Blank Storage Disk is a craftable disk that is the base disk for crafting into another sort of TARDIS disk."),
    BIOME_DISK_INFO("A Biome Storage Disk is a craftable disk that lets you travel to specific biomes."),
    PLAYER_DISK_INFO("A Player Storage Disk is a craftable disk that lets you travel to players."),
    PRESET_DISK_INFO("A Preset Storage Disk is a craftable disk that lets you set the TARDIS exterior preset."),
    SAVE_DISK_INFO("A Save Storage Disk is a craftable disk that lets you travel to a saved location."),
    CELL_INFO("An Artron Storage Cell lets you store and transfer Artron Energy."),
    BUDGET("Small, but cosy - good if you don’t like walking too far to the console :)"),
    BIGGER("Big enough to swing a cat - schematic supplied by killeratnight & L0rd Rahl."),
    DELUXE("Luxury in a box - schematic supplied by killeratnight & L0rd Rahl."),
    ELEVENTH("It’s smaller on the outside... - schematic supplied by killeratnight & L0rd Rahl"),
    TWELFTH("The 12th Doctor's TARDIS - schematic supplied by killeratnight."),
    REDSTONE("This one has some redstone in it - schematic supplied by killeratnight & L0rd Rahl."),
    STEAMPUNK("Small, but steamy."),
    PLANK("A wood based interior."),
    TOM("The 4th Doctor's TARDIS."),
    ARS("The Architectural Reconfiguration System TARDIS - schematic supplied by killeratnight & L0rd Rahl."),
    WAR("The War Doctor's TARDIS."),
    PYRAMID("A sandstone pyramid TARDIS - schematic supplied by airomis"),
    MASTER("The Master's TARDIS - schematic supplied by pvpcraft.ca."),
    CUSTOM("A custom designed server TARDIS"),
    ANTIGRAVITY("Going up..."),
    ARBORETUM("A room full of plants."),
    BAKER("A secondary console room based on the 4th Doctor's TARDIS."),
    BEDROOM("A king sized bed for two."),
    EMPTY("Not much in here."),
    FARM("You ned this room to bring farm animals into the TARDIS."),
    GRAVITY("Going down..."),
    GREENHOUSE("Food grows here."),
    HARMONY("The Eye of Harmony from the 8th Doctor's TARDIS."),
    KITCHEN("Take a seat, relax, and have some chow."),
    LIBRARY("Plenty to read in here."),
    MUSHROOM("A small Mycellarium."),
    PASSAGE("To get from here to there."),
    POOL("Grab your togs!"),
    RAIL("You need this room to transport items into the TARDIS with a storgae minecart."),
    RENDERER("This room shows you the environment outside the TARDIS's current location."),
    STABLE("Giddyup, here come the horses..."),
    TRENZALORE("The final resting place of the Doctor."),
    VAULT("Some extra storage space."),
    VILLAGE("A home for the weary trader."),
    WOOD("A secondary console room made of wood."),
    WORKSHOP("Craft, smelt, brew, enchant and repair."),
    ALT_CONTROLS("Please refer to the Advanced Console, the Save Sign,  the Keyboard, the Destination Terminal and the travel commands."),
    MALFUNCTIONS("When the TARDIS travels, there is a chance of a malfunction happening that deposits the TARDIS in an unexpected location."),
    CONSOLE_ARS(" INFO: The Architectural Reconfiguration System allows the player to add and remove rooms in the TARDIS\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update ARS\n"
            + "BLOCK: SIGN"),
    ADVANCED("The TARDIS Advanced Console is the TARDISes control centre, it enables TARDIS Circuits and processes location Storage Disks.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update advanced\n"
            + "BLOCK: JUKEBOX"),
    ARTRON(" INFO: When pressed, it performs various Artron Energy functions.\n"
            + "USAGE: Right-click with the TARDIS key to initialise the Artron Energy Capacitor.\n"
            + "USAGE: Right-click while sneaking to transfer Time Lord energy.\n"
            + "USAGE: Right-click with the full charge item (default: NETHER_STAR) to boost the Artron Energy Capacitor to 100%.\n"
            + "USAGE: Right-click with any item other than the TARDIS key to view the reserve percentage.\n"
            + "UPDATING: /tardis update artron\n"
            + "BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"),
    BACKDOOR(" INFO: The backdoor allows an extra point of entry/exit in the TARDIS\n"
            + "USAGE: Place an IRON_DOOR inside the TARDIS and another IRON_DOOR outside the TARDIS. Right-click to enter/exit.\n"
            + "UPDATING: /tardis update backdoor (on both IRON_DOORs)\n"
            + "BLOCK: IRON_DOOR"),
    BUTTON(" INFO: The button calculates a random destination to travel to based on the coordinate repeaters.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update button\n"
            + "BLOCK: LEVER, STONE_BUTTON or WOODEN_BUTTON"),
    CHAMELEON(" INFO: The Chameleon sign allows the Chameleon Circuit to be turned on or off, and for presets to be selected. The Chameleon Circuit makes the TARDIS exterior become the block it lands on.\n"
            + "USAGE: Right-click the sign.\n"
            + "UPDATING: /tardis update chameleon\n"
            + "BLOCK: SIGN"),
    CONDENSER(" INFO: The condenser chest condenses materials into Artron Energy.\n"
            + "USAGE: Right-click to open the chest. Place items in the chest.\n"
            + "UPDATING: /tardis update condenser\n"
            + "BLOCK: CHEST"),
    CREEPER(" INFO: Updates the position where the charged Creeper spawns.\n"
            + "UPDATING: /tardis update creeper\n"
            + "BLOCK: any"),
    DOOR(" INFO: This is the door used to enter and exit the TARDIS.\n"
            + "USAGE: Right-click with the TARDIS key.\n"
            + "UPDATING: /tardis update door\n"
            + "BLOCK: IRON_DOOR"),
    EPS(" INFO: Updates the block that the Emergency Programme One appears on when enacted.\n"
            + "USAGE: Right-click the block to update\n"
            + "UPDATING: /tardis update EPS\n"
            + "BLOCK: any"),
    CONSOLE_FARM(" INFO: Updates the block that animals spawn relative to when entering the TARDIS.\n"
            + "USAGE: Right-click the block to update.\n"
            + "UPDATING: /tardis update farm\n"
            + "BLOCK: any"),
    HANDBRAKE(" INFO: The Handbrake is used to start the dematerialization and rematerialization of the TARDIS.\n"
            + "USAGE: Right-click to disengage, left-click do engage.\n"
            + "UPDATING: /tardis update handbrake\n"
            + "BLOCK: LEVER"),
    INFO(" INFO: The TARDIS Information System is the TARDIS's computer and information database. It contains information and documentation about the plugin.\n"
            + "USAGE: Right-click. Type the white letter into chat to select the menu item.\n"
            + "UPDATING: /tardis update info\n"
            + "BLOCK: SIGN"),
    KEYBOARD(" INFO: The Keyboard allows the player to type in specific coordinate, the 'home' location, a saved location, a player, a TARDIS area or a biome to materialize to.\n"
            + "USAGE: Right-click with a sign in hand.\n"
            + "USAGE: Line 1: home, save name, player name, area name or biome type.\n"
            + "or\n"
            + "USAGE: Line 1: World.\n"
            + "USAGE: Line 2: x coordinate.\n"
            + "USAGE: Line 3: y coordinate\n"
            + "USAGE: Line 4: z coordinate.\n"
            + "UPDATING:\n"
            + "BLOCK: SIGN"),
    LIGHT(" INFO: When pressed, the lamps in the TARDIS control room are toggled on/off.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update light\n"
            + "BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"),
    TOGGLE(" INFO: When pressed, the black wool blocks behind the TARDIS interior door are toggled AIR/WOOL to provide access to the Vortex.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update toggle_wool\n"
            + "BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"),
    CONSOLE_RAIL(" INFO: Updates the block that storage minecarts spawn on when entering the TARDIS.\n"
            + "USAGE: Attach a minetrack to the front of the TARDIS and have a Minecart with Chest run into the TARDIS door.\n"
            + "UPDATING: /tardis update rail\n"
            + "BLOCK: FENCE"),
    SAVE_SIGN(" INFO: The save sign can be used to quickly set the TARDIS destination. When Right-clicked, it opens up a GUI that allows the player to select a saved location to travel to.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update save-sign\n"
            + "BLOCK: SIGN"),
    SCANNER(" INFO: The scanner will show information for the current location of the TARDIS Police Box, or, if a destination has been set, the next travel location.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update scanner\n"
            + "BLOCK: LEVER, STONE_BUTTON or WOOD_BUTTON"),
    CONSOLE_STABLE(" INFO: Updates the block that horses spawn on when entering the TARDIS.\n"
            + "USAGE: Right-click the block that horses are to spawn on.\n"
            + "UPDATING: /tardis update stable\n"
            + "BLOCK: any"),
    STORAGE("The Disk Storage Container lets you store Storage Disks and Circuits.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update storage\n"
            + "BLOCK: NOTEBLOCK"),
    TERMINAL(" INFO: The Terminal is the GUI alternative to setting TARDIS coordinates.\n"
            + "USAGE: Right-click.\n"
            + "UPDATING: /tardis update terminal\n"
            + "BLOCK: SIGN"),
    TEMPORAL(" INFO: The Temporal Locator sets the time the Time Lord will be in when exiting the TARDIS.\n"
            + "USAGE: Right-click. Select the time of day you want to be in.\n"
            + "UPDATING: /tardis update temporal\n"
            + "BLOCK: SIGN"),
    WORLD_REPEATER(" INFO: The World Repater controls the environment type of the world you will travel to.\n"
            + "USAGE: 1-tick: Select a location within the current world.\n"
            + "USAGE: 2-tick: Select a location from a random (Normal) Overworld.\n"
            + "USAGE: 3-tick: Select a location from The Nether worlds.\n"
            + "USAGE: 4-tick: Select a location from The End worlds.\n"
            + "UPDATING: /tardis update world-repeater\n"
            + "BLOCK: REPEATER"),
    X_REPEATER(" INFO: The X Repeater controls the X coordinate. The more ticks, the greater distance travelled on the X axis.\n"
            + "USAGE: Right-click the repeater to set the ticks.\n"
            + "UPDATING: /tardis update x-repeater\n"
            + "BLOCK: REPEATER"),
    Y_REPEATER(" INFO: The Y Repeater is a multiplier for distance traveled. The more ticks, the greater distance travelled.\n"
            + "USAGE: Right-click the repeater to set the ticks.\n"
            + "UPDATING: /tardis update y-repeater\n"
            + "BLOCK: REPEATER"),
    Z_REPEATER(" INFO: The Z Repeater controls the Z coordinate. The more ticks, the greater distance travelled on the Z axis.\n"
            + "USAGE: Right-click the repeater to set the ticks.\n"
            + "UPDATING: /tardis update z-repeater\n"
            + "BLOCK: REPEATER"),;
    private final String desc;

    private TARDISDescription(String desc) {
        this.desc = desc;
    }

    /**
     * Gets the text of this TARDISDescription
     *
     * @return name of this TARDISDescription
     */
    public String getDesc() {
        return this.desc;
    }
}
