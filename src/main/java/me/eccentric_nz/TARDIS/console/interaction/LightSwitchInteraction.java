package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.actions.LightSwitchAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class LightSwitchInteraction {

    private final TARDIS plugin;

    public LightSwitchInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggle(int id, Player player) {
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        if (!tardis.isLightsOn() && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
            return;
        }
        new LightSwitchAction(plugin, id, tardis.isLightsOn(), player, tardis.getSchematic().getLights()).flickSwitch();
    }
}
