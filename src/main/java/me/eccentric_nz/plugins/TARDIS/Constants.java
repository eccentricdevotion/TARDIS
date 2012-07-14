package me.eccentric_nz.plugins.TARDIS;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Constants {

    private TARDIS plugin;
    private static int plusx;
    private static int minusx;
    private static int x;
    private static int plusz;
    private static int minusz;
    private static int z;

    public Constants(TARDIS plugin) {
        this.plugin = plugin;
    }
    public static String MY_PLUGIN_NAME;
    public static String SCHEMATIC_FILE_NAME = "schematic.csv";
    public static String CONFIG_FILE_NAME = "config.yml";
    public static String TIMELORDS_FILE_NAME = "timelords.yml";
    // messages
    public static String INSTRUCTIONS = "Your TARDIS is ready!\nRight-click the TARDIS door with your TARDIS key (a REDSTONE TORCH) to enter.\nTo time travel, adjust the repeaters on the console. For more help, type " + ChatColor.GOLD + "/TARDIS help timetravel" + ChatColor.RESET + " in chat to see more instructions.";
    public static String COMMANDS = ChatColor.BLUE + "TARDIS help\n" + ChatColor.RESET + "Type " + ChatColor.GOLD + "/TARDIS help <command>" + ChatColor.RESET + " to see more details about a command.\nType " + ChatColor.GOLD + "/TARDIS help create|delete|timetravel" + ChatColor.RESET + " for instructions on creating and removing a TARDIS and how to time travel.\nCommands\n" + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + " - list saved time travel destinations.\nThere are 4 save slots, one of which is reserved for the 'home' destination.\n" + ChatColor.GOLD + "/TARDIS save [slot number] [name]" + ChatColor.RESET + " - save the co-ordinates of a destintation to the specified slot.\n" + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + " - show the co-ordinates of a lost TARDIS.";

    public enum COMPASS {

        NORTH, EAST, SOUTH, WEST;
    }

    public enum CMDS {

        CREATE, DELETE, TIMETRAVEL, LIST, SAVE, FIND, ADMIN;
    }
    public static String COMMAND_CREATE = ChatColor.BLUE + "Creating a TARDIS\n" + ChatColor.RESET + "You create a TARDIS by placing a " + ChatColor.GOLD + "specific pattern of blocks." + ChatColor.RESET + "\nYou will need to have an IRON BLOCK, a LAPIS BLOCK, and a redstone torch in your inventory.\nYou place the blocks where you want the TARDIS to be, in the following order:\nBottom - IRON BLOCK, middle - LAPIS BLOCK, top - REDSTONE TORCH\nThe TARDIS takes up a 3 x 3 x 4 area (w x d x h), so keep this in mind.\nTo enter the TARDIS, right-click the door with your TARDIS key (a redstone torch).";
    public static String COMMAND_DELETE = ChatColor.BLUE + "Removing a TARDIS\n" + ChatColor.RESET + "To remove your TARDIS, " + ChatColor.GOLD + "break the 'POLICE BOX' wall sign" + ChatColor.RESET + " on the front of the TARDIS.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " You will lose any items you have stored in your TARDIS chest, and any saved time travel destinations.";
    public static String COMMAND_TIMETRAVEL = ChatColor.BLUE + "Time travelling in the TARDIS\n" + ChatColor.RESET + "You can time travel in the TARDIS by changing the delay settings of the redstone repeaters on the TARDIS console.\nThe repeater closest to the door controls the saved time travel destinations - the 1-tick delay setting holds the 'Home' destination (where the TARDIS was first created), and the 2-4 tick delay settings are slots that you can save destinations to.\nTo travel to saved destinations all the other repeaters must be set to the 1-tick delay setting. You then select the saved slot as desired, then click the stone button at the rear of the TARDIS console.\nTo travel to a random destination, set any of the other repeaters to a 2-4 tick delay, then click the stone button at the rear of the TARDIS console.\nWhen exiting the TARDIS (right-click the door with your TARDIS key - a redstone torch) you will time travel to the destination of choice.";
    public static String COMMAND_LIST = ChatColor.BLUE + "Listing time travel destinations\n" + ChatColor.RESET + "Simply type " + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + "\nto list the destinations saved in the TARDIS console.";
    public static String COMMAND_SAVE = ChatColor.BLUE + "Saving time travel destinations\n" + ChatColor.RESET + "To save the current TARDIS destination, type\n" + ChatColor.GOLD + "/TARDIS save [slot number] [name]" + ChatColor.RESET + "\nWhere [slot number] is a number from 1 to 3 and [name] is a what you want to call the destination.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " Specifying a slot number that already has a saved destination will cause that destination to be overwritten with the new one.";
    public static String COMMAND_FIND = ChatColor.BLUE + "Finding the TARDIS\n" + ChatColor.RESET + "Simply type " + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + "\nTo display the world name and x, y, z co-ordinates of the last saved location of your TARDIS.";
    public static String COMMAND_ADMIN = ChatColor.BLUE + "TARDIS admin commands\n" + ChatColor.RESET + "Arguments\n" + ChatColor.GOLD + "/TARDIS admin bonus [true|false]" + ChatColor.RESET + " - toggle whether the TARDIS chest is filled with items replaced during the TARDIS construction. Default: true.\n" + ChatColor.GOLD + "/TARDIS admin max_rad [x]" + ChatColor.RESET + " - set the maximum distance (in blocks) you can time travel in the TARDIS. Default: 256\n" + ChatColor.GOLD + "/TARDIS admin protect [true|false]" + ChatColor.RESET + " - set whether the TARDIS blocks will be unaffected by fire/lava. Default: true\n" + ChatColor.GOLD + "/TARDIS admin spout [true|false]" + ChatColor.RESET + " - set whether the player must be using the Spout client to create and use a TARDIS. Default: false";
    public static String NO_PERMS_MESSAGE = "You do not have permission to do that!";
    public static String NOT_OWNER = "This is not your TARDIS!";
    public static String NO_TARDIS = "You have not created a TARDIS yet!";
    public static String WRONG_MATERIAL = "The TARDIS key is a REDSTONE TORCH!";

    public static void setBlock(World w, int x, int y, int z, int m, byte d) {
        Block b = w.getBlockAt(x, y, z);
        b.setTypeIdAndData(m, d, true);
    }

    public static void setBlockCheck(World w, int x, int y, int z, int m, byte d) {
        Block b = w.getBlockAt(x, y, z);
        Material mat = b.getType();
        if (mat == Material.AIR || mat == Material.SNOW || mat == Material.LEAVES || mat == Material.WATER || mat == Material.STATIONARY_WATER) {
            b.setTypeIdAndData(m, d, true);
        }
    }
    public static String[] EFFECT_TYPES = {
        "BLAZE_SHOOT",
        "BOW_FIRE",
        "CLICK1",
        "CLICK2",
        "DOOR_TOGGLE",
        "EXTINGUISH",
        "GHAST_SHOOT",
        "GHAST_SHRIEK",
        "STEP_SOUND",
        "ZOMBIE_CHEW_IRON_DOOR",
        "ZOMBIE_CHEW_WOODEN_DOOR",
        "ZOMBIE_DESTROY_DOOR"
    };

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

    public void buildOuterTARDIS(Player p, Location l, float yaw) {
        final World world;
        // expand placed blocks to a police box
        double lowX = l.getX();
        double lowY = l.getY();
        double lowZ = l.getZ();
        l.setX(lowX + 0.5);
        l.setY(lowY + 2);
        l.setZ(lowZ + 0.5);
        // get relative locations
        x = (l.getBlockX());
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        final int y = (l.getBlockY());
        final int plusy = (l.getBlockY() + 1);
        final int minusy = (l.getBlockY() - 1);
        final int down2y = (l.getBlockY() - 2);
        final int down3y = (l.getBlockY() - 3);
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        final int signx = getSignX(yaw);
        final int signz = getSignZ(yaw);
        world = l.getWorld();
        byte grey = 8;
        final byte blue = 11;
        final byte norm = 0;
        final byte sd = getSignData(yaw);
        final int south = getDoorIdSouth(yaw);
        final int west = getDoorIdWest(yaw);
        final int north = getDoorIdNorth(yaw);
        final int east = getDoorIdEast(yaw);
        final byte mds = getDoorTopDataSouth(yaw);
        final byte mdw = getDoorTopDataWest(yaw);
        final byte mdn = getDoorTopDataNorth(yaw);
        final byte mde = getDoorTopDataEast(yaw);
        final byte bds = getDoorBotDataSouth(yaw);
        final byte bdw = getDoorBotDataWest(yaw);
        final byte bdn = getDoorBotDataNorth(yaw);
        final byte bde = getDoorBotDataEast(yaw);

        // setBlock(World w, int x, int y, int z, int m, byte d)
        // base layer - light grey wool - only set if block is air or water or leaves
        setBlockCheck(world, x, down3y, z, 35, grey); // center
        setBlockCheck(world, plusx, down3y, z, 35, grey); // east
        setBlockCheck(world, plusx, down3y, plusz, 35, grey);
        setBlockCheck(world, x, down3y, plusz, 35, grey); // south
        setBlockCheck(world, minusx, down3y, plusz, 35, grey);
        setBlockCheck(world, minusx, down3y, z, 35, grey); // west
        setBlockCheck(world, minusx, down3y, minusz, 35, grey);
        setBlockCheck(world, x, down3y, minusz, 35, grey); // north
        setBlockCheck(world, plusx, down3y, minusz, 35, grey);
        // bottom layer corners
        setBlock(world, plusx, down2y, plusz, 35, blue);
        setBlock(world, minusx, down2y, plusz, 35, blue);
        setBlock(world, minusx, down2y, minusz, 35, blue);
        setBlock(world, plusx, down2y, minusz, 35, blue);
        // middle layer corners
        setBlock(world, plusx, minusy, plusz, 35, blue);
        setBlock(world, minusx, minusy, plusz, 35, blue);
        setBlock(world, minusx, minusy, minusz, 35, blue);
        setBlock(world, plusx, minusy, minusz, 35, blue);
        // top layer
        setBlock(world, x, y, z, 35, blue); // center
        setBlock(world, plusx, y, z, 35, blue); // east
        setBlock(world, plusx, y, plusz, 35, blue);
        setBlock(world, x, y, plusz, 35, blue); // south
        setBlock(world, minusx, y, plusz, 35, blue);
        setBlock(world, minusx, y, z, 35, blue); // west
        setBlock(world, minusx, y, minusz, 35, blue);
        setBlock(world, x, y, minusz, 35, blue); // north
        setBlock(world, plusx, y, minusz, 35, blue);
        // set sign
        setBlock(world, signx, y, signz, 68, sd);
        Sign s = (Sign) world.getBlockAt(signx, y, signz).getState();
        s.setLine(1, "POLICE");
        s.setLine(2, "BOX");
        s.update();
        // put torch on top
        setBlock(world, x, plusy, z, 50, (byte) 5);
        // remove the IRON & LAPIS blocks
        setBlock(world, x, minusy, z, 0, norm);
        setBlock(world, x, down2y, z, 0, norm);
        // bottom layer with door bottom
        setBlock(world, plusx, down2y, z, east, bde);
        setBlock(world, x, down2y, plusz, south, bds);
        setBlock(world, minusx, down2y, z, west, bdw);
        setBlock(world, x, down2y, minusz, north, bdn);
        // middle layer with door top
        setBlock(world, plusx, minusy, z, east, mde);
        setBlock(world, x, minusy, plusz, south, mds);
        setBlock(world, minusx, minusy, z, west, mdw);
        setBlock(world, x, minusy, minusz, north, mdn);
    }

    public static Location getLocationFromFile(String p, String s, float yaw, float pitch, FileConfiguration c) {
        int savedx = 0, savedy = 0, savedz = 0;
        // get location from timelords file
        String saved_loc = c.getString(p + "." + s);
        String[] data = saved_loc.split(":");
        //System.out.println("saved world: " + data[0]);
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
    private static int doorID;

    private static int getDoorIdEast(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            doorID = 71;
        } else {
            doorID = 35;
        }
        return doorID;
    }

    private static int getDoorIdSouth(float yaw) {
        if (yaw >= 135 && yaw < 225) {
            doorID = 71;
        } else {
            doorID = 35;
        }
        return doorID;
    }

    private static int getDoorIdWest(float yaw) {
        if (yaw >= 225 && yaw < 315) {
            doorID = 71;
        } else {
            doorID = 35;
        }
        return doorID;
    }

    private static int getDoorIdNorth(float yaw) {
        if (yaw >= 315 || yaw < 45) {
            doorID = 71;
        } else {
            doorID = 35;
        }
        return doorID;
    }
    private static byte doorData;

    private static byte getDoorTopDataEast(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            doorData = 0x8;
        } else {
            doorData = 11;
        }
        return doorData;
    }

    private static byte getDoorTopDataSouth(float yaw) {
        if (yaw >= 135 && yaw < 225) {
            doorData = 0x8;
        } else {
            doorData = 11;
        }
        return doorData;
    }

    private static byte getDoorTopDataWest(float yaw) {
        if (yaw >= 225 && yaw < 315) {
            doorData = 0x8;
        } else {
            doorData = 11;
        }
        return doorData;
    }

    private static byte getDoorTopDataNorth(float yaw) {
        if (yaw >= 315 || yaw < 45) {
            doorData = 0x8;
        } else {
            doorData = 11;
        }
        return doorData;
    }

    private static byte getDoorBotDataEast(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            doorData = 0x2;
        } else {
            doorData = 11;
        }
        return doorData;
    }

    private static byte getDoorBotDataSouth(float yaw) {
        if (yaw >= 135 && yaw < 225) {
            doorData = 0x3;
        } else {
            doorData = 11;
        }
        return doorData;
    }

    private static byte getDoorBotDataWest(float yaw) {
        if (yaw >= 225 && yaw < 315) {
            doorData = 0x0;
        } else {
            doorData = 11;
        }
        return doorData;
    }

    private static byte getDoorBotDataNorth(float yaw) {
        if (yaw >= 315 || yaw < 45) {
            doorData = 0x1;
        } else {
            doorData = 11;
        }
        return doorData;
    }
    private static byte signData;

    private static byte getSignData(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            signData = 0x5;
        }
        if (yaw >= 135 && yaw < 225) {
            signData = 0x3;
        }
        if (yaw >= 225 && yaw < 315) {
            signData = 0x4;
        }
        if (yaw >= 315 || yaw < 45) {
            signData = 0x2;
        }
        return signData;
    }
    private static int sign;

    private static int getSignX(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            sign = (plusx + 1);
        }
        if (yaw >= 135 && yaw < 225) {
            sign = x;
        }
        if (yaw >= 225 && yaw < 315) {
            sign = (minusx - 1);
        }
        if (yaw >= 315 || yaw < 45) {
            sign = x;
        }
        return sign;
    }

    private static int getSignZ(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            sign = z;
        }
        if (yaw >= 135 && yaw < 225) {
            sign = (plusz + 1);
        }
        if (yaw >= 225 && yaw < 315) {
            sign = z;
        }
        if (yaw >= 315 || yaw < 45) {
            sign = (minusz - 1);
        }
        return sign;
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
}