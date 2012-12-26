package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

public class ResultSetTravellers {
    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private int traveller_id;
    private int tardis_id;
    private String player;

    public ResultSetTravellers(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String wheres;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("',");
            } else {
                sbw.append(entry.getValue()).append(",");
            }
        }
        wheres = sbw.toString().substring(0, sbw.length() - 1);
        where.clear();
        String query = "SELECT * FROM travellers WHERE " + wheres;
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()) {
                this.traveller_id = rs.getInt("traveller_id");
                this.tardis_id = rs.getInt("tardis_id");
                this.player = rs.getString("player");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for travellers table! " + e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing travellers table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getTraveller_id() {
        return traveller_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getPlayer() {
        return player;
    }
}
