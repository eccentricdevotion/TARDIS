package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AreaAdd {

    public void setLocation(TARDIS plugin, String name, Player player) {
        // get area_id of specified area
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", name);
        ResultSetAreas rsaId = new ResultSetAreas(plugin, wherea, false, false);
        if (!rsaId.resultSet()) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return;
        }
        if (rsaId.getArea().grid()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NOT_GRID");
            return;
        }
        // get player's location
        Location location = player.getLocation();
        HashMap<String, Object> add = new HashMap<>();
        add.put("area_id", rsaId.getArea().areaId());
        add.put("world", location.getWorld().getName());
        add.put("x", location.getBlockX());
        add.put("y", location.getBlockY());
        add.put("z", location.getBlockZ());
        plugin.getQueryFactory().doInsert("area_locations", add);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_ADD_LOCATION", name);
    }
}
