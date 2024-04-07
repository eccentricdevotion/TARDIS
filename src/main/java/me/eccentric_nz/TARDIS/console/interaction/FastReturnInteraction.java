package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.control.actions.FastReturnAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FastReturnInteraction {

    private final TARDIS plugin;

    public FastReturnInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setBack(int id, Player player) {
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "INPUT_MISSING");
            return;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        new FastReturnAction(plugin).clickButton(player, id, tardis);
    }
}
