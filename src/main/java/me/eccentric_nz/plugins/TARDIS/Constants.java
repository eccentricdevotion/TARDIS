package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Constants {

    public static TARDISdatabase service = TARDISdatabase.getInstance();
    public static String MY_PLUGIN_NAME;
    public static String TARDIS_KEY;
    public static final String SCHEMATIC_FILE_NAME = "schematic.csv";
    public static final String CONFIG_FILE_NAME = "config.yml";
    public static final String TIMELORDS_FILE_NAME = "timelords.yml";
    public static final String QUOTES_FILE_NAME = "quotes.txt";
    // chameleon blocks
    private static final Integer[] CHAMELEON_BLOCKS_VALID_ARR = new Integer[] {1, 3, 4, 5, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 35, 41, 42, 43, 45, 47, 48, 49, 56, 57, 73, 74, 79, 82, 86, 87, 88, 89, 91, 98, 99, 100, 103, 110, 112, 121, 123, 124, 129, 133};
    public static List<Integer> CHAMELEON_BLOCKS_VALID = Arrays.asList(CHAMELEON_BLOCKS_VALID_ARR);
    private static final Integer[] CHAMELEON_BLOCKS_BAD_ARR = new Integer[] {6, 8, 9, 10, 11, 23, 25, 26, 29, 33, 34, 50, 51, 52, 54, 55, 58, 59, 60, 61, 62, 63, 64, 65, 68, 70, 71, 72, 75, 76, 77, 83, 84, 85, 90, 92, 93, 94, 95, 101, 107, 111, 115, 116, 117, 118, 119, 122, 127, 130, 131, 132};
    public static List<Integer> CHAMELEON_BLOCKS_BAD = Arrays.asList(CHAMELEON_BLOCKS_BAD_ARR);
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_ARR = {2, 12, 13, 44, 53, 67, 78, 81, 96, 97, 101, 104, 105, 106, 108, 109, 113, 114, 120, 125, 126, 128, 134, 135, 136};
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_TO_ARR = {3, 24, 1, 43, 5, 4, 80, 24, 5, 1, 20, 103, 86, 18, 45, 1, 112, 112, 121, 5, 5, 24, 5, 5, 5};
    public static List<Integer> CHAMELEON_BLOCKS_CHANGE = Arrays.asList(CHAMELEON_BLOCKS_CHANGE_ARR);
    public static HashMap<Integer, Integer> CHAMELEON_BLOCKS_CHANGE_HASH = toMap(CHAMELEON_BLOCKS_CHANGE_ARR,CHAMELEON_BLOCKS_CHANGE_TO_ARR);
    private static final Integer[] CHAMELEON_BLOCKS_NEXT_ARR = {0, 27, 28, 30, 31, 32, 37, 38, 39, 40, 66};
    public static List<Integer> CHAMELEON_BLOCKS_NEXT = Arrays.asList(CHAMELEON_BLOCKS_NEXT_ARR);
    // messages
    public static final String INSTRUCTIONS = "Your TARDIS is ready!\nRight-click the TARDIS door with your TARDIS key (by default a redstone torch) to enter.\nTo time travel, adjust the repeaters on the console. For more help, type " + ChatColor.GOLD + "/TARDIS help timetravel" + ChatColor.RESET + " in chat to see more instructions.";
    public static final String COMMANDS = ChatColor.AQUA + "TARDIS help\n" + ChatColor.RESET + "Type " + ChatColor.GOLD + "/TARDIS help <command>" + ChatColor.RESET + " to see more details about a command.\nType " + ChatColor.GOLD + "/TARDIS help create|delete|timetravel" + ChatColor.RESET + " for instructions on creating and removing a TARDIS and how to time travel.\nCommands\n" + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + " - list saved time travel destinations or TARDIS companions.\nThere are 4 save slots, one of which is reserved for the 'home' destination.\n" + ChatColor.GOLD + "/TARDIS save [slot number] [name]" + ChatColor.RESET + " - save the co-ordinates of a destintation to the specified slot.\n" + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + " - show the co-ordinates of a lost TARDIS.\n" + ChatColor.GOLD + "/TARDIS add" + ChatColor.RESET + " - add a TARDIS companion.\n" + ChatColor.GOLD + "/TARDIS remove" + ChatColor.RESET + " - remove a TARDIS companion.\n" + ChatColor.GOLD + "/TARDIS update" + ChatColor.RESET + " - update the special block positions in a modified TARDIS interior.\n" + ChatColor.GOLD + "/TARDIS travel" + ChatColor.RESET + " - set the time travel destination to co-ordinates or to a player's location.\n" + ChatColor.GOLD + "/TARDIS rebuild" + ChatColor.RESET + " - rebuild a busted TARDIS Police Box.\n" + ChatColor.GOLD + "/TARDIS chameleon" + ChatColor.RESET + " - turn the Chameleon Circuit on or off, or add one if you haven't got one.\n" + ChatColor.GOLD + "/TARDIS sfx" + ChatColor.RESET + " - turn TARDIS sound effects on or off.\n" + ChatColor.GOLD + "/TARDIS platform" + ChatColor.RESET + " - turn the TARDIS safety platform on or off.";

    public enum COMPASS {

        NORTH, EAST, SOUTH, WEST;
    }

    public enum CMDS {

        CREATE, DELETE, TIMETRAVEL, LIST, SAVE, FIND, ADD, ADMIN, UPDATE, TRAVEL, REBUILD, CHAMELEON, SFX, PLATFORM;
    }
    public static final String COMMAND_CREATE = ChatColor.AQUA + "Creating a TARDIS\n" + ChatColor.RESET + "You create a TARDIS by placing a " + ChatColor.GOLD + "specific pattern of blocks." + ChatColor.RESET + "\nYou will need to have an IRON BLOCK, a LAPIS BLOCK, and a redstone torch in your inventory.\nYou place the blocks where you want the TARDIS to be, in the following order:\nBottom - IRON BLOCK, middle - LAPIS BLOCK, top - REDSTONE TORCH\nThe TARDIS takes up a 3 x 3 x 4 area (w x d x h), so keep this in mind.\nTo enter the TARDIS, right-click the door with your TARDIS key (by default a redstone torch).";
    public static final String COMMAND_DELETE = ChatColor.AQUA + "Removing a TARDIS\n" + ChatColor.RESET + "To remove your TARDIS, " + ChatColor.GOLD + "break the 'POLICE BOX' wall sign" + ChatColor.RESET + " on the front of the TARDIS.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " You will lose any items you have stored in your TARDIS chest, and any saved time travel destinations.";
    public static final String COMMAND_TIMETRAVEL = ChatColor.AQUA + "Time travelling in the TARDIS\n" + ChatColor.RESET + "You can time travel in the TARDIS by changing the delay settings of the redstone repeaters on the TARDIS console.\nThe repeater closest to the door controls the saved time travel destinations - the 1-tick delay setting holds the 'Home' destination (where the TARDIS was first created), and the 2-4 tick delay settings are slots that you can save destinations to.\nTo travel to saved destinations all the other repeaters must be set to the 1-tick delay setting. You then select the saved slot as desired, then click the stone button at the rear of the TARDIS console.\nTo travel to a random destination, set any of the other repeaters to a 2-4 tick delay, then click the stone button at the rear of the TARDIS console.\nWhen exiting the TARDIS (right-click the door with your TARDIS key - by default a redstone torch) you will time travel to the destination of choice.";
    public static final String COMMAND_LIST = ChatColor.AQUA + "TARDIS Lists\n" + ChatColor.RESET + "Type " + ChatColor.GOLD + "/TARDIS list saves" + ChatColor.RESET + "\nto list the destinations saved in the TARDIS console.\nType " + ChatColor.GOLD + "/TARDIS list companions" + ChatColor.RESET + "\nto list players who you have added as TARDIS companions";
    public static final String COMMAND_SAVE = ChatColor.AQUA + "Saving time travel destinations\n" + ChatColor.RESET + "To save the current TARDIS destination, type\n" + ChatColor.GOLD + "/TARDIS save [slot number] [name]" + ChatColor.RESET + "\nWhere [slot number] is a number from 1 to 3 and [name] is a what you want to call the destination.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " Specifying a slot number that already has a saved destination will cause that destination to be overwritten with the new one.";
    public static final String COMMAND_FIND = ChatColor.AQUA + "Finding the TARDIS\n" + ChatColor.RESET + "Simply type " + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + "\nTo display the world name and x, y, z co-ordinates of the last saved location of your TARDIS.";
    public static final String COMMAND_TRAVEL = ChatColor.AQUA + "Travel commands\n" + ChatColor.RESET + "You can set your next destination using the command: " + ChatColor.GOLD + "/TARDIS travel" + ChatColor.RESET + "\nTo travel to specfic co-ordinates, type: " + ChatColor.GOLD + "/TARDIS travel [world] [x] [y] [z]" + ChatColor.RESET + "\nTo travel to a players location, type: " + ChatColor.GOLD + "/TARDIS travel [player]" + ChatColor.RESET + "\nYou must run these commands from inside the TARDIS.";
    public static final String COMMAND_ADD = ChatColor.AQUA + "Adding companions\n" + ChatColor.RESET + "To allow other players to travel with you in the TARDIS you need to add them to your companions list\nTo do this type: " + ChatColor.GOLD + "/TARDIS add [player]" + ChatColor.RESET + "\nTo remove a companion from the list, type: " + ChatColor.GOLD + "/TARDIS remove [player]" + ChatColor.RESET;
    public static final String COMMAND_UPDATE = ChatColor.AQUA + "Modifying the TARDIS interior\n" + ChatColor.RESET + "To update the position of special blocks in the TARDIS\nie. the door, button and repeaters. First move the block, then type:\n" + ChatColor.GOLD + "/TARDIS update [door|button|save-repeater|x-repeater|z-repeater|y-repeater]" + ChatColor.RESET + "\nYou will be asked to click the block in its new position\n'save-repeater' is the one closest to the door\nWith your back to the door the 'x-repeater' is the one on the right\nWith your back to the door the 'z-repeater' is the one on the left\n'y-repeater' is the one at the back above the button";
    public static final String COMMAND_REBUILD = ChatColor.AQUA + "Rebuilding the TARDIS\n" + ChatColor.RESET + "Sometimes you may need to rebuild the TARDIS Police Box\nfor example if the server crashes. To rebuild it, type:\n" + ChatColor.GOLD + "/TARDIS rebuild" + ChatColor.RESET + "\nThe plugin will retrieve the last saved location and rebuild the TARDIS in that spot";
    public static final String COMMAND_CHAMELEON = ChatColor.AQUA + "The TARDIS Chameleon Circuit\n" + ChatColor.RESET + "You can make the TARDIS Police Box blend in with its surroundings by turning on the Chameleon Circuit.\nTo do this by command, type:\n" + ChatColor.GOLD + "/TARDIS chameleon [on|off]" + ChatColor.RESET + "\nOtherwise just right-click Chameleon Circuit sign to toggle it on or off\nIf you created your TARDIS using a previous verion of the plugin (and you still have an unmodified TARDIS interior, you can add a Chameleon Circuit by typing:\n" + ChatColor.GOLD + "/TARDIS chameleon add" + ChatColor.RESET + "\nYou can also just place a sign anywhere, and use the " + ChatColor.GREEN + "/tardis update chameleon" + ChatColor.RESET + " command to add it to the TARDIS database.";
    public static final String COMMAND_SFX = ChatColor.AQUA + "TARDIS sound effects\n" + ChatColor.RESET + "By default, TARDIS sound effects are enabled and play while you are inside the TARDIS\nYou can toggle the sounds on or off by typing:\n" + ChatColor.GOLD + "/TARDIS sfx [on|off]\n" + ChatColor.RESET + "If the admin has disabled sound effects, this command will have no effect.";
    public static final String COMMAND_PLATFORM = ChatColor.AQUA + "The TARDIS safety platform\n" + ChatColor.RESET + "By default, TARDIS safety platforms are disabled. If the admin has enabled safety platforms, you can toggle the platform on or off by typing:\n" + ChatColor.GOLD + "/TARDIS platform [on|off]\n" + ChatColor.RESET + "The change will take effect next time you time travel.";
    public static final String COMMAND_ADMIN = ChatColor.AQUA + "TARDIS admin commands\n" + ChatColor.RESET + "Arguments\n" + ChatColor.GOLD + "/TARDIS admin key [Material]" + ChatColor.RESET + " - set the Material used as the TARDIS key. Default: REDSTONE_TORCH_ON.\n" + ChatColor.GOLD + "/TARDIS admin bonus [true|false]" + ChatColor.RESET + " - toggle whether the TARDIS chest is filled with items replaced during the TARDIS construction. Default: true.\n" + ChatColor.GOLD + "/TARDIS admin max_rad [x]" + ChatColor.RESET + " - set the maximum distance (in blocks) you can time travel in the TARDIS. Default: 256\n" + ChatColor.GOLD + "/TARDIS admin protect [true|false]" + ChatColor.RESET + " - set whether the TARDIS blocks will be unaffected by fire/lava. Default: true\n" + ChatColor.GOLD + "/TARDIS admin default [true|false]" + ChatColor.RESET + " - set whether the (inner) TARDIS is created in a specific world. Default: false\n" + ChatColor.GOLD + "/TARDIS admin name [world]" + ChatColor.RESET + " - set the default world the (inner) TARDIS is created in.\n" + ChatColor.GOLD + "/TARDIS admin include [true|false]" + ChatColor.RESET + " - set whether the default world is included in random time travel destinations. Default: false\n" + ChatColor.GOLD + "/TARDIS admin exclude [world]" + ChatColor.RESET + " - excludes the specified world from random time travel destinations.\n" + ChatColor.GOLD + "/TARDIS admin platform [true|false]" + ChatColor.RESET + " - set whether a platform is built outside the Police Box door (preventing the player falling). Default: false, but if true, a player can also set their own preference to false if they so wish.\n" + ChatColor.GOLD + "/TARDIS admin sfx [true|false]" + ChatColor.RESET + " - set whether a sound effects play inside the TARDIS. Default: true, but a player can also set their own preference if they wish.";
    public static final String NO_PERMS_MESSAGE = "You do not have permission to do that!";
    public static final String NOT_OWNER = "You are not the Timelord or Companion for this TARDIS!";
    public static final String NO_TARDIS = "You have not created a TARDIS yet!";
    public static final String WRONG_MATERIAL = "The TARDIS key is a ";
    public static final String TIMELORD_OFFLINE = "The Timelord who owns this TARDIS is offline!";
    public static final String TIMELORD_NOT_IN = "The Timelord who owns this TARDIS is not inside!";

    public static <T extends Enum<T>>  T getEnumFromString(Class<T> c, String string) {
        if (c != null && string != null) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
            }
        }
        return null;
    }

    public static CMDS fromString(String name) {
        return getEnumFromString(CMDS.class, name);
    }

    public static Location getLocationFromDB(String s, float yaw, float pitch) {
        int savedx = 0, savedy = 0, savedz = 0;
        // compile location from string
        String[] data = s.split(":");
        World savedw = Bukkit.getServer().getWorld(data[0]);
        try {
            savedx = Integer.parseInt(data[1]);
            savedy = Integer.parseInt(data[2]);
            savedz = Integer.parseInt(data[3]);
        } catch (NumberFormatException n) {
            System.err.println(Constants.MY_PLUGIN_NAME + "Could not convert to number");
        }
        Location dest = new Location(savedw, savedx, savedy, savedz, yaw, pitch);
        return dest;
    }

    public static void list(Player p, String l) {
        String playerNameStr = p.getName();
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String querySaves = "SELECT * FROM tardis WHERE owner = '" + playerNameStr + "'";

            ResultSet rs = statement.executeQuery(querySaves);
            if (rs != null && rs.next()) {
                // list saves
                if (l.equalsIgnoreCase("saves")) {
                    // construct home string
                    String h = rs.getString("home");
                    String[] h_data = h.split(":");
                    p.sendMessage(ChatColor.GREEN + "HOME: " + h_data[0] + " at x:" + h_data[1] + " y:" + h_data[2] + " z:" + h_data[3]);
                    if (!rs.getString("save1").equals("") && !rs.getString("save1").equals("null") && rs.getString("save1") != null) {
                        String[] s1 = rs.getString("save1").split("~");
                        String[] d1_data = s1[1].split(":");
                        p.sendMessage(ChatColor.GREEN + "1. [" + s1[0] + "]: " + d1_data[0] + " at x:" + d1_data[1] + " y:" + d1_data[2] + " z:" + d1_data[3]);
                    } else {
                        p.sendMessage(ChatColor.GREEN + "1. No destination saved");
                    }
                    if (!rs.getString("save2").equals("") && !rs.getString("save2").equals("null") && rs.getString("save2") != null) {
                        String[] s2 = rs.getString("save2").split("~");
                        String[] d2_data = s2[1].split(":");
                        p.sendMessage(ChatColor.GREEN + "2. [" + s2[0] + "]: " + d2_data[0] + " at x:" + d2_data[1] + " y:" + d2_data[2] + " z:" + d2_data[3]);
                    } else {
                        p.sendMessage(ChatColor.GREEN + "2. No destination saved");
                    }
                    if (!rs.getString("save3").equals("") && !rs.getString("save3").equals("null") && rs.getString("save3") != null) {
                        String[] s3 = rs.getString("save3").split("~");
                        String[] d3_data = s3[1].split(":");
                        p.sendMessage(ChatColor.GREEN + "3. [" + s3[0] + "]: " + d3_data[0] + " at x:" + d3_data[1] + " y:" + d3_data[2] + " z:" + d3_data[3]);
                    } else {
                        p.sendMessage(ChatColor.GREEN + "3. No destination saved");
                    }
                }
                if (l.equalsIgnoreCase("companions")) {
                    // list companions
                    String comps = rs.getString("companions");
                    if (comps != null && !comps.equals("") && !comps.equals("[Null]")) {
                        String[] companionData = comps.split(":");
                        p.sendMessage(ChatColor.AQUA + "Your TARDIS companions are:");
                        for (String c : companionData) {
                            p.sendMessage(ChatColor.AQUA + c);
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_BLUE + "You don't have any TARDIS companions yet." + ChatColor.RESET + " Use " + ChatColor.GREEN + "/tardis add [player]" + ChatColor.RESET + " to add some");
                    }
                }
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " List Saves Error: " + e);
        }
    }

    public static int swapId(int id) {
        int swappedId = CHAMELEON_BLOCKS_CHANGE_HASH.get(id);
        return swappedId;
    }

    public static HashMap toMap(Object[] keys, Object[] values) {
        int keysSize = (keys != null) ? keys.length : 0;
        int valuesSize = (values != null) ? values.length : 0;
        if (keysSize == 0 && valuesSize == 0) {
            return new HashMap();
        }
        if (keysSize != valuesSize) {
            throw new IllegalArgumentException( "The number of keys doesn't match the number of values.");
        }
        HashMap map = new HashMap();
        for (int i = 0; i < keysSize; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }
}
