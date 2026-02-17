package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class BindButton {

    public void click(TARDIS plugin, Player player, Bind bind) {
        int id = BindUtility.checkForId(plugin, player);
        if (id != -1) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            set.put("type", 1);
            set.put("name", bind.toString().toLowerCase(Locale.ROOT));
            int bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
            plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_CLICK");
        }
    }
}
