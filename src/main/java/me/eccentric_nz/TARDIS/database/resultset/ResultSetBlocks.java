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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of the TARDIS Police Box
 * blocks.
 *
 * @author eccentric_nz
 */
public class ResultSetBlocks {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final boolean multiple;
    private final List<ReplacedBlock> data = new ArrayList<>();
    private final String prefix;
    private ReplacedBlock replacedBlock;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the blocks table.
     *
     * @param plugin   an instance of the main class.
     * @param where    a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     * @param multiple a boolean indicating whether multiple rows should be fetched
     */
    public ResultSetBlocks(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the blocks table. This method builds an SQL query string from the parameters
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
            wheres = " WHERE " + sbw.substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM " + prefix + "blocks" + wheres;
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
                while (rs.next()) {
                    String str = rs.getString("location");
                    String bd = rs.getString("data");
                    if (rs.wasNull() || bd.equals("0")) {
                        bd = "minecraft:air";
                    }
                    // deal with pre-1.17 grass_path database records
                    if (bd.contains("grass_path")) {
                        bd = "minecraft:dirt_path";
                    }
                    replacedBlock = new ReplacedBlock(rs.getInt("b_id"), rs.getInt("tardis_id"), TARDISStaticLocationGetters.getLocationFromBukkitString(str), str, plugin.getServer().createBlockData(bd), rs.getInt("police_box"));
                    if (multiple) {
                        data.add(replacedBlock);
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for blocks table! " + e.getMessage());
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
                plugin.debug("Error closing blocks table! " + e.getMessage());
            }
        }
        return true;
    }

    public ReplacedBlock getReplacedBlock() {
        return replacedBlock;
    }

    public List<ReplacedBlock> getData() {
        return data;
    }
}
