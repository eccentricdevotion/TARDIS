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
    public Statement statement;

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
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS tardis (tardis_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, owner TEXT COLLATE NOCASE, chunk TEXT, direction TEXT, home TEXT, save TEXT, current TEXT, replaced TEXT DEFAULT '', chest TEXT, button TEXT, repeater0 TEXT, repeater1 TEXT, repeater2 TEXT, repeater3 TEXT, companions TEXT, platform TEXT DEFAULT '', chameleon TEXT DEFAULT '', chamele_on INTEGER DEFAULT 0, size TEXT DEFAULT '', save_sign TEXT DEFAULT '')";
            statement.executeUpdate(queryTARDIS);
            String queryTravellers = "CREATE TABLE IF NOT EXISTS travellers (traveller_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, player TEXT COLLATE NOCASE)";
            statement.executeUpdate(queryTravellers);
            String queryChunks = "CREATE TABLE IF NOT EXISTS chunks (chunk_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, world TEXT, x INTEGER, z INTEGER)";
            statement.executeUpdate(queryChunks);
            String queryDoors = "CREATE TABLE IF NOT EXISTS doors (door_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, door_type INTEGER, door_location TEXT, door_direction TEXT DEFAULT 'SOUTH')";
            statement.executeUpdate(queryDoors);
            String queryPlayers = "CREATE TABLE IF NOT EXISTS player_prefs (pp_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, player TEXT COLLATE NOCASE, sfx_on INTEGER DEFAULT 0, platform_on INTEGER DEFAULT 0)";
            statement.executeUpdate(queryPlayers);
            String queryProtectBlocks = "CREATE TABLE IF NOT EXISTS blocks (b_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, location TEXT COLLATE NOCASE DEFAULT '', block INTEGER DEFAULT 0, data INTEGER DEFAULT 0)";
            statement.executeUpdate(queryProtectBlocks);
            String queryDestinations = "CREATE TABLE IF NOT EXISTS destinations (dest_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, dest_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x INTEGER, y INTEGER, z INTEGER)";
            statement.executeUpdate(queryDestinations);
            String queryPresets = "CREATE TABLE IF NOT EXISTS areas (area_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, area_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', minx INTEGER, minz INTEGER, maxx INTEGER, maxz INTEGER)";
            statement.executeUpdate(queryPresets);
            // update tardis if there is no chameleon column
            String queryChameleon = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%chameleon TEXT%'";
            ResultSet rsChameleon = statement.executeQuery(queryChameleon);
            if (!rsChameleon.next()) {
                String queryAlter1 = "ALTER TABLE tardis ADD chameleon TEXT DEFAULT ''";
                String queryAlter2 = "ALTER TABLE tardis ADD chamele_on INTEGER DEFAULT 0";
                statement.executeUpdate(queryAlter1);
                statement.executeUpdate(queryAlter2);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Adding new chameleon circuit!");
            }
            // update player_prefs if there is no quotes_on column
            String queryQuotes = "SELECT sql FROM sqlite_master WHERE tbl_name = 'player_prefs' AND sql LIKE '%quotes_on INTEGER%'";
            ResultSet rsQuotes = statement.executeQuery(queryQuotes);
            if (!rsQuotes.next()) {
                String queryAlter3 = "ALTER TABLE player_prefs ADD quotes_on INTEGER DEFAULT 1";
                statement.executeUpdate(queryAlter3);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Adding new quotes to player prefs!");
            }
            // update doors if there is no door_direction
            String queryDoorDirection = "SELECT sql FROM sqlite_master WHERE tbl_name = 'doors' AND sql LIKE '%door_direction TEXT%'";
            ResultSet rsDoorDirection = statement.executeQuery(queryDoorDirection);
            if (!rsDoorDirection.next()) {
                String queryAlter4 = "ALTER TABLE doors ADD door_direction TEXT";
                statement.executeUpdate(queryAlter4);
                // find all TARDISs get & add direction to doors table
                String queryTardii = "SELECT tardis_id, direction FROM tardis";
                ResultSet rsTardii = statement.executeQuery(queryTardii);
                while (rsTardii.next()) {
                    int tid = rsTardii.getInt("tardis_id");
                    String dir = rsTardii.getString("direction");
                    String queryAddDoor = "UPDATE doors SET door_direction = '" + dir + "' WHERE tardis_id = " + tid;
                    statement.executeUpdate(queryAddDoor);
                }
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Adding door directions to doors table!");
            }
            // update chunks if there is no tardis_id column
            String queryUpChunks = "SELECT sql FROM sqlite_master WHERE tbl_name = 'chunks' AND sql LIKE '%tardis_id INTEGER%'";
            ResultSet rsUpChunks = statement.executeQuery(queryUpChunks);
            if (!rsUpChunks.next()) {
                String queryAlter5 = "ALTER TABLE chunks ADD tardis_id INTEGER";
                statement.executeUpdate(queryAlter5);

                // add tardis IDs to chunks table
                String queryAllChunks = "SELECT * FROM chunks WHERE chunk_id != 0";
                ResultSet rsAllChunks = statement.executeQuery(queryAllChunks);
                while (rsAllChunks.next()) {
                    int chunkID = rsAllChunks.getInt("chunk_id");
                    String queryTardisIDs = "SELECT tardis_id FROM tardis WHERE chunk = '" + rsAllChunks.getString("world") + ":" + rsAllChunks.getString("x") + ":" + rsAllChunks.getString("z") + "'";
                    ResultSet rsTID = statement.executeQuery(queryTardisIDs);
                    rsTID.next();
                    String queryUpdateChunks = "UPDATE chunks SET tardis_id = " + rsTID.getInt("tardis_id") + " WHERE chunk_id = " + chunkID + "";
                    statement.executeUpdate(queryUpdateChunks);
                    TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Adding tardis_ids to chunks table.");
                }
            }
            // update tardis if there is no size column
            String querySize = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%size TEXT%'";
            ResultSet rsSize = statement.executeQuery(querySize);
            if (!rsSize.next()) {
                String queryAlter6 = "ALTER TABLE tardis ADD size TEXT DEFAULT ''";
                statement.executeUpdate(queryAlter6);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Adding new TARDIS size to DB!");
                String queryAddSize = "UPDATE tardis SET size = 'BUDGET' WHERE size != 'BUDGET'";
                statement.executeUpdate(queryAddSize);
            }
            // update blocks if there is no data column
            String queryData = "SELECT sql FROM sqlite_master WHERE tbl_name = 'blocks' AND sql LIKE '%data INTEGER%'";
            ResultSet rsData = statement.executeQuery(queryData);
            if (!rsData.next()) {
                String queryAlter7 = "ALTER TABLE blocks ADD block INTEGER";
                String queryAlter8 = "ALTER TABLE blocks ADD data INTEGER";
                statement.executeUpdate(queryAlter7);
                statement.executeUpdate(queryAlter8);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Adding block ID and data to blocks table!");
            }
            // update tardis if there is no size column
            String querySign = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%save_sign TEXT%'";
            ResultSet rsSign = statement.executeQuery(querySign);
            if (!rsSign.next()) {
                String queryAlter6 = "ALTER TABLE tardis ADD save_sign TEXT DEFAULT ''";
                statement.executeUpdate(queryAlter6);
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Adding new TARDIS save_sign to DB!");
            }

            statement.close();
        } catch (SQLException e) {
            TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Create table error: " + e);
        }
    }

    public ResultSet getTardis(String name, String cols) {
        ResultSet tardis = null;
        try {
            statement = connection.createStatement();
            String queryTardis = "SELECT " + cols + " FROM tardis WHERE owner = '" + name + "'";
            tardis = statement.executeQuery(queryTardis);
        } catch (SQLException e) {
            TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " Timelords insert error: " + e);
        }
        return tardis;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}