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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final String limit;
    private final boolean multiple;
    private int tardis_id;
    private String owner;
    private String chunk;
    private int tips;
    private TARDISConstants.SCHEMATIC schematic;
    private String replaced;
    private String companions;
    private String platform;
    private String save_sign;
    private String chameleon;
    private boolean chamele_on;
    private int chameleon_id;
    private byte chameleon_data;
    private TARDISConstants.PRESET preset;
    private TARDISConstants.PRESET demat;
    private boolean adapti_on;
    private int artron_level;
    private int middle_id;
    private byte middle_data;
    private String creeper;
    private String condenser;
    private String beacon;
    private boolean handbrake_on;
    private boolean tardis_init;
    private boolean recharging;
    private String scanner;
    private String farm;
    private String stable;
    private boolean hidden;
    private long lastuse;
    private boolean iso_on;
    private String eps;
    private String rail;
    private String village;
    private final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

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
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        if (!limit.isEmpty()) {
            thelimit = " LIMIT " + limit;
        }
        String query = "SELECT * FROM tardis" + wheres + thelimit;
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
                            row.put(rsmd.getColumnName(i).toLowerCase(Locale.ENGLISH), rs.getString(i));
                        }
                        data.add(row);
                    }
                    this.tardis_id = rs.getInt("tardis_id");
                    this.owner = rs.getString("owner");
                    this.chunk = rs.getString("chunk");
                    this.tips = rs.getInt("tips");
                    this.schematic = TARDISConstants.SCHEMATIC.valueOf(rs.getString("size"));
                    this.replaced = rs.getString("replaced");
                    if (rs.wasNull()) {
                        this.replaced = "";
                    }
                    this.companions = rs.getString("companions");
                    if (rs.wasNull()) {
                        this.companions = "";
                    }
                    this.platform = rs.getString("platform");
                    if (rs.wasNull()) {
                        this.platform = "";
                    }
                    this.save_sign = rs.getString("save_sign");
                    this.chameleon = rs.getString("chameleon");
                    this.chamele_on = rs.getBoolean("chamele_on");
                    this.chameleon_id = rs.getInt("chameleon_id");
                    this.chameleon_data = rs.getByte("chameleon_data");
                    this.preset = TARDISConstants.PRESET.valueOf(rs.getString("chameleon_preset"));
                    this.demat = TARDISConstants.PRESET.valueOf(rs.getString("chameleon_demat"));
                    this.adapti_on = rs.getBoolean("adapti_on");
                    this.artron_level = rs.getInt("artron_level");
                    this.middle_id = rs.getInt("middle_id");
                    this.middle_data = rs.getByte("middle_data");
                    this.creeper = rs.getString("creeper");
                    this.condenser = rs.getString("condenser");
                    this.beacon = rs.getString("beacon");
                    this.handbrake_on = rs.getBoolean("handbrake_on");
                    this.tardis_init = rs.getBoolean("tardis_init");
                    this.recharging = rs.getBoolean("recharging");
                    this.scanner = rs.getString("scanner");
                    this.farm = rs.getString("farm");
                    this.stable = rs.getString("stable");
                    this.hidden = rs.getBoolean("hidden");
                    this.lastuse = rs.getLong("lastuse");
                    this.iso_on = rs.getBoolean("iso_on");
                    this.eps = rs.getString("eps");
                    this.rail = rs.getString("rail");
                    this.village = rs.getString("village");
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

    public int getTardis_id() {
        return tardis_id;
    }

    public String getOwner() {
        return owner;
    }

    public String getChunk() {
        return chunk;
    }

    public int getTIPS() {
        return tips;
    }

    public TARDISConstants.SCHEMATIC getSchematic() {
        return schematic;
    }

    public String getReplaced() {
        return replaced;
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

    public int getChameleon_id() {
        return chameleon_id;
    }

    public byte getChameleon_data() {
        return chameleon_data;
    }

    public TARDISConstants.PRESET getPreset() {
        return preset;
    }

    public TARDISConstants.PRESET getDemat() {
        return demat;
    }

    public boolean isAdapti_on() {
        return adapti_on;
    }

    public int getArtron_level() {
        return artron_level;
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

    public String getCondenser() {
        return condenser;
    }

    public String getBeacon() {
        return beacon;
    }

    public boolean isHandbrake_on() {
        return handbrake_on;
    }

    public boolean isTardis_init() {
        return tardis_init;
    }

    public boolean isRecharging() {
        return recharging;
    }

    public String getScanner() {
        return scanner;
    }

    public String getFarm() {
        return farm;
    }

    public String getStable() {
        return stable;
    }

    public boolean isHidden() {
        return hidden;
    }

    public long getLastuse() {
        return lastuse;
    }

    public boolean isIso_on() {
        return iso_on;
    }

    public String getEps() {
        return eps;
    }

    public String getRail() {
        return rail;
    }

    public String getVillage() {
        return village;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}
