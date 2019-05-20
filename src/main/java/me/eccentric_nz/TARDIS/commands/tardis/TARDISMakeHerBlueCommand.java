/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
            TARDISMessage.send(player, "INVISIBILITY_DISABLED");
            return true;
        }
        // do the usual checks
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
            long now = System.currentTimeMillis();
            long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
            long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
            if (now < then) {
                TARDISMessage.send(player.getPlayer(), "COOLDOWN", String.format("%d", cooldown / 1000));
                return true;
            }
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.send(player.getPlayer(), "NO_TARDIS");
            return true;
        }
        Tardis tardis = rs.getTardis();
        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
            TARDISMessage.send(player.getPlayer(), "POWER_DOWN");
            return true;
        }
        if (!tardis.getPreset().equals(PRESET.INVISIBLE) && !tardis.getPreset().equals(PRESET.JUNK_MODE)) {
            TARDISMessage.send(player.getPlayer(), "INVISIBILITY_NOT");
            return true;
        }
        int id = tardis.getTardis_id();
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(DIFFICULTY.EASY)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null) {
            if (!tcc.hasInvisibility() && !tardis.getPreset().equals(PRESET.JUNK_MODE)) {
                TARDISMessage.send(player.getPlayer(), "INVISIBILITY_MISSING");
                return true;
            }
            if (!tcc.hasMaterialisation()) {
                TARDISMessage.send(player.getPlayer(), "NO_MAT_CIRCUIT");
                return true;
            }
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            TARDISMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
            return true;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            TARDISMessage.send(player.getPlayer(), "NOT_WHILE_MAT");
            return true;
        }
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", tardis.getTardis_id());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            TARDISMessage.send(player.getPlayer(), "CURRENT_NOT_FOUND");
            TARDISMessage.send(player.getPlayer(), "REBUILD_FAIL");
            return true;
        }
        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        QueryFactory qf = new QueryFactory(plugin);
        int level = tardis.getArtron_level();
        int rebuild = plugin.getArtronConfig().getInt("random");
        if (level < rebuild) {
            TARDISMessage.send(player.getPlayer(), "ENERGY_NO_REBUILD");
            return true;
        }
        plugin.getTrackerKeeper().getRebuildCooldown().put(uuid, System.currentTimeMillis());
        // set the preset to NEW
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("chameleon_preset", "NEW");
        qf.doUpdate("tardis", set, wherep);
        BuildData bd = new BuildData(plugin, uuid.toString());
        bd.setDirection(rsc.getDirection());
        bd.setLocation(l);
        bd.setMalfunction(false);
        bd.setOutside(false);
        bd.setPlayer(player);
        bd.setRebuild(true);
        bd.setSubmarine(rsc.isSubmarine());
        bd.setTardisID(id);
        bd.setBiome(rsc.getBiome());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 20L);
        String message = (tardis.getPreset().equals(PRESET.JUNK_MODE)) ? "JUNK_PRESET_OFF" : "INVISIBILITY_REMOVED";
        TARDISMessage.send(player.getPlayer(), message);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        qf.alterEnergyLevel("tardis", -rebuild, wheret, player.getPlayer());
        return true;
    }
}
