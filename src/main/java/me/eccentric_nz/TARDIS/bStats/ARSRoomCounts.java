package me.eccentric_nz.TARDIS.bStats;

import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.Consoles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ARSRoomCounts {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final List<String> STONE = Collections.singletonList("STONE");
    private final List<Double> numberOfRoomsPerTARDIS = new ArrayList<>();

    public ARSRoomCounts(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public Map<String, Integer> getRoomCounts() {
        HashMap<String, Integer> data = new HashMap<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT json FROM " + prefix + "ars";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String json = rs.getString("json").replaceAll("[\"\\[\\]]", "");
                    double num = 0;
                    List<String> materials = new ArrayList<>(Arrays.asList(json.split(",")));
                    materials.removeAll(STONE);
                    for (String material : materials) {
                        // only count if not a console block
                        if (!Consoles.getBY_MATERIALS().keySet().contains(material)) {
                            num += 1.0;
                            String room = TARDISARS.ARSFor(material).toString();
                            int count = (data.containsKey(room)) ? data.get(room) + 1 : 1;
                            data.put(room, count);
                        }
                    }
                    numberOfRoomsPerTARDIS.add(num);
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for ars getting room counts! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing ars table getting room counts! " + e.getMessage());
            }
        }
        return data;
    }

    public String getMedian() {
        Collections.sort(numberOfRoomsPerTARDIS);
        double middle;
        if (numberOfRoomsPerTARDIS.size() % 2 == 0) {
            middle = (numberOfRoomsPerTARDIS.get(numberOfRoomsPerTARDIS.size() / 2) + numberOfRoomsPerTARDIS.get(numberOfRoomsPerTARDIS.size() / 2 - 1)) / 2.0;
        } else {
            middle = numberOfRoomsPerTARDIS.get(numberOfRoomsPerTARDIS.size() / 2);
        }
        return String.format("%.1f", middle);
    }
}
