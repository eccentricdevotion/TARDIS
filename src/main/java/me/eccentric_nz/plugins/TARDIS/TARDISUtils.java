package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TARDISUtils {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISUtils(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setBlock(World w, int x, int y, int z, int m, byte d) {
        Block b = w.getBlockAt(x, y, z);
        b.setTypeIdAndData(m, d, true);
    }

    public void setBlockAndRemember(World w, int x, int y, int z, int m, byte d, int id, boolean rebuild) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        int bid = b.getTypeId();
        String l = b.getLocation().toString();
        String queryAddBlock;
        if (rebuild == false) {
            if (bid != 0) {
                byte data = b.getData();
                queryAddBlock = "INSERT INTO blocks (tardis_id, location, block, data) VALUES (" + id + ",'" + l + "', " + bid + ", " + data + ")";
            } else {
                queryAddBlock = "INSERT INTO blocks (tardis_id, location) VALUES (" + id + ",'" + l + "')";
            }
            try {
                Connection connection = service.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(queryAddBlock);
            } catch (SQLException e) {
                System.err.println(Constants.MY_PLUGIN_NAME + " Could not save block location to DB!");
            }
        }
        b.setTypeIdAndData(m, d, true);
    }

    public void setBlockCheck(World w, int x, int y, int z, int m, byte d, int id) {
        // List of blocks that a door cannot be placed on
        List<Integer> ids = Arrays.asList(0, 6, 8, 9, 10, 11, 18, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 44, 46, 50, 51, 53, 54, 55, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 76, 77, 78, 79, 81, 83, 85, 89, 92, 93, 94, 96, 101, 102, 104, 105, 106, 107, 108, 109, 111, 113, 114, 115, 116, 117, 118, 119, 120, 122, 128, 130, 131, 132, 134, 135, 136);
        Block b = w.getBlockAt(x, y, z);
        Integer bId = Integer.valueOf(b.getTypeId());
        byte bData = b.getData();
        if (ids.contains(bId)) {
            b.setTypeIdAndData(m, d, true);
            // remember replaced block location, TypeId and Data so we can restore it later
            try {
                Connection connection = service.getConnection();
                Statement statement = connection.createStatement();
                String replaced = w.getName() + ":" + x + ":" + y + ":" + z + ":" + bId + ":" + bData;
                String queryReplaced = "UPDATE tardis SET replaced = '" + replaced + "' WHERE tardis_id = " + id;
                statement.executeUpdate(queryReplaced);
                statement.close();
            } catch (SQLException e) {
                System.err.println(Constants.MY_PLUGIN_NAME + "Set Replaced Block Error: " + e);
            }
        }
    }
    private static int[] startLoc = new int[6];

    public int[] getStartLocation(int id, Constants.COMPASS dir) {
        int cx = 0, cz = 0;
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String queryChunk = "SELECT chunk FROM tardis WHERE tardis_id = " + id + " LIMIT 1";
            ResultSet rs = statement.executeQuery(queryChunk);
            String chunkstr = rs.getString("chunk");
            String[] split = chunkstr.split(":");
            World w = plugin.getServer().getWorld(split[0]);
            cx = parseNum(split[1]);
            cz = parseNum(split[2]);
            Chunk chunk = w.getChunkAt(cx, cz);
            startLoc[0] = chunk.getBlock(0, 15, 0).getX();
            startLoc[1] = startLoc[0];
            startLoc[2] = chunk.getBlock(0, 15, 0).getZ();
            startLoc[3] = startLoc[2];
            startLoc[4] = 1;
            startLoc[5] = 1;
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Get Chunk Error: " + e);
        }
        return startLoc;
    }

    public boolean checkChunk(String w, int x, int z) {
        boolean chunkchk = false;
        String queryCheck = "SELECT * FROM chunks WHERE world = '" + w + "' AND x =" + x + " AND z = " + z + " LIMIT 1";
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(queryCheck);
            if (rs.next()) {
                chunkchk = true;
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Get All Chunks Error: " + e);
        }
        return chunkchk;
    }

    public int parseNum(String i) {
        int num = 0;
        try {
            num = Integer.parseInt(i);
        } catch (NumberFormatException n) {
            System.err.println("Could not convert to number");
        }
        return num;
    }

    public static String colorSign(String txt) {
        return txt.replace('&', '¤');
    }
}