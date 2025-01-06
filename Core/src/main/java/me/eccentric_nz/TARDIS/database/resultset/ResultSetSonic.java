/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.Sonic;
import me.eccentric_nz.TARDIS.recipes.shaped.SonicScrewdriverRecipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a Time Lord's preferred sonic
 * screwdriver specifications.
 *
 * @author eccentric_nz
 */
public class ResultSetSonic {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final String prefix;
    private Sonic sonic;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     * @param where  a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     */
    public ResultSetSonic(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the sonic table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            where.forEach((key, value) -> sbw.append(key).append(" = ? AND "));
            wheres = " WHERE " + sbw + "sonic_uuid = ''";
        }
        String query = "SELECT * FROM " + prefix + "sonic" + wheres;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue() instanceof String) {
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
                rs.next();
                sonic = new Sonic(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getBoolean("activated"),
                        switch (rs.getString("model")) {
                            case "mark1", "10000001" -> SonicVariant.MARK1.getKey();
                            case "mark2", "10000002" -> SonicVariant.MARK2.getKey();
                            case "mark3", "10000003" -> SonicVariant.MARK3.getKey();
                            case "mark4", "10000004" -> SonicVariant.MARK4.getKey();
                            case "eighth", "10000008" -> SonicVariant.EIGHTH.getKey();
                            case "ninth", "10000009" -> SonicVariant.NINTH.getKey();
                            case "tenth", "10000010" -> SonicVariant.TENTH.getKey();
                            case "eleventh", "10000011" -> SonicVariant.ELEVENTH.getKey();
                            case "twelfth", "10000012" -> SonicVariant.TWELFTH.getKey();
                            case "thirteenth", "10000013" -> SonicVariant.THIRTEENTH.getKey();
                            case "fourteenth", "10000014" -> SonicVariant.FOURTEENTH.getKey();
                            case "fifteenth", "10000015" -> SonicVariant.FIFTEENTH.getKey();
                            case "river_song", "10000031" -> SonicVariant.RIVER_SONG.getKey();
                            case "master", "10000032" -> SonicVariant.MASTER.getKey();
                            case "sarah_jane", "10000033" -> SonicVariant.SARAH_JANE.getKey();
                            case "sonic_probe", "10000034" -> SonicVariant.SONIC_PROBE.getKey();
                            case "umbrella", "10000035" -> SonicVariant.UMBRELLA.getKey();
                            case "war", "10000085" -> SonicVariant.WAR.getKey();
                            default -> SonicScrewdriverRecipe.sonicModelLookup.getOrDefault(plugin.getConfig().getString("sonic.default_model").toLowerCase(Locale.ROOT), SonicVariant.ELEVENTH.getKey());
                        },
                        rs.getBoolean("bio"),
                        rs.getBoolean("diamond"),
                        rs.getBoolean("emerald"),
                        rs.getBoolean("redstone"),
                        rs.getBoolean("painter"),
                        rs.getBoolean("ignite"),
                        rs.getBoolean("arrow"),
                        rs.getBoolean("knockback"),
                        rs.getBoolean("brush"),
                        rs.getBoolean("conversion")
                );
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for sonic table! " + e.getMessage());
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
                plugin.debug("Error closing sonic table! " + e.getMessage());
            }
        }
        return true;
    }

    public Sonic getSonic() {
        return sonic;
    }
}
