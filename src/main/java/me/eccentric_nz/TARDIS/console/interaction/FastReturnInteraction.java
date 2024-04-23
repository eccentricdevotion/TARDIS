package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import me.eccentric_nz.TARDIS.control.actions.FastReturnAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FastReturnInteraction {

    private final TARDIS plugin;

    public FastReturnInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setBack(int id, Player player, Interaction interaction) {
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
        // set custom model data for random button item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin);
        }
        new FastReturnAction(plugin).clickButton(player, id, tardis);
    }
}
