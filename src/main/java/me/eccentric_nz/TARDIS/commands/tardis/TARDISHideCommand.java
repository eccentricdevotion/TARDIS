/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHideCommand {

    private final TARDIS plugin;

    public TARDISHideCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean hide(OfflinePlayer player) {
        if (player.getPlayer().hasPermission("tardis.rebuild")) {
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getHideCooldown().containsKey(uuid)) {
                long now = System.currentTimeMillis();
                long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                long then = plugin.getTrackerKeeper().getHideCooldown().get(uuid) + cooldown;
                if (now < then) {
                    TARDISMessage.send(player.getPlayer(), "COOLDOWN_HIDE", String.format("%d", cooldown / 1000));
                    return true;
                }
            }
            plugin.getTrackerKeeper().getHideCooldown().put(uuid, System.currentTimeMillis());
            int id;
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player.getPlayer(), "NO_TARDIS");
                return false;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                TARDISMessage.send(player.getPlayer(), "POWER_DOWN");
                return true;
            }
            if (rs.getPreset().equals(PRESET.INVISIBLE)) {
                TARDISMessage.send(player.getPlayer(), "INVISIBILITY_ENGAGED");
                return true;
            }
            id = rs.getTardis_id();
            TARDISCircuitChecker tcc = null;
            if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player.getPlayer(), true)) {
                tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMaterialisation()) {
                TARDISMessage.send(player.getPlayer(), "NO_MAT_CIRCUIT");
                return true;
            }
            HashMap<String, Object> wherein = new HashMap<String, Object>();
            wherein.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
            if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                TARDISMessage.send(player.getPlayer(), "TARDIS_NO_HIDE");
                return true;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                TARDISMessage.send(player.getPlayer(), "NOT_WHILE_MAT");
                return true;
            }
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", rs.getTardis_id());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player.getPlayer(), "CURRENT_NOT_FOUND");
                return true;
            }
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            int level = rs.getArtron_level();
            int hide = plugin.getArtronConfig().getInt("hide");
            if (level < hide) {
                TARDISMessage.send(player.getPlayer(), "ENERGY_NO_HIDE");
                return false;
            }
            final TARDISMaterialisationData pdd = new TARDISMaterialisationData(plugin, player.getUniqueId().toString());
            pdd.setChameleon(false);
            pdd.setDirection(rsc.getDirection());
            pdd.setLocation(l);
            pdd.setDematerialise(plugin.getConfig().getBoolean("police_box.materialise"));
            pdd.setPlayer(player.getPlayer());
            pdd.setHide(true);
            pdd.setOutside(false);
            pdd.setSubmarine(rsc.isSubmarine());
            pdd.setTardisID(id);
            pdd.setBiome(rsc.getBiome());
            plugin.getPresetDestroyer().destroyPreset(pdd);
            TARDISMessage.send(player.getPlayer(), "TARDIS_HIDDEN", ChatColor.GREEN + " /tardis rebuild " + ChatColor.RESET);
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", id);
            qf.alterEnergyLevel("tardis", -hide, wheret, player.getPlayer());
            // set hidden to true
            HashMap<String, Object> whereh = new HashMap<String, Object>();
            whereh.put("tardis_id", id);
            HashMap<String, Object> seth = new HashMap<String, Object>();
            seth.put("hidden", 1);
            qf.doUpdate("tardis", seth, whereh);
            return true;
        } else {
            TARDISMessage.send(player.getPlayer(), "NO_PERMS");
            return false;
        }
    }
}
