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
package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

public class WorldNameConverter {

    private final HashMap<String, String> worldTables = new HashMap<>();
    private final Set<SQLTable> locationTables = new HashSet<>();
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final String defaultWorld;

    public WorldNameConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        // world fields
        worldTables.put("area_locations", "area_location_id");
        worldTables.put("areas", "area_id");
        worldTables.put("back", "back_id");
        worldTables.put("chunks", "chunk_id");
        worldTables.put("current", "current_id");
        worldTables.put("deaths", "uuid");
        worldTables.put("destinations", "dest_id");
        worldTables.put("dispersed", "d_id");
        worldTables.put("gardens", "garden_id");
        worldTables.put("homes", "home_id");
        worldTables.put("next", "next_id");
        worldTables.put("plots", "plot_id");
        worldTables.put("previewers", "uuid");
        worldTables.put("saves", "save_id");
        worldTables.put("transmats", "transmat_id");
        // database location strings "world:x:y:z" -> "namespace:dimension:x:y:z"
        locationTables.add(new SQLTable("controls", "c_id")); // type IN (2,3,4,5) AND location LIKE "%:%"
        locationTables.add(new SQLTable("lamps", "l_id"));
        locationTables.add(new SQLTable("room_progress", "progress_id"));
        locationTables.add(new SQLTable("doors", "door_id", "door_location"));
        locationTables.add(new SQLTable("interactions", "i_id", "uuid"));
        // default world name
        defaultWorld = plugin.getServer().getLevelDirectory().toString().substring(2);
    }

    private String checkDefault(String w) {
        if (w.equalsIgnoreCase(defaultWorld)) {
            return "overworld";
        } else if (w.equalsIgnoreCase(defaultWorld + "_nether")) {
            return "the_nether";
        } else if (w.equalsIgnoreCase(defaultWorld + "_the_end")) {
            return "the_end";
        } else {
            return w;
        }
    }

    public boolean update() {
        Statement statement = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // update world names from "TARDIS_TimeVortex" to "minecraft:tardis_timevortex"
            for (Map.Entry<String, String> entry : worldTables.entrySet()) {
                String worldQuery = "SELECT " + entry.getValue() + ", world FROM " + prefix + entry.getKey();
                String worldUpdate = "UPDATE " + prefix + entry.getKey() + " SET world = ? WHERE " + entry.getValue() + " = ?";
                ps = connection.prepareStatement(worldUpdate);
                // get records
                ResultSet rsw = statement.executeQuery(worldQuery);
                if (rsw.isBeforeFirst()) {
                    while (rsw.next()) {
                        String key = "minecraft:" + checkDefault(rsw.getString("world").toLowerCase(Locale.ROOT));
                        // if there is a record
                        ps.setString(1, key);
                        ps.setInt(2, rsw.getInt(entry.getValue()));
                        ps.addBatch();
                        i++;
                    }
                    if (i > 0) {
                        ps.executeBatch();
                        connection.commit();
                        plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " " + entry.getKey() + " world name records to keyed.");
                    }
                }
            }
            i = 0;
            for (SQLTable entry : locationTables) {
                String locationQuery = "SELECT " + entry.id() + ", " + entry.column() + " FROM " + prefix + entry.table();
                if (entry.table().equals("controls")) {
                    locationQuery += " WHERE `type` IN (2,3,4,5) AND location NOT LIKE '%key=%'";
                }
                if (entry.table().equals("interactions")) {
                    locationQuery += " WHERE control = 'CENTRE'";
                }
                String locationUpdate = "UPDATE " + prefix + entry.table() + " SET " + entry.column() + " = ? WHERE " + entry.id() + " = ?";
                ps = connection.prepareStatement(locationUpdate);
                // get records
                ResultSet rsl = statement.executeQuery(locationQuery);
                if (rsl.isBeforeFirst()) {
                    while (rsl.next()) {
                        String oldLocation = rsl.getString(entry.column());
                        // TARDIS_TimeVortex:520:68:1529 -> minecraft:tardis_timevortex:520:68:1529
                        String newLocation = "minecraft:" + checkDefault(oldLocation.toLowerCase(Locale.ROOT));
                        ps.setString(1, newLocation);
                        ps.setInt(2, rsl.getInt(entry.id()));
                        ps.addBatch();
                        i++;
                    }
                    if (i > 0) {
                        ps.executeBatch();
                        connection.commit();
                        plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " " + entry.table() + " world name records to keyed.");
                    }
                }
            }
            i = 0;
            // pattern needed for junk tardis creeper field
            Pattern pattern = Pattern.compile("name=([^},]+)");
            // update relevant fields in tardis records
            // chunk - "world:x:z" -> "namespace:dimension:x:z"
            // creeper, beacon, eps, rail, renderer, zero
            String tardisQuery = "SELECT tardis_id, chunk, creeper, beacon, eps, rail, renderer, zero FROM " + prefix + "tardis";
            String tardisUpdate = "UPDATE " + prefix + "tardis SET chunk = ?, creeper = ?, beacon = ?, eps = ?, rail = ?, renderer = ?, zero = ? WHERE tardis_id = ?";
            ps = connection.prepareStatement(tardisUpdate);
            // get records
            ResultSet rst = statement.executeQuery(tardisQuery);
            if (rst.isBeforeFirst()) {
                while (rst.next()) {
                    String chunk = "minecraft:" + rst.getString("chunk").toLowerCase(Locale.ROOT);
                    ps.setString(1, chunk);
                    String c = rst.getString("creeper");
                    String creeper;
                    if (c.contains("CraftWorld")) {
                        creeper = pattern.matcher(c).replaceAll(match ->
                                "key=minecraft:" + match.group(1).toLowerCase()
                        );
                    } else {
                        creeper = c.isEmpty() ? "" : "minecraft:" + c.toLowerCase(Locale.ROOT);
                    }
                    ps.setString(2, creeper);
                    String b = rst.getString("beacon");
                    String beacon = b.isEmpty() ? "" : "minecraft:" + b.toLowerCase(Locale.ROOT);
                    ps.setString(3, beacon);
                    String e = rst.getString("eps");
                    String eps = e.isEmpty() ? "" : "minecraft:" + b.toLowerCase(Locale.ROOT);
                    ps.setString(4, eps);
                    String r = rst.getString("rail");
                    String rail = r.isEmpty() ? "" : "minecraft:" + b.toLowerCase(Locale.ROOT);
                    ps.setString(5, rail);
                    String d = rst.getString("renderer");
                    String renderer = d.isEmpty() ? "" : "minecraft:" + b.toLowerCase(Locale.ROOT);
                    ps.setString(6, renderer);
                    String z = rst.getString("zero");
                    String zero = z.isEmpty() ? "" : "minecraft:" + b.toLowerCase(Locale.ROOT);
                    ps.setString(7, zero);
                    ps.setInt(8, rst.getInt("tardis_id"));
                    ps.addBatch();
                    i++;
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " tardis world name records to keyed.");
                }
            }
            i = 0;
            // ** farming
            // all fields except farm_id/tardis_id
            String farmingQuery = "SELECT * FROM " + prefix + "farming";
            String farmingUpdate = "UPDATE " + prefix + "farming SET allay = ?, apiary = ?, aquarium = ?, bamboo = ?, birdcage = ?, farm = ?, geode = ?, happy = ?, hutch = ?, igloo = ?, iistubil = ?, lava = ?, mangrove = ?, nautilus = ?, pen = ?, stable = ?, stall = ?, village = ? WHERE farm_id = ?";
            ps = connection.prepareStatement(farmingUpdate);
            // get records
            ResultSet rsf = statement.executeQuery(farmingQuery);
            if (rsf.isBeforeFirst()) {
                while (rsf.next()) {
                    String al = rsf.getString("allay");
                    String allay = al.isEmpty() ? "" : "minecraft:" + al.toLowerCase(Locale.ROOT);
                    ps.setString(1, allay);
                    String ap = rsf.getString("apiary");
                    String apiary = ap.isEmpty() ? "" : "minecraft:" + ap.toLowerCase(Locale.ROOT);
                    ps.setString(2, apiary);
                    String aq = rsf.getString("aquarium");
                    String aquarium = aq.isEmpty() ? "" : "minecraft:" + aq.toLowerCase(Locale.ROOT);
                    ps.setString(3, aquarium);
                    String ba = rsf.getString("bamboo");
                    String bamboo = ba.isEmpty() ? "" : "minecraft:" + ba.toLowerCase(Locale.ROOT);
                    ps.setString(4, bamboo);
                    String bi = rsf.getString("birdcage");
                    String birdcage = bi.isEmpty() ? "" : "minecraft:" + bi.toLowerCase(Locale.ROOT);
                    ps.setString(5, birdcage);
                    String fa = rsf.getString("farm");
                    String farm = fa.isEmpty() ? "" : "minecraft:" + fa.toLowerCase(Locale.ROOT);
                    ps.setString(6, farm);
                    String ge = rsf.getString("geode");
                    String geode = ge.isEmpty() ? "" : "minecraft:" + ge.toLowerCase(Locale.ROOT);
                    ps.setString(7, geode);
                    String ha = rsf.getString("happy");
                    String happy = ha.isEmpty() ? "" : "minecraft:" + ha.toLowerCase(Locale.ROOT);
                    ps.setString(8, happy);
                    String hu = rsf.getString("hutch");
                    String hutch = hu.isEmpty() ? "" : "minecraft:" + hu.toLowerCase(Locale.ROOT);
                    ps.setString(9, hutch);
                    String ig = rsf.getString("igloo");
                    String igloo = ig.isEmpty() ? "" : "minecraft:" + ig.toLowerCase(Locale.ROOT);
                    ps.setString(10, igloo);
                    String ii = rsf.getString("iistubil");
                    String iistubil = ii.isEmpty() ? "" : "minecraft:" + ii.toLowerCase(Locale.ROOT);
                    ps.setString(11, iistubil);
                    String la = rsf.getString("lava");
                    String lava = la.isEmpty() ? "" : "minecraft:" + la.toLowerCase(Locale.ROOT);
                    ps.setString(12, lava);
                    String ma = rsf.getString("mangrove");
                    String mangrove = ma.isEmpty() ? "" : "minecraft:" + ma.toLowerCase(Locale.ROOT);
                    ps.setString(13, mangrove);
                    String na = rsf.getString("nautilus");
                    String nautilus = na.isEmpty() ? "" : "minecraft:" + na.toLowerCase(Locale.ROOT);
                    ps.setString(14, nautilus);
                    String pe = rsf.getString("pen");
                    String pen = pe.isEmpty() ? "" : "minecraft:" + pe.toLowerCase(Locale.ROOT);
                    ps.setString(15, pen);
                    String st = rsf.getString("stable");
                    String stable = st.isEmpty() ? "" : "minecraft:" + st.toLowerCase(Locale.ROOT);
                    ps.setString(16, stable);
                    String sl = rsf.getString("stall");
                    String stall = sl.isEmpty() ? "" : "minecraft:" + sl.toLowerCase(Locale.ROOT);
                    ps.setString(17, stall);
                    String vi = rsf.getString("village");
                    String village = vi.isEmpty() ? "" : "minecraft:" + vi.toLowerCase(Locale.ROOT);
                    ps.setString(18, village);
                    ps.setInt(19, rsf.getInt("farm_id"));
                    ps.addBatch();
                    i++;
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " farm world name records to keyed.");
                }

            }
            i = 0;
            // ** sensors
            // charging, flight, handbrake, malfunction, power
            String sensorQuery = "SELECT * FROM " + prefix + "sensors";
            String sensorUpdate = "UPDATE " + prefix + "sensors SET charging = ?, flight = ?, handbrake = ?, malfunction = ?, power = ? WHERE sensor_id = ?";
            ps = connection.prepareStatement(sensorUpdate);
            // get records
            ResultSet rss = statement.executeQuery(sensorQuery);
            if (rss.isBeforeFirst()) {
                while (rss.next()) {
                    String c = rss.getString("charging");
                    String charging = c.isEmpty() ? "" : "minecraft:" + c.toLowerCase(Locale.ROOT);
                    ps.setString(1, charging);
                    String f = rss.getString("flight");
                    String flight = f.isEmpty() ? "" : "minecraft:" + f.toLowerCase(Locale.ROOT);
                    ps.setString(2, flight);
                    String h = rss.getString("handbrake");
                    String handbrake = h.isEmpty() ? "" : "minecraft:" + h.toLowerCase(Locale.ROOT);
                    ps.setString(3, handbrake);
                    String m = rss.getString("malfunction");
                    String malfunction = m.isEmpty() ? "" : "minecraft:" + m.toLowerCase(Locale.ROOT);
                    ps.setString(4, malfunction);
                    String p = rss.getString("power");
                    String power = p.isEmpty() ? "" : "minecraft:" + p.toLowerCase(Locale.ROOT);
                    ps.setString(5, power);
                    ps.setInt(6, rss.getInt("sensor_id"));
                    ps.addBatch();
                    i++;
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " sensor world name records to keyed.");
                }
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            plugin.debug("Conversion error for non-keyed world names! " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing tables (converting legacy world names)! " + e.getMessage());
            }
        }
        return true;
    }
}

