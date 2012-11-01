package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TARDISArea {

    private TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();

    public TARDISArea(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean areaCheckInExisting(Location l) {
        boolean chk = true;
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String w = l.getWorld().getName();
            String queryArea = "SELECT * FROM areas WHERE world = '" + w + "'";
            ResultSet rsArea = statement.executeQuery(queryArea);
            if (rsArea.isBeforeFirst()) {
                while (rsArea.next()) {
                    int minx = rsArea.getInt("minx");
                    int minz = rsArea.getInt("minz");
                    int maxx = rsArea.getInt("maxx");
                    int maxz = rsArea.getInt("maxz");
                    // is clicked block within a defined TARDIS area?
                    if (l.getX() <= maxx && l.getZ() <= maxz && l.getX() >= minx && l.getZ() >= minz) {
                        chk = false;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Area resultset check error: " + e);
        }
        return chk;
    }

    public boolean areaCheckLocPlayer(Player p, Location l) {
        boolean chk = false;
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String w = l.getWorld().getName();
            String queryArea = "SELECT * FROM areas WHERE world = '" + w + "'";
            ResultSet rsArea = statement.executeQuery(queryArea);
            int i = 1;
            if (rsArea.isBeforeFirst()) {
                while (rsArea.next()) {
                    String n = rsArea.getString("area_name");
                    int minx = rsArea.getInt("minx");
                    int minz = rsArea.getInt("minz");
                    int maxx = rsArea.getInt("maxx");
                    int maxz = rsArea.getInt("maxz");
                    // is time travel destination within a defined TARDIS area?
                    if (l.getX() <= maxx && l.getZ() <= maxz && l.getX() >= minx && l.getZ() >= minz) {
                        // does the player have permmission to travel here
                        if (!p.hasPermission("TARDIS.area." + n) || !p.isPermissionSet("TARDIS.area." + n)) {
                            plugin.trackPerm.put(p.getName(), "TARDIS.area." + n);
                            chk = true;
                            break;
                        }
                    }
                    i++;
                }
            }
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Area resultset check error: " + e);
        }
        return chk;
    }
}
