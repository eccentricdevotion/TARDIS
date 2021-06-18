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
package me.eccentric_nz.tardis.database;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TARDISes prefer the environment of the Space-Time Vortex to the four dimensional world. They have Curiosity Circuits
 * to encourage them to leave the Vortex.
 *
 * @author eccentric_nz
 */
class TardisMySqlDatabaseUpdater {

    private final List<String> areaUpdates = new ArrayList<>();
    private final List<String> tardisUpdates = new ArrayList<>();
    private final List<String> prefsUpdates = new ArrayList<>();
    private final List<String> destsUpdates = new ArrayList<>();
    private final List<String> countUpdates = new ArrayList<>();
    private final List<String> portalsUpdates = new ArrayList<>();
    private final List<String> inventoryUpdates = new ArrayList<>();
    private final List<String> chameleonUpdates = new ArrayList<>();
    private final List<String> farmingUpdates = new ArrayList<>();
    private final List<String> sonicUpdates = new ArrayList<>();
    private final HashMap<String, String> uuidUpdates = new HashMap<>();
    private final Statement statement;
    private final TardisPlugin plugin;
    private final String prefix;

    TardisMySqlDatabaseUpdater(TardisPlugin plugin, Statement statement) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        this.statement = statement;
        uuidUpdates.put("achievements", "a_id");
        uuidUpdates.put("ars", "tardis_id");
        uuidUpdates.put("player_prefs", "pp_id");
        uuidUpdates.put("storage", "tardis_id");
        uuidUpdates.put("t_count", "t_id");
        uuidUpdates.put("tardis", "tardis_id");
        uuidUpdates.put("travellers", "tardis_id");
        areaUpdates.add("parking_distance int(2) DEFAULT '2'");
        areaUpdates.add("invisibility varchar(32) DEFAULT 'ALLOW'");
        areaUpdates.add("direction varchar(5) DEFAULT ''");
        tardisUpdates.add("last_known_name varchar(32) DEFAULT ''");
        tardisUpdates.add("lights_on int(1) DEFAULT '1'");
        tardisUpdates.add("monsters int(2) DEFAULT '0'");
        tardisUpdates.add("abandoned int(1) DEFAULT '0'");
        tardisUpdates.add("powered_on int(1) DEFAULT '0'");
        tardisUpdates.add("renderer varchar(512) DEFAULT ''");
        tardisUpdates.add("rotor varchar(48) DEFAULT ''");
        tardisUpdates.add("siege_on int(1) DEFAULT '0'");
        tardisUpdates.add("zero varchar(512) DEFAULT ''");
        prefsUpdates.add("auto_rescue_on int(1) DEFAULT '0'");
        prefsUpdates.add("auto_siege_on int(1) DEFAULT '0'");
        prefsUpdates.add("build_on int(1) DEFAULT '1'");
        prefsUpdates.add("difficulty int(1) DEFAULT '0'");
        prefsUpdates.add("dnd_on int(1) DEFAULT '0'");
        prefsUpdates.add("farm_on int(1) DEFAULT '0'");
        prefsUpdates.add("flying_mode int(1) DEFAULT '1'");
        prefsUpdates.add("throttle int(1) DEFAULT '4'");
        prefsUpdates.add("hads_type varchar(12) DEFAULT 'DISPLACEMENT'");
        prefsUpdates.add("hum varchar(24) DEFAULT ''");
        prefsUpdates.add("language varchar(32) DEFAULT 'ENGLISH'");
        prefsUpdates.add("lanterns_on int(1) DEFAULT '0'");
        prefsUpdates.add("minecart_on int(1) DEFAULT '0'");
        prefsUpdates.add("renderer_on int(1) DEFAULT '1'");
        prefsUpdates.add("siege_floor varchar(64) DEFAULT 'BLACK_TERRACOTTA'");
        prefsUpdates.add("siege_wall varchar(64) DEFAULT 'GRAY_TERRACOTTA'");
        prefsUpdates.add("sign_on int(1) DEFAULT '1'");
        prefsUpdates.add("telepathy_on int(1) DEFAULT '0'");
        prefsUpdates.add("travelbar_on int(1) DEFAULT '0'");
        prefsUpdates.add("wool_lights_on int(1) DEFAULT '0'");
        prefsUpdates.add("auto_powerup_on int(1) DEFAULT '0'");
        destsUpdates.add("preset varchar(32) DEFAULT ''");
        destsUpdates.add("slot int(1) DEFAULT '-1'");
        destsUpdates.add("icon varchar(64) DEFAULT ''");
        countUpdates.add("grace int(3) DEFAULT '0'");
        portalsUpdates.add("abandoned int(1) DEFAULT '0'");
        inventoryUpdates.add("attributes text");
        inventoryUpdates.add("armour_attributes text");
        chameleonUpdates.add("line1 varchar(48) DEFAULT ''");
        chameleonUpdates.add("line2 varchar(48) DEFAULT ''");
        chameleonUpdates.add("line3 varchar(48) DEFAULT ''");
        chameleonUpdates.add("line4 varchar(48) DEFAULT ''");
        chameleonUpdates.add("asymmetric int(1) DEFAULT '0'");
        farmingUpdates.add("apiary varchar(512) DEFAULT ''");
        farmingUpdates.add("bamboo varchar(512) DEFAULT ''");
        sonicUpdates.add("arrow int(1) DEFAULT '0'");
        sonicUpdates.add("knockback int(1) DEFAULT '0'");
        sonicUpdates.add("model int(11) DEFAULT '10000011'");
        sonicUpdates.add("sonic_uuid varchar(48) DEFAULT ''");
    }

    /**
     * Adds new fields to tables in the database.
     */
    void updateTables() {
        int i = 0;
        try {
            for (Map.Entry<String, String> uuid : uuidUpdates.entrySet()) {
                String uuidQuery = "SHOW COLUMNS FROM " + prefix + uuid.getKey() + " LIKE 'uuid'";
                ResultSet rsUuid = statement.executeQuery(uuidQuery);
                if (!rsUuid.next()) {
                    i++;
                    String uuidAlter = "ALTER TABLE " + prefix + uuid.getKey() + " ADD uuid VARCHAR(48) DEFAULT '' AFTER " + uuid.getValue();
                    statement.executeUpdate(uuidAlter);
                }
            }
            for (String area : areaUpdates) {
                String[] areaSplit = area.split(" ");
                String areaQuery = "SHOW COLUMNS FROM " + prefix + "areas LIKE '" + areaSplit[0] + "'";
                ResultSet rsArea = statement.executeQuery(areaQuery);
                if (!rsArea.next()) {
                    i++;
                    String areaAlter = "ALTER TABLE " + prefix + "areas ADD " + area;
                    statement.executeUpdate(areaAlter);
                }
            }
            for (String tardis : tardisUpdates) {
                String[] tardisSplit = tardis.split(" ");
                String tardisQuery = "SHOW COLUMNS FROM " + prefix + "tardis LIKE '" + tardisSplit[0] + "'";
                ResultSet rsTardis = statement.executeQuery(tardisQuery);
                if (!rsTardis.next()) {
                    i++;
                    String tardisAlter = "ALTER TABLE " + prefix + "tardis ADD " + tardis;
                    statement.executeUpdate(tardisAlter);
                }
            }
            for (String pref : prefsUpdates) {
                String[] prefSplit = pref.split(" ");
                String prefQuery = "SHOW COLUMNS FROM " + prefix + "player_prefs LIKE '" + prefSplit[0] + "'";
                ResultSet rsPref = statement.executeQuery(prefQuery);
                if (!rsPref.next()) {
                    i++;
                    String prefAlter = "ALTER TABLE " + prefix + "player_prefs ADD " + pref;
                    statement.executeUpdate(prefAlter);
                }
            }
            for (String dest : destsUpdates) {
                String[] destSplit = dest.split(" ");
                String destQuery = "SHOW COLUMNS FROM " + prefix + "destinations LIKE '" + destSplit[0] + "'";
                ResultSet rsDest = statement.executeQuery(destQuery);
                if (!rsDest.next()) {
                    i++;
                    String destAlter = "ALTER TABLE " + prefix + "destinations ADD " + dest;
                    statement.executeUpdate(destAlter);
                }
            }
            for (String count : countUpdates) {
                String[] countSplit = count.split(" ");
                String countQuery = "SHOW COLUMNS FROM " + prefix + "t_count LIKE '" + countSplit[0] + "'";
                ResultSet rsCount = statement.executeQuery(countQuery);
                if (!rsCount.next()) {
                    i++;
                    String countAlter = "ALTER TABLE " + prefix + "t_count ADD " + count;
                    statement.executeUpdate(countAlter);
                }
            }
            for (String portal : portalsUpdates) {
                String[] portalSplit = portal.split(" ");
                String portalQuery = "SHOW COLUMNS FROM " + prefix + "portals LIKE '" + portalSplit[0] + "'";
                ResultSet rsPortal = statement.executeQuery(portalQuery);
                if (!rsPortal.next()) {
                    i++;
                    String portalAlter = "ALTER TABLE " + prefix + "portals ADD " + portal;
                    statement.executeUpdate(portalAlter);
                }
            }
            for (String inventory : inventoryUpdates) {
                String[] inventorySplit = inventory.split(" ");
                String inventoryQuery = "SHOW COLUMNS FROM " + prefix + "inventories LIKE '" + inventorySplit[0] + "'";
                ResultSet rsInventory = statement.executeQuery(inventoryQuery);
                if (!rsInventory.next()) {
                    i++;
                    String inventoryAlter = "ALTER TABLE " + prefix + "inventories ADD " + inventory;
                    statement.executeUpdate(inventoryAlter);
                }
            }
            for (String chameleon : chameleonUpdates) {
                String[] chameleonSplit = chameleon.split(" ");
                String chameleonQuery = "SHOW COLUMNS FROM " + prefix + "chameleon LIKE '" + chameleonSplit[0] + "'";
                ResultSet rsChameleon = statement.executeQuery(chameleonQuery);
                if (!rsChameleon.next()) {
                    i++;
                    String chameleonAlter = "ALTER TABLE " + prefix + "chameleon ADD " + chameleon;
                    statement.executeUpdate(chameleonAlter);
                }
            }
            for (String farming : farmingUpdates) {
                String[] farmingSplit = farming.split(" ");
                String farmingQuery = "SHOW COLUMNS FROM " + prefix + "farming LIKE '" + farmingSplit[0] + "'";
                ResultSet rsFarming = statement.executeQuery(farmingQuery);
                if (!rsFarming.next()) {
                    i++;
                    String farmingAlter = "ALTER TABLE " + prefix + "farming ADD " + farming;
                    statement.executeUpdate(farmingAlter);
                }
            }
            for (String sonic : sonicUpdates) {
                String[] sonicSplit = sonic.split(" ");
                String sonicQuery = "SHOW COLUMNS FROM " + prefix + "sonic LIKE '" + sonicSplit[0] + "'";
                ResultSet rsSonic = statement.executeQuery(sonicQuery);
                if (!rsSonic.next()) {
                    i++;
                    String sonicAlter = "ALTER TABLE " + prefix + "sonic ADD " + sonic;
                    statement.executeUpdate(sonicAlter);
                }
            }
            // update data type for `data` in blocks
            String blockDataCheck = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '" + prefix + "blocks' AND COLUMN_NAME = 'data'";
            ResultSet rsBlockDataCheck = statement.executeQuery(blockDataCheck);
            if (rsBlockDataCheck.next() && !rsBlockDataCheck.getString("DATA_TYPE").equalsIgnoreCase("text")) {
                String blockDataQuery = "ALTER TABLE " + prefix + "blocks CHANGE `data` `data` TEXT";
                statement.executeUpdate(blockDataQuery);
            }
            // update data type for `time` in tag
            String tagTimeCheck = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '" + prefix + "blocks' AND COLUMN_NAME = 'data'";
            ResultSet rsTagTimeCheck = statement.executeQuery(tagTimeCheck);
            if (rsTagTimeCheck.next() && !rsTagTimeCheck.getString("DATA_TYPE").equalsIgnoreCase("int")) {
                String tagTimeQuery = "ALTER TABLE " + prefix + "tag CHANGE `time` `time` BIGINT NULL DEFAULT '0'";
                statement.executeUpdate(tagTimeQuery);
            }
            // add biome to current location
            String bioQuery = "SHOW COLUMNS FROM " + prefix + "current LIKE 'biome'";
            ResultSet rsBio = statement.executeQuery(bioQuery);
            if (!rsBio.next()) {
                i++;
                String bioAlter = "ALTER TABLE " + prefix + "current ADD biome varchar(64) DEFAULT ''";
                statement.executeUpdate(bioAlter);
            }
            // add preset to homes
            String presetQuery = "SHOW COLUMNS FROM " + prefix + "homes LIKE 'biome'";
            ResultSet rsPreset = statement.executeQuery(presetQuery);
            if (!rsPreset.next()) {
                i++;
                String presetAlter = "ALTER TABLE " + prefix + "homes ADD preset varchar(64) DEFAULT ''";
                statement.executeUpdate(presetAlter);
            }
            // add tardis_id to dispersed
            String dispersedQuery = "SHOW COLUMNS FROM " + prefix + "dispersed LIKE 'tardis_id'";
            ResultSet rsDispersed = statement.executeQuery(dispersedQuery);
            if (!rsDispersed.next()) {
                i++;
                String dispersedAlter = "ALTER TABLE " + prefix + "dispersed ADD tardis_id int(11)";
                statement.executeUpdate(dispersedAlter);
                // update tardis_id column for existing records
                new TardisDispersalUpdater(plugin).updateTardisIds();
            }
            // add repair to t_count
            String repQuery = "SHOW COLUMNS FROM " + prefix + "t_count LIKE 'repair'";
            ResultSet rsRep = statement.executeQuery(repQuery);
            if (!rsRep.next()) {
                i++;
                String repAlter = "ALTER TABLE " + prefix + "t_count ADD repair int(3) DEFAULT '0'";
                statement.executeUpdate(repAlter);
            }
            // add task to vortex
            String vortexQuery = "SHOW COLUMNS FROM " + prefix + "vortex LIKE 'task'";
            ResultSet rsVortex = statement.executeQuery(vortexQuery);
            if (!rsVortex.next()) {
                i++;
                String vortexAlter = "ALTER TABLE " + prefix + "vortex ADD task int(11) DEFAULT '0'";
                statement.executeUpdate(vortexAlter);
            }
            // add post_blocks to room_progress
            String postQuery = "SHOW COLUMNS FROM " + prefix + "room_progress LIKE 'post_blocks'";
            ResultSet rsPost = statement.executeQuery(postQuery);
            if (!rsPost.next()) {
                i++;
                String postAlter = "ALTER TABLE " + prefix + "room_progress ADD post_blocks text NULL";
                statement.executeUpdate(postAlter);
            }
            // add chest_type to vaults
            String vctQuery = "SHOW COLUMNS FROM " + prefix + "vaults LIKE 'chest_type'";
            ResultSet rsVct = statement.executeQuery(vctQuery);
            if (!rsVct.next()) {
                i++;
                String vctAlter = "ALTER TABLE " + prefix + "vaults ADD chest_type varchar(8) DEFAULT 'DROP'";
                statement.executeUpdate(vctAlter);
            }
            // add y to archive
            String yQuery = "SHOW COLUMNS FROM " + prefix + "archive LIKE 'y'";
            ResultSet rsY = statement.executeQuery(yQuery);
            if (!rsY.next()) {
                i++;
                String yAlter = "ALTER TABLE " + prefix + "archive ADD y int(3) DEFAULT '64'";
                statement.executeUpdate(yAlter);
            }
            // transfer `void` data to `thevoid`, then remove `void` table
            String voidQuery = "SHOW TABLES LIKE '" + prefix + "void'";
            ResultSet rsVoid = statement.executeQuery(voidQuery);
            if (rsVoid.next()) {
                String getVoid = "SELECT * FROM '" + prefix + "void'";
                ResultSet rsV = statement.executeQuery(getVoid);
                while (rsV.next()) {
                    String transfer = "INSERT IGNORE INTO " + prefix + "thevoid (tardis_id) VALUES (" + rsV.getInt("tardis_id") + ")";
                    statement.executeUpdate(transfer);
                }
                String delVoid = "DROP TABLE '" + prefix + "void'";
                statement.executeUpdate(delVoid);
            }
        } catch (SQLException e) {
            plugin.debug("MySQL database add fields error: " + e.getMessage() + " " + e.getErrorCode());
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(TardisPlugin.plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the MySQL database!");
        }
    }
}
