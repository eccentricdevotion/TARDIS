package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BindPlayer {

    public void click(TARDIS plugin, Player player, Player target) {
        int id = BindUtility.checkForId(plugin, player);
        if (id != -1) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            set.put("name", target.getName());
            set.put("type", 2);
            int bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
            plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_CLICK");
        }
    }
}
