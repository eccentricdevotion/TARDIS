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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite database creator and updater.
 * <p>
 * Many facts, figures, and formulas are contained within the Matrix - a supercomputer and micro-universe used by the
 * High Council of the Time Lords as a storehouse of knowledge to predict future events.
 *
 * @author eccentric_nz
 */
public class TARDISSQLiteDatabase {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private Statement statement = null;

    public TARDISSQLiteDatabase(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Creates the TARDIS default tables in the database.
     */
    public void createTables() {
        service.setIsMySQL(false);
        try {
            statement = connection.createStatement();

            // Table structure for table 'achievements'
            String queryAchievements = "CREATE TABLE IF NOT EXISTS " + prefix + "achievements (a_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', name TEXT DEFAULT '', amount TEXT DEFAULT '', completed INTEGER DEFAULT 0)";
            statement.executeUpdate(queryAchievements);

            // Table structure for table 'arched'
            String queryArched = "CREATE TABLE IF NOT EXISTS " + prefix + "arched (uuid TEXT PRIMARY KEY NOT NULL, arch_name TEXT DEFAULT '', arch_time INTEGER DEFAULT 0)";
            statement.executeUpdate(queryArched);

            // Table structure for table 'archive'
            String queryArchive = "CREATE TABLE IF NOT EXISTS " + prefix + "archive (archive_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT, name TEXT, console_size TEXT, beacon INTEGER, lanterns INTEGER, use INTEGER, y INTEGER DEFAULT '64', data TEXT, description TEXT DEFAULT '')";
            statement.executeUpdate(queryArchive);

            // Table structure for table 'areas'
            String queryAreas = "CREATE TABLE IF NOT EXISTS " + prefix + "areas (area_id INTEGER PRIMARY KEY NOT NULL, area_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', minx INTEGER, minz INTEGER, maxx INTEGER, maxz INTEGER, y INTEGER, parking_distance INTEGER DEFAULT 2, invisibility TEXT DEFAULT 'ALLOW', direction TEXT DEFAULT '')";
            statement.executeUpdate(queryAreas);

            // Table structure for table 'ars'
            String queryARS = "CREATE TABLE IF NOT EXISTS " + prefix + "ars (ars_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', ars_x_east INTEGER DEFAULT 2, ars_z_south INTEGER DEFAULT 2, ars_y_layer INTEGER DEFAULT 1, json TEXT DEFAULT '')";
            statement.executeUpdate(queryARS);

            // Table structure for table 'back'
            String queryBack = "CREATE TABLE IF NOT EXISTS " + prefix + "back (back_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0)";
            statement.executeUpdate(queryBack);

            // Table structure for table 'bind'
            String queryBind = "CREATE TABLE IF NOT EXISTS " + prefix + "bind (bind_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, type INTEGER, location TEXT DEFAULT '', name TEXT DEFAULT '')";
            statement.executeUpdate(queryBind);

            // Table structure for table 'blocks'
            String queryProtectBlocks = "CREATE TABLE IF NOT EXISTS " + prefix + "blocks (b_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', data TEXT DEFAULT 'minecraft:air', police_box INTEGER DEFAULT 0)";
            statement.executeUpdate(queryProtectBlocks);

            // Table structure for table 'blueprint'
            String queryBlueprint = "CREATE TABLE IF NOT EXISTS " + prefix + "blueprint (bp_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', permission TEXT DEFAULT '')";
            statement.executeUpdate(queryBlueprint);

            // Table structure for table 'chameleon'
            String queryChameleon = "CREATE TABLE IF NOT EXISTS " + prefix + "chameleon (chameleon_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, blueprintData TEXT, stainData TEXT, glassData TEXT, line1 TEXT DEFAULT '', line2 TEXT DEFAULT '', line3 TEXT DEFAULT '', line4 TEXT DEFAULT '', asymmetric INTEGER DEFAULT 0)";
            statement.executeUpdate(queryChameleon);

            // Table structure for table 'chunks'
            String queryChunks = "CREATE TABLE IF NOT EXISTS " + prefix + "chunks (chunk_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT, x INTEGER, z INTEGER)";
            statement.executeUpdate(queryChunks);

            // Table structure for table 'condenser'
            String queryCondenser = "CREATE TABLE IF NOT EXISTS " + prefix + "condenser (c_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, block_data TEXT COLLATE NOCASE DEFAULT '', block_count INTEGER)";
            statement.executeUpdate(queryCondenser);

            // Table structure for table 'controls'
            String queryControls = "CREATE TABLE IF NOT EXISTS " + prefix + "controls (c_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, type INTEGER, location TEXT COLLATE NOCASE DEFAULT '', secondary INTEGER DEFAULT 0)";
            statement.executeUpdate(queryControls);

            // Table structure for table 'current'
            String queryCurrents = "CREATE TABLE IF NOT EXISTS " + prefix + "current (current_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0, biome TEXT DEFAULT '')";
            statement.executeUpdate(queryCurrents);

            // Table structure for table 'destinations'
            String queryDestinations = "CREATE TABLE IF NOT EXISTS " + prefix + "destinations (dest_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, dest_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', preset TEXT DEFAULT '', bind TEXT DEFAULT '', type INTEGER DEFAULT 0, submarine INTEGER DEFAULT 0, slot INTEGER DEFAULT '-1', icon TEXT DEFAULT '')";
            statement.executeUpdate(queryDestinations);

            // Table structure for table 'dispersed'
            String queryDispersed = "CREATE TABLE IF NOT EXISTS " + prefix + "dispersed (d_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, tardis_id INTEGER)";
            statement.executeUpdate(queryDispersed);

            // Table structure for table 'doors'
            String queryDoors = "CREATE TABLE IF NOT EXISTS " + prefix + "doors (door_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, door_type INTEGER, door_location TEXT, door_direction TEXT DEFAULT 'SOUTH', locked INTEGER DEFAULT 0)";
            statement.executeUpdate(queryDoors);

            // Table structure for table 'farming'
            String queryFarming = "CREATE TABLE IF NOT EXISTS " + prefix + "farming (farm_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, apiary TEXT DEFAULT '', aquarium TEXT DEFAULT '', bamboo TEXT DEFAULT '', birdcage TEXT DEFAULT '', farm TEXT DEFAULT '', hutch TEXT DEFAULT '', igloo TEXT DEFAULT '', stable TEXT DEFAULT '', stall TEXT DEFAULT '', village TEXT DEFAULT '')";
            statement.executeUpdate(queryFarming);

            // Table structure for table 'forcefield'
            String queryForce = "CREATE TABLE IF NOT EXISTS " + prefix + "forcefield (uuid TEXT DEFAULT '', location TEXT DEFAULT '')";
            statement.executeUpdate(queryForce);

            // Table structure for table 'gravity_well'
            String queryGravity = "CREATE TABLE IF NOT EXISTS " + prefix + "gravity_well (g_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', direction INTEGER, distance INTEGER DEFAULT 11, velocity REAL DEFAULT 0.5)";
            statement.executeUpdate(queryGravity);

            // Table structure for table 'homes'
            String queryHomes = "CREATE TABLE IF NOT EXISTS " + prefix + "homes (home_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0, preset TEXT DEFAULT '')";
            statement.executeUpdate(queryHomes);

            // Table structure for inventories
            String queryInventories = "CREATE TABLE IF NOT EXISTS " + prefix + "inventories (id INTEGER PRIMARY KEY NOT NULL, uuid TEXT, player TEXT, arch INTEGER, inventory TEXT, armour TEXT, attributes TEXT, armour_attributes TEXT)";
            statement.executeUpdate(queryInventories);

            // Table structure for junk
            String queryJunk = "CREATE TABLE IF NOT EXISTS " + prefix + "junk (id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', tardis_id INTEGER, save_sign TEXT COLLATE NOCASE DEFAULT '', handbrake TEXT COLLATE NOCASE DEFAULT '', wall TEXT DEFAULT 'ORANGE_WOOL', floor TEXT DEFAULT 'LIGHT_GRAY_WOOL', preset TEXT DEFAULT '')";
            statement.executeUpdate(queryJunk);

            // Table structure for table 'lamps'
            String queryLamps = "CREATE TABLE IF NOT EXISTS " + prefix + "lamps (l_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '')";
            statement.executeUpdate(queryLamps);

            // Table structure for table 'movers'
            String moversQuery = "CREATE TABLE IF NOT EXISTS " + prefix + "movers (uuid TEXT PRIMARY KEY NOT NULL)";
            statement.executeUpdate(moversQuery);

            // Table structure for table 'next'
            String queryNext = "CREATE TABLE IF NOT EXISTS " + prefix + "next (next_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, direction TEXT DEFAULT '', submarine INTEGER DEFAULT 0)";
            statement.executeUpdate(queryNext);

            // Table structure for table 'paper_bag'
            String queryPaperBag = "CREATE TABLE IF NOT EXISTS " + prefix + "paper_bag (paper_bag_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', flavour_1 TEXT DEFAULT '', amount_1 INTEGER, flavour_2 TEXT DEFAULT '', amount_2 INTEGER, flavour_3 TEXT DEFAULT '', amount_3 INTEGER, flavour_4 TEXT DEFAULT '', amount_4 INTEGER)";
            statement.executeUpdate(queryPaperBag);

            // Table structure for table 'player_prefs'
            String queryPlayers = "CREATE TABLE IF NOT EXISTS " + prefix + "player_prefs (pp_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', key TEXT DEFAULT '', sfx_on INTEGER DEFAULT 0, quotes_on INTEGER DEFAULT 0, artron_level INTEGER DEFAULT 0, wall TEXT DEFAULT 'ORANGE_WOOL', floor TEXT DEFAULT 'LIGHT_GRAY_WOOL', siege_wall TEXT DEFAULT 'GRAY_TERRACOTTA', siege_floor TEXT DEFAULT 'BLACK_TERRACOTTA', auto_on INTEGER DEFAULT 0, beacon_on INTEGER DEFAULT 1, hads_on INTEGER DEFAULT 1, hads_type TEXT DEFAULT 'DISPLACEMENT', build_on INTEGER DEFAULT 1, eps_on INTEGER DEFAULT 0, eps_message TEXT DEFAULT '', language TEXT DEFAULT 'ENGLISH', texture_on INTEGER DEFAULT 0, texture_in TEXT DEFAULT '', texture_out TEXT DEFAULT 'default', submarine_on INTEGER DEFAULT 0, dnd_on INTEGER DEFAULT 0, minecart_on INTEGER DEFAULT 0, renderer_on INTEGER DEFAULT 1, wool_lights_on INTEGER DEFAULT 0, sign_on INTEGER DEFAULT 1, telepathy_on INTEGER DEFAULT 0, travelbar_on INTEGER DEFAULT 0, farm_on INTEGER DEFAULT 1, lanterns_on INTEGER DEFAULT 0, auto_siege_on INTEGER DEFAULT 0, flying_mode INTEGER DEFAULT 1, throttle INTEGER DEFAULT 4, difficulty INTEGER DEFAULT 0, auto_powerup_on INTEGER DEFAULT 0, auto_rescue_on INTEGER DEFAULT 0, hum TEXT DEFAULT '')";
            statement.executeUpdate(queryPlayers);

            // Table structure for table 'portals'
            String queryPortals = "CREATE TABLE IF NOT EXISTS " + prefix + "portals (portal_id INTEGER PRIMARY KEY NOT NULL, portal TEXT DEFAULT '', teleport TEXT DEFAULT '', direction TEXT DEFAULT '', tardis_id INTEGER DEFAULT 0, abandoned INTEGER DEFAULT 0)";
            statement.executeUpdate(queryPortals);

            // Table structure for table 'programs'
            String queryHandles = "CREATE TABLE IF NOT EXISTS " + prefix + "programs (program_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', name TEXT DEFAULT '', inventory TEXT DEFAULT '', parsed TEXT DEFAULT '', checked INTEGER DEFAULT 1)";
            statement.executeUpdate(queryHandles);

            // Table structure for table 'reminders'
            String queryReminders = "CREATE TABLE IF NOT EXISTS " + prefix + "reminders (reminder_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', reminder TEXT DEFAULT '', time INTEGER DEFAULT '0')";
            statement.executeUpdate(queryReminders);

            // Table structure for table 'room_progress'
            String queryRoomProgress = "CREATE TABLE IF NOT EXISTS " + prefix + "room_progress (progress_id INTEGER PRIMARY KEY NOT NULL, direction TEXT DEFAULT '', room TEXT DEFAULT '', location TEXT DEFAULT '', tardis_id INTEGER DEFAULT 0, progress_row INTEGER DEFAULT 0, progress_column INTEGER DEFAULT 0, progress_level INTEGER DEFAULT 0, middle_type TEXT DEFAULT '', floor_type TEXT DEFAULT '', post_blocks TEXT DEFAULT '')";
            statement.executeUpdate(queryRoomProgress);

            // Table structure for table 'seeds'
            String querySeeds = "CREATE TABLE IF NOT EXISTS " + prefix + "seeds (seed_id INTEGER PRIMARY KEY NOT NULL, schematic TEXT DEFAULT '', wall TEXT DEFAULT 'ORANGE_WOOL', floor TEXT DEFAULT 'LIGHT_GRAY_WOOL', location TEXT DEFAULT '')";
            statement.executeUpdate(querySeeds);

            // Table structure for table 'siege'
            String querySiege = "CREATE TABLE IF NOT EXISTS " + prefix + "siege (siege_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', tardis_id INTEGER DEFAULT 0)";
            statement.executeUpdate(querySiege);

            // Table structure for table 'sonic'
            String querySonic = "CREATE TABLE IF NOT EXISTS " + prefix + "sonic (sonic_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', activated INTEGER DEFAULT 0, sonic_type TEXT DEFAULT '', bio INTEGER DEFAULT 0, diamond INTEGER DEFAULT 0, emerald INTEGER DEFAULT 0, redstone INTEGER DEFAULT 0, painter INTEGER DEFAULT 0, ignite INTEGER DEFAULT 0, arrow INTEGER DEFAULT 0, knockback INTEGER DEFAULT 0, model INTEGER DEFAULT 10000011, sonic_uuid TEXT DEFAULT '')";
            statement.executeUpdate(querySonic);

            // reset storage table
            String s_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "storage'";
            ResultSet rss = statement.executeQuery(s_query);
            if (rss.next()) {
                String sd_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "storage' AND sql LIKE '%console%'";
                ResultSet rssd = statement.executeQuery(sd_query);
                if (!rssd.next()) {
                    String s_drop = "DROP TABLE storage";
                    statement.executeUpdate(s_drop);
                }
            }
            // Table structure for table 'storage'
            String queryStorage = "CREATE TABLE IF NOT EXISTS " + prefix + "storage (storage_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, uuid TEXT DEFAULT '', owner TEXT DEFAULT '', saves_one TEXT DEFAULT '', saves_two TEXT DEFAULT '', areas TEXT DEFAULT '', presets_one TEXT DEFAULT '', presets_two TEXT DEFAULT '', biomes_one TEXT DEFAULT '', biomes_two TEXT DEFAULT '', players TEXT DEFAULT '', circuits TEXT DEFAULT '', console TEXT DEFAULT '')";
            statement.executeUpdate(queryStorage);

            // Table structure for table 'tag'
            String queryTag = "CREATE TABLE IF NOT EXISTS " + prefix + "tag (tag_id INTEGER PRIMARY KEY NOT NULL, player TEXT COLLATE NOCASE DEFAULT '', time INTEGER)";
            statement.executeUpdate(queryTag);

            // Table structure for table 'tardis'
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS " + prefix + "tardis (tardis_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', owner TEXT COLLATE NOCASE DEFAULT '', last_known_name TEXT COLLATE NOCASE DEFAULT '', chunk TEXT, tips INTEGER DEFAULT '-1', replaced TEXT DEFAULT '', companions TEXT, chameleon_preset TEXT DEFAULT 'FACTORY', chameleon_demat TEXT DEFAULT 'FACTORY', adapti_on INTEGER DEFAULT 0, size TEXT DEFAULT '', abandoned INTEGER DEFAULT 0, artron_level INTEGER DEFAULT 0, creeper TEXT DEFAULT '', handbrake_on INTEGER DEFAULT 1, tardis_init INTEGER DEFAULT 0, recharging INTEGER DEFAULT 0, hidden INTEGER DEFAULT 0, powered_on INTEGER DEFAULT 0, lights_on INTEGER DEFAULT 1, siege_on INTEGER DEFAULT 0, lastuse INTEGER DEFAULT (strftime('%s', 'now')), iso_on INTEGER DEFAULT 0, beacon TEXT DEFAULT '', eps TEXT DEFAULT '', rail TEXT DEFAULT '', renderer TEXT DEFAULT '', zero TEXT DEFAULT '', rotor TEXT DEFAULT '', monsters INTEGER DEFAULT 0)";
            statement.executeUpdate(queryTARDIS);

            // Table structure for table 'transmats'
            String queryTransmats = "CREATE TABLE IF NOT EXISTS " + prefix + "transmats (transmat_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x REAL DEFAULT 0.0, y REAL DEFAULT 0.0, z REAL DEFAULT 0.0, yaw REAL DEFAULT 0.0)";
            statement.executeUpdate(queryTransmats);

            // Table structure for table 'travel_stats'
            String queryTravelStats = "CREATE TABLE IF NOT EXISTS " + prefix + "travel_stats (travel_stats_id INTEGER PRIMARY KEY NOT NULL, travel_type TEXT DEFAULT '', tardis_id INTEGER, uuid TEXT DEFAULT '')";
            statement.executeUpdate(queryTravelStats);

            // Table structure for table 'traveled_to'
            String queryTraveledTo = "CREATE TABLE IF NOT EXISTS " + prefix + "traveled_to (uuid TEXT NOT NULL, environment TEXT NOT NULL)";
            statement.executeUpdate(queryTraveledTo);

            // Table structure for table 'travellers'
            String queryTravellers = "CREATE TABLE IF NOT EXISTS " + prefix + "travellers (traveller_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '')";
            statement.executeUpdate(queryTravellers);

            // Table structure for table 't_count'
            String queryT_Counts = "CREATE TABLE IF NOT EXISTS " + prefix + "t_count (t_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', player TEXT COLLATE NOCASE DEFAULT '', count INTEGER DEFAULT 0, grace INTEGER DEFAULT 0, repair INTEGER DEFAULT 0)";
            statement.executeUpdate(queryT_Counts);

            // Table structure for table 'vaults'
            String queryVaults = "CREATE TABLE IF NOT EXISTS " + prefix + "vaults (v_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', chest_type TEXT COLLATE NOCASE DEFAULT 'DROP', x INTEGER DEFAULT 0, y INTEGER DEFAULT 0, z INTEGER DEFAULT 0)";
            statement.executeUpdate(queryVaults);

            // Table structure for table 'thevoid'
            String queryVoid = "CREATE TABLE IF NOT EXISTS " + prefix + "thevoid (tardis_id INTEGER PRIMARY KEY NOT NULL)";
            statement.executeUpdate(queryVoid);

            // Table structure for table 'vortex'
            String queryVortex = "CREATE TABLE IF NOT EXISTS " + prefix + "vortex (tardis_id INTEGER PRIMARY KEY NOT NULL, task INTEGER DEFAULT 0)";
            statement.executeUpdate(queryVortex);

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
            String queryRaisedPreset = "UPDATE " + prefix + "tardis SET chameleon_preset = 'SWAMP' WHERE chameleon_preset = 'RAISED'";
            statement.executeUpdate(queryRaisedPreset);
            String queryRaisedDemat = "UPDATE " + prefix + "tardis SET chameleon_demat = 'SWAMP' WHERE chameleon_demat = 'RAISED'";
            statement.executeUpdate(queryRaisedDemat);
        } catch (SQLException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "SQLite create table error: " + e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "SQLite close statement error: " + e);
            }
        }
    }
}
