/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISInteriorPostioning {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISInteriorPostioning(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Gets the next unused TIPS slot in a 20 x 20 grid.
     *
     * @return the first vacant slot
     */
    int getFreeSlot() {
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
     * @param slot the slot position in the grid (a number between 0, 399 inclusive)
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
        List<Integer> usedSlots = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT tips FROM " + prefix + "tardis";
        int i = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int s = rs.getInt("tips");
                    usedSlots.add(s);
                    i++;
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table (getting TIPS slots)! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis table (getting TIPS slots)! " + e.getMessage());
            }
        }
        return usedSlots;
    }

    @Deprecated
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
                w.unloadChunk(cx, cz, true);
            }
        }
    }

    // won't remove manually grown rooms...
    public void reclaimChunks(World w, int id) {
        // get ARS data
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            String[][][] json = TARDISARSMethods.getGridFromJSON(rs.getJson());
            Chunk c = plugin.getLocationUtils().getTARDISChunk(id);
            for (int l = 0; l < 3; l++) {
                for (int x = 0; x < 9; x++) {
                    for (int z = 0; z < 9; z++) {
                        if (!json[l][x][z].equalsIgnoreCase("STONE")) {
                            // get ARS slot
                            TARDISARSSlot slot = new TARDISARSSlot();
                            slot.setChunk(c);
                            slot.setY(l);
                            slot.setX(x);
                            slot.setZ(z);
                            for (int y = 0; y < 16; y++) {
                                for (int col = 0; col < 16; col++) {
                                    for (int row = 0; row < 16; row++) {
                                        w.getBlockAt(slot.getX() + row, slot.getY() + y, slot.getZ() + col).setBlockData(TARDISConstants.AIR);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void reclaimZeroChunk(World w, TARDISTIPSData data) {
        // get starting chunk
        Location l = new Location(w, data.getMinX(), 0, data.getMinZ());
        Chunk chunk = w.getChunkAt(l);
        Block block = chunk.getBlock(0, 0, 0);
        int sx = block.getX();
        int sz = block.getZ();
        for (int y = 64; y < 80; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int cx = sx + x;
                    int cz = sz + z;
                    w.getBlockAt(cx, y, cz).setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }
}
