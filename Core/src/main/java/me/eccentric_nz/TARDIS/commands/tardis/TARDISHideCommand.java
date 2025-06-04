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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
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
public class TARDISHideCommand {

    private final TARDIS plugin;

    public TARDISHideCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean hide(OfflinePlayer player) {
        if (TARDISPermission.hasPermission(player, "tardis.rebuild")) {
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getHideCooldown().containsKey(uuid)) {
                long now = System.currentTimeMillis();
                long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                long then = plugin.getTrackerKeeper().getHideCooldown().get(uuid) + cooldown;
                if (now < then) {
                    plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "COOLDOWN_HIDE", String.format("%d", cooldown / 1000));
                    return true;
                }
            }
            plugin.getTrackerKeeper().getHideCooldown().put(uuid, System.currentTimeMillis());
            int id;
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_TARDIS");
                return false;
            }
            Tardis tardis = rs.getTardis();
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "POWER_DOWN");
                return true;
            }
            if (tardis.getPreset().equals(ChameleonPreset.INVISIBLE)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "INVISIBILITY_ENGAGED");
                return true;
            }
            id = tardis.getTardisId();
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player.getPlayer(), true) && !tcc.hasMaterialisation()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                return true;
            }
            // damage circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.telepathic") > 0) {
                // decrement uses
                int uses_left = tcc.getTelepathicUses();
                new TARDISCircuitDamager(plugin, DiskCircuit.TELEPATHIC, uses_left, id, player.getPlayer()).damage();
            }
            HashMap<String, Object> wherein = new HashMap<>();
            wherein.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
            if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "TARDIS_NO_HIDE");
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
            // make sure TARDIS is not dispersed
            if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                return true;
            }
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return true;
            }
            Current current = rsc.getCurrent();
            int level = tardis.getArtronLevel();
            int hide = plugin.getArtronConfig().getInt("hide");
            if (level < hide) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "ENERGY_NO_HIDE");
                return false;
            }
            DestroyData dd = new DestroyData();
            dd.setDirection(current.direction());
            dd.setLocation(current.location());
            dd.setPlayer(player.getPlayer());
            dd.setHide(true);
            dd.setOutside(false);
            dd.setSubmarine(current.submarine());
            dd.setTardisID(id);
            dd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getPresetDestroyer().destroyPreset(dd);
            plugin.getTrackerKeeper().getInVortex().add(id);
            plugin.getMessenger().sendColouredCommand(player.getPlayer(), "TARDIS_HIDDEN", "/tardis rebuild ", plugin);
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", -hide, wheret, player.getPlayer());
            // set hidden to true
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            HashMap<String, Object> seth = new HashMap<>();
            seth.put("hidden", 1);
            plugin.getQueryFactory().doUpdate("tardis", seth, whereh);
            // turn force field off
            if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid)) {
                plugin.getTrackerKeeper().getActiveForceFields().remove(uuid);
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "FORCE_FIELD", "OFF");
            }
            return true;
        } else {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
