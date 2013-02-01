/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.destroyers;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;

/**
 * Destroys the inner TARDIS.
 *
 * If a TARDIS landed in the same space and time as another TARDIS, a time ram
 * could occur, destroying both TARDISes, their occupants and even cause a black
 * hole that would tear a hole in the universe
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerInner {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISDestroyerInner(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Destroys the inside of the TARDIS.
     *
     * @param schm the name of the schematic file to use can be DEFAULT, BIGGER
     * or DELUXE.
     * @param id the unique key of the record for this TARDIS in the database.
     * @param w the world where the TARDIS is to be built.
     * @param i the Material type id of the replacement block, this will either
     * be 0 (AIR) or 1 (STONE).
     * @param p an instance of the player who owns the TARDIS.
     */
    public final void destroyInner(TARDISConstants.SCHEMATIC schm, int id, World w, int i, String p) {
        // get dimensions
        short[] d;
        switch (schm) {
            case BIGGER:
                d = plugin.biggerdimensions;
                break;
            case DELUXE:
                d = plugin.deluxedimensions;
                break;
            default:
                d = plugin.budgetdimensions;
                break;
        }
        short h = d[0];
        short width = d[1];
        short l = d[2];
        // destroy TARDIS
        int level, row, col, x, y, z, startx, starty = (14 + h), startz, resetx, resetz;
        // calculate startx, starty, startz
        int gsl[] = plugin.utils.getStartLocation(id);
        startx = gsl[0];
        resetx = gsl[1];
        startz = gsl[2];
        resetz = gsl[3];
        x = gsl[4];
        z = gsl[5];
        for (level = 0; level < h; level++) {
            for (row = 0; row < width; row++) {
                for (col = 0; col < l; col++) {
                    // set the block to stone
                    Block b = w.getBlockAt(startx, starty, startz);
                    Material m = b.getType();
                    // if it's a chest clear the inventory first
                    if (m.equals(Material.CHEST)) {
                        Chest container = (Chest) b.getState();
                        //Is it a double chest?
                        Chest chest = getDoubleChest(b);
                        if (chest != null) {
                            chest.getInventory().clear();
                            chest.getBlock().setTypeId(0);
                            container.getBlock().setTypeId(0);
                        } else if (container != null) {
                            container.getInventory().clear();
                            container.getBlock().setTypeId(0);
                        }
                    }
                    // if it's a furnace clear the inventory first
                    if (m.equals(Material.FURNACE)) {
                        Furnace fur = (Furnace) b.getState();
                        fur.getInventory().clear();
                    }
                    if (!m.equals(Material.CHEST)) {
                        plugin.utils.setBlock(w, startx, starty, startz, i, (byte) 0);
                    }
                    startx += x;
                }
                startx = resetx;
                startz += z;
            }
            startz = resetz;
            starty -= 1;
        }
        // remove blocks saved to blocks table (iron/gold/diamond/emerald)
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        qf.doDelete("blocks", where);
        if (plugin.worldGuardOnServer) {
            plugin.wgchk.removeRegion(w, p);
        }
    }
    //Originally stolen from Babarix. Thank you :)

    public Chest getDoubleChest(Block block) {
        Chest chest = null;
        if (block.getRelative(BlockFace.NORTH).getTypeId() == 54) {
            chest = (Chest) block.getRelative(BlockFace.NORTH).getState();
            return chest;
        } else if (block.getRelative(BlockFace.EAST).getTypeId() == 54) {
            chest = (Chest) block.getRelative(BlockFace.EAST).getState();
            return chest;
        } else if (block.getRelative(BlockFace.SOUTH).getTypeId() == 54) {
            chest = (Chest) block.getRelative(BlockFace.SOUTH).getState();
            return chest;
        } else if (block.getRelative(BlockFace.WEST).getTypeId() == 54) {
            chest = (Chest) block.getRelative(BlockFace.WEST).getState();
            return chest;
        }
        return chest;
    }
}
