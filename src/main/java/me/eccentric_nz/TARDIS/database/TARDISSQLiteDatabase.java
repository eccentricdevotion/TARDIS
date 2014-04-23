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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Singleton class to get the database connection.
 *
 * Many facts, figures, and formulas are contained within the Matrix - a
 * supercomputer and micro-universe used by the High Council of the Time Lords
 * as a storehouse of knowledge to predict future events.
 *
 * @author eccentric_nz
 */
public class TARDISSQLiteDatabase {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private Statement statement = null;
    private final TARDIS plugin;

    public TARDISSQLiteDatabase(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the TARDIS default tables in the database.
     */
    public void createTables() {
        service.setIsMySQL(false);
        try {

            // Table structure for table 'ars'
            statement = connection.createStatement();
            String queryARS = "CREATE TABLE IF NOT EXISTS ars (ars_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', ars_x_east INTEGER DEFAULT 2, ars_z_south INTEGER DEFAULT 2, ars_y_layer INTEGER DEFAULT 1, json TEXT DEFAULT '')";
            statement.executeUpdate(queryARS);

            // Table structure for table 'achievements'
            String queryAchievements = "CREATE TABLE IF NOT EXISTS achievements (a_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', name TEXT DEFAULT '', amount TEXT DEFAULT '', completed INTEGER DEFAULT 0)";
            statement.executeUpdate(queryAchievements);

            // Table structure for table 'areas'
            String queryPresets = "CREATE TABLE IF NOT EXISTS areas (area_id INTEGER PRIMARY KEY NOT NULL, area_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', minx INTEGER, minz INTEGER, maxx INTEGER, maxz INTEGER, y INTEGER)";
            statement.executeUpdate(queryPresets);

            // Table structure for table 'back'
            String queryBack = "CREATE TABLE IF NOT EXISTS back (back_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0)";
            statement.executeUpdate(queryBack);

            // Table structure for table 'blocks'
            String queryProtectBlocks = "CREATE TABLE IF NOT EXISTS blocks (b_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', block INTEGER DEFAULT 0, data INTEGER DEFAULT 0, police_box INTEGER DEFAULT 0)";
            statement.executeUpdate(queryProtectBlocks);

            // Table structure for table 'chunks'
            String queryChunks = "CREATE TABLE IF NOT EXISTS chunks (chunk_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT, x INTEGER, z INTEGER)";
            statement.executeUpdate(queryChunks);

            // Table structure for table 'condenser'
            String queryCondenser = "CREATE TABLE IF NOT EXISTS condenser (c_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, block_data TEXT COLLATE NOCASE DEFAULT '', block_count INTEGER)";
            statement.executeUpdate(queryCondenser);

            // Table structure for table 'controls'
            String queryControls = "CREATE TABLE IF NOT EXISTS controls (c_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, type INTEGER, location TEXT COLLATE NOCASE DEFAULT '', secondary INTEGER DEFAULT 0)";
            statement.executeUpdate(queryControls);

            // Table structure for table 'current'
            String queryCurrents = "CREATE TABLE IF NOT EXISTS current (current_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0)";
            statement.executeUpdate(queryCurrents);

            // Table structure for table 'destinations'
            String queryDestinations = "CREATE TABLE IF NOT EXISTS destinations (dest_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, dest_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', bind TEXT DEFAULT '', type INTEGER DEFAULT 0, submarine INTEGER DEFAULT 0, slot INTEGER DEFAULT '-1')";
            statement.executeUpdate(queryDestinations);

            // Table structure for table 'doors'
            String queryDoors = "CREATE TABLE IF NOT EXISTS doors (door_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, door_type INTEGER, door_location TEXT, door_direction TEXT DEFAULT 'SOUTH', locked INTEGER DEFAULT 0)";
            statement.executeUpdate(queryDoors);

            // Table structure for table 'gravity_well'
            String queryGravity = "CREATE TABLE IF NOT EXISTS gravity_well (g_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', direction INTEGER, distance INTEGER DEFAULT 11, velocity REAL DEFAULT 0.5)";
            statement.executeUpdate(queryGravity);

            // Table structure for table 'homes'
            String queryHomes = "CREATE TABLE IF NOT EXISTS homes (home_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0)";
            statement.executeUpdate(queryHomes);

            // Table structure for table 'lamps'
            String queryLamps = "CREATE TABLE IF NOT EXISTS lamps (l_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '')";
            statement.executeUpdate(queryLamps);

            // Table structure for table 'next'
            String queryNext = "CREATE TABLE IF NOT EXISTS next (next_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0)";
            statement.executeUpdate(queryNext);

            // Table structure for table 'player_prefs'
            String queryPlayers = "CREATE TABLE IF NOT EXISTS player_prefs (pp_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', key TEXT DEFAULT '', sfx_on INTEGER DEFAULT 0, platform_on INTEGER DEFAULT 0, quotes_on INTEGER DEFAULT 0, artron_level INTEGER DEFAULT 0, wall TEXT DEFAULT 'ORANGE_WOOL', floor TEXT DEFAULT 'LIGHT_GREY_WOOL', auto_on INTEGER DEFAULT 0, beacon_on INTEGER DEFAULT 1, hads_on INTEGER DEFAULT 1, build_on INTEGER DEFAULT 1, eps_on INTEGER DEFAULT 0, eps_message TEXT DEFAULT '', lamp INTEGER, language TEXT DEFAULT 'AUTO_DETECT', texture_on INTEGER DEFAULT 0, texture_in TEXT DEFAULT '', texture_out TEXT DEFAULT 'default', submarine_on INTEGER DEFAULT 0, dnd_on INTEGER DEFAULT 0, minecart_on INTEGER DEFAULT 0, renderer_on INTEGER DEFAULT 1, wool_lights_on INTEGER DEFAULT 0)";
            statement.executeUpdate(queryPlayers);

            // reset storage table
            String s_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'storage'";
            ResultSet rss = statement.executeQuery(s_query);
            if (rss.next()) {
                String sd_query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'storage' AND sql LIKE '%console%'";
                ResultSet rssd = statement.executeQuery(sd_query);
                if (!rssd.next()) {
                    String s_drop = "DROP TABLE storage";
                    statement.executeUpdate(s_drop);
                }
            }

            // Table structure for table 'storage'
            String queryStorage = "CREATE TABLE IF NOT EXISTS storage (storage_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, uuid TEXT DEFAULT '', owner TEXT DEFAULT '', saves_one TEXT DEFAULT '', saves_two TEXT DEFAULT '', areas TEXT DEFAULT '', presets_one TEXT DEFAULT '', presets_two TEXT DEFAULT '', biomes_one TEXT DEFAULT '', biomes_two TEXT DEFAULT '', players TEXT DEFAULT '', circuits TEXT DEFAULT '', console TEXT DEFAULT '')";
            statement.executeUpdate(queryStorage);

            // Table structure for table 'tag'
            String queryTag = "CREATE TABLE IF NOT EXISTS tag (tag_id INTEGER PRIMARY KEY NOT NULL, player TEXT COLLATE NOCASE DEFAULT '', time INTEGER)";
            statement.executeUpdate(queryTag);

            // Table structure for table 'tardis'
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS tardis (tardis_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', owner TEXT COLLATE NOCASE DEFAULT '', chunk TEXT, tips INTEGER DEFAULT '-1', replaced TEXT DEFAULT '', companions TEXT, platform TEXT DEFAULT '', chameleon TEXT DEFAULT '', chamele_on INTEGER DEFAULT 0, chameleon_preset TEXT DEFAULT 'NEW', chameleon_demat TEXT DEFAULT 'NEW', chameleon_id INTEGER DEFAULT 35, chameleon_data INTEGER DEFAULT 11, adapti_on INTEGER DEFAULT 0, size TEXT DEFAULT '', save_sign TEXT DEFAULT '', artron_level INTEGER DEFAULT 0, creeper TEXT DEFAULT '', handbrake_on INTEGER DEFAULT 1, tardis_init INTEGER DEFAULT 0, middle_id INTEGER, middle_data INTEGER, condenser TEXT DEFAULT '', scanner TEXT DEFAULT '', farm TEXT DEFAULT '', stable TEXT DEFAULT '', recharging INTEGER DEFAULT 0, hidden INTEGER DEFAULT 0, lastuse INTEGER DEFAULT (strftime('%s', 'now')), iso_on INTEGER DEFAULT 0, beacon TEXT DEFAULT '', eps TEXT DEFAULT '', rail TEXT DEFAULT '', village TEXT DEFAULT '', renderer TEXT DEFAULT '', zero TEXT DEFAULT '')";
            statement.executeUpdate(queryTARDIS);

            // Table structure for table 'travellers'
            String queryTravellers = "CREATE TABLE IF NOT EXISTS travellers (traveller_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '')";
            statement.executeUpdate(queryTravellers);

            // Table structure for table 't_count'
            String queryT_Counts = "CREATE TABLE IF NOT EXISTS t_count (t_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', count INTEGER DEFAULT 0)";
            statement.executeUpdate(queryT_Counts);

            // delete old submerged, gravity and levers tables
            String dropSubmerged = "DROP TABLE IF EXISTS submerged";
            statement.executeUpdate(dropSubmerged);
            String dropGravity = "DROP TABLE IF EXISTS gravity";
            statement.executeUpdate(dropGravity);
            String dropLevers = "DROP TABLE IF EXISTS levers";
            statement.executeUpdate(dropLevers);

            // update tables
            TARDISSQLiteDatabaseUpdater dbu = new TARDISSQLiteDatabaseUpdater(plugin, statement);
            dbu.updateTables();

            // change RAISED preset to SWAMP
            String queryRaisedPreset = "UPDATE tardis SET chameleon_preset = 'SWAMP' WHERE chameleon_preset = 'RAISED'";
            statement.executeUpdate(queryRaisedPreset);
            String queryRaisedDemat = "UPDATE tardis SET chameleon_demat = 'SWAMP' WHERE chameleon_demat = 'RAISED'";
            statement.executeUpdate(queryRaisedDemat);

        } catch (SQLException e) {
            plugin.getConsole().sendMessage(TARDIS.plugin.getPluginName() + "SQLite create table error: " + e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.getConsole().sendMessage(TARDIS.plugin.getPluginName() + "SQLite close statement error: " + e);
            }
        }
    }
}
