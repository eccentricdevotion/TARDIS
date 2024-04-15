package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.FlightModeModel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FlightModeInteraction {

    private final TARDIS plugin;

    public FlightModeInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player, Interaction interaction) {
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
            // set custom model data for relativity differentiator item display
            UUID model = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
            if (model != null) {
                ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(model);
                new FlightModeModel().setState(display, mode);
            }
            // TODO make relativity differentiator standalone control set player_prefs flightmode when toggled - exterior|normal
            // TODO then alter `isRelativityDifferentiated()` to check player prefs instead
            // should !exterior|normal flight modes even be accessible from this interaction? if so:
            // TODO make malfunction / manual / regulator classes?
        }
    }
}
