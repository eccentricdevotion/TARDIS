/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISMakeHerBlueCommand {

    private final TARDIS plugin;

    TARDISMakeHerBlueCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean show(Player player) {
        if (!plugin.getConfig().getBoolean("allow.invisibility")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "INVISIBILITY_DISABLED");
            return true;
        }
        // do the usual checks
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
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_TARDIS");
            return true;
        }
        Tardis tardis = rs.getTardis();
        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "POWER_DOWN");
            return true;
        }
        if (!tardis.getPreset().equals(ChameleonPreset.INVISIBLE) && !tardis.getPreset().equals(ChameleonPreset.JUNK_MODE)) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "INVISIBILITY_NOT");
            return true;
        }
        int id = tardis.getTardisId();
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null) {
            if (!tcc.hasInvisibility() && !tardis.getPreset().equals(ChameleonPreset.JUNK_MODE)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "INVISIBILITY_MISSING");
                return true;
            }
            if (!tcc.hasMaterialisation()) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                return true;
            }
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_IN_VORTEX");
            return true;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_MAT");
            return true;
        }
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", tardis.getTardisId());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            plugin.getMessenger().sendColouredCommand(player.getPlayer(), "REBUILD_FAIL", "/tardis comehere", plugin);
            return true;
        }
        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        int level = tardis.getArtronLevel();
        int rebuild = plugin.getArtronConfig().getInt("random");
        if (level < rebuild) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "ENERGY_NO_REBUILD");
            return true;
        }
        plugin.getTrackerKeeper().getRebuildCooldown().put(uuid, System.currentTimeMillis());
        // set the preset to POLICE_BOX_BLUE
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("chameleon_preset", "POLICE_BOX_BLUE");
        plugin.getQueryFactory().doUpdate("tardis", set, wherep);
        BuildData bd = new BuildData(uuid.toString());
        bd.setDirection(rsc.getDirection());
        bd.setLocation(l);
        bd.setMalfunction(false);
        bd.setOutside(false);
        bd.setPlayer(player);
        bd.setRebuild(true);
        bd.setSubmarine(rsc.isSubmarine());
        bd.setTardisID(id);
        bd.setThrottle(SpaceTimeThrottle.REBUILD);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 20L);
        String message = (tardis.getPreset().equals(ChameleonPreset.JUNK_MODE)) ? "JUNK_PRESET_OFF" : "INVISIBILITY_REMOVED";
        plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, message);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        plugin.getQueryFactory().alterEnergyLevel("tardis", -rebuild, wheret, player.getPlayer());
        return true;
    }
}
