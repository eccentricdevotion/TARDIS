package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.FlightModeModel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
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

    public void process(Player player, int id, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        String uuid = player.getUniqueId().toString();
        // get current throttle setting
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            int mode = rsp.getFlightMode() + 1;
            if (mode > 4) {
                mode = 1;
            }
            FlightMode flightMode = FlightMode.getByMode().get(mode);
            if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && flightMode == FlightMode.EXTERIOR &&  !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.EXTERIOR_FLIGHT)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Exterior Flight");
                mode = 1;
                flightMode = FlightMode.NORMAL;
            }
            plugin.getMessenger().announceRepeater(player, TARDISStringUtils.capitalise(flightMode.toString()));
            // update control record
            HashMap<String, Object> set = new HashMap<>();
            set.put("secondary", mode == 4 ? 1 : 0);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("type", 47);
            plugin.getQueryFactory().doUpdate("controls", set, where);
            // also update player prefs
            HashMap<String, Object> setp = new HashMap<>();
            setp.put("flying_mode", mode);
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doUpdate("player_prefs", setp, wherep);
            // set custom model data for relativity differentiator item display
            UUID model = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
            if (model != null) {
                ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(model);
                new FlightModeModel().setState(display, mode);
            }
        }
    }
}
