package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

public class ResultSetDestinations {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private boolean multiple;
    private int dest_id;
    private int tardis_id;
    private String dest_name;
    private String world;
    private int x;
    private int y;
    private int z;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the destinations table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     * @param multiple a boolean indicating whether multiple rows should be
     * fetched
     */
    public ResultSetDestinations(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the destinations table. This method
     * builds an SQL query string from the parameters supplied and then executes
     * the query. Use the getters to retrieve the results.
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ");
                if (entry.getValue().getClass().equals(String.class)) {
                    sbw.append("'").append(entry.getValue()).append("' AND ");
                } else {
                    sbw.append(entry.getValue()).append(" AND ");
                }
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
            where.clear();
        }
        String query = "SELECT * FROM destinations" + wheres;
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (multiple) {
                        HashMap<String, String> row = new HashMap<String, String>();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columns = rsmd.getColumnCount();
                        for (int i = 1; i < columns + 1; i++) {
                            row.put(rsmd.getColumnName(i).toLowerCase(), rs.getString(i));
                        }
                        data.add(row);
                    }
                    this.dest_id = rs.getInt("traveller_id");
                    this.tardis_id = rs.getInt("tardis_id");
                    this.dest_name = rs.getString("dest_name");
                    this.world = rs.getString("world");
                    this.x = rs.getInt("x");
                    this.y = rs.getInt("y");
                    this.z = rs.getInt("z");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for destinations table! " + e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing destinations table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getDest_id() {
        return dest_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getDest_name() {
        return dest_name;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}
