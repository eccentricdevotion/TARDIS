package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;

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

    public void createTables() {
        try {
            statement = connection.createStatement();
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS tardis (tardis_id INTEGER PRIMARY KEY NOT NULL, owner TEXT COLLATE NOCASE, chunk TEXT, direction TEXT, home TEXT, save TEXT, current TEXT, replaced TEXT DEFAULT '', chest TEXT, button TEXT, repeater0 TEXT, repeater1 TEXT, repeater2 TEXT, repeater3 TEXT, companions TEXT, platform TEXT DEFAULT '', chameleon TEXT DEFAULT '', chamele_on INTEGER DEFAULT 0, size TEXT DEFAULT '', save_sign TEXT DEFAULT '', artron_button TEXT DEFAULT '', artron_level INTEGER DEFAULT 500)";
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
            String queryDestinations = "CREATE TABLE IF NOT EXISTS destinations (dest_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, dest_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER)";
            statement.executeUpdate(queryDestinations);
            String queryPresets = "CREATE TABLE IF NOT EXISTS areas (area_id INTEGER PRIMARY KEY NOT NULL, area_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', minx INTEGER, minz INTEGER, maxx INTEGER, maxz INTEGER)";
            statement.executeUpdate(queryPresets);
            // going to need to store room start locations, so we can jettison them easily
            String queryRooms = "CREATE TABLE IF NOT EXISTS rooms (room_id INTEGER PRIMARY KEY NOT NULL, tardis_id INTEGER, world TEXT COLLATE NOCASE DEFAULT '', startx INTEGER, starty INTEGER, startz INTEGER, endx INTEGER, endy INTEGER, endz INTEGER, room_type TEXT, room_direction TEXT DEFAULT 'SOUTH'";
            statement.executeUpdate(queryRooms);

            // just when I thought I'd got rid of them all... another check to add a column
            String queryAddArtron = "SELECT sql FROM sqlite_master WHERE tbl_name = 'player_prefs' AND sql LIKE '%artron_level INTEGER%'";
            ResultSet rsArtron = statement.executeQuery(queryAddArtron);
            if (!rsArtron.next()) {
                String queryAlter = "ALTER TABLE player_prefs ADD artron_level INTEGER DEFAULT 0";
                statement.executeUpdate(queryAlter);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Adding new Artron Levels to player prefs!");
            }
            // add artron levels to tardis table as well
            String queryAddTardis = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%artron_button TEXT%'";
            ResultSet rsTardis = statement.executeQuery(queryAddTardis);
            if (!rsTardis.next()) {
                String queryAlter2 = "ALTER TABLE tardis ADD artron_button TEXT DEFAULT ''";
                statement.executeUpdate(queryAlter2);
                String queryAlter3 = "ALTER TABLE tardis ADD artron_level INTEGER DEFAULT 0";
                statement.executeUpdate(queryAlter3);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Adding new Artron fields to tardis!");
            }
            // combine this with  above for release - these fields will record the middle block used to create the TARDIS so that rooms will use the same block
            String queryAddMiddle = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%middle_id INTEGER%'";
            ResultSet rsMiddle = statement.executeQuery(queryAddMiddle);
            if (!rsMiddle.next()) {
                String queryAlter4 = "ALTER TABLE tardis ADD middle_id INTEGER";
                statement.executeUpdate(queryAlter4);
                String queryAlter5 = "ALTER TABLE tardis ADD middle_data INTEGER";
                statement.executeUpdate(queryAlter5);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Adding new middle block fields to tardis!");
            }
            // and update existing TARDISs with 500 Artron Energy?

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