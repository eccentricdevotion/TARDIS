package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class AreaDirection {
    
    public void set(TARDIS plugin, String name, String value, Player player) {
        HashMap<String, Object> dirWhere = new HashMap<>();
        dirWhere.put("area_name", name);
        ResultSetAreas rsaDir = new ResultSetAreas(plugin, dirWhere, false, false);
        if (!rsaDir.resultSet()) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return;
        }
        String dir = value.toUpperCase(Locale.ROOT);
        try {
            COMPASS.valueOf(dir);
        } catch (IllegalArgumentException e) {
            if (dir.equals("NONE")) {
                dir = "";
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_COMPASS");
                return;
            }
        }
        HashMap<String, Object> dirSet = new HashMap<>();
        dirSet.put("direction", dir);
        HashMap<String, Object> whereDir = new HashMap<>();
        whereDir.put("area_name", name);
        plugin.getQueryFactory().doUpdate("areas", dirSet, whereDir);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_DIRECTION_SET", name);
    }
}
