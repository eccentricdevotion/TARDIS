package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class TARDISdatabase {

    private static TARDISdatabase instance = new TARDISdatabase();
    public Connection connection = null;
    public Statement statement;
    private static TARDIS plugin;

    public static synchronized TARDISdatabase getInstance() {
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
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS tardis (tardis_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, owner TEXT COLLATE NOCASE, chunk TEXT, direction TEXT, home TEXT, save TEXT, current TEXT, replaced TEXT DEFAULT '', chest TEXT, button TEXT, repeater0 TEXT, repeater1 TEXT, repeater2 TEXT, repeater3 TEXT, save1 TEXT DEFAULT '', save2 TEXT DEFAULT '', save3 TEXT DEFAULT '', companions TEXT, platform TEXT DEFAULT '', chameleon TEXT DEFAULT '', chamele_on INTEGER DEFAULT 0, size TEXT DEFAULT '')";
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
                System.out.println(Constants.MY_PLUGIN_NAME + " Adding new chameleon circuit!");
            }
            // update player_prefs if there is no quotes_on column
            String queryQuotes = "SELECT sql FROM sqlite_master WHERE tbl_name = 'player_prefs' AND sql LIKE '%quotes_on INTEGER%'";
            ResultSet rsQuotes = statement.executeQuery(queryQuotes);
            if (!rsQuotes.next()) {
                String queryAlter3 = "ALTER TABLE player_prefs ADD quotes_on INTEGER DEFAULT 1";
                statement.executeUpdate(queryAlter3);
                System.out.println(Constants.MY_PLUGIN_NAME + " Adding new quotes to player prefs!");
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
                System.out.println(Constants.MY_PLUGIN_NAME + " Adding door directions to doors table!");
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
                    System.out.println(Constants.MY_PLUGIN_NAME + " Adding tardis_ids to chunks table.");
                }
            }
            // update tardis if there is no size column
            String querySize = "SELECT sql FROM sqlite_master WHERE tbl_name = 'tardis' AND sql LIKE '%size TEXT%'";
            ResultSet rsSize = statement.executeQuery(querySize);
            if (!rsSize.next()) {
                String queryAlter6 = "ALTER TABLE tardis ADD size TEXT DEFAULT ''";
                statement.executeUpdate(queryAlter6);
                System.out.println(Constants.MY_PLUGIN_NAME + " Adding new TARDIS size to DB!");
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
                System.out.println(Constants.MY_PLUGIN_NAME + " Adding block ID and data to blocks table!");
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Create table error: " + e);
        }
    }

    public void insertChunks(String w, int x, int z) {
        try {
            statement = connection.createStatement();
            String queryChunkInsert = "INSERT INTO chunks (world, x, z) VALUES ('" + w + "'," + x + "," + z + ")";
            statement.executeUpdate(queryChunkInsert);
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Chunk insert error: " + e);
        }
    }

    public void insertTimelords(String p, String c, String d, String h, String s, String cur, String r, String chest, String b, String r0, String r1, String r2, String r3, String s1, String s2, String s3, String t) {
        try {
            statement = connection.createStatement();
            String queryTimelordInsert = "INSERT INTO tardis (owner,chunk,direction,home,save,current,replaced,chest,button,repeater0,repeater1,repeater2,repeater3,save1,save2,save3) VALUES ('" + p + "','" + c + "','" + d + "','" + h + "','" + s + "','" + cur + "','" + r + "','" + chest + "','" + b + "','" + r0 + "','" + r1 + "','" + r2 + "','" + r3 + "','" + s1 + "','" + s2 + "','" + s3 + "')";
            statement.executeUpdate(queryTimelordInsert);
            ResultSet rs = statement.getGeneratedKeys();
            int id;
            if (rs.next()) {
                id = rs.getInt(1);
                Constants.COMPASS dir = Constants.COMPASS.valueOf(d);
                // determine door location
                String[] inner_data = c.split(":");
                String[] outer_data = s.split(":");
                String innerdoorloc = "";
                String outerdoorloc = "";
                int savedix = 0, savediy = 0, savediz = 0, savedox = 0, savedoy = 0, savedoz = 0, newox = 0, newoz = 0, newix = 0, newiz = 0;
                try {
                    savedix = Integer.parseInt(inner_data[1]);
                    savediz = Integer.parseInt(inner_data[2]);
                    savedox = Integer.parseInt(outer_data[1]);
                    savedoy = Integer.parseInt(outer_data[2]);
                    savedoz = Integer.parseInt(outer_data[3]);
                } catch (NumberFormatException n) {
                    System.err.println(Constants.MY_PLUGIN_NAME + "Could not convert to number");
                }
                World w = Bukkit.getServer().getWorld(inner_data[0]);
                Chunk chunk = w.getChunkAt(savedix, savediz);
                int newoy = savedoy - 2;
                switch (dir) {
                    case SOUTH:
                        newix = chunk.getBlock(6, 19, 1).getX();
                        newiz = chunk.getBlock(6, 19, 1).getZ();
                        newox = savedox;
                        newoz = savedoz - 1;
                        innerdoorloc = inner_data[0] + ":" + newix + ":19:" + newiz;
                        outerdoorloc = outer_data[0] + ":" + newox + ":" + newoy + ":" + newoz;
                        break;
                    case EAST:
                        newix = chunk.getBlock(1, 19, 9).getX();
                        newiz = chunk.getBlock(1, 19, 9).getZ();
                        newox = savedox - 1;
                        newoz = savedoz;
                        innerdoorloc = inner_data[0] + ":" + newix + ":19:" + newiz;
                        outerdoorloc = outer_data[0] + ":" + newox + ":" + newoy + ":" + newoz;
                        break;
                    case NORTH:
                        newix = chunk.getBlock(9, 19, 14).getX();
                        newiz = chunk.getBlock(9, 19, 14).getZ();
                        newox = savedox;
                        newoz = savedoz + 1;
                        innerdoorloc = inner_data[0] + ":" + newix + ":19:" + newiz;
                        outerdoorloc = outer_data[0] + ":" + newox + ":" + newoy + ":" + newoz;
                        break;
                    case WEST:
                        newix = chunk.getBlock(14, 19, 6).getX();
                        newiz = chunk.getBlock(14, 19, 6).getZ();
                        newox = savedox + 1;
                        newoz = savedoz;
                        innerdoorloc = inner_data[0] + ":" + newix + ":19:" + newiz;
                        outerdoorloc = outer_data[0] + ":" + newox + ":" + newoy + ":" + newoz;
                        break;
                }
                PreparedStatement prep = connection.prepareStatement("INSERT INTO doors VALUES (?,?,?,?)");
                //outer door
                prep.setInt(2, id);
                prep.setInt(3, 0);
                prep.setString(4, outerdoorloc);
                prep.execute();
                // inner door
                prep.setInt(2, id);
                prep.setInt(3, 1);
                prep.setString(4, innerdoorloc);
                prep.execute();
                if (t.equals("true")) {
                    String queryTraveller = "INSERT INTO travellers (tardis_id,player) VALUES (" + id + ",'" + p + "')";
                    statement.executeUpdate(queryTraveller);
                }
            } else {
                System.err.println(Constants.MY_PLUGIN_NAME + " Doors/Travellers insert error: Empty Result Set");
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Timelords insert error: " + e);
        }
    }

    public ResultSet getTardis(String name, String cols) {
        ResultSet tardis = null;
        try {
            statement = connection.createStatement();
            String queryTardis = "SELECT " + cols + " FROM tardis WHERE owner = '" + name + "'";
            tardis = statement.executeQuery(queryTardis);
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Timelords insert error: " + e);
        }

        return tardis;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}
