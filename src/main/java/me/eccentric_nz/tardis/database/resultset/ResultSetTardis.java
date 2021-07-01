/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.database.resultset;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.Preset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... everything about the construction of
 * the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetTardis {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final HashMap<String, Object> where;
    private final String limit;
    private final boolean multiple;
    private final List<Tardis> data = new ArrayList<>();
    private final String prefix;
    private final int abandoned;
    private Tardis tardis;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the TARDIS table.
     *
     * @param plugin    an instance of the main class.
     * @param where     a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     * @param limit     an SQL LIMIT statement
     * @param multiple  a boolean indicating whether multiple rows should be fetched
     * @param abandoned whether to select TARDISes that are abandoned (1) or not (0)
     */
    public ResultSetTardis(TardisPlugin plugin, HashMap<String, Object> where, String limit, boolean multiple, int abandoned) {
        this.plugin = plugin;
        this.where = where;
        this.limit = limit;
        this.multiple = multiple;
        prefix = this.plugin.getPrefix();
        this.abandoned = abandoned;
    }

    /**
     * Retrieves an SQL ResultSet from the TARDIS table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
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
            if (abandoned < 2) {
                wheres = " WHERE " + sbw + "abandoned = " + abandoned;
            } else {
                wheres = " WHERE " + sbw.substring(0, sbw.length() - 5);
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
                    String uuid = rs.getString("uuid");
                    if (rs.wasNull() || uuid.equals("")) {
                        uuid = UUID.randomUUID().toString();
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
                        frame = UUID.fromString(rotor);
                    }
                    Preset preset;
                    Preset demat;
                    try {
                        preset = Preset.valueOf(rs.getString("chameleon_preset"));
                    } catch (IllegalArgumentException e) {
                        preset = Preset.FACTORY;
                    }
                    try {
                        demat = Preset.valueOf(rs.getString("chameleon_demat"));
                    } catch (IllegalArgumentException e) {
                        demat = Preset.FACTORY;
                    }
                    tardis = new Tardis(rs.getInt("tardis_id"), UUID.fromString(uuid), rs.getString("owner"), rs.getString("last_known_name"), rs.getString("chunk"), rs.getInt("tips"), Consoles.schematicFor(rs.getString("size").toLowerCase(Locale.ENGLISH)), rs.getBoolean("abandoned"), companions, preset, demat, rs.getInt("adapti_on"), rs.getInt("artron_level"), rs.getString("creeper"), rs.getString("beacon"), rs.getBoolean("handbrake_on"), rs.getBoolean("tardis_init"), rs.getBoolean("recharging"), rs.getBoolean("hidden"), rs.getLong("last_use"), rs.getBoolean("iso_on"), rs.getString("eps"), rs.getString("rail"), rs.getString("renderer"), zero, frame, rs.getBoolean("powered_on"), rs.getBoolean("lights_on"), rs.getBoolean("siege_on"), rs.getInt("monsters"));
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
