package me.eccentric_nz.plugins.TARDIS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Constants {

    public static String MY_PLUGIN_NAME;
    public static String TARDIS_KEY;
    public static final String SCHEMATIC_FILE_NAME = "schematic.csv";
    public static final String CONFIG_FILE_NAME = "config.yml";
    public static final String TIMELORDS_FILE_NAME = "timelords.yml";
    // messages
    public static final String INSTRUCTIONS = "Your TARDIS is ready!\nRight-click the TARDIS door with your TARDIS key (by default a redstone torch) to enter.\nTo time travel, adjust the repeaters on the console. For more help, type " + ChatColor.GOLD + "/TARDIS help timetravel" + ChatColor.RESET + " in chat to see more instructions.";
    public static final String COMMANDS = ChatColor.BLUE + "TARDIS help\n" + ChatColor.RESET + "Type " + ChatColor.GOLD + "/TARDIS help <command>" + ChatColor.RESET + " to see more details about a command.\nType " + ChatColor.GOLD + "/TARDIS help create|delete|timetravel" + ChatColor.RESET + " for instructions on creating and removing a TARDIS and how to time travel.\nCommands\n" + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + " - list saved time travel destinations.\nThere are 4 save slots, one of which is reserved for the 'home' destination.\n" + ChatColor.GOLD + "/TARDIS save [slot number] [name]" + ChatColor.RESET + " - save the co-ordinates of a destintation to the specified slot.\n" + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + " - show the co-ordinates of a lost TARDIS.";

    public enum COMPASS {

        NORTH, EAST, SOUTH, WEST;
    }

    public enum CMDS {

        CREATE, DELETE, TIMETRAVEL, LIST, SAVE, FIND, ADMIN;
    }
    public static final String COMMAND_CREATE = ChatColor.BLUE + "Creating a TARDIS\n" + ChatColor.RESET + "You create a TARDIS by placing a " + ChatColor.GOLD + "specific pattern of blocks." + ChatColor.RESET + "\nYou will need to have an IRON BLOCK, a LAPIS BLOCK, and a redstone torch in your inventory.\nYou place the blocks where you want the TARDIS to be, in the following order:\nBottom - IRON BLOCK, middle - LAPIS BLOCK, top - REDSTONE TORCH\nThe TARDIS takes up a 3 x 3 x 4 area (w x d x h), so keep this in mind.\nTo enter the TARDIS, right-click the door with your TARDIS key (by default a redstone torch).";
    public static final String COMMAND_DELETE = ChatColor.BLUE + "Removing a TARDIS\n" + ChatColor.RESET + "To remove your TARDIS, " + ChatColor.GOLD + "break the 'POLICE BOX' wall sign" + ChatColor.RESET + " on the front of the TARDIS.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " You will lose any items you have stored in your TARDIS chest, and any saved time travel destinations.";
    public static final String COMMAND_TIMETRAVEL = ChatColor.BLUE + "Time travelling in the TARDIS\n" + ChatColor.RESET + "You can time travel in the TARDIS by changing the delay settings of the redstone repeaters on the TARDIS console.\nThe repeater closest to the door controls the saved time travel destinations - the 1-tick delay setting holds the 'Home' destination (where the TARDIS was first created), and the 2-4 tick delay settings are slots that you can save destinations to.\nTo travel to saved destinations all the other repeaters must be set to the 1-tick delay setting. You then select the saved slot as desired, then click the stone button at the rear of the TARDIS console.\nTo travel to a random destination, set any of the other repeaters to a 2-4 tick delay, then click the stone button at the rear of the TARDIS console.\nWhen exiting the TARDIS (right-click the door with your TARDIS key - by default a redstone torch) you will time travel to the destination of choice.";
    public static final String COMMAND_LIST = ChatColor.BLUE + "Listing time travel destinations\n" + ChatColor.RESET + "Simply type " + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + "\nto list the destinations saved in the TARDIS console.";
    public static final String COMMAND_SAVE = ChatColor.BLUE + "Saving time travel destinations\n" + ChatColor.RESET + "To save the current TARDIS destination, type\n" + ChatColor.GOLD + "/TARDIS save [slot number] [name]" + ChatColor.RESET + "\nWhere [slot number] is a number from 1 to 3 and [name] is a what you want to call the destination.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " Specifying a slot number that already has a saved destination will cause that destination to be overwritten with the new one.";
    public static final String COMMAND_FIND = ChatColor.BLUE + "Finding the TARDIS\n" + ChatColor.RESET + "Simply type " + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + "\nTo display the world name and x, y, z co-ordinates of the last saved location of your TARDIS.";
    public static final String COMMAND_ADMIN = ChatColor.BLUE + "TARDIS admin commands\n" + ChatColor.RESET + "Arguments\n" + ChatColor.GOLD + "/TARDIS admin key [Material]" + ChatColor.RESET + " - set the Material used as the TARDIS key. Default: REDSTONE_TORCH_ON.\n" + ChatColor.GOLD + "/TARDIS admin bonus [true|false]" + ChatColor.RESET + " - toggle whether the TARDIS chest is filled with items replaced during the TARDIS construction. Default: true.\n" + ChatColor.GOLD + "/TARDIS admin max_rad [x]" + ChatColor.RESET + " - set the maximum distance (in blocks) you can time travel in the TARDIS. Default: 256\n" + ChatColor.GOLD + "/TARDIS admin protect [true|false]" + ChatColor.RESET + " - set whether the TARDIS blocks will be unaffected by fire/lava. Default: true\n" + ChatColor.GOLD + "/TARDIS admin default [true|false]" + ChatColor.RESET + " - set whether the (inner) TARDIS is created in a specific world. Default: false\n" + ChatColor.GOLD + "/TARDIS admin name [world]" + ChatColor.RESET + " - set the default world the (inner) TARDIS is created in.\n" + ChatColor.GOLD + "/TARDIS admin include [true|false]" + ChatColor.RESET + " - set whether the default world is included in random time travel destinations. Default: false";
    public static final String NO_PERMS_MESSAGE = "You do not have permission to do that!";
    public static final String NOT_OWNER = "This is not your TARDIS!";
    public static final String NO_TARDIS = "You have not created a TARDIS yet!";
    public static final String WRONG_MATERIAL = "The TARDIS key is a ";

    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
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

    public static Location getLocationFromFile(String p, String s, float yaw, float pitch, FileConfiguration c) {
        int savedx = 0, savedy = 0, savedz = 0;
        // get location from timelords file
        String saved_loc = c.getString(p + "." + s);
        String[] data = saved_loc.split(":");
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

    public static void list(FileConfiguration c, Player p) {
        String configPath = p.getName();
        // construct home string
        String h = c.getString(configPath + ".home");
        String[] h_data = h.split(":");
        p.sendMessage(ChatColor.GREEN + "HOME: " + h_data[0] + " at x:" + h_data[1] + " y:" + h_data[2] + " z:" + h_data[3]);
        if (c.isSet(configPath + ".dest1")) {
            String d1 = c.getString(configPath + ".dest1.location");
            String d1_name = c.getString(configPath + ".dest1.name");
            String[] d1_data = d1.split(":");
            p.sendMessage(ChatColor.GREEN + "1. [" + d1_name + "]: " + d1_data[0] + " at x:" + d1_data[1] + " y:" + d1_data[2] + " z:" + d1_data[3]);
        } else {
            p.sendMessage(ChatColor.GREEN + "1. No destination saved");
        }
        if (c.isSet(configPath + ".dest2")) {
            String d2 = c.getString(configPath + ".dest2.location");
            String d2_name = c.getString(configPath + ".dest2.name");
            String[] d2_data = d2.split(":");
            p.sendMessage(ChatColor.GREEN + "2. [" + d2_name + "]: " + d2_data[0] + " at x:" + d2_data[1] + " y:" + d2_data[2] + " z:" + d2_data[3]);
        } else {
            p.sendMessage(ChatColor.GREEN + "2. No destination saved");
        }
        if (c.isSet(configPath + ".dest3")) {
            String d3 = c.getString(configPath + ".dest3.location");
            String d3_name = c.getString(configPath + ".dest3.name");
            String[] d3_data = d3.split(":");
            p.sendMessage(ChatColor.GREEN + "3. [" + d3_name + "]: " + d3_data[0] + " at x:" + d3_data[1] + " y:" + d3_data[2] + " z:" + d3_data[3]);
        } else {
            p.sendMessage(ChatColor.GREEN + "3. No destination saved");
        }
    }

    public void removeLineFromFile(File file, String lineToRemove) {
        try {
            //File inFile = new File(file);
            if (!file.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }
            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(file.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            String line;
            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(lineToRemove)) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            bw.close();
            br.close();
            //Delete the original file
            if (!file.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(file)) {
                System.out.println("Could not rename file");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, "Chunk file does not exist!", ex);
        } catch (IOException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, "Could not read chunk file!", ex);
        }
    }
}