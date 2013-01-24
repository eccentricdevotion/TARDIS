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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... everything about the construction of the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetTardis {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private String limit;
    private boolean multiple;
    private int tardis_id;
    private String owner;
    private String chunk;
    private TARDISConstants.COMPASS direction;
    private TARDISConstants.SCHEMATIC schematic;
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
    private boolean chamele_on;
    private int artron_level;
    private String artron_button;
    private int middle_id;
    private byte middle_data;
    private String creeper;
    private String handbrake;
    private boolean handbrake_on;
    private boolean tardis_init;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     * @param multiple a boolean indicating whether multiple rows should be
     * fetched
     */
    public ResultSetTardis(TARDIS plugin, HashMap<String, Object> where, String limit, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.limit = limit;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
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
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        if (!limit.equals("")) {
            thelimit = " LIMIT " + limit;
        }
        String query = "SELECT * FROM tardis" + wheres + thelimit;
        //plugin.debug(query);
        try {
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue().getClass().equals(String.class)) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        statement.setInt(s, plugin.utils.parseNum(entry.getValue().toString()));
                    }
                    s++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (multiple) {
                        HashMap<String, String> row = new HashMap<String, String>();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columns = rsmd.getColumnCount();
                        for (int i = 1; i < columns + 1; i++) {
                            row.put(rsmd.getColumnName(i).toLowerCase(), rs.getString(i));
                        }
                        data.add(row);
                    }
                    this.tardis_id = rs.getInt("tardis_id");
                    this.owner = rs.getString("owner");
                    this.chunk = rs.getString("chunk");
                    this.direction = TARDISConstants.COMPASS.valueOf(rs.getString("direction"));
                    this.schematic = TARDISConstants.SCHEMATIC.valueOf(rs.getString("size"));
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
                    this.chamele_on = rs.getBoolean("chamele_on");
                    this.artron_level = rs.getInt("artron_level");
                    this.artron_button = rs.getString("artron_button");
                    this.middle_id = rs.getInt("middle_id");
                    this.middle_data = rs.getByte("middle_data");
                    this.creeper = rs.getString("creeper");
                    this.handbrake = rs.getString("handbrake");
                    this.handbrake_on = rs.getBoolean("handbrake_on");
                    this.tardis_init = rs.getBoolean("tardis_init");
                }
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

    public TARDISConstants.COMPASS getDirection() {
        return direction;
    }

    public TARDISConstants.SCHEMATIC getSchematic() {
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

    public boolean isChamele_on() {
        return chamele_on;
    }

    public int getArtron_level() {
        return artron_level;
    }

    public String getArtron_button() {
        return artron_button;
    }

    public int getMiddle_id() {
        return middle_id;
    }

    public byte getMiddle_data() {
        return middle_data;
    }

    public String getCreeper() {
        return creeper;
    }

    public String getHandbrake() {
        return handbrake;
    }

    public boolean isHandbrake_on() {
        return handbrake_on;
    }

    public boolean isTardis_init() {
        return tardis_init;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}