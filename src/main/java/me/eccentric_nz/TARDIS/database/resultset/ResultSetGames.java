package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResultSetGames {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int game_id;
    private String playerLocation;
    private String tetrisBoard;
    private String tetrisSign;
    private String pongDisplay;
    private List<UUID> pongUUIDs = new ArrayList<>();


    public ResultSetGames(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean fromId(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "games WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                game_id = rs.getInt("game_id");
                playerLocation = rs.getString("player_location");
                tetrisBoard = rs.getString("tetris_board");
                tetrisSign = rs.getString("tetris_sign");
                pongDisplay = rs.getString("pong_display");
                for (String u : rs.getString("pong_uuids").split(":")) {
                    try {
                        pongUUIDs.add(UUID.fromString(u));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for games table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing games table! " + e.getMessage());
            }
        }
        return false;
    }

    public int getGameId() {
        return game_id;
    }

    public String getPlayerLocation() {
        return playerLocation;
    }

    public String getTetrisBoard() {
        return tetrisBoard;
    }

    public String getTetrisSign() {
        return tetrisSign;
    }

    public String getPongDisplay() {
        return pongDisplay;
    }

    public List<UUID> getPongUUIDs() {
        return pongUUIDs;
    }
}
