package me.eccentric_nz.TARDIS.autonomous;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class TARDISAutonomousUtils {

    public static Location getRecharger(String world, Player player) {
        Location l = null;
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("world", world);
        ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, wherea, false, false);
        if (rsa.resultSet()) {
            String area = rsa.getArea().getAreaName();
            if (!TARDISPermission.hasPermission(player, "tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                return null;
            }
            if (rsa.getArea().isGrid()) {
                l = TARDIS.plugin.getTardisArea().getNextSpot(area);
            } else {
                l = TARDIS.plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().getAreaId());
            }
        }
        return l;
    }

    public static Location getConfiguredRecharger(Player player) {
        // get configured area names
        List<String> areas = TARDIS.plugin.getConfig().getStringList("autonomous_areas");
        // will always return the first area in the list if there is room to park
        for (String area : areas) {
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("area_name", area);
            ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, wherea, false, false);
            if (rsa.resultSet()) {
                if (!TARDISPermission.hasPermission(player, "tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                    return null;
                }
                Location l;
                if (rsa.getArea().isGrid()) {
                    l = TARDIS.plugin.getTardisArea().getNextSpot(area);
                } else {
                    l = TARDIS.plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().getAreaId());
                }
                if (l != null) {
                    return l;
                }
            }
        }
        return null;
    }

    public static boolean compareCurrentToHome(ResultSetCurrentFromId c, ResultSetHomeLocation h) {
        return (c.getWorld().equals(h.getWorld()) && c.getX() == h.getX() && c.getY() == h.getY() && c.getZ() == h.getZ());
    }
}
