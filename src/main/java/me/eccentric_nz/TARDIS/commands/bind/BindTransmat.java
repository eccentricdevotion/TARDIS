package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BindTransmat {

    public void click(TARDIS plugin, Player player, String which) {
        int id = BindUtility.checkForId(plugin, player);
        if (id != -1) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            ResultSetTransmat rst = new ResultSetTransmat(plugin, id, which);
            if (rst.resultSet()) {
                set.put("name", which);
            } else if (which.equalsIgnoreCase("console")) {
                set.put("name", "console");
            } else {
                // abort
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_NOT_FOUND");
                return;
            }
            set.put("type", 6);
            int bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
            plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_CLICK");
        }
    }
}
