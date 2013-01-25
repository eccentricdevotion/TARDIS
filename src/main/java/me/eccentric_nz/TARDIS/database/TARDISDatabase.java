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
import java.sql.DriverManager;
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
public class TARDISDatabase {

    private static TARDISDatabase instance = new TARDISDatabase();
    public Connection connection = null;
    public Statement statement = null;

    public static synchronized TARDISDatabase getInstance() {
        return instance;
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Creates the TARDIS default tables in the database.
     */
    public void createTables() {
        try {
            statement = connection.createStatement();
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS tardis (tardis_id INTEGER PRIMARY KEY NOT NULL, owner TEXT COLLATE NOCASE, chunk TEXT, direction TEXT, home TEXT, save TEXT, current TEXT, replaced TEXT DEFAULT '', chest TEXT, button TEXT, repeater0 TEXT, repeater1 TEXT, repeater2 TEXT, repeater3 TEXT, companions TEXT, platform TEXT DEFAULT '', chameleon TEXT DEFAULT '', chamele_on INTEGER DEFAULT 0, size TEXT DEFAULT '', save_sign TEXT DEFAULT '', artron_button TEXT DEFAULT '', artron_level INTEGER DEFAULT 0, creeper TEXT DEFAULT '', handbrake TEXT DEFAULT '', handbrake_on INT DEFAULT 1, tardis_init INTEGER DEFAULT 0, middle_id INTEGER, middle_data INTEGER, recharging INTEGER DEFAULT 0)";
            statement.executeUpdate(queryTARDIS);
            String queryTravellers = "CREATE TABLE IF NOT EXISTS travellers (traveller_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, player TEXT COLLATE NOCASE)";
            statement.executeUpdate(queryTravellers);
            String queryChunks = "CREATE TABLE IF NOT EXISTS chunks (chunk_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT, x INTEGER, z INTEGER)";
            statement.executeUpdate(queryChunks);
            String queryDoors = "CREATE TABLE IF NOT EXISTS doors (door_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, door_type INTEGER, door_location TEXT, door_direction TEXT DEFAULT 'SOUTH')";
            statement.executeUpdate(queryDoors);
            String queryPlayers = "CREATE TABLE IF NOT EXISTS player_prefs (pp_id INTEGER PRIMARY KEY NOT NULL, player TEXT COLLATE NOCASE, sfx_on INTEGER DEFAULT 0, platform_on INTEGER DEFAULT 0, quotes_on INTEGER DEFAULT 0, artron_level INTEGER DEFAULT 0)";
            statement.executeUpdate(queryPlayers);
            String queryProtectBlocks = "CREATE TABLE IF NOT EXISTS blocks (b_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', block INTEGER DEFAULT 0, data INTEGER DEFAULT 0)";
            statement.executeUpdate(queryProtectBlocks);
            String queryDestinations = "CREATE TABLE IF NOT EXISTS destinations (dest_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, dest_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER, bind TEXT DEFAULT '')";
            statement.executeUpdate(queryDestinations);
            String queryPresets = "CREATE TABLE IF NOT EXISTS areas (area_id INTEGER PRIMARY KEY NOT NULL, area_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', minx INTEGER, minz INTEGER, maxx INTEGER, maxz INTEGER)";
            statement.executeUpdate(queryPresets);
            String queryGravity = "CREATE TABLE IF NOT EXISTS gravity (g_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', upx INTEGER, upz INTEGER, downx INTEGER, downz INTEGER)";
            statement.executeUpdate(queryGravity);

            // just when I thought I'd got rid of them all... another check to add a column
            String queryAddArtron = "SELECT sql FROM sqlite_master WHERE tbl_name = 'player_prefs' AND sql LIKE '%artron_level INTEGER%'";
            ResultSet rsArtron = statement.executeQuery(queryAddArtron);
            if (!rsArtron.next()) {
                String queryAlter = "ALTER TABLE player_prefs ADD artron_level INTEGER DEFAULT 0";
                statement.executeUpdate(queryAlter);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Adding new Artron Levels to player prefs!");
            }
            // add other fields to tardis table as well
            String queryAddTardis = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%artron_button TEXT%'";
            ResultSet rsTardis = statement.executeQuery(queryAddTardis);
            if (!rsTardis.next()) {
                String queryAlter2 = "ALTER TABLE tardis ADD artron_button TEXT DEFAULT ''";
                statement.executeUpdate(queryAlter2);
                String queryAlter3 = "ALTER TABLE tardis ADD artron_level INTEGER DEFAULT 0";
                statement.executeUpdate(queryAlter3);
                String queryAlter4 = "ALTER TABLE tardis ADD middle_id INTEGER";
                statement.executeUpdate(queryAlter4);
                String queryAlter5 = "ALTER TABLE tardis ADD middle_data INTEGER";
                statement.executeUpdate(queryAlter5);
                String queryAlter6 = "ALTER TABLE tardis ADD creeper TEXT DEFAULT ':'";
                statement.executeUpdate(queryAlter6);
                String queryAlter7 = "ALTER TABLE tardis ADD tardis_init INTEGER DEFAULT 0";
                statement.executeUpdate(queryAlter7);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Adding new database fields to tardis!");
            }
            // add bind to destinations
            String queryAddBind = "SELECT sql FROM sqlite_master WHERE tbl_name = 'destinations' AND sql LIKE '%bind TEXT%'";
            ResultSet rsBind = statement.executeQuery(queryAddBind);
            if (!rsBind.next()) {
                String queryAlter11 = "ALTER TABLE destinations ADD bind TEXT DEFAULT ''";
                statement.executeUpdate(queryAlter11);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Adding new bind field to destinations!");
            }
            // handbrake
            String queryAddHandbrake = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%handbrake TEXT%'";
            ResultSet rsHandbrake = statement.executeQuery(queryAddHandbrake);
            if (!rsHandbrake.next()) {
                String queryAlter8 = "ALTER TABLE tardis ADD handbrake TEXT DEFAULT ''";
                statement.executeUpdate(queryAlter8);
                String queryAlter9 = "ALTER TABLE tardis ADD handbrake_on INT DEFAULT 1";
                statement.executeUpdate(queryAlter9);
            }
            // recharge persistence
            String queryAddRecharge = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%recharging INTEGER%'";
            ResultSet rsRecharge = statement.executeQuery(queryAddRecharge);
            if (!rsRecharge.next()) {
                String queryAlter10 = "ALTER TABLE tardis ADD recharging INTEGER DEFAULT 0";
                statement.executeUpdate(queryAlter10);
            }

        } catch (SQLException e) {
            TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Create table error: " + e);
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}
