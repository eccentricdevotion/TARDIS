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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerInner {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISDestroyerInner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public final void destroyInner(TARDISConstants.SCHEMATIC schm, int id, World w, TARDISConstants.COMPASS d, int i, String p) {
        short h, width, l;
        switch (schm) {
            case BIGGER:
                h = plugin.biggerdimensions[0];
                width = plugin.biggerdimensions[1];
                l = plugin.biggerdimensions[2];
                break;
            case DELUXE:
                h = plugin.deluxedimensions[0];
                width = plugin.deluxedimensions[1];
                l = plugin.deluxedimensions[2];
                break;
            default:
                h = plugin.budgetdimensions[0];
                width = plugin.budgetdimensions[1];
                l = plugin.budgetdimensions[2];
                break;
        }
        // buildI TARDIS
        int level, row, col, x, y, z, startx, starty = (14 + h), startz, resetx, resetz;
        // calculate startx, starty, startz
        int gsl[] = plugin.utils.getStartLocation(id, d);
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
                    if (m == Material.CHEST) {
                        Chest che = (Chest) b.getState();
                        Inventory inv = che.getBlockInventory();
                        inv.clear();
                    }
                    // if it's a furnace clear the inventory first
                    if (m == Material.FURNACE) {
                        Furnace fur = (Furnace) b.getState();
                        fur.getInventory().clear();
                    }
                    if (m != Material.CHEST) {
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
        if (plugin.worldGuardOnServer) {
            plugin.wgchk.removeRegion(w, p);
        }
    }
}