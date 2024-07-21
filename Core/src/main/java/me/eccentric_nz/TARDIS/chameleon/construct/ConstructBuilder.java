package me.eccentric_nz.TARDIS.chameleon.construct;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonFrame;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ConstructBuilder {

    private final TARDIS plugin;

    public ConstructBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void build(String preset, int id, Player player) {
        // update tardis table
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("chameleon_preset", "CONSTRUCT");
        sett.put("chameleon_demat", preset);
        sett.put("adapti_on", 0);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", sett, wheret);
        // update the Chameleon Circuit sign(s)
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        wherec.put("type", Control.CHAMELEON.getId());
        ResultSetControls rsc = new ResultSetControls(plugin, wherec, true);
        if (rsc.resultSet()) {
            for (HashMap<String, String> map : rsc.getData()) {
                TARDISStaticUtils.setSign(map.get("location"), 3, "CONSTRUCT", player);
            }
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", Control.FRAME.getId());
        ResultSetControls rsf = new ResultSetControls(plugin, where, false);
        if (rsf.resultSet()) {
            new TARDISChameleonFrame().updateChameleonFrame(ChameleonPreset.CONSTRUCT, rsf.getLocation());
        }
        plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", "Construct", plugin);
        // rebuild
        player.performCommand("tardis rebuild");
        plugin.getTrackerKeeper().getConstructors().remove(player.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> close(player), 2L);
        // damage the circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            // decrement uses
            int uses_left = tcc.getChameleonUses();
            new TARDISCircuitDamager(plugin, DiskCircuit.CHAMELEON, uses_left, id, player).damage();
        }
    }

    protected void close(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }
}
