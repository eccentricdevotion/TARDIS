package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BindSave {

    public void click(TARDIS plugin, Player player, String which) {
        int id = BindUtility.checkForId(plugin, player);
        if (id != -1) {
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", id);
            whered.put("name", which);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                plugin.getMessenger().sendColouredCommand(player, "SAVE_NOT_FOUND", "/tardis list saves", plugin);
            } else {
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", id);
                set.put("type", 0);
                set.put("name", which);
                int bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_CLICK");
            }
        }
    }
}