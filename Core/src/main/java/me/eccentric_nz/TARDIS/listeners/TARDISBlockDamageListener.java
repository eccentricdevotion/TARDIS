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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.hads.TARDISHostileAction;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * The Judoon are a race of rhinocerid humanoids frequently employed as a mercenary police force.
 *
 * @author eccentric_nz
 */
public class TARDISBlockDamageListener implements Listener {

    private final TARDIS plugin;

    public TARDISBlockDamageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for block damage to the TARDIS Police Box. If the block is a Police Box block then the event is
     * cancelled, and the player warned.
     *
     * @param event a block being damaged
     */
    @EventHandler(ignoreCancelled = true)
    public void onPoliceBoxDamage(BlockDamageEvent event) {
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(l)) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l);
            ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, false);
            if (rsb.resultSet()) {
                Player p = event.getPlayer();
                ReplacedBlock rb = rsb.getReplacedBlock();
                int id = rb.getTardis_id();
                if (TARDISPermission.hasPermission(p, "tardis.sonic.admin")) {
                    ItemStack is = event.getItemInHand();
                    if (is.getType().equals(Material.BLAZE_ROD)) {
                        // unhide TARDIS
                        unhide(id, p);
                    }
                }
                boolean m = false;
                boolean isDoor = false;
                int damage = plugin.getTrackerKeeper().getHadsDamage().getOrDefault(id, 0);
                if (damage <= plugin.getConfig().getInt("preferences.hads_damage")
                        && plugin.getConfig().getBoolean("allow.hads")
                        && !plugin.getTrackerKeeper().getInVortex().contains(id)
                        && TARDISStaticUtils.isOwnerOnline(id)
                        && !plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                    if (TARDISMaterials.doors.contains(b.getType())) {
                        if (isOwner(id, p.getUniqueId().toString())) {
                            isDoor = true;
                        }
                    }
                    if (!isDoor && rb.getPolice_box() == 1) {
                        plugin.getTrackerKeeper().getHadsDamage().put(id, damage + 1);
                        if (damage == plugin.getConfig().getInt("preferences.hads_damage")) {
                            new TARDISHostileAction(plugin).processAction(id, p);
                            m = true;
                        }
                        if (!m) {
                            plugin.getMessenger().send(p, TardisModule.TARDIS, "HADS_WARNING", String.format("%d", (plugin.getConfig().getInt("preferences.hads_damage") - damage)));
                        }
                    }
                } else {
                    plugin.getMessenger().sendStatus(p, "TARDIS_BREAK");
                }
                event.setCancelled(true);
            }
        }
    }

    private boolean isOwner(int id, String uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("uuid", uuid);
        ResultSetTardis rst = new ResultSetTardis(plugin, where, "", false);
        return rst.resultSet();
    }

    private void unhide(int id, Player player) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rst = new ResultSetTardis(plugin, where, "", false);
        if (rst.resultSet() && rst.getTardis().isHidden()) {
            // un-hide this tardis
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return;
            }
            Current current = rsc.getCurrent();
            BuildData bd = new BuildData(player.getUniqueId().toString());
            bd.setDirection(current.direction());
            bd.setLocation(current.location());
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(player);
            bd.setRebuild(true);
            bd.setSubmarine(current.submarine());
            bd.setTardisID(id);
            bd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 5L);
            // set hidden to false
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            HashMap<String, Object> seth = new HashMap<>();
            seth.put("hidden", 0);
            plugin.getQueryFactory().doUpdate("tardis", seth, whereh);
        }
    }
}
