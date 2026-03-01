package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

public class AreaEnd {

    public void track(TARDIS plugin, Player player) {
        if (!plugin.getTrackerKeeper().getAreaStartBlock().containsKey(player.getUniqueId())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NO_START");
            return;
        }
        plugin.getTrackerKeeper().getAreaEndBlock().put(player.getUniqueId(), "end");
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_CLICK_END");
    }
}
