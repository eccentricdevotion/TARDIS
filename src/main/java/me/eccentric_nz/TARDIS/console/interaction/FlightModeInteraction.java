package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FlightModeInteraction {

    private final TARDIS plugin;

    public FlightModeInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player) {
        String uuid = player.getUniqueId().toString();
        // get current throttle setting
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            int mode = rsp.getFlightMode() + 1;
            if (mode > 4) {
                mode = 1;
            }
            FlightMode fm = FlightMode.getByMode().get(mode);
            plugin.getMessenger().announceRepeater(player, TARDISStringUtils.capitalise(fm.toString()));
            HashMap<String, Object> setf = new HashMap<>();
            setf.put("flying_mode", mode);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            TARDIS.plugin.getQueryFactory().doUpdate("player_prefs", setf, where);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_SAVED");
            // TODO set custom model data for relativity differentiator item display
        }
    }
}
