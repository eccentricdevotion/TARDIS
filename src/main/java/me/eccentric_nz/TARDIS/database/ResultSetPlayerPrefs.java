/*
 * Copyright (C) 2014 eccentric_nz
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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;

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
    private UUID uuid;
    private String key;
    private boolean sfxOn;
    private boolean quotesOn;
    private boolean autoOn;
    private boolean autoSiegeOn;
    private boolean beaconOn;
    private boolean hadsOn;
    private boolean submarineOn;
    private int artronLevel;
    private int lamp;
    private String language;
    private String wall;
    private String floor;
    private String siegeWall;
    private String siegeFloor;
    private boolean buildOn;
    private boolean epsOn;
    private String epsMessage;
    private boolean textureOn;
    private String textureIn;
    private String textureOut;
    private boolean DND;
    private boolean minecartOn;
    private boolean rendererOn;
    private boolean woolLightsOn;
    private boolean ctmOn;
    private boolean signOn;
    private boolean travelbarOn;
    private boolean farmOn;
    private boolean lanternsOn;
    private int flightMode;
    private boolean easyDifficulty;
    private final String prefix;

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
        this.prefix = this.plugin.getPrefix();
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
        String query = "SELECT * FROM " + prefix + "player_prefs" + wheres;
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
            if (rs.next()) {
                this.pp_id = rs.getInt("pp_id");
                this.uuid = UUID.fromString(rs.getString("uuid"));
                this.key = (plugin.getConfig().getString("storage.database").equals("sqlite")) ? rs.getString("key") : rs.getString("key_item");
                this.sfxOn = rs.getBoolean("sfx_on");
                this.quotesOn = rs.getBoolean("quotes_on");
                this.autoOn = rs.getBoolean("auto_on");
                this.autoSiegeOn = rs.getBoolean("auto_siege_on");
                this.beaconOn = rs.getBoolean("beacon_on");
                this.hadsOn = rs.getBoolean("hads_on");
                this.submarineOn = rs.getBoolean("submarine_on");
                this.artronLevel = rs.getInt("artron_level");
                this.lamp = rs.getInt("lamp");
                this.language = rs.getString("language");
                if (rs.wasNull()) {
                    this.lamp = plugin.getConfig().getInt("police_box.tardis_lamp");
                }
                this.wall = rs.getString("wall");
                this.floor = rs.getString("floor");
                this.siegeWall = rs.getString("siege_wall");
                this.siegeFloor = rs.getString("siege_floor");
                this.buildOn = rs.getBoolean("build_on");
                this.epsOn = rs.getBoolean("eps_on");
                // if empty use default
                String message = rs.getString("eps_message");
                if (rs.wasNull() || message.isEmpty()) {
                    this.epsMessage = "This is Emergency Programme One. I have died. I'm sure I will regenerate soon, but just in case. I have engaged the TARDIS autonomous circuit, and we are returning to my Home location or a recharge point - which ever is closest!";
                } else {
                    this.epsMessage = rs.getString("eps_message");
                }
                this.textureOn = rs.getBoolean("texture_on");
                this.textureIn = rs.getString("texture_in");
                String tp_out = rs.getString("texture_out");
                this.textureOut = (tp_out.equals("default")) ? plugin.getResourcePack() : tp_out;
                this.DND = rs.getBoolean("dnd_on");
                this.minecartOn = rs.getBoolean("minecart_on");
                this.rendererOn = rs.getBoolean("renderer_on");
                this.woolLightsOn = rs.getBoolean("wool_lights_on");
                this.ctmOn = rs.getBoolean("ctm_on");
                this.signOn = rs.getBoolean("sign_on");
                this.travelbarOn = rs.getBoolean("travelbar_on");
                this.farmOn = rs.getBoolean("farm_on");
                this.lanternsOn = rs.getBoolean("lanterns_on");
                this.flightMode = rs.getInt("flying_mode");
                this.easyDifficulty = rs.getBoolean("difficulty");
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

    public UUID getUuid() {
        return uuid;
    }

    public String getKey() {
        return key;
    }

    public boolean isSfxOn() {
        return sfxOn;
    }

    public boolean isQuotesOn() {
        return quotesOn;
    }

    public boolean isAutoOn() {
        return autoOn;
    }

    public boolean isAutoSiegeOn() {
        return autoSiegeOn;
    }

    public boolean isBeaconOn() {
        return beaconOn;
    }

    public boolean isHadsOn() {
        return hadsOn;
    }

    public int getArtronLevel() {
        return artronLevel;
    }

    public String getWall() {
        return wall;
    }

    public String getFloor() {
        return floor;
    }

    public String getSiegeWall() {
        return siegeWall;
    }

    public String getSiegeFloor() {
        return siegeFloor;
    }

    public boolean isBuildOn() {
        return buildOn;
    }

    public boolean isEpsOn() {
        return epsOn;
    }

    public String getEpsMessage() {
        return epsMessage;
    }

    public boolean isTextureOn() {
        return textureOn;
    }

    public String getTextureIn() {
        return textureIn;
    }

    public String getTextureOut() {
        return textureOut;
    }

    public int getLamp() {
        return lamp;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isSubmarineOn() {
        return submarineOn;
    }

    public boolean isDND() {
        return DND;
    }

    public boolean isMinecartOn() {
        return minecartOn;
    }

    public boolean isRendererOn() {
        return rendererOn;
    }

    public boolean isWoolLightsOn() {
        return woolLightsOn;
    }

    public boolean isCtmOn() {
        return ctmOn;
    }

    public boolean isSignOn() {
        return signOn;
    }

    public boolean isTravelbarOn() {
        return travelbarOn;
    }

    public boolean isFarmOn() {
        return farmOn;
    }

    public boolean isLanternsOn() {
        return lanternsOn;
    }

    public int getFlightMode() {
        return flightMode;
    }

    public boolean isEasyDifficulty() {
        return easyDifficulty;
    }
}
