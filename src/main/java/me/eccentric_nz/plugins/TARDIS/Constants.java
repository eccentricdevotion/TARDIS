package me.eccentric_nz.plugins.TARDIS;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class Constants {

    private final TARDIS plugin;

    public Constants(TARDIS plugin) {
        this.plugin = plugin;
    }
    public static String MY_PLUGIN_NAME;
    public static String SCHEMATIC_FILE_NAME = "schematic.csv";
    public static String CONFIG_FILE_NAME = "config.yml";
    public static String TIMELORDS_FILE_NAME = "timelords.yml";
    // messages
    public static final String INSTRUCTIONS = "Your TARDIS is ready!\nRight-click the TARDIS door with your TARDIS key (a REDSTONE TORCH) to enter.\nTo time travel, adjust the repeaters on the console. For more help, type " + ChatColor.GOLD + "/TARDIS help timetravel" + ChatColor.RESET + " in chat to see more instructions.";
    public static final String COMMANDS = "Type " + ChatColor.GOLD + "/TARDIS help <command>" + ChatColor.RESET + " to see more details about a command.\nType " + ChatColor.GOLD + "/TARDIS help create|delete" + ChatColor.RESET + " for instructions on creating and removing a TARDIS.\nCommands\n" + ChatColor.GOLD + "/TARDIS timetravel [dest] - teleports you to a random or saved location.\nDestinations are determined by the delay settings of the redstone repeaters on the TARDIS console." + ChatColor.GOLD + "/TARDIS list" + ChatColor.RESET + " - list saved time travel destinations.\nThere are 4 save slots, one of which is reserved for the 'home' destination." + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + " - show the co-ordinates of a lost TARDIS.";

    public enum COMPASS {

        NORTH, EAST, SOUTH, WEST;
    }

    public enum CMDS {

        CREATE, DELETE, TIMETRAVEL, LIST, FIND, ADMIN;
    }
    public static final String COMMAND_CREATE = "You create a TARDIS by placing a " + ChatColor.GOLD + "specific pattern of blocks." + ChatColor.RESET + "\nYou will need to have an IRON BLOCK, a LAPIS BLOCK, and a redstone torch in your inventory.\nYou place the blocks where you want the TARDIS to be,in the following order:\nBottom - IRON BLOCK, middle - LAPIS BLOCK, top - REDSTONE TORCH\nThe TARDIS takes up a 3 x 3 x 4 area (w x d x h), so keep this in mind.";
    public static final String COMMAND_DELETE = "To remove your TARDIS, " + ChatColor.GOLD + "break the 'POLICE BOX' wall sign" + ChatColor.RESET + " on the front of the TARDIS.\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " You will lose any items you have stored in your TARDIS chest.";
    public static final String COMMAND_TIMETRAVEL = "Select the TARDIS you want to add a todo to\nby right-clicking it with a FEATHER\nTo add a todo, type " + ChatColor.GOLD + "/TARDIS todo add [the thing you need to do]" + ChatColor.RESET + "\nTo list your todos, type " + ChatColor.GOLD + "/TARDIS todo list" + ChatColor.RESET + " (or right-click with PAPER).\nTo mark a todo as DONE, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo mark [x]" + ChatColor.RESET + ", where [x] is a number.\nTo delete a todo, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo delete [x]" + ChatColor.RESET + ", where [x] is a number.";
    public static final String COMMAND_LIST = "Select the TARDIS you want to add a todo to\nby right-clicking it with a FEATHER\nTo add a todo, type " + ChatColor.GOLD + "/TARDIS todo add [the thing you need to do]" + ChatColor.RESET + "\nTo list your todos, type " + ChatColor.GOLD + "/TARDIS todo list" + ChatColor.RESET + " (or right-click with PAPER).\nTo mark a todo as DONE, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo mark [x]" + ChatColor.RESET + ", where [x] is a number.\nTo delete a todo, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo delete [x]" + ChatColor.RESET + ", where [x] is a number.";
    public static final String COMMAND_FIND = "Simply type " + ChatColor.GOLD + "/TARDIS find" + ChatColor.RESET + "\nTo display the world name and x, y, z co-ordinates of the last saved location of your TARDIS.";
    public static final String COMMAND_ADMIN = "Arguments\n" + ChatColor.GOLD + "/TARDIS admin bonus [true|false]" + ChatColor.RESET + " - toggle whether the TARDIS chest is filled with items replaced during the TARDIS construction. Default: true.\n" + ChatColor.GOLD + "/TARDIS admin max_rad [x]" + ChatColor.RESET + " - set the maximum distance (in blocks) you can time travel in the TARDIS. Default: 200\n" + ChatColor.GOLD + "/TARDIS admin use_inv [true|false]" + ChatColor.RESET + " - set whether a player must have the required blocks in their inventory to create a TARDIS. SURVIVAL mode only. Default: true\n" + ChatColor.GOLD + "/TARDIS admin spout [true|false]" + ChatColor.RESET + " - set whether the player must be using the Spout client to create and use a TARDIS. Default: false";
    public static final String NO_PERMS_MESSAGE = "You do not have permission to do that!";
    public static final String NOT_OWNER = "This is not your TARDIS!";
    public static final String NO_TARDIS = "You have not created a TARDIS yet!";
    public static final String WRONG_MATERIAL = "The TARDIS key is a REDSTONE TORCH!";

    public static void setBlock(World w, int x, int y, int z, int m, byte d) {
        Block b = w.getBlockAt(x, y, z);
        b.setTypeIdAndData(m, d, true);
        if (m == 69) {
            Lever lever = new Lever(b.getData());
            lever.setFacingDirection(BlockFace.DOWN);
            lever.setPowered(false);
            lever.setData(d);
            b.setData(lever.getData());
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

    public static void buildOuterTARDIS(Player p, Location l, float yaw) {
        World world;
        Location under_door = null;
        // expand placed blocks to a police box
        double lowX = l.getX();
        double lowY = l.getY();
        double lowZ = l.getZ();
        l.setX(lowX + 0.5);
        l.setY(lowY + 2);
        l.setZ(lowZ + 0.5);
        // get relative locations
        int x = (l.getBlockX());
        int plusx = (l.getBlockX() + 1);
        int minusx = (l.getBlockX() - 1);
        int y = (l.getBlockY());
        int plusy = (l.getBlockY() + 1);
        int minusy = (l.getBlockY() - 1);
        int down2y = (l.getBlockY() - 2);
        int down3y = (l.getBlockY() - 3);
        int z = (l.getBlockZ());
        int plusz = (l.getBlockZ() + 1);
        int minusz = (l.getBlockZ() - 1);
        int signx = x;
        int signz = z;
        world = l.getWorld();
        byte grey = 8;
        byte blue = 11;
        byte norm = 0;
        byte sd = 2;
        int south, west, north, east;
        byte mds, mdw, mdn, mde, bds, bdw, bdn, bde;

        // setBlock(World w, int x, int y, int z, int m, byte d)
        // check which side to put the door and sign on
        if (yaw >= 45 && yaw < 135) { // east
            east = 71;
            mde = 0x8;
            bde = 0x2;
            signx = (plusx + 1);
            signz = z;
            sd = 0x5;
        } else {
            east = 35;
            mde = blue;
            bde = blue;
        }
        if (yaw >= 135 && yaw < 225) { // south
            south = 71;
            mds = 0x8;
            bds = 0x3;
            signx = x;
            signz = (plusz + 1);
            sd = 0x3;
        } else {
            south = 35;
            mds = blue;
            bds = blue;
        }
        if (yaw >= 225 && yaw < 315) { // west
            west = 71;
            mdw = 0x8;
            bdw = norm;
            signx = (minusx - 1);
            signz = z;
            sd = 0x4;
        } else {
            west = 35;
            mdw = blue;
            bdw = blue;
        }
        if (yaw >= 315 || yaw < 45) { // north
            north = 71;
            mdn = 0x8;
            bdn = 0x1;
            signx = x;
            signz = (minusz - 1);
            sd = 0x2;
        } else {
            north = 35;
            mdn = blue;
            bdn = blue;
        }
        // base layer - light grey wool
        setBlock(world, x, down3y, z, 35, grey); // center
        setBlock(world, plusx, down3y, z, 35, grey); // east
        setBlock(world, plusx, down3y, plusz, 35, grey);
        setBlock(world, x, down3y, plusz, 35, grey); // south
        setBlock(world, minusx, down3y, plusz, 35, grey);
        setBlock(world, minusx, down3y, z, 35, grey); // west
        setBlock(world, minusx, down3y, minusz, 35, grey);
        setBlock(world, x, down3y, minusz, 35, grey); // north
        setBlock(world, plusx, down3y, minusz, 35, grey);
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
        // middle layer with door top
        setBlock(world, plusx, minusy, z, east, mde);
        setBlock(world, plusx, minusy, plusz, 35, blue);
        setBlock(world, x, minusy, plusz, south, mds);
        setBlock(world, minusx, minusy, plusz, 35, blue);
        setBlock(world, minusx, minusy, z, west, mdw);
        setBlock(world, minusx, minusy, minusz, 35, blue);
        setBlock(world, x, minusy, minusz, north, mdn);
        setBlock(world, plusx, minusy, minusz, 35, blue);
        // bottom layer with door bottom
        setBlock(world, plusx, down2y, z, east, bde);
        setBlock(world, plusx, down2y, plusz, 35, blue);
        setBlock(world, x, down2y, plusz, south, bds);
        setBlock(world, minusx, down2y, plusz, 35, blue);
        setBlock(world, minusx, down2y, z, west, bdw);
        setBlock(world, minusx, down2y, minusz, 35, blue);
        setBlock(world, x, down2y, minusz, north, bdn);
        setBlock(world, plusx, down2y, minusz, 35, blue);
        // put torch on top
        setBlock(world, x, plusy, z, 50, (byte) 5);
        // remove the IRON & LAPIS blocks
        setBlock(world, x, minusy, z, 0, norm);
        setBlock(world, x, down2y, z, 0, norm);
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
}