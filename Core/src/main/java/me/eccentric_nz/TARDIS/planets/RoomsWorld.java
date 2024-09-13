package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;

import java.util.HashMap;

public class RoomsWorld {

    public void check(TARDIS plugin) {
        ResultSetTransmat rst = new ResultSetTransmat(plugin, -1, "rooms");
        if (!rst.resultSet()) {
            // insert record
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", -1);
            set.put("name", "rooms");
            set.put("world", "rooms");
            set.put("x", plugin.getPlanetsConfig().getDouble("planets.rooms.transmat.x", 8.5d));
            set.put("y", plugin.getPlanetsConfig().getDouble("planets.rooms.transmat.y", 68.0d));
            set.put("z", plugin.getPlanetsConfig().getDouble("planets.rooms.transmat.z", 2.5d));
            set.put("yaw", 0.0d);
            plugin.getQueryFactory().doInsert("transmats", set);
        }
    }

    public Transmat getTransmat(TARDIS plugin) {
        ResultSetTransmat rst = new ResultSetTransmat(plugin, -1, "rooms");
        return rst.resultSet() ? new Transmat("Rooms World", rst.getWorld(), rst.getX(), rst.getY(), rst.getZ(), rst.getYaw()) : null;
    }
}
