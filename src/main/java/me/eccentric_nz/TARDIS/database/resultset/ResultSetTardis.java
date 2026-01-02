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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... everything about the construction of the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetTardis {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final String limit;
    private final boolean multiple;
    private final List<Tardis> data = new ArrayList<>();
    private final String prefix;
    private Tardis tardis;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis table.
     *
     * @param plugin    an instance of the main class.
     * @param where     a HashMap&lt;String, Object&gt; of table fields and values
     *                  to refine the search.
     * @param limit     an SQL LIMIT statement
     * @param multiple  a boolean indicating whether multiple rows should be
     *                  fetched
     */
    public ResultSetTardis(TARDIS plugin, HashMap<String, Object> where, String limit, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.limit = limit;
        this.multiple = multiple;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        String thelimit = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            where.forEach((key, value) -> sbw.append(key).append(" = ? AND "));
            wheres = " WHERE " + sbw.substring(0, sbw.length() - 5);
        }
        if (!limit.isEmpty()) {
            thelimit = " LIMIT " + limit;
        }
        String query = "SELECT * FROM " + prefix + "tardis" + wheres + thelimit;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue() instanceof String || entry.getValue() instanceof UUID) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        statement.setInt(s, (Integer) entry.getValue());
                    }
                    s++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String uid = rs.getString("uuid");
                    if (rs.wasNull() || uid.isEmpty()) {
                        uid = UUID.randomUUID().toString();
                    }
                    String companions = rs.getString("companions");
                    if (rs.wasNull()) {
                        companions = "";
                    }
                    String zero = rs.getString("zero");
                    if (rs.wasNull()) {
                        zero = "";
                    }
                    UUID frame = null;
                    String rotor = rs.getString("rotor");
                    if (!rs.wasNull() && !rotor.isEmpty()) {
                        frame = rotor.contains("Location") ? TARDISConstants.UUID_ZERO : UUID.fromString(rotor);
                    }
                    ChameleonPreset preset;
                    ChameleonPreset demat;
                    String itemPreset = "";
                    String itemDemat = "";
                    UUID uuid = UUID.fromString(uid);
                    try {
                        String p = rs.getString("chameleon_preset");
                        if (p.startsWith("ITEM:")) {
                            preset = ChameleonPreset.ITEM;
                            String[] split = p.split(":");
                            if (split.length > 1) {
                                itemPreset = split[1];
                            } else {
                                itemPreset = "Bad Wolf";
                                TARDISStaticUtils.warnPreset(uuid);
                            }
                        } else {
                            preset = ChameleonPreset.valueOf(p);
                        }
                    } catch (IllegalArgumentException e) {
                        preset = ChameleonPreset.FACTORY;
                    }
                    try {
                        String d = rs.getString("chameleon_demat");
                        if (d.startsWith("ITEM:")) {
                            demat = ChameleonPreset.ITEM;
                            String[] split = d.split(":");
                            if (split.length > 1) {
                                itemPreset = split[1];
                            } else {
                                itemPreset = "Bad Wolf";
                                TARDISStaticUtils.warnPreset(uuid);
                            }
                        } else {
                            demat = ChameleonPreset.valueOf(d);
                        }
                    } catch (IllegalArgumentException e) {
                        demat = ChameleonPreset.FACTORY;
                    }
                    tardis = new Tardis(
                            rs.getInt("tardis_id"),
                            uuid,
                            rs.getString("owner"),
                            rs.getString("last_known_name"),
                            rs.getString("chunk"),
                            rs.getInt("tips"),
                            Desktops.schematicFor(rs.getString("size").toLowerCase(Locale.ROOT)),
                            rs.getBoolean("abandoned"),
                            companions,
                            preset,
                            demat,
                            itemPreset,
                            itemDemat,
                            rs.getInt("adapti_on"),
                            rs.getInt("artron_level"),
                            rs.getString("creeper"),
                            rs.getString("beacon"),
                            rs.getBoolean("handbrake_on"),
                            rs.getBoolean("tardis_init"),
                            rs.getBoolean("recharging"),
                            rs.getBoolean("hidden"),
                            rs.getLong("lastuse"),
                            rs.getBoolean("iso_on"),
                            rs.getString("eps"),
                            rs.getString("rail"),
                            rs.getString("renderer"),
                            zero,
                            frame,
                            rs.getBoolean("powered_on"),
                            rs.getBoolean("lights_on"),
                            rs.getBoolean("siege_on"),
                            rs.getInt("monsters"),
                            rs.getInt("furnaces")
                    );
                    if (multiple) {
                        data.add(tardis);
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table! " + e.getMessage());
            return false;
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
        return true;
    }

    public Tardis getTardis() {
        return tardis;
    }

    public List<Tardis> getData() {
        return data;
    }
}
