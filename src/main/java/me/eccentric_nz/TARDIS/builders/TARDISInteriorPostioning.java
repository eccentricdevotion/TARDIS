/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.TARDISDatabase;

/**
 *
 * @author eccentric_nz
 */
public class TARDISInteriorPostioning {

    private final TARDISDatabase service = TARDISDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;

    public TARDISInteriorPostioning(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the next unused TIPS slot in a 20 x 20 grid.
     *
     * @return the first vacant slot
     */
    public int getFreeSlot() {
        List<Integer> usedSlots = makeUsedSlotList();
        int slot = -1;
        for (int i = 0; i < 400; i++) {
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
     * @param size the size (width) of the TARDIS being created
     * @return a TIPS Data container
     */
    public TARDISTIPSData getTIPSData(int slot, int size) {
        TARDISTIPSData data = new TARDISTIPSData();
        int row = slot / 20;
        int col = slot % 20;
        data.setMinX(row * 1000);
        data.setCentreX(row * 1000 + (499 - size / 2));
        data.setMaxX(row * 1000 + 999);
        data.setMinZ(col * 1000);
        data.setCentreZ(col * 1000 + (499 - size / 2));
        data.setMaxZ(col * 1000 + 999);
        data.setSlot(slot);
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
        String query = "SELECT tips FROM tardis";
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
}
