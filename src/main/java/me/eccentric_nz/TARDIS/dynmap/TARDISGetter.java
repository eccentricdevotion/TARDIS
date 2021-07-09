package me.eccentric_nz.TARDIS.dynmap;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.TARDISData;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TARDISGetter {

    private final TARDIS plugin;
    private final String prefix;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();

    TARDISGetter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = plugin.getPrefix();
    }

    List<TARDISData> getList(World world) {
        List<TARDISData> dataList = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT "
                + prefix + "tardis.tardis_id, "
                + prefix + "tardis.owner, "
                + prefix + "tardis.chameleon_preset, "
                + prefix + "tardis.size, "
                + prefix + "tardis.abandoned, "
                + prefix + "tardis.powered_on, "
                + prefix + "tardis.siege_on, "
                + prefix + "current.x, "
                + prefix + "current.y, "
                + prefix + "current.z"
                + " FROM " + prefix + "tardis, " + prefix + "current WHERE "
                + prefix + "tardis.tardis_id = " + prefix + "current.tardis_id";
        if (world != null) {
            query += " AND " + prefix + "current.world = '" + world.getName() + "'";
        } else {
            // build world list
            StringBuilder sb = new StringBuilder();
            // only get worlds that are enabled for time travel, and only regular worlds as dynmap doesn't support custom dimensions yet
            for (String planet : plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
                if (!TARDISConstants.isDatapackWorld(planet) && plugin.getPlanetsConfig().getBoolean("planets." + planet + ".time_travel")) {
                    sb.append("'" + planet + "',");
                }
            }
            query += " AND " + prefix + "current.world IN (" + sb.substring(0, sb.length() - 1) + ")";
        }
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("tardis_id");
                    // get TARDIS data
                    String owner = rs.getString("owner");
                    Location current = new Location(world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                    String console = rs.getString("size");
                    String chameleon = rs.getString("chameleon_preset");
                    String door = "Closed";
                    for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
                        if (!map.getKey().getWorld().getName().contains("TARDIS") && !map.getValue().isAbandoned()) {
                            if (id == map.getValue().getTardisId()) {
                                door = "Open";
                                break;
                            }
                        }
                    }
                    String powered = (rs.getBoolean("powered_on")) ? "Yes" : "No";
                    String siege = (rs.getBoolean("siege_on")) ? "Yes" : "No";
                    String abandoned = (rs.getBoolean("abandoned")) ? "Yes" : "No";
                    List<String> occupants = plugin.getTardisAPI().getPlayersInTARDIS(id);
                    TARDISData data = new TARDISData(owner, current, console, chameleon, door, powered, siege, abandoned, occupants);
                    dataList.add(data);
                }
            }
        } catch (SQLException e) {
            TARDIS.plugin.debug("ResultSet error for tardis/current table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                TARDIS.plugin.debug("Error closing tardis/current table! " + e.getMessage());
            }
        }
        return dataList;
    }
}
