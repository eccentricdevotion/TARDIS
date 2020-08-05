/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.hads.TARDISHostileAction;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

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
        Player p = event.getPlayer();
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(l)) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l);
            ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, false);
            if (rsb.resultSet()) {
                ReplacedBlock rb = rsb.getReplacedBlock();
                int id = rb.getTardis_id();
                if (TARDISPermission.hasPermission(p, "tardis.sonic.admin")) {
                    String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
                    Material sonic = Material.valueOf(split[0]);
                    ItemStack is = event.getItemInHand();
                    if (is != null && is.getType().equals(sonic)) {
                        // unhide TARDIS
                        unhide(id, p);
                    }
                }
                boolean m = false;
                boolean isDoor = false;
                int damage = plugin.getTrackerKeeper().getDamage().getOrDefault(id, 0);
                if (damage <= plugin.getConfig().getInt("preferences.hads_damage") && plugin.getConfig().getBoolean("allow.hads") && !plugin.getTrackerKeeper().getInVortex().contains(id) && isOwnerOnline(id) && !plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                    if (TARDISMaterials.doors.contains(b.getType())) {
                        if (isOwner(id, p.getUniqueId().toString())) {
                            isDoor = true;
                        }
                    }
                    if (!isDoor && rb.getPolice_box() == 1) {
                        plugin.getTrackerKeeper().getDamage().put(id, damage + 1);
                        if (damage == plugin.getConfig().getInt("preferences.hads_damage")) {
                            new TARDISHostileAction(plugin).processAction(id, p);
                            m = true;
                        }
                        if (!m) {
                            TARDISMessage.send(p, "HADS_WARNING", String.format("%d", (plugin.getConfig().getInt("preferences.hads_damage") - damage)));
                        }
                    }
                } else {
                    TARDISMessage.send(p, "TARDIS_BREAK");
                }
                event.setCancelled(true);
            }
        }
    }

    private boolean isOwner(int id, String uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("uuid", uuid);
        ResultSetTardis rst = new ResultSetTardis(plugin, where, "", false, 0);
        return rst.resultSet();
    }

    private boolean isOwnerOnline(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rst = new ResultSetTardis(plugin, where, "", false, 0);
        if (rst.resultSet()) {
            Tardis tardis = rst.getTardis();
            if (!tardis.isTardis_init()) {
                return false;
            }
            UUID ownerUUID = tardis.getUuid();
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, ownerUUID.toString());
            boolean hads_on = true;
            if (rsp.resultSet()) {
                hads_on = rsp.isHadsOn();
            }
            return (plugin.getServer().getOfflinePlayer(ownerUUID).isOnline() && hads_on);
        } else {
            return false;
        }
    }

    private void unhide(int id, Player player) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rst = new ResultSetTardis(plugin, where, "", false, 2);
        if (rst.resultSet() && rst.getTardis().isHidden()) {
            // unhide this tardis
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
            }
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            BuildData bd = new BuildData(player.getUniqueId().toString());
            bd.setDirection(rsc.getDirection());
            bd.setLocation(l);
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(player);
            bd.setRebuild(true);
            bd.setSubmarine(rsc.isSubmarine());
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
