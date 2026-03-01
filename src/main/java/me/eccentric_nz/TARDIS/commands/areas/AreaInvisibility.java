package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class AreaInvisibility {
    
    public void set(TARDIS plugin, String name, String value, Player player) {
        HashMap<String, Object> invisWhere = new HashMap<>();
        invisWhere.put("area_name", name);
        ResultSetAreas rsaInvis = new ResultSetAreas(plugin, invisWhere, false, false);
        if (!rsaInvis.resultSet()) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return;
        }
        value = value.toUpperCase(Locale.ROOT);
        if (!value.equals("ALLOW") && !value.equals("DENY")) {
            try {
                ChameleonPreset.valueOf(value);
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PRESET");
                return;
            }
        }
        HashMap<String, Object> invisSet = new HashMap<>();
        invisSet.put("invisibility", value);
        HashMap<String, Object> whereInvis = new HashMap<>();
        whereInvis.put("area_name", name);
        plugin.getQueryFactory().doUpdate("areas", invisSet, whereInvis);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_INVISIBILITY_SET", name);
    }
}
