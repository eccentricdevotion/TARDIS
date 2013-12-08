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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.ChatColor;

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
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();

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
            Statement del = null;
            PreparedStatement ps = null;
            try {
                // clear the controls table first - just incase they have reset `conversion_done` in the config
                del = connection.createStatement();
                del.executeUpdate("DELETE FROM controls");
                // insert values from tardis table
                ps = connection.prepareStatement("INSERT INTO controls (tardis_id, type, location) VALUES (?,?,?)");
                for (HashMap<String, String> map : data) {
                    int id = plugin.utils.parseNum(map.get("tardis_id"));
                    String tmph;
                    if (map.get("handbrake") == null || map.get("handbrake").isEmpty()) {
                        tmph = estimateHandbrake(map.get("size"), map.get("chameleon"));
                        plugin.console.sendMessage(plugin.pluginName + ChatColor.RED + "Handbrake location not found, making an educated guess...");
                    } else {
                        tmph = map.get("handbrake");
                    }
                    String tmpb;
                    if (map.get("button") == null || map.get("button").isEmpty()) {
                        tmpb = estimateButton(map.get("size"), map.get("chameleon"));
                        plugin.console.sendMessage(plugin.pluginName + ChatColor.RED + "Button location not found, making an educated guess...");
                    } else {
                        tmpb = map.get("button");
                    }
                    String tmpa;
                    if (map.get("artron_button") == null || map.get("artron_button").isEmpty()) {
                        tmpa = estimateArtron(map.get("size"), map.get("chameleon"));
                        plugin.console.sendMessage(plugin.pluginName + ChatColor.RED + "Artron Button location not found, making an educated guess...");
                    } else {
                        tmpa = map.get("artron_button");
                    }
                    String[] tmpr = new String[4];
                    if (map.get("repeater0") == null || map.get("repeater0").isEmpty()) {
                        tmpr = estimateRepeaters(map.get("size"), map.get("chameleon"));
                        plugin.console.sendMessage(plugin.pluginName + ChatColor.RED + "Repeater locations not found, making an educated guess...");
                    } else {
                        tmpr[0] = map.get("repeater0");
                        tmpr[1] = map.get("repeater1");
                        tmpr[2] = map.get("repeater2");
                        tmpr[3] = map.get("repeater3");
                    }
                    String hb = plugin.utils.makeLocationStr(tmph);
                    String bn = plugin.utils.makeLocationStr(tmpb);
                    String ab = plugin.utils.makeLocationStr(tmpa);
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
                    ps.setString(3, tmpr[0]);
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 3);
                    ps.setString(3, tmpr[1]);
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 4);
                    ps.setString(3, tmpr[2]);
                    ps.addBatch();
                    ps.setInt(1, id);
                    ps.setInt(2, 5);
                    ps.setString(3, tmpr[3]);
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
                if (del != null) {
                    try {
                        del.close();
                    } catch (SQLException e) {
                        plugin.debug("Control delete statement close error: " + e.getMessage());
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        plugin.debug("Control prepared statement close error: " + e.getMessage());
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

    private String estimateButton(String size, String cham) {
        TARDISConstants.SCHEMATIC s = TARDISConstants.SCHEMATIC.valueOf(size);
        String[] data = cham.split(":");
        int x = plugin.utils.parseNum(data[1]);
        int y = plugin.utils.parseNum(data[2]);
        int z = plugin.utils.parseNum(data[3]);
        switch (s) {
            case DELUXE:
                return data[0] + ":" + (x - 1) + ":" + y + ":" + (z - 1);
            default:
                return data[0] + ":" + x + ":" + y + ":" + (z + 2);
        }
    }

    private String estimateArtron(String size, String cham) {
        TARDISConstants.SCHEMATIC s = TARDISConstants.SCHEMATIC.valueOf(size);
        String[] data = cham.split(":");
        int x = plugin.utils.parseNum(data[1]);
        int y = plugin.utils.parseNum(data[2]);
        int z = plugin.utils.parseNum(data[3]);
        switch (s) {
            case DELUXE:
                return data[0] + ":" + (x + 5) + ":" + y + ":" + (z - 1);
            default:
                return data[0] + ":" + (x - 2) + ":" + y + ":" + (z + 2);
        }
    }

    private String[] estimateRepeaters(String size, String cham) {
        String[] r = new String[4];
        TARDISConstants.SCHEMATIC s = TARDISConstants.SCHEMATIC.valueOf(size);
        String[] data = cham.split(":");
        int x = plugin.utils.parseNum(data[1]);
        int y = plugin.utils.parseNum(data[2]);
        int z = plugin.utils.parseNum(data[3]);
        switch (s) {
            case DELUXE:
                r[0] = data[0] + ":" + (x + 2) + ":" + (y + 1) + ":" + (z - 3); // environment
                r[1] = data[0] + ":" + x + ":" + (y + 1) + ":" + (z - 1); // x
                r[2] = data[0] + ":" + (x + 4) + ":" + (y + 1) + ":" + (z - 1); // z
                r[3] = data[0] + ":" + (x + 2) + ":" + (y + 1) + ":" + (z + 1); // y
                break;
            default:
                r[0] = data[0] + ":" + (x - 1) + ":" + y + ":" + (z - 1);
                r[1] = data[0] + ":" + (x - 3) + ":" + y + ":" + (z + 1);
                r[2] = data[0] + ":" + (x + 1) + ":" + y + ":" + (z + 1);
                r[3] = data[0] + ":" + (x - 1) + ":" + y + ":" + (z + 3);
                break;
        }
        return r;
    }
}
