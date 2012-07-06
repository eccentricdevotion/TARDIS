package me.eccentric_nz.plugins.TARDIS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
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
            String line = null;
            //read each line of text file
            for (int level = 0; level < 8; level++) {
                for (int row = 0; row < 11; row++) {
                    line = bufRdr.readLine();
                    String[] strArr = line.split(",");
                    System.arraycopy(strArr, 0, blocks[level][row], 0, 11);
                }
            }
            //System.out.println(blocks[2][5][5]);
        } catch (IOException io) {
            System.out.println("Could not read csv file");
        }
        return blocks;
    }

    public void buildInnerTARDIS(String[][][] s, Player p, Location l, Constants.COMPASS d) {
        int level, row, col, id, x = 0, y, z = 0, startx = 0, starty = 15, startz = 0, resetx = 0, resetz = 0, cx = 0, cy = 0, cz = 0, rid = 0, multiplier = 1;
        byte data = 0x0;
        String tmp, replacedBlocks = "";
        World world = l.getWorld();
        // calculate startx, starty, startz
        switch (d) {
            case NORTH:
                startx = l.getBlockX() - 5;
                resetx = startx;
                startz = l.getBlockZ() - 1;
                resetz = startz;
                x = 1;
                z = 1;
                break;
            case EAST:
                startx = l.getBlockX() + 1;
                resetx = startx;
                startz = l.getBlockZ() - 5;
                resetz = startz;
                x = -1;
                z = 1;
                break;
            case SOUTH:
                startx = l.getBlockX() + 5;
                resetx = startx;
                startz = l.getBlockZ() + 1;
                resetz = startz;
                x = -1;
                z = -1;
                break;
            case WEST:
                startx = l.getBlockX() - 1;
                resetx = startx;
                startz = l.getBlockZ() + 5;
                resetz = startz;
                x = 1;
                z = -1;
                break;
        }
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
                                            data = 0x4;
                                            break;
                                        case EAST:
                                            data = 0x1;
                                            break;
                                        case SOUTH:
                                            data = 0x3;
                                            break;
                                        case WEST:
                                            data = 0x2;
                                            break;
                                    }
                                }
                                if (id == 76 && row == 3) { // 2nd redstone torch
                                    switch (d) {
                                        case NORTH:
                                            data = 0x1;
                                            break;
                                        case EAST:
                                            data = 0x3;
                                            break;
                                        case SOUTH:
                                            data = 0x2;
                                            break;
                                        case WEST:
                                            data = 0x4;
                                            break;
                                    }
                                }
                                if (id == 93 && col == 2 && level == 1) { // repeaters facing towards door
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
                                if (id == 93 && col == 3 && level == 1) { // repeaters facing away from door
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
                                if (id == 54) { // chest
                                    switch (d) {
                                        case NORTH:
                                        case EAST:
                                            data = 0x3;
                                            break;
                                        case SOUTH:
                                        case WEST:
                                            data = 0x2;
                                            break;
                                    }
                                    // remember the location of this chest
                                    plugin.timelords.set(p.getName() + ".chest", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                                }
                                if (id == 61) { // furnace
                                    switch (d) {
                                        case NORTH:
                                        case WEST:
                                            data = 0x2;
                                            break;
                                        case SOUTH:
                                        case EAST:
                                            data = 0x3;
                                            break;
                                    }
                                }
                                if (id == 93 && row == 3 && col == 5 && level == 5) { // redstone repeater facing towards door
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
                                }
                                if (id == 93 && row == 5 && col == 3 && level == 5) { // redstone repeater facing right from door
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
                                if (id == 93 && row == 5 && col == 7 && level == 5) { // redstone repeater facing left from door
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
                                }
                                if (id == 93 && row == 7 && col == 5 && level == 5) { // redstone repeater facing away door
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
                        if (plugin.config.getBoolean("bonus_chest") == Boolean.valueOf("true")) {
                            // get block at location
                            Location replaceLoc = new Location(world, startx, starty, startz);
                            int replacedMaterialId = replaceLoc.getBlock().getTypeId();
                            replacedBlocks += replacedMaterialId + ":";
                        }
                        //setBlock(World w, int x, int y, int z, int m, byte d)
                        Constants.setBlock(world, startx, starty, startz, id, data);
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
            replacedBlocks = replacedBlocks.substring(0, replacedBlocks.length() - 1);
            String[] replaceddata = replacedBlocks.split(":");
            System.out.println(replacedBlocks);
            // get saved chest location
            String saved_chestloc = plugin.timelords.getString(p.getName() + ".chest");
            System.out.println(saved_chestloc);
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
            Inventory chestInv = chest.getInventory();
            for (String i : replaceddata) {
                try {
                    rid = Integer.parseInt(i);
                } catch (NumberFormatException n) {
                    System.err.println("Could not convert to number");
                }
                switch (rid) {
                    case 16: rid = 263; break;
                    case 56: rid = 264; break;
                    case 73: rid = 331; multiplier = 4; break;
                }
                chestInv.addItem(new ItemStack(rid,multiplier));
                multiplier = 1; // reset multiplier
            }
        }
    }
}