/*
 * Copyright (C) 2013 eccentric_nz
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
    SONIC_INFO("The Sonic Screwdriver comes in 5 diffrent flavours, and each has its own special uses. Check out the Sonic Screwdriver types for further information."),
    SONIC_Q_INFO("The Quartz Sonic Screwdriver is the base model, and has no specific functions. It will opening doors at a distance, deadlocking doors, and press buttons & levers at a distance."),
    SONIC_R_INFO("The Redstone Sonic Screwdriver is useful for building redstone contraptions. It will power on redstone by right clicking, power pistons, dispensers, hoppers, droppers, redstone tracks, and unlock deadlocked doors."),
    SONIC_D_INFO("The Diamond Sonic Screwdriver can be used for immediately breaking iron fences, breaking Glass, cutting Spider web, igniting blocks, and remote detonation of TNT."),
    SONIC_E_INFO("When you right-click the ground with a The Emerald Sonic Screwdriver, it tells you the same information as the TARDIS Scanner, but relative to you. You can also scan the health/hunger of players, and freeze players in place for a second or two."),
    SONIC_A_INFO("The Admin Sonic Screwdriver allows you to scan a player's inventory, scan a player's level, or when right-clicking a TARDIS sign, see who is inside a TARDIS."),
    LOCATOR_INFO("The TARDIS Locator is a craftable compass that will point the way towards a Time Lord's TARDIS."),
    REMOTE_INFO("The Stattenheim Remote is a craftable item used to summon a Time Lord's TARDIS."),
    L_CIRCUIT_INFO("The Locator Circuit is used in crafting the TARDIS Locator."),
    M_CIRCUIT_INFO("The Materialisation Circuit enables a TARDIS to dematerialize. It is used in crafting a Stattenhiem Circuit."),
    S_CIRCUIT_INFO("The Stattenheim Circuit is used in the crafting of a Stattenhiem Remote. This circuit combines the Materialization Circuit and the Locator Circuit to allow the two to fit in the remote's casing."),
    BUDGET("Small, but cosy - good if you don’t like walking too far to the console :)"),
    BIGGER("Big enough to swing a cat."),
    DELUXE("Luxury in a box."),
    ELEVENTH("It’s smaller on the outside..."),
    REDSTONE("Multi-level madness."),
    STEAMPUNK("Small, but steamy."),
    PLANK("A wood based interior."),
    TOM("The 4th Doctor's TARDIS."),
    ARS("The Architectural Reconfiguration System TARDIS."),
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
    STABLE("Giddyup, here come the horses..."),
    TRENZALORE("The final resting place of the Doctor."),
    VAULT("Some extra storage space."),
    WOOD("A secondary console room made of wood."),
    WORKSHOP("Craft, smelt, brew, enchant and repair."),
    MALFUNCTIONS("When the TARDIS travels, there is a chance of a malfunctioning happening that deposits the TARDIS in an unexpected location."),
    KEYBOARD("You can use the TARDIS keyboard to set destinations\n"
            + "\n" 
            + "To use it: \n" 
            + "1. Place a sign in the TARDIS console room\n" 
            + "2. Use the command /tardis update keyboard, then click the sign\n" 
            + "3. Right-click the keyboard with another sign \n" 
            + "4. "),
    SAVE_SIGN("The save sign can be used to quickly set the TARDIS destination. When right clicked, it opens up a GUI that allows the player to select a saved location to travel to."),
    DOOR("This is the door used to enter and exit the TARDIS.\n" + 
            "Updating: /tardis update door\n" + 
            "Right click an IRON_DOOR"),
    BUTTON("This button is used to select a random destination.\n"
            + "Updating: /tarids update button\n"
            + "Right click a STONE_BUTTON or WOOD_BUTTON"),
    WORLD_REPEATER("The World Repater controls the enviroment type of the world you will travel to.\n"
            + "The 1-tick positionwill select a world with a random enviroment.\n"
            + "The 2-tick position will only select (Normal) Overworlds.\n"
            + "The 3-tick position will select from Nether Worlds.\n"
            + "The 4-tick position will select from The End Worlds.\n"
            + "Updating: /tardis update world-repeater\n"
            + "Right click a REPEATER"),
    X_REPEATER("The X Repeater controls the X coordinate. The more ticks, the greater distance travelled on the X axis.\n"
            + "Updating: /tardis update x-repeater\n"
            + "Right click a REPEATER"),
    Y_REPEATER("The Y Repeater controls the Y coordinate. The more ticks, the greater distance travelled on teh Y axis.\n"
            + "Updating: /tardis update y-repeater\n"
            + "Right click a REPEATER"),
    Z_REPEATER("The Z Repeater controls the Z coordinate. The more ticks, the greater distance travelled on teh Z axis.\n"
            + "Updating: /tardis update Z-repeater\n"
            + "Right click a REPEATER"),
    CHAMELEON("The Chameleon sign allows the Chameleon Circuit to be turned on or off. The Chameleon Circuit makes the TARDIS exterior become the block it lands on."),
    ARTRON(""),
    HANDBRAKE("This Handbrake is used to start the dematerialization and rematerialization of the TARDIS.\n"
            + "Usage: Flip the handbrake to make the TARDIS travel.\n"
            + "Updating: /tardis update lever"
            + "Right click a LEVER"),
    CONDENSER(""),
    SCANNER(""),
    BACKDOOR(""),
    CREEPER(""),
    EPS(""),
    BACK(""),
    TERMINAL(""),
    CONSOLE_ARS(""),
    TEMPORAL(""),
    LIGHT(""),
    CONSOLE_FARM(""),
    CONSOLE_STABLE(""),
    CONSOLE_RAIL(""),
    INFO("The TARDIS Information System is a computer that contains information and instructions on controlling the TARDIS and using the TARDIS plugin.\n"
            + "Usage: Right click the sign.\n"
            + "Updating: /tardis update info\n"
            + "Right click a SIGN");
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
