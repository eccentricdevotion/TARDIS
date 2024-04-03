package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ThrottleInteraction {
    private final TARDIS plugin;

    public ThrottleInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player) {
        String uuid = player.getUniqueId().toString();
        // get current throttle setting
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            int delay = rsp.getThrottle() - 1;
            if (delay < 1) {
                delay = 4;
            }
            String throttle = SpaceTimeThrottle.getByDelay().get(delay).toString();
            // update player prefs
            HashMap<String, Object> wherer = new HashMap<>();
            wherer.put("uuid", uuid);
            HashMap<String, Object> setr = new HashMap<>();
            setr.put("throttle", delay);
            plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "THROTTLE", throttle);
            // TODO set custom model data for throttle item display
        }
    }
}
