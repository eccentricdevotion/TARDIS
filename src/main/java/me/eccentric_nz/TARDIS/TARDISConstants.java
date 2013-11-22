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
package me.eccentric_nz.TARDIS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.bukkit.ChatColor;

/**
 * One of the key features of a TARDIS is that the interior exists in a
 * dimension different from the exterior. The main application of this concept
 * is that they are bigger on the inside than the outside.
 *
 * @author eccentric_nz
 */
public class TARDISConstants {

    // TARDIS schematics supplied by Lord_Rahl and killeratnight at mcnovus.net
    public static final String SCHEMATIC_ARS = "ars.schematic";
    public static final String SCHEMATIC_BUDGET = "budget.schematic";
    public static final String SCHEMATIC_BIGGER = "bigger.schematic";
    public static final String SCHEMATIC_DELUXE = "deluxe.schematic";
    public static final String SCHEMATIC_ELEVENTH = "eleventh.schematic";
    public static final String SCHEMATIC_REDSTONE = "redstone.schematic";
    public static final String SCHEMATIC_STEAMPUNK = "steampunk.schematic";
    public static final String SCHEMATIC_PLANK = "plank.schematic";
    public static final String SCHEMATIC_TOM = "tom.schematic";
    public static final String SCHEMATIC_CUSTOM = "custom.schematic";
    public static final String SCHEMATIC_ANTIGRAVITY = "antigravity.schematic";
    public static final String SCHEMATIC_ARBORETUM = "arboretum.schematic";
    public static final String SCHEMATIC_BAKER = "baker.schematic";
    public static final String SCHEMATIC_BEDROOM = "bedroom.schematic";
    public static final String SCHEMATIC_EMPTY = "empty.schematic";
    public static final String SCHEMATIC_FARM = "farm.schematic";
    public static final String SCHEMATIC_GRAVITY = "gravity.schematic";
    public static final String SCHEMATIC_GREENHOUSE = "greenhouse.schematic";
    public static final String SCHEMATIC_HARMONY = "harmony.schematic";
    public static final String SCHEMATIC_KITCHEN = "kitchen.schematic";
    public static final String SCHEMATIC_LIBRARY = "library.schematic";
    public static final String SCHEMATIC_MUSHROOM = "mushroom.schematic";
    public static final String SCHEMATIC_PASSAGE = "passage.schematic";
    public static final String SCHEMATIC_POOL = "pool.schematic";
    public static final String SCHEMATIC_RAIL = "rail.schematic";
    public static final String SCHEMATIC_STABLE = "stable.schematic";
    public static final String SCHEMATIC_TRENZALORE = "trenzalore.schematic";
    public static final String SCHEMATIC_VAULT = "vault.schematic";
    public static final String SCHEMATIC_VILLAGE = "village.schematic";
    public static final String SCHEMATIC_WOOD = "wood.schematic";
    public static final String SCHEMATIC_WORKSHOP = "workshop.schematic";
    public static final String CONFIG_FILE_NAME = "config.yml";
    public static final String QUOTES_FILE_NAME = "quotes.txt";
    // chameleon blocks
    public static final List<Integer> CHAMELEON_BLOCKS_VALID = Arrays.asList(new Integer[]{1, 3, 4, 5, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 35, 41, 42, 43, 45, 46, 47, 48, 49, 56, 57, 58, 73, 74, 79, 80, 82, 84, 86, 87, 88, 89, 91, 98, 99, 100, 103, 110, 112, 121, 123, 124, 129, 133, 155, 159, 170, 172, 173});
    public static final List<Integer> CHAMELEON_BLOCKS_BAD = Arrays.asList(new Integer[]{6, 8, 9, 10, 11, 23, 26, 29, 33, 34, 50, 51, 52, 54, 55, 59, 61, 62, 63, 64, 65, 68, 70, 71, 72, 75, 76, 77, 83, 85, 90, 92, 93, 94, 95, 101, 107, 111, 115, 116, 117, 118, 119, 122, 127, 130, 131, 132});
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_ARR = {2, 12, 13, 44, 53, 60, 67, 78, 81, 96, 97, 101, 104, 105, 106, 108, 109, 113, 114, 120, 125, 126, 128, 134, 135, 136, 156};
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_TO_ARR = {3, 24, 1, 43, 5, 3, 4, 80, 24, 5, 1, 20, 103, 86, 18, 45, 1, 112, 112, 121, 5, 5, 24, 5, 5, 5, 155};
    public static final List<Integer> CHAMELEON_BLOCKS_CHANGE = Arrays.asList(new Integer[]{2, 12, 13, 44, 53, 67, 78, 81, 96, 97, 101, 104, 105, 106, 108, 109, 113, 114, 120, 125, 126, 128, 134, 135, 136, 156});
    public static final HashMap<Integer, Integer> CHAMELEON_BLOCKS_CHANGE_HASH = toMap(CHAMELEON_BLOCKS_CHANGE_ARR, CHAMELEON_BLOCKS_CHANGE_TO_ARR);
    public static final List<Integer> CHAMELEON_BLOCKS_NEXT = Arrays.asList(new Integer[]{0, 27, 28, 30, 31, 32, 37, 38, 39, 40, 66});
    public static final List<Integer> CHAMELEON_BLOCKS_PRECIOUS = Arrays.asList(new Integer[]{41, 42, 46, 57, 133, 152, 173});
    // problem blocks for schematics
    public static final List<Integer> PROBLEM_BLOCKS = Arrays.asList(new Integer[]{23, 26, 29, 33, 34, 50, 53, 54, 61, 65, 67, 68, 69, 75, 76, 77, 93, 94, 96, 99, 100, 108, 109, 114, 127, 128, 130, 131, 134, 135, 136, 143, 145, 146, 149, 150, 154, 156, 158, -98, -100, -102, -106, -107, -110, -111, -113, -120, -121, -122, -125, -126});
    // messages
    public static final String INSTRUCTIONS = "Your TARDIS is ready!\nRight-click the TARDIS door with your TARDIS key (by default a STICK) to enter.\nTo time travel, adjust the repeaters on the console. For more help, type " + ChatColor.GOLD + "/TARDIS help timetravel" + ChatColor.RESET + " in chat to see more instructions.";
    public static final String COMMANDS = ChatColor.AQUA + "TARDIS help\n" + ChatColor.RESET + "Type " + ChatColor.GOLD + "/TARDIS help <command>" + ChatColor.RESET + " to see more details about a command.\nType " + ChatColor.GOLD + "/TARDIS help create|delete|timetravel" + ChatColor.RESET + " for instructions on creating and removing a TARDIS and how to time travel.\nCommands\n" + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + " - list saved time travel destinations, TARDIS companions, Artron Energy Rechargers or admin defined areas.\n" + ChatColor.GOLD + "/TARDIS save [name]" + ChatColor.RESET + " - save the current location of the Police Box.\n" + ChatColor.GOLD + "/TARDIS removesave [name]" + ChatColor.RESET + " - delete a saved destination.\n" + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + " - show the co-ordinates of a lost TARDIS.\n" + ChatColor.GOLD + "/TARDIS add" + ChatColor.RESET + " - add a TARDIS companion.\n" + ChatColor.GOLD + "/TARDIS remove" + ChatColor.RESET + " - remove a TARDIS companion.\n" + ChatColor.GOLD + "/TARDIS update" + ChatColor.RESET + " - update the special block positions in a modified TARDIS interior.\n" + ChatColor.GOLD + "/TARDIS bind" + ChatColor.RESET + " - bind saved locations and some commands to blocks in the TARDIS interior.\n" + ChatColor.GOLD + "/tardistravel" + ChatColor.RESET + " - set the time travel destination to co-ordinates, a player's location, a saved destination, or to an admin defined area.\n" + ChatColor.GOLD + "/TARDIS rebuild" + ChatColor.RESET + " - rebuild a busted TARDIS Police Box.\n" + ChatColor.GOLD + "/TARDIS chameleon" + ChatColor.RESET + " - turn the Chameleon Circuit on or off.\n" + ChatColor.GOLD + "/tardisprefs sfx" + ChatColor.RESET + " - turn TARDIS sound effects on or off.\n" + ChatColor.GOLD + "/tardisprefs platform" + ChatColor.RESET + " - turn the TARDIS safety platform on or off.\n" + ChatColor.GOLD + "/tardisprefs quotes" + ChatColor.RESET + " - turn TARDIS quotes on or off.\n" + ChatColor.GOLD + "/TARDIS setdest" + ChatColor.RESET + " - save the block you are looking at as a destination.\n" + ChatColor.GOLD + "/TARDIS home" + ChatColor.RESET + " - change saved TARDIS home location to the block you are looking at.\n" + ChatColor.GOLD + "/TARDIS hide" + ChatColor.RESET + " - hide the TARDIS police box - use /tardis rebuild to bring it back.\n" + ChatColor.GOLD + "/TARDIS direction" + ChatColor.RESET + " - change the direction the TARDIS police box is facing.\n" + ChatColor.GOLD + "/TARDIS comehere" + ChatColor.RESET + " - Make the TARDIS come to the block you are looking at.\n" + ChatColor.GOLD + "/TARDIS namekey" + ChatColor.RESET + " - rename the TARDIS key.\n" + ChatColor.GOLD + "/TARDIS version" + ChatColor.RESET + " - check the version of the TARDIS plugin and CraftBukkit on the server.\n" + ChatColor.GOLD + "/TARDIS room [type]" + ChatColor.RESET + " - Add rooms to the TARDIS.";
    public static final String COMMAND_CREATE = ChatColor.AQUA + "Creating a TARDIS\n" + ChatColor.RESET + "You create a TARDIS by placing a " + ChatColor.GOLD + "specific pattern of blocks." + ChatColor.RESET + "\nYou will need to have an IRON, GOLD, REDSTONE, EMERALD or DIAMOND BLOCK, a LAPIS BLOCK, and a redstone torch in your inventory.\nYou place the blocks where you want the TARDIS to be, in the following order:\nBottom - IRON, GOLD, REDSTONE, EMERALD or DIAMOND BLOCK, middle - LAPIS BLOCK, top - REDSTONE TORCH\nThe TARDIS takes up a 3 x 3 x 4 area (w x d x h), so keep this in mind.\nTo enter the TARDIS, right-click the door with your TARDIS key (by default a STICK).";
    public static final String COMMAND_DELETE = ChatColor.AQUA + "Removing a TARDIS\n" + ChatColor.RESET + "To remove your TARDIS, " + ChatColor.GOLD + "break the 'POLICE BOX' wall sign" + ChatColor.RESET + " on the front of the TARDIS.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " You will lose any items you have stored in your TARDIS chest, and any saved time travel destinations.";
    public static final String COMMAND_TIMETRAVEL = ChatColor.AQUA + "Time travelling in the TARDIS\n" + ChatColor.RESET + "You can time travel in the TARDIS by changing the delay settings of the redstone repeaters on the TARDIS console.\nThe repeater closest to the door controls the kind of world you will travel to - the 1-tick delay setting selects a random world type, the 2 tick delay setting selects NORMAL worlds,\nthe 3-tick setting selects NETHER worlds, and the 4-tick setting selects THE END worlds. After changing the repeater settings, you then click the stone button at the rear of the TARDIS console.\nWhen exiting the TARDIS (right-click the door with your TARDIS key - by default a STICK) you will time travel to a random destination.";
    public static final String COMMAND_LIST = ChatColor.AQUA + "TARDIS Lists\n" + ChatColor.RESET + "Type " + ChatColor.GOLD + "/TARDIS list saves" + ChatColor.RESET + "\nto list the destinations saved in the TARDIS console.\nType " + ChatColor.GOLD + "/TARDIS list companions" + ChatColor.RESET + "\nto list players who you have added as TARDIS companions\nType " + ChatColor.GOLD + "/TARDIS list areas" + ChatColor.RESET + "\nto list admin defined TARDIS areas (like landing pads and airports)\nType " + ChatColor.GOLD + "/TARDIS list rechargers" + ChatColor.RESET + "\nto list Artron Energy Rechargers";
    public static final String COMMAND_SAVE = ChatColor.AQUA + "Saving time travel destinations\n" + ChatColor.RESET + "To save the current TARDIS destination (where the Police Box is located), type\n" + ChatColor.GOLD + "/TARDIS save [name]" + ChatColor.RESET + "\nWhere [name] is a what you want to call the destination.";
    public static final String COMMAND_REMOVESAVE = ChatColor.AQUA + "Deleting time travel destinations\n" + ChatColor.RESET + "To delete a destination, type\n" + ChatColor.GOLD + "/TARDIS removesave [name]" + ChatColor.RESET + "\nWhere [name] is the destination to delete.";
    public static final String COMMAND_SETDEST = ChatColor.AQUA + "Setting destinations\n" + ChatColor.RESET + "To set and save a TARDIS destination to the block you are looking at, type\n" + ChatColor.GOLD + "/TARDIS setdest [name]" + ChatColor.RESET + "\nWhere [name] is a what you want to call the destination.\nUse the /tardistravel dest [name] command to travel to the specified destination.";
    public static final String COMMAND_FIND = ChatColor.AQUA + "Finding the TARDIS\n" + ChatColor.RESET + "Simply type " + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + "\nTo display the world name and x, y, z co-ordinates of the last saved location of your TARDIS.";
    public static final String COMMAND_TRAVEL = ChatColor.AQUA + "Travel commands\n" + ChatColor.RESET + "You can set your next destination using the command: " + ChatColor.GOLD + "/tardistravel" + ChatColor.RESET + "\nTo travel to specfic co-ordinates, type: " + ChatColor.GOLD + "/tardistravel [world] [x] [y] [z]" + ChatColor.RESET + "\nTo travel to a players location, type: " + ChatColor.GOLD + "/tardistravel [player]" + ChatColor.RESET + "\nTo travel to the 'home' location, type: " + ChatColor.GOLD + "/tardistravel home" + ChatColor.RESET + "\nYou must run these commands from inside the TARDIS.\nTo travel to a saved destination, type: " + ChatColor.GOLD + "/tardistravel dest [name]" + ChatColor.RESET + "\nYou must run these commands from inside the TARDIS.\nYou must run these commands from inside the TARDIS.";
    public static final String COMMAND_ADD = ChatColor.AQUA + "Adding companions\n" + ChatColor.RESET + "To allow other players to travel with you in the TARDIS you need to add them to your companions list\nTo do this type: " + ChatColor.GOLD + "/TARDIS add [player]" + ChatColor.RESET + "\nTo remove a companion from the list, type: " + ChatColor.GOLD + "/TARDIS remove [player]" + ChatColor.RESET;
    public static final String COMMAND_UPDATE = ChatColor.AQUA + "Modifying the TARDIS interior\n" + ChatColor.RESET + "To update the position of special blocks in the TARDIS\nie. the door, button and repeaters. First move the block, then type:\n" + ChatColor.GOLD + "/TARDIS update [door|button|world-repeater|x-repeater|z-repeater|y-repeater|chameleon|save-sign]" + ChatColor.RESET + "\nYou will be asked to click the block in its new position\n'world-repeater' is the one closest to the door\nWith your back to the door the 'x-repeater' is the one on the right\nWith your back to the door the 'z-repeater' is the one on the left\n'y-repeater' is the one at the back above the button";
    public static final String COMMAND_REBUILD = ChatColor.AQUA + "Rebuilding the TARDIS\n" + ChatColor.RESET + "Sometimes you may need to rebuild the TARDIS Police Box\nfor example if the server crashes. To rebuild it, type:\n" + ChatColor.GOLD + "/TARDIS rebuild" + ChatColor.RESET + "\nThe plugin will retrieve the last saved location and rebuild the TARDIS in that spot";
    public static final String COMMAND_CHAMELEON = ChatColor.AQUA + "The TARDIS Chameleon Circuit\n" + ChatColor.RESET + "You can make the TARDIS Police Box blend in with its surroundings by turning on the Chameleon Circuit.\nTo do this by command, type:\n" + ChatColor.GOLD + "/TARDIS chameleon [on|off]" + ChatColor.RESET + "\nOtherwise just right-click the Chameleon Circuit sign to toggle it on or off\nIf you created your TARDIS using a previous verion of the plugin, you can also just place a sign anywhere, and use the " + ChatColor.GREEN + "/tardis update chameleon" + ChatColor.RESET + " command to add it to the TARDIS database.";
    public static final String COMMAND_SFX = ChatColor.AQUA + "TARDIS sound effects\n" + ChatColor.RESET + "By default, TARDIS sound effects are enabled and play while you are inside the TARDIS\nYou can toggle the sounds on or off by typing:\n" + ChatColor.GOLD + "/tardisprefs sfx [on|off]\n" + ChatColor.RESET + "If the admin has disabled sound effects, this command will have no effect.";
    public static final String COMMAND_PLATFORM = ChatColor.AQUA + "The TARDIS safety platform\n" + ChatColor.RESET + "By default, TARDIS safety platforms are disabled. If the admin has enabled safety platforms, you can toggle the platform on or off by typing:\n" + ChatColor.GOLD + "/tardisprefs platform [on|off]\n" + ChatColor.RESET + "The change will take effect next time you time travel.";
    public static final String COMMAND_HOME = ChatColor.AQUA + "Change the TARDIS home location\n" + ChatColor.RESET + "To change the location the TARDIS calls home, look at a block and type:\n" + ChatColor.GOLD + "/TARDIS home";
    public static final String COMMAND_HIDE = ChatColor.AQUA + "Hide the TARDIS\n" + ChatColor.RESET + "To temporarily hide the TARDIS type:\n" + ChatColor.GOLD + "/TARDIS hide\n" + ChatColor.RESET + "To bring it back, type /tardis rebuild";
    public static final String COMMAND_ROOM = ChatColor.AQUA + "Add rooms to the TARDIS\n" + ChatColor.RESET + "To expand the TARDIS interior, you can add rooms.\nTo do this you first need to add a passage way, then the room type that you require.\nTo add rooms type:\n" + ChatColor.GOLD + "/TARDIS room [room type]\n" + ChatColor.RESET + "The room types are: passage, arboretum, bedroom, kitchen, library, pool, vault and empty.\nYou then need to seed the growing of the room by placing the appropriate seed block for the room where you want the door to the room to be.\nAfter clicking the seed block with the TARDIS key, the room will be grown.";
    public static final String COMMAND_VERSION = ChatColor.AQUA + "Show the version of TARDIS and CraftBukkit you are running\n" + ChatColor.GOLD + "/TARDIS version";
    public static final String COMMAND_ADMIN = ChatColor.AQUA + "TARDIS admin commands\n" + ChatColor.RESET + "Arguments\n" + ChatColor.GOLD + "/tardisadmin key [Material]" + ChatColor.RESET + " - set the Material used as the TARDIS key. Default: STICK.\n" + ChatColor.GOLD + "/tardisadmin tp_radius [x]" + ChatColor.RESET + " - set the maximum distance (in blocks) you can time travel in the TARDIS. Default: 256\n" + ChatColor.GOLD + "/tardisadmin default_world [true|false]" + ChatColor.RESET + " - set whether the (inner) TARDIS is created in a specific world. Default: false\n" + ChatColor.GOLD + "/tardisadmin default_world_name [world]" + ChatColor.RESET + " - set the default world the (inner) TARDIS is created in.\n" + ChatColor.GOLD + "/tardisadmin include_default_world [true|false]" + ChatColor.RESET + " - set whether the default world is included in random time travel destinations. Default: false\n" + ChatColor.GOLD + "/tardisadmin exclude [world]" + ChatColor.RESET + " - excludes the specified world from random time travel destinations.\n" + ChatColor.GOLD + "/tardisadmin platform [true|false]" + ChatColor.RESET + " - set whether a platform is built outside the Police Box door (preventing the player falling). Default: false, but if true, a player can also set their own preference to false if they so wish.\n" + ChatColor.GOLD + "/tardisadmin sfx [true|false]" + ChatColor.RESET + " - set whether a sound effects play inside the TARDIS. Default: true, but a player can also set their own preference if they wish.\n" + ChatColor.GOLD + "/tardisadmin use_worldguard [true|false]" + ChatColor.RESET + " - set whether WorldGuard should be used to protect the inner TARDIS. Default: true.\n" + ChatColor.GOLD + "/tardisadmin respect_worldguard [true|false]" + ChatColor.RESET + " - set whether travelling respects WorldGuard regions created by the WorldGuard other plugins. Default: true.\n" + ChatColor.GOLD + "/tardisadmin nether [true|false]" + ChatColor.RESET + " - set whether players can time travel to the NETHER. Default: false.\n" + ChatColor.GOLD + "/tardisadmin the_end [true|false]" + ChatColor.RESET + " - set whether players can time travel to THE END. Default: false.\n" + ChatColor.GOLD + "/tardisadmin land_on_water [true|false]" + ChatColor.RESET + " - set whether the TARDIS will land on water in NORMAL worlds. Default: true.\n" + ChatColor.GOLD + "/tardisadmin name_tardis [true|false]" + ChatColor.RESET + " - set whether the Police Box will will have the player's name on it. Default: false.\nTo set an Artron Energy Recharger to the beacon you are looking at, type\n" + ChatColor.GOLD + "/tardisadmin recharger [name]" + ChatColor.RESET + "\nWhere [name] is a what you want to call the recharger.";
    public static final String COMMAND_ARTRON = "Artron Energy\nThe wood button on the TARDIS console controls the Artron Energy Capacitor.\nIf the button is right-clicked, then the Artron levels are updated.\nClicking with a Nether Star puts the capacitor to maximum.\nClicking with the TARDIS key initialises the capacitor by spawning a charged creeper inside it and sets the level to 50%.\nClicking while sneaking transfers player Artron Energy into the capacitor.\nIf the button is just right-clicked, it displays the current capacitor level as percentage of full.";
    public static final String COMMAND_BIND = "Binding saved locations to blocks\nTo bind a saved location, the home location or set a block to hide or rebuild the TARDIS Police Box, use the " + ChatColor.GOLD + "/tardisbind [bind type] [what_to_bind]" + ChatColor.RESET + " command .\nFirst place the block where you want it to be, then type the command.\nYou will be asked to click the block so that the TARDIS matrix knows of its location.\nTo bind a saved location - /tardisbind save [name] - use the saved destination name (as it appears in the /tardis list saves command) as the second argument.\nTo bind a command - /tardisbind cmd [command] - use either 'hide', 'rebuild' or 'home'.\nTo bind a player - /tardisbind player [name] - use the player's name.\nTo bind a TARDIS area - /tardisbind area [name] - use the area name (as it appears in the /tardis list areas command).\nTo remove a bound block, use the command: " + ChatColor.GOLD + "/tardisbind remove [name]";
    public static final String COMMAND_AREA = "TARDIS area commands\n" + ChatColor.GOLD + "/tardisarea start [name]" + ChatColor.RESET + " - type this to define the starting corner of a preset admin area.\n" + ChatColor.GOLD + "/tardisarea end" + ChatColor.RESET + " - type this to define the ending corner of a preset admin area.\n" + ChatColor.GOLD + "/tardisarea show [name]" + ChatColor.RESET + " - type this to show the corners of the admin area.\n" + ChatColor.GOLD + "/tardisarea remove [name]" + ChatColor.RESET + " - type this to delete a preset admin area.";
    public static final String NO_PERMS_MESSAGE = "You do not have permission to do that!";
    public static final String NOT_OWNER = "You are not the Timelord or Companion for this TARDIS!";
    public static final String NO_TARDIS = "You have not created a TARDIS yet!";
    public static final String TIMELORD_OFFLINE = "The Timelord who owns this TARDIS is offline!";
    public static final String TIMELORD_NOT_IN = "The Timelord who owns this TARDIS is not inside!";
    public static final List<String> vowels = Arrays.asList(new String[]{"A", "E", "I", "O", "U"});

    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if (c != null && string != null) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException ex) {
                TARDIS.plugin.debug(ex.getMessage());
            }
        }
        return null;
    }

    public static CMDS fromString(String name) {
        return getEnumFromString(CMDS.class, name);
    }

    /**
     * Creates a HashMap from two arrays. The resulting map is used by the
     * chameleon circuit to change unsuitable blocks into more aesthetically
     * pleasing or robust ones i.e. GRASS -> DIRT, SAND -> SANDSTONE
     *
     * @param keys an array of block types to change
     * @param values an array of block types to change to
     * @return the combined arrays as a HashMap
     */
    public static HashMap toMap(Object[] keys, Object[] values) {
        int keysSize = (keys != null) ? keys.length : 0;
        int valuesSize = (values != null) ? values.length : 0;
        if (keysSize == 0 && valuesSize == 0) {
            return new HashMap();
        }
        if (keysSize != valuesSize) {
            throw new IllegalArgumentException("The number of keys doesn't match the number of values.");
        }
        HashMap map = new HashMap();
        for (int i = 0; i < keysSize; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    public enum SCHEMATIC {

        ANTIGRAVITY, ARBORETUM, ARS, BAKER, BEDROOM, BIGGER, BUDGET, CUSTOM, DELUXE, ELEVENTH, EMPTY, FARM, GRAVITY, GREENHOUSE, HARMONY, KITCHEN, LIBRARY, MUSHROOM, PASSAGE, PLANK, POOL, RAIL, REDSTONE, STABLE, STEAMPUNK, TOM, TRENZALORE, VAULT, VILLAGE, WOOD, WORKSHOP;
    }

    public enum COMPASS {

        EAST, SOUTH, WEST, NORTH;
    }

    public enum ROOM {

        ANTIGRAVITY, ARBORETUM, BAKER, BEDROOM, EMPTY, FARM, GRAVITY, GREENHOUSE, HARMONY, KITCHEN, LIBRARY, MUSHROOM, PASSAGE, POOL, RAIL, STABLE, TRENZALORE, VAULT, VILLAGE, WOOD, WORKSHOP;
    }

    public enum CMDS {

        CREATE, DELETE, TIMETRAVEL, LIST, REMOVESAVE, SAVE, FIND, ADD, ADMIN, UPDATE, TRAVEL, REBUILD, CHAMELEON, SFX, PLATFORM, SETDEST, HOME, HIDE, AREA, VERSION, ROOM, ARTRON, BIND;
    }

    public enum PRESET {

        ANGEL, CAKE, CHALICE, DESERT, FACTORY, FLOWER, GRAVESTONE, JUNGLE, NETHER, NEW, OLD, PARTY, SHROOM, STONE, SUBMERGED, SWAMP, TELEPHONE, THEEND, TOPSYTURVEY, VILLAGE, WELL, WINDMILL, YELLOW, CUSTOM
    }

    private TARDISConstants() {
    }
}
