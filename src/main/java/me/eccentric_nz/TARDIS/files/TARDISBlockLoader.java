/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGravity;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An antigravity spiral is a projectable beam used for removing gravity from an object. The Seventh Doctor used his
 * TARDIS to project a beam around a bus in space after it crashed. He manoeuvred it down to Earth and dropped it
 * outside Shangri-La camp in southern Wales.
 *
 * @author eccentric_nz
 */
public class TARDISBlockLoader {

    private final TARDIS plugin;

    public TARDISBlockLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads Police Box and precious TARDIS blocks for protection from griefing and harvesting. This speeds the looking
     * up of block locations, as no database interaction is required.
     */
    public void loadProtectedBlocks() {
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, null, true);
        rsb.resultSetAsync((hasResult, resultSetBlocks) -> {
            if (hasResult) {
                resultSetBlocks.getData().forEach((rb) -> plugin.getGeneralKeeper().getProtectBlockMap().put(rb.getStrLocation(), rb.getTardis_id()));
            }
        });
    }

    /**
     * Unloads Police Box and precious TARDIS blocks from protection. Called when a TARDIS is deleted.
     *
     * @param id the TARDIS id to unload protection from
     */
    public void unloadProtectedBlocks(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, true);
        rsb.resultSetAsync((hasResult, resultSetBlocks) -> {
            if (hasResult) {
                resultSetBlocks.getData().forEach((rb) -> plugin.getGeneralKeeper().getProtectBlockMap().remove(rb.getStrLocation()));
            }
        });
    }

    /**
     * Loads Gravity Well blocks to speed up block lookups (no database interaction is required).
     */
    public void loadGravityWells() {
        ResultSetGravity rsg = new ResultSetGravity(plugin, null, true);
        if (rsg.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsg.getData();
            for (HashMap<String, String> map : data) {
                int i = TARDISNumberParsers.parseInt(map.get("direction"));
                Double[] values = new Double[3];
                values[0] = Double.valueOf(map.get("direction"));
                values[1] = Double.valueOf(map.get("distance"));
                values[2] = Double.valueOf(map.get("velocity"));
                switch (i) {
                    case 1 -> plugin.getGeneralKeeper().getGravityUpList().put(map.get("location"), values); // going up
                    case 2 -> plugin.getGeneralKeeper().getGravityNorthList().put(map.get("location"), values); // going north
                    case 3 -> plugin.getGeneralKeeper().getGravityWestList().put(map.get("location"), values); // going west
                    case 4 -> plugin.getGeneralKeeper().getGravitySouthList().put(map.get("location"), values); // going south
                    case 5 -> plugin.getGeneralKeeper().getGravityEastList().put(map.get("location"), values); // going east
                    default -> plugin.getGeneralKeeper().getGravityDownList().add(map.get("location")); // going down
                }
            }
        }
    }

    /**
     * Loads players antibuild preferences. Needed so that the preference is persisted between restarts.
     */
    public void loadAntiBuild() {
        TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
        Connection connection = service.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        String prefix = plugin.getPrefix();
        String query = "SELECT " + prefix + "tardis.tardis_id, " + prefix + "tardis.owner, " + prefix + "tardis.chunk FROM " + prefix + "tardis, " + prefix + "player_prefs WHERE " + prefix + "player_prefs.build_on = 0 AND " + prefix + "player_prefs.uuid = " + prefix + "tardis.uuid";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Integer id = rs.getInt("tardis_id");
                    String tl = rs.getString("owner");
                    TARDISAntiBuild tab = new TARDISAntiBuild();
                    // get region vectors
                    String[] split = rs.getString("chunk").split(":");
                    ProtectedRegion pr = plugin.getWorldGuardUtils().getRegion(split[0], tl);
                    if (pr != null) {
                        Vector min = new Vector(pr.getMinimumPoint().x(), pr.getMinimumPoint().y(), pr.getMinimumPoint().z());
                        Vector max = new Vector(pr.getMaximumPoint().x(), pr.getMaximumPoint().y(), pr.getMaximumPoint().z());
                        tab.setMin(min);
                        tab.setMax(max);
                        tab.setTimelord(tl);
                        plugin.getTrackerKeeper().getAntiBuild().put(id, tab);
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for antibuild load! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing antibuild load! " + e.getMessage());
            }
        }
    }
}
