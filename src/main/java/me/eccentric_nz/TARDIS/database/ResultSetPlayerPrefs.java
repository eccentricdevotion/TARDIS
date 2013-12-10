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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... the personal preferences of the Time lords themselves.
 *
 * @author eccentric_nz
 */
public class ResultSetPlayerPrefs {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private int pp_id;
    private String player;
    private String key;
    private boolean SFX_on;
    private boolean platform_on;
    private boolean quotes_on;
    private boolean auto_on;
    private boolean beacon_on;
    private boolean hads_on;
    private boolean submarine_on;
    private int artron_level;
    private int lamp;
    private String wall;
    private String floor;
    private boolean EPS_on;
    private String EPS_message;
    private boolean texture_on;
    private String texture_in;
    private String texture_out;
    private boolean DND;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the player_prefs table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public ResultSetPlayerPrefs(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
    }

    /**
     * Retrieves an SQL ResultSet from the player_prefs table. This method
     * builds an SQL query string from the parameters supplied and then executes
     * the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ? AND ");
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM player_prefs" + wheres;
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
            if (rs.next()) {
                this.pp_id = rs.getInt("pp_id");
                this.player = rs.getString("player");
                this.key = (plugin.getConfig().getString("database").equals("sqlite")) ? rs.getString("key") : rs.getString("key_item");
                this.SFX_on = rs.getBoolean("sfx_on");
                this.platform_on = rs.getBoolean("platform_on");
                this.quotes_on = rs.getBoolean("quotes_on");
                this.auto_on = rs.getBoolean("auto_on");
                this.beacon_on = rs.getBoolean("beacon_on");
                this.hads_on = rs.getBoolean("hads_on");
                this.submarine_on = rs.getBoolean("submarine_on");
                this.artron_level = rs.getInt("artron_level");
                this.lamp = rs.getInt("lamp");
                if (rs.wasNull()) {
                    this.lamp = plugin.getConfig().getInt("tardis_lamp");
                }
                this.wall = rs.getString("wall");
                this.floor = rs.getString("floor");
                this.EPS_on = rs.getBoolean("eps_on");
                // if empty use default
                String message = rs.getString("eps_message");
                if (rs.wasNull() || message.isEmpty()) {
                    this.EPS_message = "This is Emergency Programme One. I have died. I'm sure I will regenerate soon, but just in case. I have engaged the TARDIS autonomous circuit, and we are returning to my Home location or a recharge point - which ever is closest!";
                } else {
                    this.EPS_message = rs.getString("eps_message");
                }
                this.texture_on = rs.getBoolean("texture_on");
                this.texture_in = rs.getString("texture_in");
                String tp_out = rs.getString("texture_out");
                this.texture_out = (tp_out.equals("default")) ? plugin.tp : tp_out;
                this.DND = rs.getBoolean("dnd_on");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for player_prefs table! " + e.getMessage());
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
                plugin.debug("Error closing player_prefs table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getPp_id() {
        return pp_id;
    }

    public String getPlayer() {
        return player;
    }

    public String getKey() {
        return key;
    }

    public boolean isSFX_on() {
        return SFX_on;
    }

    public boolean isPlatform_on() {
        return platform_on;
    }

    public boolean isQuotes_on() {
        return quotes_on;
    }

    public boolean isAuto_on() {
        return auto_on;
    }

    public boolean isBeacon_on() {
        return beacon_on;
    }

    public boolean isHads_on() {
        return hads_on;
    }

    public int getArtron_level() {
        return artron_level;
    }

    public String getWall() {
        return wall;
    }

    public String getFloor() {
        return floor;
    }

    public boolean isEPS_on() {
        return EPS_on;
    }

    public String getEPS_message() {
        return EPS_message;
    }

    public boolean isTexture_on() {
        return texture_on;
    }

    public String getTexture_in() {
        return texture_in;
    }

    public String getTexture_out() {
        return texture_out;
    }

    public int getLamp() {
        return lamp;
    }

    public boolean isSubmarine_on() {
        return submarine_on;
    }

    public boolean isDND() {
        return DND;
    }
}
