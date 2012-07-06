package me.eccentric_nz.plugins.TARDIS;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
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
    public static final String INSTRUCTIONS = "Secretaries must be selected to perform commands on them.\nRight-click the TARDIS with a FEATHER to toggle selection on and off.\nRight-click the TARDIS with PAPER to view your todo list.\nRight-click the TARDIS with an INK_SACK to view your reminders.\nType " + ChatColor.GOLD + "/TARDIS help" + ChatColor.RESET + " in chat to see more instructions.";
    public static final String COMMANDS = "Type " + ChatColor.GOLD + "/TARDIS help <command>" + ChatColor.RESET + " to see more details about a command.\nCommands\n" + ChatColor.GOLD + "/TARDIS create" + ChatColor.RESET + " - makes a new TARDIS.\n" + ChatColor.GOLD + "/TARDIS todo [add|mark|delete|list]" + ChatColor.RESET + " - manipulates todo list\n" + ChatColor.GOLD + "/TARDIS remind [add|list] - adds and views reminders.\n" + ChatColor.GOLD + "/TARDIS delete" + ChatColor.RESET + " - remove a TARDIS.\n" + ChatColor.GOLD + "/TARDIS setsound" + ChatColor.RESET + " - sets the alarm sound for a TARDIS.\n" + ChatColor.GOLD + "/TARDIS name" + ChatColor.RESET + " - view a TARDIS's name.";

    public enum COMPASS {
        NORTH, EAST, SOUTH, WEST;
    }
    public enum CMDS {
        CREATE, DELETE, TIMETRAVEL, LIST, ADMIN;
    }
    public static final String COMMAND_CREATE = "You can create a TARDIS in one of two ways:\nby placing a specific pattern of blocks,\nor using the command /TARDIS create.\nEither way, you will need to have an IRON BLOCK, a LAPIS BLOCK, and a redstone torch in your inventory.\nYou then either place the blocks where you want the TARDIS to be,\nor look at the block where you want the TARDIS to stand and type the command " + ChatColor.GOLD + "/TARDIS create" + ChatColor.RESET + ".\nThe TARDIS takes up a 3 x 3 x 4 area (w x d x h), so keep this in mind.";
    public static final String COMMAND_DELETE = "This is the safe way to remove your TARDIS. You will recoop the original blocks that you created the tardis with.\nJust type the command " + ChatColor.GOLD + "/TARDIS delete" + ChatColor.RESET + ".\n" + ChatColor.RED + "WARNING:" + ChatColor.RESET + " Breaking the TARDIS manually means you will lose your blocks.";
    public static final String COMMAND_TIMETRAVEL = "Select the TARDIS you want to add a todo to\nby right-clicking it with a FEATHER\nTo add a todo, type " + ChatColor.GOLD + "/TARDIS todo add [the thing you need to do]" + ChatColor.RESET + "\nTo list your todos, type " + ChatColor.GOLD + "/TARDIS todo list" + ChatColor.RESET + " (or right-click with PAPER).\nTo mark a todo as DONE, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo mark [x]" + ChatColor.RESET + ", where [x] is a number.\nTo delete a todo, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo delete [x]" + ChatColor.RESET + ", where [x] is a number.";
    public static final String COMMAND_LIST = "Select the TARDIS you want to add a todo to\nby right-clicking it with a FEATHER\nTo add a todo, type " + ChatColor.GOLD + "/TARDIS todo add [the thing you need to do]" + ChatColor.RESET + "\nTo list your todos, type " + ChatColor.GOLD + "/TARDIS todo list" + ChatColor.RESET + " (or right-click with PAPER).\nTo mark a todo as DONE, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo mark [x]" + ChatColor.RESET + ", where [x] is a number.\nTo delete a todo, list the todos to get the todo's number,\nthen type " + ChatColor.GOLD + "/TARDIS todo delete [x]" + ChatColor.RESET + ", where [x] is a number.";
    public static final String COMMAND_ADMIN = "Arguments\n" + ChatColor.GOLD + "/TARDIS admin s_limit [x]" + ChatColor.RESET + " - set the number of secretaries allowed per player.\n" + ChatColor.GOLD + "/TARDIS admin t_limit [x]" + ChatColor.RESET + " - set the number of todo items allowed per TARDIS.\n" + ChatColor.GOLD + "/TARDIS admin r_limit [x]" + ChatColor.RESET + " - set the number of reminders allowed per TARDIS.\n" + ChatColor.GOLD + "/TARDIS admin use_inv [true|false]" + ChatColor.RESET + " - set whether a player must have the required fences and pressure plates in their inventory. SURVIVAL mode only.\n" + ChatColor.GOLD + "/TARDIS admin damage [x]" + ChatColor.RESET + " - set the amount of time (in minutes) secretaries take no damage.";
    public static final String NO_PERMS_MESSAGE = "You do not have permission to do that!";
    public static final String NOT_OWNER = "This is not your TARDIS!";
    public static final String WRONG_MATERIAL = "The TARDIS key is a REDSTONE TORCH!";

    public static void setBlock(World w, int x, int y, int z, int m, byte d) {
        Block b = w.getBlockAt(x, y, z);
        b.setTypeIdAndData(m, d, true);
        if (m == 76) {
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
        // expand placed blocks to a police box
        /*
         * need to set the x and z values + 0.5 as block coords seem to start
         * from the corner of the block causing the spawned villager to take
         * damage from surrounding blocks. also need to set the y value + 1 as
         * block coords seem to start from bottom of the block and we want to
         * spawn the villager on top of and in the middle of the block!
         */
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
        int z = (l.getBlockZ());
        int plusz = (l.getBlockZ() + 1);
        int minusz = (l.getBlockZ() - 1);
        int signx = x;
        int signz = z;
        world = l.getWorld();
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
        setBlock(world, x, plusy, z, 50, (byte)5);
        // remove the IRON & LAPIS blocks
        setBlock(world, x, minusy, z, 0, norm);
        setBlock(world, x, down2y, z, 0, norm);
    }
}