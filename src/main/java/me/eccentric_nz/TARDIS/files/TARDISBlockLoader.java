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
package me.eccentric_nz.TARDIS.files;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetGravity;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import org.bukkit.util.Vector;

/**
 * An anti-gravity spiral is a projectable beam used for removing gravity from
 * an object. The Seventh Doctor used his TARDIS to project a beam around a bus
 * in space after it crashed. He manoeuvred it down to Earth and dropped it
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
     * Loads Police Box and precious TARDIS blocks for protection from griefing
     * and harvesting. This speeds the looking up of block locations, as no
     * database interaction is required.
     */
    public void loadProtectBlocks() {
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, null, true);
        if (rsb.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsb.getData();
            for (HashMap<String, String> map : data) {
                plugin.getGeneralKeeper().getProtectBlockMap().put(map.get("location"), Integer.valueOf(map.get("tardis_id")));
            }
        }
    }

    /**
     * Loads Gravity Well blocks to speed up block lookups (no database
     * interaction is required).
     */
    public void loadGravityWells() {
        ResultSetGravity rsg = new ResultSetGravity(plugin, null, true);
        if (rsg.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsg.getData();
            for (HashMap<String, String> map : data) {
                int i = plugin.getUtils().parseInt(map.get("direction"));
                Double[] values = new Double[3];
                values[0] = Double.valueOf(map.get("direction"));
                values[1] = Double.valueOf(map.get("distance"));
                values[2] = Double.valueOf(map.get("velocity"));
                switch (i) {
                    case 1:
                        // going up
                        plugin.getGeneralKeeper().getGravityUpList().put(map.get("location"), values);
                        break;
                    case 2:
                        // going north
                        plugin.getGeneralKeeper().getGravityNorthList().put(map.get("location"), values);
                        break;
                    case 3:
                        // going west
                        plugin.getGeneralKeeper().getGravityWestList().put(map.get("location"), values);
                        break;
                    case 4:
                        // going south
                        plugin.getGeneralKeeper().getGravitySouthList().put(map.get("location"), values);
                        break;
                    case 5:
                        // going east
                        plugin.getGeneralKeeper().getGravityEastList().put(map.get("location"), values);
                        break;
                    default:
                        // going down
                        plugin.getGeneralKeeper().getGravityDownList().add(map.get("location"));
                        break;
                }
            }
        }
    }

    /**
     * Loads players antibuild preferences. Needed so that the preference is
     * persisted between restarts.
     */
    public void loadAntiBuild() {
        TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
        Connection connection = service.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis.tardis_id, tardis.owner, tardis.chunk FROM tardis, player_prefs WHERE player_prefs.build_on = 0 AND player_prefs.player = tardis.owner";
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
                    ProtectedRegion pr = plugin.getWorldGuardUtils().getRegion(rs.getString("chunk").split(":")[0], tl);
                    if (pr != null) {
                        Vector min = new Vector(pr.getMinimumPoint().getBlockX(), pr.getMinimumPoint().getBlockY(), pr.getMinimumPoint().getBlockZ());
                        Vector max = new Vector(pr.getMaximumPoint().getBlockX(), pr.getMaximumPoint().getBlockY(), pr.getMaximumPoint().getBlockZ());
                        tab.setMin(min);
                        tab.setMax(max);
                        tab.setTimelord(tl);
                        plugin.getTrackerKeeper().getTrackAntiBuild().put(id, tab);
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
