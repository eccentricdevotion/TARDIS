/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.chameleon.construct;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
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
        DamageUtility.run(plugin, DiskCircuit.CHAMELEON, id, player);
    }

    protected void close(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }
}
