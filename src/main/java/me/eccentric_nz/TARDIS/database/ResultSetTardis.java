/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class ResultSetTardis {

    private int tardis_id;
    private String owner;
    private String chunk;
    private String direction;
    private String schematic;
    private String home;
    private String save;
    private String current;
    private String replaced;
    private String chest;
    private String button;
    private String repeater0;
    private String repeater1;
    private String repeater2;
    private String repeater3;
    private String companions;
    private String platform;
    private String save_sign;
    private String chameleon;
    private boolean chameleon_on;
    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;

    public ResultSetTardis(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String wheres;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("',");
            } else {
                sbw.append(entry.getValue()).append(",");
            }
        }
        wheres = sbw.toString().substring(0, sbw.length() - 1);
        where.clear();
        String query = "SELECT * FROM tardis WHERE " + wheres;
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()) {
                this.tardis_id = rs.getInt("tardis_id");
                this.owner = rs.getString("owner");
                this.chunk = rs.getString("chunk");
                this.direction = rs.getString("direction");
                this.schematic = rs.getString("size");
                this.home = rs.getString("home");
                this.save = rs.getString("save");
                this.current = rs.getString("current");
                this.replaced = rs.getString("replaced");
                this.chest = rs.getString("chest");
                this.button = rs.getString("button");
                this.repeater0 = rs.getString("repeater0");
                this.repeater1 = rs.getString("repeater1");
                this.repeater2 = rs.getString("repeater2");
                this.repeater3 = rs.getString("repeater3");
                this.companions = rs.getString("companions");
                this.platform = rs.getString("platform");
                this.save_sign = rs.getString("save_sign");
                this.chameleon = rs.getString("chameleon");
                this.chameleon_on = rs.getBoolean("chamele_on");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table! " + e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getOwner() {
        return owner;
    }

    public String getChunk() {
        return chunk;
    }

    public String getDirection() {
        return direction;
    }

    public String getSchematic() {
        return schematic;
    }

    public String getHome() {
        return home;
    }

    public String getSave() {
        return save;
    }

    public String getCurrent() {
        return current;
    }

    public String getReplaced() {
        return replaced;
    }

    public String getChest() {
        return chest;
    }

    public String getButton() {
        return button;
    }

    public String getRepeater0() {
        return repeater0;
    }

    public String getRepeater1() {
        return repeater1;
    }

    public String getRepeater2() {
        return repeater2;
    }

    public String getRepeater3() {
        return repeater3;
    }

    public String getCompanions() {
        return companions;
    }

    public String getPlatform() {
        return platform;
    }

    public String getSave_sign() {
        return save_sign;
    }

    public String getChameleon() {
        return chameleon;
    }

    public boolean getChameleon_on() {
        return chameleon_on;
    }
}