package me.eccentric_nz.plugins.TARDIS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Schematic {

    private final TARDIS plugin;

    public Schematic(TARDIS plugin) {
        this.plugin = plugin;
    }
    private static String[][][] blocks;

    public static String[][][] schematic(File file) {

        // load data from csv file
        try {
            blocks = new String[8][11][11];

            BufferedReader bufRdr = new BufferedReader(new FileReader(file));
            String line;
            //read each line of text file
            for (int level = 0; level < 8; level++) {
                for (int row = 0; row < 11; row++) {
                    line = bufRdr.readLine();
                    String[] strArr = line.split(",");
                    System.arraycopy(strArr, 0, blocks[level][row], 0, 11);
                }
            }
        } catch (IOException io) {
            System.err.println("Could not read csv file");
        }
        return blocks;
    }

    public void setBlock(World w, int x, int y, int z, int m, byte d) {
        Block b = w.getBlockAt(x, y, z);
        b.setTypeIdAndData(m, d, true);
    }

    public void setBlockCheck(World w, int x, int y, int z, int m, byte d, String p) {
        List<Integer> ids = Arrays.asList(0, 6, 8, 9, 10, 11, 18, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 44, 46, 50, 51, 53, 54, 55, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 76, 77, 78, 79, 81, 83, 85, 89, 92, 93, 94, 96, 101, 102, 104, 105, 106, 107, 108, 109, 111, 113, 114, 115, 116, 117, 118, 119, 120, 122, 128, 130, 131, 132, 134, 135, 136);
        Block b = w.getBlockAt(x, y, z);
        Integer bId = Integer.valueOf(b.getTypeId());
        byte bData = b.getData();
        if (ids.contains(bId)) {
            b.setTypeIdAndData(m, d, true);
            // remember replaced block location, TypeId and Data so we can restore it later
            plugin.timelords.set(p + ".replaced", w.getName() + ":" + x + ":" + y + ":" + z + ":" + bId + ":" + bData);
        }
    }

    public void buildOuterTARDIS(Player p, Location l, Constants.COMPASS d) {
        int plusx;
        int minusx;
        int x;
        int plusz;
        int minusz;
        int z;
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
        final byte pink = 6;
        final byte lime = 5;
        final byte yell = 4;
        final byte red = 14;
        int south = 35, west = 35, north = 35, east = 35, signx = 0, signz = 0;
        byte sd = 0, mds = 11, mdw = 11, mdn = 11, mde = 11, bds = 11, bdw = 11, bdn = 11, bde = 11, norm = 0, grey = 8, blue = 11;

        // get direction player id facing from yaw place block under door if block is in list of blocks an iron door cannot go on
        switch (d) {
            case SOUTH:
                //if (yaw >= 315 || yaw < 45)
                setBlockCheck(world, x, down3y, minusz, 35, lime, p.getName()); // door is here if player facing south
                sd = 0x2;
                signx = x;
                signz = (minusz - 1);
                south = 71;
                mds = 0x8;
                bds = 0x1;
                break;
            case EAST:
                //if (yaw >= 225 && yaw < 315)
                setBlockCheck(world, minusx, down3y, z, 35, red, p.getName()); // door is here if player facing east
                sd = 0x4;
                signx = (minusx - 1);
                signz = z;
                east = 71;
                mde = 0x8;
                bde = 0x0;
                break;
            case NORTH:
                //if (yaw >= 135 && yaw < 225)
                setBlockCheck(world, x, down3y, plusz, 35, yell, p.getName()); // door is here if player facing north
                sd = 0x3;
                signx = x;
                signz = (plusz + 1);
                north = 71;
                mdn = 0x8;
                bdn = 0x3;
                break;
            case WEST:
                //if (yaw >= 45 && yaw < 135)
                setBlockCheck(world, plusx, down3y, z, 35, pink, p.getName()); // door is here if player facing west
                sd = 0x5;
                signx = (plusx + 1);
                signz = z;
                west = 71;
                mdw = 0x8;
                bdw = 0x2;
                break;
        }

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
        s.setLine(1, "¤fPOLICE");
        s.setLine(2, "¤fBOX");
        s.update();
        // put torch on top
        setBlock(world, x, plusy, z, 50, (byte) 5);
        // remove the IRON & LAPIS blocks
        setBlock(world, x, minusy, z, 0, norm);
        setBlock(world, x, down2y, z, 0, norm);
        // bottom layer with door bottom
        setBlock(world, plusx, down2y, z, west, bdw);
        setBlock(world, x, down2y, plusz, north, bdn);
        setBlock(world, minusx, down2y, z, east, bde);
        setBlock(world, x, down2y, minusz, south, bds);
        // middle layer with door top
        setBlock(world, plusx, minusy, z, west, mdw);
        setBlock(world, x, minusy, plusz, north, mdn);
        setBlock(world, minusx, minusy, z, east, mde);
        setBlock(world, x, minusy, minusz, south, mds);
    }

    public void buildInnerTARDIS(String[][][] s, Player p, World world, Constants.COMPASS d) {
        int level, row, col, id, x, y, z, startx, starty = 15, startz, resetx, resetz, cx = 0, cy = 0, cz = 0, rid = 0, multiplier = 1, tx = 0, ty = 0, tz = 0;
        byte data = 0x0;
        short damage = 0;
        String tmp, replacedBlocks = "";
        // calculate startx, starty, startz
        // getStartLocation(Location loc, Constants.COMPASS dir)
        int gsl[] = getStartLocation(p, d);
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
                        sb.append(replacedMaterialId).append(":");
                    }
                    setBlock(world, startx, starty, startz, 0, (byte) 0);
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
                                            data = 0x3;
                                            break;
                                        case EAST:
                                            data = 0x2;
                                            break;
                                        case SOUTH:
                                            data = 0x4;
                                            break;
                                        case WEST:
                                            data = 0x1;
                                            break;
                                    }
                                }
                                if (id == 76 && row == 3) { // 2nd redstone torch
                                    switch (d) {
                                        case NORTH:
                                            data = 0x2;
                                            break;
                                        case EAST:
                                            data = 0x4;
                                            break;
                                        case SOUTH:
                                            data = 0x1;
                                            break;
                                        case WEST:
                                            data = 0x3;
                                            break;
                                    }
                                }
                                if (id == 93 && col == 2 && level == 1) { // repeaters facing towards door
                                    switch (d) {
                                        case NORTH:
                                            data = 0x6;
                                            break;
                                        case EAST:
                                            data = 0x7;
                                            break;
                                        case SOUTH:
                                            data = 0x4;
                                            break;
                                        case WEST:
                                            data = 0x5;
                                            break;
                                    }
                                }
                                if (id == 93 && col == 3 && level == 1) { // repeaters facing away from door
                                    switch (d) {
                                        case NORTH:
                                            data = 0x4;
                                            break;
                                        case EAST:
                                            data = 0x5;
                                            break;
                                        case SOUTH:
                                            data = 0x6;
                                            break;
                                        case WEST:
                                            data = 0x7;
                                            break;
                                    }
                                }
                                if (id == 54) { // chest
                                    switch (d) {
                                        case NORTH:
                                        case EAST:
                                            data = 0x2;
                                            break;
                                        case SOUTH:
                                        case WEST:
                                            data = 0x3;
                                            break;
                                    }
                                    // remember the location of this chest
                                    plugin.timelords.set(p.getName() + ".chest", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                                }
                                if (id == 61) { // furnace
                                    switch (d) {
                                        case NORTH:
                                        case WEST:
                                            data = 0x3;
                                            break;
                                        case SOUTH:
                                        case EAST:
                                            data = 0x2;
                                            break;
                                    }
                                }
                                if (id == 77) { // stone button
                                    switch (d) {
                                        case NORTH:
                                            data = 0x4;
                                            break;
                                        case WEST:
                                            data = 0x2;
                                            break;
                                        case SOUTH:
                                            data = 0x3;
                                            break;
                                        case EAST:
                                            data = 0x1;
                                            break;
                                    }
                                    // remember the location of this button
                                    plugin.timelords.set(p.getName() + ".button", world.getName() + ":" + startx + ":" + starty + ":" + startz);

                                }
                                if (id == 93 && row == 3 && col == 5 && level == 5) { // redstone repeater facing towards door
                                    switch (d) {
                                        case NORTH:
                                            data = 0x0;
                                            break;
                                        case EAST:
                                            data = 0x1;
                                            break;
                                        case SOUTH:
                                            data = 0x2;
                                            break;
                                        case WEST:
                                            data = 0x3;
                                            break;
                                    }
                                    // save repeater location
                                    plugin.timelords.set(p.getName() + ".repeater0", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                                }
                                if (id == 93 && row == 5 && col == 3 && level == 5) { // redstone repeater facing right from door
                                    switch (d) {
                                        case NORTH:
                                            data = 0x3;
                                            break;
                                        case EAST:
                                            data = 0x0;
                                            break;
                                        case SOUTH:
                                            data = 0x1;
                                            break;
                                        case WEST:
                                            data = 0x2;
                                            break;
                                    }
                                    // save repeater location
                                    plugin.timelords.set(p.getName() + ".repeater1", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                                }
                                if (id == 93 && row == 5 && col == 7 && level == 5) { // redstone repeater facing left from door
                                    switch (d) {
                                        case NORTH:
                                            data = 0x1;
                                            break;
                                        case EAST:
                                            data = 0x2;
                                            break;
                                        case SOUTH:
                                            data = 0x3;
                                            break;
                                        case WEST:
                                            data = 0x0;
                                            break;
                                    }
                                    // save repeater location
                                    plugin.timelords.set(p.getName() + ".repeater2", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                                }
                                if (id == 93 && row == 7 && col == 5 && level == 5) { // redstone repeater facing away from door
                                    switch (d) {
                                        case NORTH:
                                            data = 0x2;
                                            break;
                                        case EAST:
                                            data = 0x3;
                                            break;
                                        case SOUTH:
                                            data = 0x0;
                                            break;
                                        case WEST:
                                            data = 0x1;
                                            break;
                                    }
                                    // save repeater location
                                    plugin.timelords.set(p.getName() + ".repeater3", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                                }
                                if (id == 71) { // iron door bottom
                                    switch (d) {
                                        case NORTH:
                                            data = 0x1;
                                            break;
                                        case EAST:
                                            data = 0x2;
                                            break;
                                        case SOUTH:
                                            data = 0x3;
                                            break;
                                        case WEST:
                                            data = 0x0;
                                            break;
                                    }
                                }
                            } else {
                                data = Byte.parseByte(iddata[1]);
                            }
                        } else {
                            id = Integer.parseInt(tmp);
                            data = 0x0;
                        }
                        //setBlock(World w, int x, int y, int z, int m, byte d)
                        setBlock(world, startx, starty, startz, id, data);
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
        if (plugin.config.getBoolean("bonus_chest") == Boolean.valueOf("true")) {
            // get rid of last ":" and assign ids to an array
            String rb = sb.toString();
            replacedBlocks = rb.substring(0, rb.length() - 1);
            String[] replaceddata = replacedBlocks.split(":");
            // get saved chest location
            String saved_chestloc = plugin.timelords.getString(p.getName() + ".chest");
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
                    case 16: // coal
                        rid = 263;
                        break;
                    case 21: // lapis
                        rid = 351;
                        multiplier = 4;
                        damage = 4;
                        break;
                    case 56: // diamond
                        rid = 264;
                        break;
                    case 73: // redstone
                        rid = 331;
                        multiplier = 4;
                        break;
                }
                // add items to chest
                chestInv.addItem(new ItemStack(rid, multiplier, damage));
                multiplier = 1; // reset multiplier
                damage = 0; // reset damage
            }
        }
        // save the timelords file
        try {
            plugin.timelords.save(plugin.timelordsfile);
        } catch (IOException io) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Could not save timelords file!");
        }
    }

    public void destroyTARDIS(Player p, Location l, World w, Constants.COMPASS d) {
        // inner TARDIS
        int level, row, col, x, y, z, startx, starty = 22, startz, resetx, resetz;
        // calculate startx, starty, startz
        int gsl[] = getStartLocation(p, d);
        startx = gsl[0];
        resetx = gsl[1];
        startz = gsl[2];
        resetz = gsl[3];
        x = gsl[4];
        z = gsl[5];
        for (level = 0; level < 8; level++) {
            for (row = 0; row < 11; row++) {
                for (col = 0; col < 11; col++) {
                    // set the block to stone
                    Block b = w.getBlockAt(startx, starty, startz);
                    Material m = b.getType();
                    // if it's a chest clear the inventory first
                    if (m == Material.CHEST) {
                        Chest che = (Chest) b.getState();
                        che.getInventory().clear();
                    }
                    // if it's a furnace clear the inventory first
                    if (m == Material.FURNACE) {
                        Furnace fur = (Furnace) b.getState();
                        fur.getInventory().clear();
                    }
                    if (w.getWorldType() == WorldType.FLAT) {
                        // flat world - set to AIR
                        setBlock(w, startx, starty, startz, 0, (byte) 0);
                    } else {
                        // normal world - set to stone
                        setBlock(w, startx, starty, startz, 1, (byte) 0);
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
            starty -= 1;
        }
    }

    public void destroyBlueBox(Location l, Constants.COMPASS d, String p) {
        World w = l.getWorld();
        int sbx = l.getBlockX() - 1;
        int rbx = sbx;
        int gbx = sbx;
        int sby = l.getBlockY() - 2;
        int sbywool = l.getBlockY() - 3;
        int sbz = l.getBlockZ() - 1;
        int rbz = sbz;
        int gbz = sbz;
        // remove blue wool and door
        for (int yy = 0; yy < 3; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    setBlock(w, sbx, sby, sbz, 0, (byte) 0);
                    sbx++;
                }
                sbx = rbx;
                sbz++;
            }
            sbz = rbz;
            sby++;
        }
        // replace the block under the door if there is one
        if (plugin.timelords.contains(p + ".replaced")) {
            String replacedData = plugin.timelords.getString(p + ".replaced");
            String[] parts = replacedData.split(":");
            World rw = plugin.getServer().getWorld(parts[0]);
            int rx = 0, ry = 0, rz = 0, rID = 0;
            byte rb = 0;
            try {
                rx = Integer.valueOf(parts[1]);
                ry = Integer.valueOf(parts[2]);
                rz = Integer.valueOf(parts[3]);
                rID = Integer.valueOf(parts[4]);
                rb = Byte.valueOf(parts[5]);
            } catch (NumberFormatException nfe) {
                System.err.println(Constants.MY_PLUGIN_NAME + "Could not convert to number!");
            }
            Block b = w.getBlockAt(rx, ry, rz);
            b.setTypeIdAndData(rID, rb, true);
        }
        // finally forget the replaced block
        plugin.timelords.set(p + ".replaced", null);
    }

    public void destroySign(Location l, Constants.COMPASS d) {
        World w = l.getWorld();
        int signx = 0, signz = 0;
        switch (d) {
            case EAST:
                signx = -2;
                signz = 0;
                break;
            case SOUTH:
                signx = 0;
                signz = -2;
                break;
            case WEST:
                signx = 2;
                signz = 0;
                break;
            case NORTH:
                signx = 0;
                signz = 2;
                break;
        }
        setBlock(w, l.getBlockX() + signx, l.getBlockY(), l.getBlockZ() + signz, 0, (byte) 0);
    }

    public void destroyTorch(Location l) {
        World w = l.getWorld();
        int tx = l.getBlockX();
        int ty = l.getBlockY() + 1;
        int tz = l.getBlockZ();
        setBlock(w, tx, ty, tz, 0, (byte) 0);
    }
    private static int[] startLoc = new int[6];

    public int[] getStartLocation(Player p, Constants.COMPASS dir) {
        int cx = 0, cz = 0;
        String chunkstr = plugin.timelords.getString(p.getName() + ".chunk");
        String[] split = chunkstr.split(":");
        World w = plugin.getServer().getWorld(split[0]);
        try {
            cx = Integer.parseInt(split[1]);
            cz = Integer.parseInt(split[2]);
        } catch (NumberFormatException nfe) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Could not convert to number!");
        }
        Chunk chunk = w.getChunkAt(cx, cz);
        switch (dir) {
            case NORTH:
                startLoc[0] = chunk.getBlock(14, 15, 14).getX();
                startLoc[1] = startLoc[0];
                startLoc[2] = chunk.getBlock(14, 15, 14).getZ();
                startLoc[3] = startLoc[2];
                startLoc[4] = -1;
                startLoc[5] = -1;
                break;
            case EAST:
                startLoc[0] = chunk.getBlock(1, 15, 14).getX();
                startLoc[1] = startLoc[0];
                startLoc[2] = chunk.getBlock(1, 15, 14).getZ();
                startLoc[3] = startLoc[2];
                startLoc[4] = 1;
                startLoc[5] = -1;
                break;
            case SOUTH:
                startLoc[0] = chunk.getBlock(1, 15, 1).getX();
                startLoc[1] = startLoc[0];
                startLoc[2] = chunk.getBlock(1, 15, 1).getZ();
                startLoc[3] = startLoc[2];
                startLoc[4] = 1;
                startLoc[5] = 1;
                break;
            case WEST:
                startLoc[0] = chunk.getBlock(14, 15, 1).getX();
                startLoc[1] = startLoc[0];
                startLoc[2] = chunk.getBlock(14, 15, 1).getZ();
                startLoc[3] = startLoc[2];
                startLoc[4] = -1;
                startLoc[5] = 1;
                break;
        }
        return startLoc;
    }

    public boolean checkChunk(String w, int x, int z) {
        boolean chunkchk = false;
        String check = w + ":" + x + ":" + z;
        BufferedReader br = null;
        try {
            File chunkFile = new File(plugin.getDataFolder() + File.separator + "chunks" + File.separator + w + ".chunks");
            br = new BufferedReader(new FileReader(chunkFile));
            String str;
            try {
                while ((str = br.readLine()) != null) {
                    System.out.println(str + " ?= " + w + ":" + x + ":" + z);
                    if (str.equals(check)) {
                        chunkchk = true;
                        Bukkit.broadcastMessage(str);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, "Could not read chunk file!", ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, "Chunk file does not exist!", ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, "Could not close chunk file!", ex);
            }
        }
        return chunkchk;
    }
}