/*
 * Copyright (C) 2014 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author eccentric_nz
 */
public class TARDISInteriorPostioning {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISInteriorPostioning(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    /**
     * Gets the next unused TIPS slot in a 20 x 20 grid.
     *
     * @return the first vacant slot
     */
    public int getFreeSlot() {
        int limit = plugin.getConfig().getInt("creation.tips_limit");
        List<Integer> usedSlots = makeUsedSlotList();
        int slot = -1;
        for (int i = 0; i < limit; i++) {
            if (!usedSlots.contains(i)) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    /**
     * Calculate the position data for a TIPS slot.
     *
     * @param slot the slot position in the grid (a number between 0, 399
     * inclusive)
     * @return a TIPS Data container
     */
    public TARDISTIPSData getTIPSData(int slot) {
        TARDISTIPSData data = new TARDISTIPSData();
        int factorX = 0;
        int factorZ = 0;
        int subtract = 0;
        if (slot > 399 && slot < 800) {
            factorX = 20480;
            subtract = 400;
        }
        if (slot > 799 && slot < 1200) {
            factorZ = 20480;
            subtract = 800;
        }
        if (slot > 1199 && slot < 1600) {
            factorX = 20480;
            factorZ = 20480;
            subtract = 1200;
        }
        int row = (slot - subtract) / 20;
        int col = (slot - subtract) % 20;
        data.setMinX((row * 1024) + factorX);
        data.setCentreX((row * 1024 + 496) + factorX);
        data.setMaxX((row * 1024 + 1023) + factorX);
        data.setMinZ((col * 1024) + factorZ);
        data.setCentreZ((col * 1024 + 496) + factorZ);
        data.setMaxZ((col * 1024 + 1023) + factorZ);
        data.setSlot(slot);
        return data;
    }

    /**
     * Calculate the position data for the Junk TARDIS TIPS slot.
     *
     * @return a TIPS Data container
     */
    public TARDISTIPSData getTIPSJunkData() {
        TARDISTIPSData data = new TARDISTIPSData();
        int row = -1;
        int col = -1;
        data.setMinX((row * 1024));
        data.setCentreX((row * 1024 + 496));
        data.setMaxX((row * 1024 + 1023));
        data.setMinZ((col * 1024));
        data.setCentreZ((col * 1024 + 496));
        data.setMaxZ((col * 1024 + 1023));
        data.setSlot(-999);
        return data;
    }

    /**
     * Make a list of the currently used TIPS slots.
     *
     * @return a list of slot numbers
     */
    private List<Integer> makeUsedSlotList() {
        List<Integer> usedSlots = new ArrayList<Integer>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT tips FROM " + prefix + "tardis";
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    usedSlots.add(rs.getInt("tips"));
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        return usedSlots;
    }

    public void reclaimChunks(World w, TARDISTIPSData data) {
        // get starting chunk
        Location l = new Location(w, data.getMinX(), 0, data.getMinZ());
        Chunk chunk = w.getChunkAt(l);
        int sx = chunk.getX();
        int sz = chunk.getZ();
        for (int x = 0; x < 64; x++) {
            for (int z = 0; z < 64; z++) {
                int cx = sx + x;
                int cz = sz + z;
                w.regenerateChunk(cx, cz);
                w.getChunkAt(cx, cz).unload(true, false);
            }
        }
    }
}
