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

public class ResultSetAreas {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private boolean multiple;
    private int area_id;
    private String area_name;
    private String world;
    private String location;
    private int minx;
    private int minz;
    private int maxx;
    private int maxz;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the areas table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     * @param multiple a boolean indicating whether multiple rows should be
     * fetched
     */
    public ResultSetAreas(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the areas table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
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
                    sbw.append("'").append(entry.getValue()).append("',");
                } else {
                    sbw.append(entry.getValue()).append(",");
                }
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 1);
            where.clear();
        }
        String query = "SELECT * FROM areas" + wheres;
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
                    this.area_id = rs.getInt("traveller_id");
                    this.area_name = rs.getString("dest_name");
                    this.world = rs.getString("world");
                    this.minx = rs.getInt("minx");
                    this.minz = rs.getInt("minz");
                    this.maxx = rs.getInt("maxx");
                    this.maxz = rs.getInt("maxz");
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

    public int getArea_id() {
        return area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public String getWorld() {
        return world;
    }

    public String getLocation() {
        return location;
    }

    public int getMinx() {
        return minx;
    }

    public int getMinz() {
        return minz;
    }

    public int getMaxx() {
        return maxx;
    }

    public int getMaxz() {
        return maxz;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}