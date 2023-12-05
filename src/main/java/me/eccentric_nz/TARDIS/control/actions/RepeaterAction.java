package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.RepeaterControl;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.Player;

public class RepeaterAction {

    private final TARDIS plugin;

    public RepeaterAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void announce(Player player, Block block, int type) {
        // message setting when clicked
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        if (rsp.resultSet() && rsp.isAnnounceRepeatersOn()) {
            Repeater repeater = (Repeater) block.getBlockData();
            int delay = repeater.getDelay();
            if (delay == 4) {
                delay = 0;
            }
            RepeaterControl rc = RepeaterControl.getControl(type);
            plugin.getMessenger().announceRepeater(player, rc.getDescriptions().get(delay));
        }
    }
}
