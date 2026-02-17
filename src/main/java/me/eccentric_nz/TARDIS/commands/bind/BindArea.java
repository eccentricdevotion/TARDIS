package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BindArea {

    public void click(TARDIS plugin, Player player, String which) {
        int id = BindUtility.checkForId(plugin, player);
        if (id != -1) {
            // check area name
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("area_name", which);
            ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
            if (!rsa.resultSet()) {
                plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
                return;
            }
            if (!TARDISPermission.hasPermission(player, "tardis.area." + which) || !player.isPermissionSet("tardis.area." + which)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_NO_AREA_PERM", which);
                return;
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            set.put("name", which);
            set.put("type", 3);
            int bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
            plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_CLICK");
        }
    }
}
