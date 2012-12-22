package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TARDISBuilder {

    private final TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();
    Statement statement;

    public TARDISBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void buildOuterTARDIS(int id, Location l, Constants.COMPASS d, boolean c, Player p) {
        int plusx, minusx, x, plusz, minusz, z, wall_block = 35;
        byte sd = 0, norm = 0, grey = 8, blue = 11, chameleonData = 11;
        if (c) {
            Block chameleonBlock;
            // chameleon circuit is on - get block under TARDIS
            if (l.getBlock().getType() == Material.SNOW) {
                chameleonBlock = l.getBlock();
            } else {
                chameleonBlock = l.getBlock().getRelative(BlockFace.DOWN);
            }
            int chameleonType = chameleonBlock.getTypeId();
            // determine wall_block
            if (Constants.CHAMELEON_BLOCKS_VALID.contains((Integer) chameleonType)) {
                wall_block = chameleonType;
                chameleonData = chameleonBlock.getData();
            }
            if (Constants.CHAMELEON_BLOCKS_BAD.contains((Integer) chameleonType)) {
                p.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Bummer, the TARDIS could not engage the Chameleon Circuit!");
            }
            if (Constants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) chameleonType)) {
                wall_block = Constants.swapId(chameleonType);
                switch (chameleonType) {
                    case 134:
                        chameleonData = 1;
                        break;
                    case 135:
                        chameleonData = 2;
                        break;
                    case 136:
                        chameleonData = 3;
                        break;
                    default:
                        chameleonData = chameleonBlock.getData();
                        break;
                }
            }

            if (Constants.CHAMELEON_BLOCKS_NEXT.contains((Integer) chameleonType)) {
                List<BlockFace> surrounding = Arrays.asList(new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST});                // try the surrounding blocks
                for (BlockFace bf : surrounding) {
                    Block surroundblock = chameleonBlock.getRelative(bf);
                    int eid = surroundblock.getTypeId();
                    if (Constants.CHAMELEON_BLOCKS_VALID.contains((Integer) eid)) {
                        wall_block = eid;
                        chameleonData = surroundblock.getData();
                        break;
                    }
                    if (Constants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) eid)) {
                        wall_block = Constants.swapId(eid);
                        switch (eid) {
                            case 134:
                                chameleonData = 1;
                                break;
                            case 135:
                                chameleonData = 2;
                                break;
                            case 136:
                                chameleonData = 3;
                                break;
                            default:
                                chameleonData = chameleonBlock.getData();
                                break;
                        }
                        break;
                    }
                }
            }
        }
        byte mds = chameleonData, mdw = chameleonData, mdn = chameleonData, mde = chameleonData, bds = chameleonData, bdw = chameleonData, bdn = chameleonData, bde = chameleonData;
        final World world;
        // expand placed blocks to a police box
        double lowX = l.getX();
        double lowY = l.getY();
        double lowZ = l.getZ();
        l.setX(lowX + 0.5);
        l.setY(lowY + 2);
        l.setZ(lowZ + 0.5);
        // get relative locations
        x = l.getBlockX();
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        final int y = (l.getBlockY());
        final int plusy = (l.getBlockY() + 1), minusy = (l.getBlockY() - 1), down2y = (l.getBlockY() - 2), down3y = (l.getBlockY() - 3);
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        world = l.getWorld();
        int south = wall_block, west = wall_block, north = wall_block, east = wall_block, signx = 0, signz = 0;
        String doorloc = "";

        TARDISUtils utils = new TARDISUtils(plugin);
        try {
            Connection connection = service.getConnection();
            statement = connection.createStatement();
            // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
            switch (d) {
                case SOUTH:
                    //if (yaw >= 315 || yaw < 45)
                    utils.setBlockCheck(world, x, down3y, minusz, 35, grey, id); // door is here if player facing south
                    doorloc = world.getName() + ":" + x + ":" + down2y + ":" + minusz;
                    sd = 2;
                    signx = x;
                    signz = (minusz - 1);
                    south = 71;
                    mds = 8;
                    bds = 1;
                    break;
                case EAST:
                    //if (yaw >= 225 && yaw < 315)
                    utils.setBlockCheck(world, minusx, down3y, z, 35, grey, id); // door is here if player facing east
                    doorloc = world.getName() + ":" + minusx + ":" + down2y + ":" + z;
                    sd = 4;
                    signx = (minusx - 1);
                    signz = z;
                    east = 71;
                    mde = 8;
                    bde = 0;
                    break;
                case NORTH:
                    //if (yaw >= 135 && yaw < 225)
                    utils.setBlockCheck(world, x, down3y, plusz, 35, grey, id); // door is here if player facing north
                    doorloc = world.getName() + ":" + x + ":" + down2y + ":" + plusz;
                    sd = 3;
                    signx = x;
                    signz = (plusz + 1);
                    north = 71;
                    mdn = 8;
                    bdn = 3;
                    break;
                case WEST:
                    //if (yaw >= 45 && yaw < 135)
                    utils.setBlockCheck(world, plusx, down3y, z, 35, grey, id); // door is here if player facing west
                    doorloc = world.getName() + ":" + plusx + ":" + down2y + ":" + z;
                    sd = 5;
                    signx = (plusx + 1);
                    signz = z;
                    west = 71;
                    mdw = 8;
                    bdw = 2;
                    break;
            }
            // should insert the door when tardis is first made, and then update location there after!
            String queryInsertOrUpdate = "SELECT door_id FROM doors WHERE door_type = 0 AND tardis_id = " + id;
            ResultSet rs = statement.executeQuery(queryInsertOrUpdate);
            String queryDoor;
            if (rs.next()) {
                queryDoor = "UPDATE doors SET door_location = '" + doorloc + "' WHERE door_id = " + rs.getInt("door_id");
            } else {
                queryDoor = "INSERT INTO doors (tardis_id, door_type, door_location) VALUES (" + id + ", 0, '" + doorloc + "')";
            }
            statement.executeUpdate(queryDoor);

            // bottom layer corners
            utils.setBlock(world, plusx, down2y, plusz, wall_block, chameleonData);
            utils.setBlock(world, minusx, down2y, plusz, wall_block, chameleonData);
            utils.setBlock(world, minusx, down2y, minusz, wall_block, chameleonData);
            utils.setBlock(world, plusx, down2y, minusz, wall_block, chameleonData);
            // middle layer corners
            utils.setBlock(world, plusx, minusy, plusz, wall_block, chameleonData);
            utils.setBlock(world, minusx, minusy, plusz, wall_block, chameleonData);
            utils.setBlock(world, minusx, minusy, minusz, wall_block, chameleonData);
            utils.setBlock(world, plusx, minusy, minusz, wall_block, chameleonData);
            // top layer
            switch (wall_block) {
                case 18:
                    utils.setBlock(world, x, y, z, 17, chameleonData);
                    break;
                case 46:
                    utils.setBlock(world, x, y, z, 35, (byte) 14);
                    break;
                case 79:
                    utils.setBlock(world, x, y, z, 35, (byte) 3);
                    break;
                case 89:
                    utils.setBlock(world, x, y, z, 35, (byte) 4);
                    break;
                default:
                    utils.setBlock(world, x, y, z, wall_block, chameleonData);
                    break;
            }
            utils.setBlock(world, plusx, y, z, wall_block, chameleonData); // east
            utils.setBlock(world, plusx, y, plusz, wall_block, chameleonData);
            utils.setBlock(world, x, y, plusz, wall_block, chameleonData); // south
            utils.setBlock(world, minusx, y, plusz, wall_block, chameleonData);
            utils.setBlock(world, minusx, y, z, wall_block, chameleonData); // west
            utils.setBlock(world, minusx, y, minusz, wall_block, chameleonData);
            utils.setBlock(world, x, y, minusz, wall_block, chameleonData); // north
            utils.setBlock(world, plusx, y, minusz, wall_block, chameleonData);
            // set sign
            utils.setBlock(world, signx, y, signz, 68, sd);
            Sign s = (Sign) world.getBlockAt(signx, y, signz).getState();
            if (plugin.config.getBoolean("name_tardis") == Boolean.valueOf("true")) {
                s.setLine(0, ChatColor.WHITE + "" + ChatColor.ITALIC + p.getName());
            }
            s.setLine(1, ChatColor.WHITE + "POLICE");
            s.setLine(2, ChatColor.WHITE + "BOX");
            s.update();
            // put torch on top
            if (wall_block == 79) {
                utils.setBlock(world, x, plusy, z, 76, (byte) 5);
            } else {
                utils.setBlock(world, x, plusy, z, 50, (byte) 5);
            }
            // remove the IRON & LAPIS blocks
            utils.setBlock(world, x, minusy, z, 0, norm);
            utils.setBlock(world, x, down2y, z, 0, norm);
            // bottom layer with door bottom
            utils.setBlock(world, plusx, down2y, z, west, bdw);
            utils.setBlock(world, x, down2y, plusz, north, bdn);
            utils.setBlock(world, minusx, down2y, z, east, bde);
            utils.setBlock(world, x, down2y, minusz, south, bds);
            // middle layer with door top
            utils.setBlock(world, plusx, minusy, z, west, mdw);
            utils.setBlock(world, x, minusy, plusz, north, mdn);
            utils.setBlock(world, minusx, minusy, z, east, mde);
            utils.setBlock(world, x, minusy, minusz, south, mds);
            // add platform if configured and necessary
            if (plugin.config.getBoolean("platform") == Boolean.valueOf("true")) {
                // check if user has platform pref
                String queryGetPlatform = "SELECT platform_on FROM player_prefs WHERE player = '" + p.getName() + "'";
                ResultSet rsPlatform = statement.executeQuery(queryGetPlatform);
                boolean userPlatform;
                if (rsPlatform.next()) {
                    userPlatform = rsPlatform.getBoolean("platform_on");
                } else {
                    userPlatform = true;
                }
                if (userPlatform) {
                    List<Block> platform_blocks = null;
                    switch (d) {
                        case SOUTH:
                            platform_blocks = Arrays.asList(world.getBlockAt(x - 1, down3y, minusz - 1), world.getBlockAt(x, down3y, minusz - 1), world.getBlockAt(x + 1, down3y, minusz - 1), world.getBlockAt(x - 1, down3y, minusz - 2), world.getBlockAt(x, down3y, minusz - 2), world.getBlockAt(x + 1, down3y, minusz - 2));
                            break;
                        case EAST:
                            platform_blocks = Arrays.asList(world.getBlockAt(minusx - 1, down3y, z - 1), world.getBlockAt(minusx - 1, down3y, z), world.getBlockAt(minusx - 1, down3y, z + 1), world.getBlockAt(minusx - 2, down3y, z - 1), world.getBlockAt(minusx - 2, down3y, z), world.getBlockAt(minusx - 2, down3y, z + 1));
                            break;
                        case NORTH:
                            platform_blocks = Arrays.asList(world.getBlockAt(x + 1, down3y, plusz + 1), world.getBlockAt(x, down3y, plusz + 1), world.getBlockAt(x - 1, down3y, plusz + 1), world.getBlockAt(x + 1, down3y, plusz + 2), world.getBlockAt(x, down3y, plusz + 2), world.getBlockAt(x - 1, down3y, plusz + 2));
                            break;
                        case WEST:
                            platform_blocks = Arrays.asList(world.getBlockAt(plusx + 1, down3y, z + 1), world.getBlockAt(plusx + 1, down3y, z), world.getBlockAt(plusx + 1, down3y, z - 1), world.getBlockAt(plusx + 2, down3y, z + 1), world.getBlockAt(plusx + 2, down3y, z), world.getBlockAt(plusx + 2, down3y, z - 1));
                            break;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (Block pb : platform_blocks) {
                        Material mat = pb.getType();
                        if (mat == Material.AIR || mat == Material.STATIONARY_WATER || mat == Material.WATER || mat == Material.VINE || mat == Material.RED_MUSHROOM || mat == Material.BROWN_MUSHROOM || mat == Material.LONG_GRASS || mat == Material.SAPLING || mat == Material.DEAD_BUSH || mat == Material.RED_ROSE || mat == Material.YELLOW_FLOWER) {
                            utils.setBlock(world, pb.getX(), pb.getY(), pb.getZ(), 35, grey);
                            String p_tmp = world.getName() + ":" + pb.getX() + ":" + pb.getY() + ":" + pb.getZ() + ":" + mat.toString();
                            sb.append(p_tmp).append("~");
                        }
                    }
                    String recall = sb.toString();
                    String platform_recall = "";
                    if (recall.length() > 0) {
                        platform_recall = recall.substring(0, recall.length() - 1);
                    }
                    String queryPlatform = "UPDATE tardis SET platform = '" + platform_recall + "' WHERE tardis_id = " + id;
                    statement.executeUpdate(queryPlatform);
                }
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Door Insert Error: " + e);
        }
    }

    public void buildInnerTARDIS(String[][][] s, World world, Constants.COMPASS d, int dbID) {
        int level, row, col, id, x, y, z, startx, starty = 15, startz, resetx, resetz, cx = 0, cy = 0, cz = 0, rid = 0, multiplier = 1, tx = 0, ty = 0, tz = 0;
        byte data = 0;
        short damage = 0;
        String tmp, replacedBlocks = "";
        HashMap<Block, Byte> postDoorBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postTorchBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postSignBlocks = new HashMap<Block, Byte>();
        // calculate startx, starty, startz
        TARDISUtils utils = new TARDISUtils(plugin);
        int gsl[] = utils.getStartLocation(dbID, d);
        startx = gsl[0];
        resetx = gsl[1];
        startz = gsl[2];
        resetz = gsl[3];
        x = gsl[4];
        z = gsl[5];

        // need to set TARDIS space to air first otherwise torches may be placed askew
        // also getting and storing block ids for bonus chest if configured
        StringBuilder sb = new StringBuilder();
        for (level = 0; level < 8; level++) {
            for (row = 0; row < 11; row++) {
                for (col = 0; col < 11; col++) {
                    if (plugin.config.getBoolean("bonus_chest") == Boolean.valueOf("true")) {
                        // get block at location
                        Location replaceLoc = new Location(world, startx, starty, startz);
                        int replacedMaterialId = replaceLoc.getBlock().getTypeId();
                        if (replacedMaterialId != 8 && replacedMaterialId != 9 && replacedMaterialId != 10 && replacedMaterialId != 11) {
                            sb.append(replacedMaterialId).append(":");
                        }
                    }
                    utils.setBlock(world, startx, starty, startz, 0, (byte) 0);
                    switch (d) {
                        case NORTH:
                        case SOUTH:
                            startx += x;
                            break;
                        case EAST:
                        case WEST:
                            startz += z;
                            break;
                    }
                }
                switch (d) {
                    case NORTH:
                    case SOUTH:
                        startx = resetx;
                        startz += z;
                        break;
                    case EAST:
                    case WEST:
                        startz = resetz;
                        startx += x;
                        break;
                }
            }
            switch (d) {
                case NORTH:
                case SOUTH:
                    startz = resetz;
                    break;
                case EAST:
                case WEST:
                    startx = resetx;
                    break;
            }
            starty += 1;
        }
        // reset start positions and do over
        startx = resetx;
        starty = 15;
        startz = resetz;
        try {
            Connection connection = service.getConnection();
            statement = connection.createStatement();

            for (level = 0; level < 8; level++) {
                for (row = 0; row < 11; row++) {
                    for (col = 0; col < 11; col++) {
                        tmp = s[level][row][col];
                        if (!tmp.equals("-")) {
                            if (tmp.contains(":")) {
                                String[] iddata = tmp.split(":");
                                id = Integer.parseInt(iddata[0]);
                                if (iddata[1].equals("~")) {
                                    // determine data bit from direction (d) and block type
                                    if (id == 76 && row == 1) { // 1st redstone torch
                                        switch (d) {
                                            case NORTH:
                                                data = 3;
                                                break;
                                            case EAST:
                                                data = 2;
                                                break;
                                            case SOUTH:
                                                data = 4;
                                                break;
                                            case WEST:
                                                data = 1;
                                                break;
                                        }
                                    }
                                    if (id == 76 && row == 3) { // 2nd redstone torch
                                        switch (d) {
                                            case NORTH:
                                                data = 2;
                                                break;
                                            case EAST:
                                                data = 4;
                                                break;
                                            case SOUTH:
                                                data = 1;
                                                break;
                                            case WEST:
                                                data = 3;
                                                break;
                                        }
                                    }
                                    if (id == 93 && col == 2 && level == 1) { // repeaters facing towards door
                                        switch (d) {
                                            case NORTH:
                                                data = 6;
                                                break;
                                            case EAST:
                                                data = 7;
                                                break;
                                            case SOUTH:
                                                data = 4;
                                                break;
                                            case WEST:
                                                data = 5;
                                                break;
                                        }
                                    }
                                    if (id == 93 && col == 3 && level == 1) { // repeaters facing away from door
                                        switch (d) {
                                            case NORTH:
                                                data = 4;
                                                break;
                                            case EAST:
                                                data = 5;
                                                break;
                                            case SOUTH:
                                                data = 6;
                                                break;
                                            case WEST:
                                                data = 7;
                                                break;
                                        }
                                    }
                                    if (id == 54) { // chest
                                        switch (d) {
                                            case NORTH:
                                            case EAST:
                                                data = 2;
                                                break;
                                            case SOUTH:
                                            case WEST:
                                                data = 3;
                                                break;
                                        }
                                        // remember the location of this chest
                                        String chest = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryChest = "UPDATE tardis SET chest = '" + chest + "' WHERE tardis_id = " + dbID;
                                        statement.executeUpdate(queryChest);
                                    }
                                    if (id == 61) { // furnace
                                        switch (d) {
                                            case NORTH:
                                            case WEST:
                                                data = 3;
                                                break;
                                            case SOUTH:
                                            case EAST:
                                                data = 2;
                                                break;
                                        }
                                    }
                                    if (id == 77) { // stone button
                                        switch (d) {
                                            case NORTH:
                                                data = 4;
                                                break;
                                            case WEST:
                                                data = 2;
                                                break;
                                            case SOUTH:
                                                data = 3;
                                                break;
                                            case EAST:
                                                data = 1;
                                                break;
                                        }
                                        // remember the location of this button
                                        String button = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryButton = "UPDATE tardis SET button = '" + button + "' WHERE tardis_id = " + dbID;
                                        statement.executeUpdate(queryButton);
                                    }
                                    if (id == 93 && row == 3 && col == 5 && level == 5) { // redstone repeater facing towards door
                                        switch (d) {
                                            case NORTH:
                                                data = 0;
                                                break;
                                            case EAST:
                                                data = 1;
                                                break;
                                            case SOUTH:
                                                data = 2;
                                                break;
                                            case WEST:
                                                data = 3;
                                                break;
                                        }
                                        // save repeater location
                                        String repeater0 = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryRepeater0 = "UPDATE tardis SET repeater0 = '" + repeater0 + "' WHERE tardis_id = " + dbID;
                                        statement.executeUpdate(queryRepeater0);
                                    }
                                    if (id == 93 && row == 5 && col == 3 && level == 5) { // redstone repeater facing right from door
                                        switch (d) {
                                            case NORTH:
                                                data = 3;
                                                break;
                                            case EAST:
                                                data = 0;
                                                break;
                                            case SOUTH:
                                                data = 1;
                                                break;
                                            case WEST:
                                                data = 2;
                                                break;
                                        }
                                        // save repeater location
                                        String repeater1 = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryRepeater1 = "UPDATE tardis SET repeater1 = '" + repeater1 + "' WHERE tardis_id = " + dbID;
                                        statement.executeUpdate(queryRepeater1);
                                    }
                                    if (id == 93 && row == 5 && col == 7 && level == 5) { // redstone repeater facing left from door
                                        switch (d) {
                                            case NORTH:
                                                data = 1;
                                                break;
                                            case EAST:
                                                data = 2;
                                                break;
                                            case SOUTH:
                                                data = 3;
                                                break;
                                            case WEST:
                                                data = 0;
                                                break;
                                        }
                                        // save repeater location
                                        String repeater2 = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryRepeater2 = "UPDATE tardis SET repeater2 = '" + repeater2 + "' WHERE tardis_id = " + dbID;
                                        statement.executeUpdate(queryRepeater2);
                                    }
                                    if (id == 93 && row == 7 && col == 5 && level == 5) { // redstone repeater facing away from door
                                        switch (d) {
                                            case NORTH:
                                                data = 2;
                                                break;
                                            case EAST:
                                                data = 3;
                                                break;
                                            case SOUTH:
                                                data = 0;
                                                break;
                                            case WEST:
                                                data = 1;
                                                break;
                                        }
                                        // save repeater location
                                        String repeater3 = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryRepeater3 = "UPDATE tardis SET repeater3 = '" + repeater3 + "' WHERE tardis_id = " + dbID;
                                        statement.executeUpdate(queryRepeater3);
                                    }
                                    if (id == 71) { // iron door bottom
                                        switch (d) {
                                            case NORTH:
                                                data = 3;
                                                break;
                                            case EAST:
                                                data = 0;
                                                break;
                                            case SOUTH:
                                                data = 1;
                                                break;
                                            case WEST:
                                                data = 2;
                                                break;
                                        }
                                        String doorloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryDoor = "INSERT INTO doors (tardis_id, door_type, door_location, door_direction) VALUES (" + dbID + ", 1, '" + doorloc + "', '" + d + "')";
                                        statement.executeUpdate(queryDoor);
                                    }
                                    if (id == 68) { // chameleon circuit sign
                                        switch (d) {
                                            case NORTH:
                                                data = 3;
                                                break;
                                            case EAST:
                                                data = 4;
                                                break;
                                            case SOUTH:
                                                data = 2;
                                                break;
                                            case WEST:
                                                data = 5;
                                                break;
                                        }
                                        String chameleonloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        String queryChameleon = "UPDATE tardis SET chameleon = '" + chameleonloc + "', chamele_on = 0 WHERE tardis_id = " + dbID;
                                        statement.executeUpdate(queryChameleon);
                                    }
                                } else {
                                    data = Byte.parseByte(iddata[1]);
                                }
                            } else {
                                id = Integer.parseInt(tmp);
                                data = 0;
                            }
                            //utils.setBlock(World w, int x, int y, int z, int m, byte d)
                            // if its the door, don't set it just remember its block then do it at the end
                            if (id == 71) {
                                postDoorBlocks.put(world.getBlockAt(startx, starty, startz), data);
                            } else if (id == 76) {
                                postTorchBlocks.put(world.getBlockAt(startx, starty, startz), data);
                            } else if (id == 68) {
                                postSignBlocks.put(world.getBlockAt(startx, starty, startz), data);
                            } else {
                                utils.setBlock(world, startx, starty, startz, id, data);
                            }
                        }
                        switch (d) {
                            case NORTH:
                            case SOUTH:
                                startx += x;
                                break;
                            case EAST:
                            case WEST:
                                startz += z;
                                break;
                        }
                    }
                    switch (d) {
                        case NORTH:
                        case SOUTH:
                            startx = resetx;
                            startz += z;
                            break;
                        case EAST:
                        case WEST:
                            startz = resetz;
                            startx += x;
                            break;
                    }
                }
                switch (d) {
                    case NORTH:
                    case SOUTH:
                        startz = resetz;
                        break;
                    case EAST:
                    case WEST:
                        startx = resetx;
                        break;
                }
                starty += 1;
            }
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Save Block Locations Error: " + e);
        }
        // put on the door and the redstone torches
        for (Map.Entry<Block, Byte> entry : postDoorBlocks.entrySet()) {
            Block pdb = entry.getKey();
            byte pddata = Byte.valueOf(entry.getValue());
            pdb.setTypeIdAndData(71, pddata, true);
        }
        for (Map.Entry<Block, Byte> entry : postTorchBlocks.entrySet()) {
            Block ptb = entry.getKey();
            byte ptdata = Byte.valueOf(entry.getValue());
            ptb.setTypeIdAndData(76, ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
            Block psb = entry.getKey();
            byte psdata = Byte.valueOf(entry.getValue());
            psb.setTypeIdAndData(68, psdata, true);
            Sign cs = (Sign) psb.getState();
            cs.setLine(0, "Chameleon");
            cs.setLine(1, "Circuit");
            cs.setLine(3, ChatColor.RED + "OFF");
            cs.update();
        }
        if (plugin.config.getBoolean("bonus_chest") == Boolean.valueOf("true")) {
            // get rid of last ":" and assign ids to an array
            String rb = sb.toString();
            replacedBlocks = rb.substring(0, rb.length() - 1);
            String[] replaceddata = replacedBlocks.split(":");
            // get saved chest location
            try {
                String queryGetChest = "SELECT chest FROM tardis WHERE tardis_id = " + dbID;
                ResultSet chestRS = statement.executeQuery(queryGetChest);
                String saved_chestloc = chestRS.getString("chest");
                String[] cdata = saved_chestloc.split(":");
                World cw = plugin.getServer().getWorld(cdata[0]);
                try {
                    cx = Integer.parseInt(cdata[1]);
                    cy = Integer.parseInt(cdata[2]);
                    cz = Integer.parseInt(cdata[3]);
                } catch (NumberFormatException n) {
                    System.err.println("Could not convert to number");
                }
                Location chest_loc = new Location(cw, cx, cy, cz);
                Block bonus_chest = chest_loc.getBlock();
                Chest chest = (Chest) bonus_chest.getState();
                // get chest inventory
                Inventory chestInv = chest.getInventory();
                // convert non-smeltable ores to items
                for (String i : replaceddata) {
                    try {
                        rid = Integer.parseInt(i);
                    } catch (NumberFormatException n) {
                        System.err.println("Could not convert to number");
                    }
                    switch (rid) {
                        case 1: // stone to cobblestone
                            rid = 4;
                            break;
                        case 16: // coal ore to coal
                            rid = 263;
                            break;
                        case 21: // lapis ore to lapis dye
                            rid = 351;
                            multiplier = 4;
                            damage = 4;
                            break;
                        case 56: // diamond ore to diamonds
                            rid = 264;
                            break;
                        case 73: // redstone ore to redstone dust
                            rid = 331;
                            multiplier = 4;
                            break;
                        case 129: // emerald ore to emerald
                            rid = 388;
                            break;
                    }
                    // add items to chest
                    chestInv.addItem(new ItemStack(rid, multiplier, damage));
                    multiplier = 1; // reset multiplier
                    damage = 0; // reset damage
                }
                chestRS.close();
                statement.close();
            } catch (SQLException e) {
                System.err.println(Constants.MY_PLUGIN_NAME + " Could not get chest location from DB!" + e);
            }
        }
    }
}
