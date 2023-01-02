/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISSeedBlockPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TARDISSeedBlockPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save() {
        try {
            // save the seed blocks
            ps = connection.prepareStatement("INSERT INTO " + prefix + "seeds (schematic, wall, floor, location) VALUES (?,?,?,?)");
            for (Map.Entry<Location, TARDISBuildData> map : plugin.getBuildKeeper().getTrackTARDISSeed().entrySet()) {
                TARDISBuildData data = map.getValue();
                ps.setString(1, data.getSchematic().getPermission());
                ps.setString(2, data.getWallType().toString());
                ps.setString(3, data.getFloorType().toString());
                ps.setString(4, map.getKey().toString());
                count += ps.executeUpdate();
            }
            if (count > 0) {
                plugin.getLogger().log(Level.INFO, "Saved " + count + " placed seed blocks.");
            }
        } catch (SQLException ex) {
            plugin.debug("Insert error for seeds table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing portals statement: " + ex.getMessage());
            }
        }
    }

    public void load() {
        try {
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "seeds");
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String l = rs.getString("location");
                    // check for null worlds
                    if (!l.contains("null")) {
                        Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(l);
                        if (location != null) {
                            TARDISBuildData data = new TARDISBuildData();
                            Schematic schm = Consoles.schematicFor(rs.getString("schematic"));
                            Material wall = Material.valueOf(rs.getString("wall"));
                            Material floor = Material.valueOf(rs.getString("floor"));
                            data.setSchematic(schm);
                            data.setWallType(wall);
                            data.setFloorType(floor);
                            plugin.getBuildKeeper().getTrackTARDISSeed().put(location, data);
                            count++;
                        }
                    }
                }
            }
            if (count > 0) {
                plugin.getLogger().log(Level.INFO, "Loaded " + count + " placed seed blocks.");
            }
            // clear the portals table so we don't get any duplicates when saving them
            ps = connection.prepareStatement("DELETE FROM " + prefix + "seeds");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for seeds table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing seeds statement or resultset: " + ex.getMessage());
            }
        }
    }
}
