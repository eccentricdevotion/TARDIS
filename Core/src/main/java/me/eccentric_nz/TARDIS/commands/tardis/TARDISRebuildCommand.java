/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISRebuildCommand {

    private final TARDIS plugin;

    public TARDISRebuildCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean rebuildPreset(OfflinePlayer player) {
        if (TARDISPermission.hasPermission(player, "tardis.rebuild")) {
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
                long now = System.currentTimeMillis();
                long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
                if (now < then) {
                    plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "COOLDOWN", String.format("%d", cooldown / 1000));
                    return true;
                }
            }
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_TARDIS");
                return true;
            }
            Tardis tardis = rs.getTardis();
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "POWER_DOWN");
                return true;
            }
            if (tardis.getPreset().equals(ChameleonPreset.INVISIBLE) && tardis.getDemat().equals(ChameleonPreset.INVISIBLE)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "INVISIBILITY_ENGAGED");
                return true;
            }
            int id = tardis.getTardisId();
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player.getPlayer(), true) && !tcc.hasMaterialisation()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                return true;
            }
            // damage circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                // decrement uses
                int uses_left = tcc.getMaterialisationUses();
                new TARDISCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player.getPlayer()).damage();
            }
            HashMap<String, Object> wherein = new HashMap<>();
            wherein.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
            if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "TARDIS_NO_REBUILD");
                return true;
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_IN_VORTEX");
                return true;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_MAT");
                return true;
            }
            if (plugin.getTrackerKeeper().getDispersed().containsKey(uuid)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                return true;
            }
            Current current = TARDISCache.CURRENT.get(id);
            if (current == null) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                plugin.getMessenger().sendColouredCommand(player.getPlayer(), "REBUILD_FAIL", "/tardis comehere", plugin);
                return true;
            }
            int level = tardis.getArtronLevel();
            int rebuild = plugin.getArtronConfig().getInt("random");
            if (level < rebuild) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "ENERGY_NO_REBUILD");
                return false;
            }
            plugin.getTrackerKeeper().getRebuildCooldown().put(uuid, System.currentTimeMillis());
            BuildData bd = new BuildData(uuid.toString());
            bd.setDirection(current.direction());
            bd.setLocation(current.location());
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(player);
            bd.setRebuild(true);
            bd.setSubmarine(current.submarine());
            bd.setTardisID(id);
            bd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getTrackerKeeper().getDematerialising().add(id);
                plugin.getPresetBuilder().buildPreset(bd);
                plugin.getTrackerKeeper().getInVortex().add(id);
            }, 10L);
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "TARDIS_REBUILT");
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", -rebuild, wheret, player.getPlayer());
            // set hidden to false
            if (tardis.isHidden()) {
                HashMap<String, Object> whereh = new HashMap<>();
                whereh.put("tardis_id", id);
                HashMap<String, Object> seth = new HashMap<>();
                seth.put("hidden", 0);
                plugin.getQueryFactory().doUpdate("tardis", seth, whereh);
            }
            return true;
        } else {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
