package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class TARDISArea {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

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
            rsArea.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Area block check error: " + e);
        }
        return chk;
    }

    public boolean areaCheckInExile(String a, Location l) {
        boolean chk = true;
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String queryArea = "SELECT * FROM areas WHERE area_name = '" + a + "'";
            ResultSet rsArea = statement.executeQuery(queryArea);
            if (rsArea.next()) {
                String w = rsArea.getString("world");
                String lw = l.getWorld().getName();
                int minx = rsArea.getInt("minx");
                int minz = rsArea.getInt("minz");
                int maxx = rsArea.getInt("maxx");
                int maxz = rsArea.getInt("maxz");
                // is clicked block within a defined TARDIS area?
                if (w.equals(lw) && (l.getX() <= maxx && l.getZ() <= maxz && l.getX() >= minx && l.getZ() >= minz)) {
                    chk = false;
                }
            }
            rsArea.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Area block check error: " + e);
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
                        if (!p.hasPermission("tardis.area." + n) || !p.isPermissionSet("tardis.area." + n)) {
                            plugin.trackPerm.put(p.getName(), "tardis.area." + n);
                            chk = true;
                            break;
                        }
                    }
                    i++;
                }
            }
            rsArea.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Area player check error: " + e);
        }
        return chk;
    }

    public Location getNextSpot(String a) {
        Location location = null;
        // find the next available slot in this area
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String queryArea = "SELECT * FROM areas WHERE area_name = '" + a + "'";
            ResultSet rsArea = statement.executeQuery(queryArea);
            if (rsArea.next()) {
                int minx = rsArea.getInt("minx");
                int x = minx + 2;
                int minz = rsArea.getInt("minz");
                int z = minz + 2;
                int maxx = rsArea.getInt("maxx");
                int maxz = rsArea.getInt("maxz");
                String wStr = rsArea.getString("world");
                rsArea.close();
                boolean chk = false;
                while (chk == false) {
                    String queryLoc = "SELECT save FROM tardis WHERE save LIKE '" + wStr + ":" + x + ":%:" + z + "'";
                    ResultSet rsLoc = statement.executeQuery(queryLoc);
                    if (rsLoc.next()) {
                        if (x <= maxx) {
                            x += 5;
                        } else {
                            x = minx + 2;
                            if (z <= maxz) {
                                z += 5;
                            } else {
                                z = minz + 2;
                            }
                        }
                    } else {
                        chk = true;
                        rsLoc.close();
                        statement.close();
                        break;
                    }
                }
                if (chk == true) {
                    World w = plugin.getServer().getWorld(wStr);
                    int y = w.getHighestBlockYAt(x, z);
                    location = w.getBlockAt(x, y, z).getLocation();
                }
            }
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Area parking error: " + e);
        }
        return location;
    }

    public String getExileArea(Player p) {
        Set<PermissionAttachmentInfo> perms = p.getEffectivePermissions();
        String area = "";
        for (PermissionAttachmentInfo pai : perms) {
            if (pai.getPermission().contains("tardis.area")) {
                int len = pai.getPermission().length();
                area = pai.getPermission().substring(12, len);
            }
        }
        return area;
    }
}
