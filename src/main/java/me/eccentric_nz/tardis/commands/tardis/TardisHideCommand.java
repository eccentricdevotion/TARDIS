/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisHideCommand {

    private final TardisPlugin plugin;

    public TardisHideCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean hide(OfflinePlayer player) {
        if (TardisPermission.hasPermission(player, "tardis.rebuild")) {
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getHideCooldown().containsKey(uuid)) {
                long now = System.currentTimeMillis();
                long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                long then = plugin.getTrackerKeeper().getHideCooldown().get(uuid) + cooldown;
                if (now < then) {
                    TardisMessage.send(player.getPlayer(), "COOLDOWN_HIDE", String.format("%d", cooldown / 1000));
                    return true;
                }
            }
            plugin.getTrackerKeeper().getHideCooldown().put(uuid, System.currentTimeMillis());
            int id;
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TardisMessage.send(player.getPlayer(), "NO_TARDIS");
                return false;
            }
            Tardis tardis = rs.getTardis();
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered()) {
                TardisMessage.send(player.getPlayer(), "POWER_DOWN");
                return true;
            }
            if (tardis.getPreset().equals(Preset.INVISIBLE)) {
                TardisMessage.send(player.getPlayer(), "INVISIBILITY_ENGAGED");
                return true;
            }
            id = tardis.getTardisId();
            TardisCircuitChecker tcc = null;
            if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player.getPlayer(), true)) {
                tcc = new TardisCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMaterialisation()) {
                TardisMessage.send(player.getPlayer(), "NO_MAT_CIRCUIT");
                return true;
            }
            HashMap<String, Object> wherein = new HashMap<>();
            wherein.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
            if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                TardisMessage.send(player.getPlayer(), "TARDIS_NO_HIDE");
                return true;
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                TardisMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
                return true;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                TardisMessage.send(player.getPlayer(), "NOT_WHILE_MAT");
                return true;
            }
            // make sure TARDIS is not dispersed
            if (plugin.getTrackerKeeper().getDispersedTardises().contains(id)) {
                TardisMessage.send(player.getPlayer(), "NOT_WHILE_DISPERSED");
                return true;
            }
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", tardis.getTardisId());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TardisMessage.send(player.getPlayer(), "CURRENT_NOT_FOUND");
                return true;
            }
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            int level = tardis.getArtronLevel();
            int hide = plugin.getArtronConfig().getInt("hide");
            if (level < hide) {
                TardisMessage.send(player.getPlayer(), "ENERGY_NO_HIDE");
                return false;
            }
            DestroyData dd = new DestroyData();
            dd.setDirection(rsc.getDirection());
            dd.setLocation(l);
            dd.setPlayer(player.getPlayer());
            dd.setHide(true);
            dd.setOutside(false);
            dd.setSubmarine(rsc.isSubmarine());
            dd.setTardisId(id);
            dd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getPresetDestroyer().destroyPreset(dd);
            plugin.getTrackerKeeper().getInVortex().add(id);
            TardisMessage.send(player.getPlayer(), "TARDIS_HIDDEN", ChatColor.GREEN + "/tardis rebuild " + ChatColor.RESET);
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
                TardisMessage.send(player.getPlayer(), "FORCE_FIELD", "OFF");
            }
            return true;
        } else {
            TardisMessage.send(player.getPlayer(), "NO_PERMS");
            return false;
        }
    }
}
