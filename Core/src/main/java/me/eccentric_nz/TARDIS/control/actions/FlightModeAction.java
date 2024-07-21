package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FlightModeAction {

    private final TARDIS plugin;

    public FlightModeAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setMode(String uuid, Player player) {
        // get current flight mode
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            int mode = rsp.getFlightMode() + 1;
            if (mode > 4) {
                mode = 1;
            }
            // set flight mode
            HashMap<String, Object> setf = new HashMap<>();
            setf.put("flying_mode", mode);
            HashMap<String, Object> wheref = new HashMap<>();
            wheref.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doUpdate("player_prefs", setf, wheref);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_TOGGLED", FlightMode.getByMode().get(mode).toString());
        }
    }
}
