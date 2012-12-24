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
        Statement statement = null;
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
                statement = connection.createStatement();
                statement.executeUpdate(queryAddBlock);
                statement.close();
            } catch (SQLException e) {
                plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Could not save block location to DB!");
            } finally {
                try {
                    statement.close();
                } catch (Exception e) {
                }
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
        Statement statement = null;
        if (ids.contains(bId)) {
            b.setTypeIdAndData(m, d, true);
            // remember replaced block location, TypeId and Data so we can restore it later
            try {
                Connection connection = service.getConnection();
                statement = connection.createStatement();
                String replaced = w.getName() + ":" + x + ":" + y + ":" + z + ":" + bId + ":" + bData;
                String queryReplaced = "UPDATE tardis SET replaced = '" + replaced + "' WHERE tardis_id = " + id;
                statement.executeUpdate(queryReplaced);
                statement.close();
            } catch (SQLException e) {
                plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + "Set Replaced Block Error: " + e);
            } finally {
                try {
                    statement.close();
                } catch (Exception e) {
                }
            }
        }
    }
    private static int[] startLoc = new int[6];

    public int[] getStartLocation(int id, Constants.COMPASS dir) {
        int cx, cz;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Connection connection = service.getConnection();
            statement = connection.createStatement();
            String queryChunk = "SELECT chunk FROM tardis WHERE tardis_id = " + id + " LIMIT 1";
            rs = statement.executeQuery(queryChunk);
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
            plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Get Chunk Error: " + e);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                statement.close();
            } catch (Exception e) {
            }
        }
        return startLoc;
    }

    public boolean checkChunk(String w, int x, int z, Constants.SCHEMATIC schm) {
        boolean chunkchk = false;
        int cw, cl;
        Statement statement = null;
        ResultSet rs = null;
        switch (schm) {
            case BIGGER:
                cw = roundUp(plugin.biggerdimensions[1], 16);
                cl = roundUp(plugin.biggerdimensions[2], 16);
                break;
            case DELUXE:
                cw = roundUp(plugin.deluxedimensions[1], 16);
                cl = roundUp(plugin.deluxedimensions[2], 16);
                break;
            default:
                cw = roundUp(plugin.budgetdimensions[1], 16);
                cl = roundUp(plugin.budgetdimensions[2], 16);
                break;
        }
        // check all the chunks that will be used by the schematic
        for (int cx = 0; cx < cw; cx++) {
            for (int cz = 0; cz < cl; cz++) {
                String queryCheck = "SELECT * FROM chunks WHERE world = '" + w + "' AND x =" + (x + cx) + " AND z = " + (z + cl) + " LIMIT 1";
                try {
                    Connection connection = service.getConnection();
                    statement = connection.createStatement();
                    rs = statement.executeQuery(queryCheck);
                    if (rs.next()) {
                        chunkchk = true;
                    }
                    rs.close();
                    statement.close();
                } catch (SQLException e) {
                    plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Get All Chunks Error: " + e);
                } finally {
                    try {
                        rs.close();
                    } catch (Exception e) {
                    }
                    try {
                        statement.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return chunkchk;
    }

    public static int roundUp(int num, int divisor) {
        return (num + divisor - 1) / divisor;
    }

    public int parseNum(String i) {
        int num = 0;
        try {
            num = Integer.parseInt(i);
        } catch (NumberFormatException n) {
            plugin.console.sendMessage("Could not convert to number");
        }
        return num;
    }

    public void updateTravellerCount(int id) {
        // how many travellers are in the TARDIS?
        Statement statement = null;
        ResultSet rsCount = null;
        try {
            Connection connection = service.getConnection();
            statement = connection.createStatement();
            String queryCount = "SELECT COUNT (*) AS count FROM travellers WHERE tardis_id = " + id;
            rsCount = statement.executeQuery(queryCount);
            if (rsCount.next()) {
                int count = rsCount.getInt("count");
                plugin.trackTravellers.put(id, count);
            }
            rsCount.close();
            statement.close();
        } catch (SQLException e) {
            plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " /TARDIS travel to location Error: " + e);
        } finally {
            try {
                rsCount.close();
            } catch (Exception e) {
            }
            try {
                statement.close();
            } catch (Exception e) {
            }
        }
    }
}