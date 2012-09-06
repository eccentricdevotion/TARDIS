package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TARDISdatabase {

    private static TARDISdatabase instance = new TARDISdatabase();
    public Connection connection = null;
    public int timeout = 30;
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
            String queryTARDIS = "CREATE TABLE IF NOT EXISTS tardis (tardis_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, owner TEXT COLLATE NOCASE, chunk TEXT, direction TEXT, home TEXT, save TEXT, current TEXT, replaced TEXT DEFAULT '', chest TEXT, button TEXT, repeater0 TEXT, repeater1 TEXT, repeater2 TEXT, repeater3 TEXT, save1 TEXT DEFAULT '', save2 TEXT DEFAULT '', save3 TEXT DEFAULT '', companions TEXT, platform TEXT)";
            statement.executeUpdate(queryTARDIS);
            String queryTravellers = "CREATE TABLE IF NOT EXISTS travellers (traveller_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, player TEXT COLLATE NOCASE)";
            statement.executeUpdate(queryTravellers);
            String queryChunks = "CREATE TABLE IF NOT EXISTS chunks (chunk_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, world TEXT, x INTEGER, z INTEGER)";
            statement.executeUpdate(queryChunks);
            String queryDoors = "CREATE TABLE IF NOT EXISTS doors (door_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tardis_id INTEGER, door_type INTEGER, door_location TEXT)";
            statement.executeUpdate(queryDoors);
        } catch (SQLException e) {
            System.err.println("[TARDIS] Create table error: " + e);
        }
    }

    public void insertChunks(String w, int x, int z) {
        try {
            statement = connection.createStatement();
            String queryChunkInsert = "INSERT INTO chunks (world, x, z) VALUES ('" + w + "'," + x + "," + z + ")";
            statement.executeUpdate(queryChunkInsert);
            statement.close();
        } catch (SQLException e) {
            System.err.println("[TARDIS] Chunk insert error: " + e);
        }
    }
    // service.insertTimelords(c,d,h,s,cur,r,chest,b,r0,r1,r2,r3,s1,s2,s3,t);

    public void insertTimelords(String p, String c, String d, String h, String s, String cur, String r, String chest, String b, String r0, String r1, String r2, String r3, String s1, String s2, String s3, String t) {
        try {
            statement = connection.createStatement();
            String queryTimelordInsert = "INSERT INTO tardis (owner,chunk,direction,home,save,current,replaced,chest,button,repeater0,repeater1,repeater2,repeater3,save1,save2,save3) VALUES ('" + p + "','" + c + "','" + d + "','" + h + "','" + s + "','" + cur + "','" + r + "','" + chest + "','" + b + "','" + r0 + "','" + r1 + "','" + r2 + "','" + r3 + "','" + s1 + "','" + s2 + "','" + s3 + "')";
            statement.executeUpdate(queryTimelordInsert);
            if (t.equals("true")) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    int id = rs.getInt(1);
                    String queryTraveller = "INSERT INTO travellers (tardis_id,player) VALUES (" + id + ",'" + p + "')";
                    statement.executeUpdate(queryTraveller);
                }
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("[TARDIS] Timelords insert error: " + e);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}