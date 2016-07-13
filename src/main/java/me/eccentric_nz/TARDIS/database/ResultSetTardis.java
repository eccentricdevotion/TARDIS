/*
 * Copyright (C) 2016 eccentric_nz
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... everything about the construction of the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetTardis {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final String limit;
    private final boolean multiple;
    private Tardis tardis;
    private final List<Tardis> data = new ArrayList<Tardis>();
    private final String prefix;
    private final int abandoned;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     * @param limit
     * @param multiple a boolean indicating whether multiple rows should be
     * fetched
     * @param abandoned whether to select TARDISes that abandoned (1) or not (0)
     */
    public ResultSetTardis(TARDIS plugin, HashMap<String, Object> where, String limit, boolean multiple, int abandoned) {
        this.plugin = plugin;
        this.where = where;
        this.limit = limit;
        this.multiple = multiple;
        this.prefix = this.plugin.getPrefix();
        this.abandoned = abandoned;
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
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ? AND ");
            }
            if (abandoned < 2) {
                wheres = " WHERE " + sbw.toString() + "abandoned = " + abandoned;
            } else {
                wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
            }
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
                    if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        statement.setInt(s, TARDISNumberParsers.parseInt(entry.getValue().toString()));
                    }
                    s++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String companions = rs.getString("companions");
                    if (rs.wasNull()) {
                        companions = "";
                    }
                    String zero = rs.getString("zero");
                    if (rs.wasNull()) {
                        zero = "";
                    }
                    tardis = new Tardis(
                            rs.getInt("tardis_id"),
                            UUID.fromString(rs.getString("uuid")),
                            rs.getString("owner"),
                            rs.getString("last_known_name"),
                            rs.getString("chunk"),
                            rs.getInt("tips"),
                            CONSOLES.SCHEMATICFor(rs.getString("size").toLowerCase()),
                            rs.getBoolean("abandoned"),
                            companions,
                            rs.getString("save_sign"),
                            rs.getString("chameleon"),
                            PRESET.valueOf(rs.getString("chameleon_preset")),
                            PRESET.valueOf(rs.getString("chameleon_demat")),
                            rs.getInt("adapti_on"),
                            rs.getInt("artron_level"),
                            rs.getString("creeper"),
                            rs.getString("condenser"),
                            rs.getString("beacon"),
                            rs.getBoolean("handbrake_on"),
                            rs.getBoolean("tardis_init"),
                            rs.getBoolean("recharging"),
                            rs.getString("scanner"),
                            rs.getString("farm"),
                            rs.getString("stable"),
                            rs.getBoolean("hidden"),
                            rs.getLong("lastuse"),
                            rs.getBoolean("iso_on"),
                            rs.getString("eps"),
                            rs.getString("rail"),
                            rs.getString("village"),
                            rs.getString("renderer"),
                            zero,
                            rs.getString("hutch"),
                            rs.getString("igloo"),
                            rs.getBoolean("powered_on"),
                            rs.getBoolean("lights_on"),
                            rs.getBoolean("siege_on"),
                            rs.getInt("monsters")
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
