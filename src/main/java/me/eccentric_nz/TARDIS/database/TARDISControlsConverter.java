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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 * Cyber-conversion into Cybermen involves the replacement of body parts
 * (including limbs, organs, and vital systems) with artificial components.
 * Partial conversion, with the victim retaining autonomy and a human identity
 * and body parts, is possible.
 *
 * @author eccentric_nz
 */
public class TARDISControlsConverter {

    private final TARDIS plugin;
    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();

    public TARDISControlsConverter(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Convert pre-TARDIS v2.3 controls to the new system.
     */
    public void convertControls() {
        ResultSetTardis rs = new ResultSetTardis(plugin, null, "", true);
        if (rs.resultSet()) {
            int i = 0;
            ArrayList<HashMap<String, String>> data = rs.getData();
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement("INSERT INTO controls (tardis_id, type, location) VALUES (?,?,?)");
                for (HashMap<String, String> map : data) {
                    int id = plugin.utils.parseNum(map.get("tardis_id"));
                    String hb = plugin.utils.makeLocationStr(map.get("handbrake"));
                    if (hb.isEmpty()) {
                        hb = estimateHandbrake(map.get("size"), map.get("chameleon"));
                    }
                    String bn = plugin.utils.makeLocationStr(map.get("button"));
                    String ab = plugin.utils.makeLocationStr(map.get("artron_button"));
                    ps.setInt(1, id);
                    ps.setInt(2, 0);
                    ps.setString(3, hb);
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 1);
                    ps.setString(3, bn);
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 2);
                    ps.setString(3, map.get("repeater0"));
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 3);
                    ps.setString(3, map.get("repeater1"));
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 4);
                    ps.setString(3, map.get("repeater2"));
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 5);
                    ps.setString(3, map.get("repeater3"));
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 6);
                    ps.setString(3, ab);
                    ps.addBatch();
                    connection.setAutoCommit(false);
                    ps.executeBatch();
                    connection.setAutoCommit(true);
                    i++;
                }
            } catch (SQLException e) {
                plugin.debug("Control conversion error: " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                    } catch (Exception e) {
                        plugin.debug("Control statement close error: " + e.getMessage());
                    }
                }
            }
            if (i > 0) {
                plugin.console.sendMessage(plugin.pluginName + "Converted " + i + " control consoles");
                plugin.getConfig().set("conversion_done", true);
                plugin.saveConfig();
            }
        }
    }

    private String estimateHandbrake(String size, String cham) {
        TARDISConstants.SCHEMATIC s = TARDISConstants.SCHEMATIC.valueOf(size);
        String[] data = cham.split(":");
        int x = plugin.utils.parseNum(data[1]);
        int y = plugin.utils.parseNum(data[2]);
        int z = plugin.utils.parseNum(data[3]);
        switch (s) {
            case DELUXE:
                return data[0] + ":" + (x + 1) + ":" + (y + 1) + ":" + (z - 2);
            default:
                return data[0] + ":" + (x - 2) + ":" + y + ":" + z;
        }
    }
}
