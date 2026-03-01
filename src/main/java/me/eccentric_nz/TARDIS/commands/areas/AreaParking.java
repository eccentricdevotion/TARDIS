package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AreaParking {

    public void set(TARDIS plugin, Player player, String name, int distance) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("area_name", name);
        HashMap<String, Object> set = new HashMap<>();
        set.put("parking_distance", distance);
        plugin.getQueryFactory().doUpdate("areas", set, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_PARK_SET", name);
    }
}
