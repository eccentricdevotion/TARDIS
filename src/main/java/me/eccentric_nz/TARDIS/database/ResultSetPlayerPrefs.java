/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the personal preferences of the Time
 * lords themselves.
 *
 * @author eccentric_nz
 */
public class ResultSetPlayerPrefs {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private int pp_id;
    private UUID uuid;
    private boolean autoOn;
    private boolean autoPowerUp;
    private boolean autoRescueOn;
    private boolean autoSiegeOn;
    private boolean beaconOn;
    private boolean buildOn;
    private boolean ctmOn;
    private boolean DND;
    private boolean easyDifficulty;
    private boolean epsOn;
    private boolean farmOn;
    private boolean hadsOn;
    private boolean lanternsOn;
    private boolean minecartOn;
    private boolean policeboxTexturesOn;
    private boolean quotesOn;
    private boolean rendererOn;
    private boolean sfxOn;
    private boolean signOn;
    private boolean submarineOn;
    private boolean telepathyOn;
    private boolean textureOn;
    private boolean travelbarOn;
    private boolean woolLightsOn;
    private HADS hadsType;
    private int artronLevel;
    private int flightMode;
    private Material lamp;
    private String epsMessage;
    private String floor;
    private String hum;
    private String key;
    private String language;
    private String siegeFloor;
    private String siegeWall;
    private String textureIn;
    private String textureOut;
    private String wall;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the player_prefs table.
     *
     * @param plugin an instance of the main class.
     * @param where  a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     */
    public ResultSetPlayerPrefs(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the player_prefs table. This method builds an SQL query string from the
     * parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            where.forEach((key1, value) -> sbw.append(key1).append(" = ? AND "));
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM " + prefix + "player_prefs" + wheres;
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
            if (rs.next()) {
                pp_id = rs.getInt("pp_id");
                uuid = UUID.fromString(rs.getString("uuid"));
                key = (plugin.getConfig().getString("storage.database").equals("sqlite")) ? rs.getString("key") : rs.getString("key_item");
                sfxOn = rs.getBoolean("sfx_on");
                quotesOn = rs.getBoolean("quotes_on");
                autoOn = rs.getBoolean("auto_on");
                autoRescueOn = rs.getBoolean("auto_rescue_on");
                autoSiegeOn = rs.getBoolean("auto_siege_on");
                beaconOn = rs.getBoolean("beacon_on");
                hadsOn = rs.getBoolean("hads_on");
                hadsType = HADS.valueOf(rs.getString("hads_type"));
                submarineOn = rs.getBoolean("submarine_on");
                artronLevel = rs.getInt("artron_level");
                String trylamp = rs.getString("lamp");
                Material default_lamp;
                try {
                    default_lamp = Material.valueOf(plugin.getConfig().getString("police_box.tardis_lamp"));
                } catch (IllegalArgumentException e) {
                    default_lamp = Material.REDSTONE_LAMP;
                }
                if (rs.wasNull()) {
                    lamp = default_lamp;
                } else {
                    try {
                        lamp = Material.valueOf(trylamp);
                    } catch (IllegalArgumentException e) {
                        lamp = default_lamp;
                    }
                }
                language = rs.getString("language");
                wall = rs.getString("wall");
                floor = rs.getString("floor");
                siegeWall = rs.getString("siege_wall");
                siegeFloor = rs.getString("siege_floor");
                buildOn = rs.getBoolean("build_on");
                epsOn = rs.getBoolean("eps_on");
                // if empty use default
                String message = rs.getString("eps_message");
                if (rs.wasNull() || message.isEmpty()) {
                    epsMessage = "This is Emergency Programme One. I have died. I'm sure I will regenerate soon, but just in case. I have engaged the TARDIS autonomous circuit, and we are returning to my Home location or a recharge point - which ever is closest!";
                } else {
                    epsMessage = rs.getString("eps_message");
                }
                textureOn = rs.getBoolean("texture_on");
                textureIn = rs.getString("texture_in");
                String tp_out = rs.getString("texture_out");
                textureOut = (tp_out.equals("default")) ? plugin.getResourcePack() : tp_out;
                DND = rs.getBoolean("dnd_on");
                minecartOn = rs.getBoolean("minecart_on");
                rendererOn = rs.getBoolean("renderer_on");
                woolLightsOn = rs.getBoolean("wool_lights_on");
                ctmOn = rs.getBoolean("ctm_on");
                signOn = rs.getBoolean("sign_on");
                telepathyOn = rs.getBoolean("telepathy_on");
                travelbarOn = rs.getBoolean("travelbar_on");
                farmOn = rs.getBoolean("farm_on");
                lanternsOn = rs.getBoolean("lanterns_on");
                policeboxTexturesOn = rs.getBoolean("policebox_textures_on");
                flightMode = rs.getInt("flying_mode");
                easyDifficulty = rs.getBoolean("difficulty");
                autoPowerUp = rs.getBoolean("auto_powerup_on");
                hum = rs.getString("hum");
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

    public HADS getHadsType() {
        return hadsType;
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

    public Material getLamp() {
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

    public boolean isTelepathyOn() {
        return telepathyOn;
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

    public boolean isPoliceboxTexturesOn() {
        return policeboxTexturesOn;
    }

    public int getFlightMode() {
        return flightMode;
    }

    public boolean isEasyDifficulty() {
        return easyDifficulty;
    }

    public boolean isAutoPowerUp() {
        return autoPowerUp;
    }

    public boolean isAutoRescueOn() {
        return autoRescueOn;
    }

    public String getHum() {
        return hum;
    }
}
