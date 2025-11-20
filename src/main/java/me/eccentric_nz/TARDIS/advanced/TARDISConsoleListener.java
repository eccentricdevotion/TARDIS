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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPowered;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISConsoleListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> onlythese = new ArrayList<>();

    public TARDISConsoleListener(TARDIS plugin) {
        this.plugin = plugin;
        for (DiskCircuit dc : DiskCircuit.values()) {
            if (!onlythese.contains(dc.getMaterial())) {
                onlythese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsoleInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!TARDISPermission.hasPermission(p, "tardis.advanced")) {
            return;
        }
        if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(uuid)) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null || (!block.getType().equals(Material.JUKEBOX) && !block.getType().equals(Material.MUSHROOM_STEM) && !block.getType().equals(Material.BARRIER))) {
            return;
        }
        // is it a TARDIS console?
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("location", block.getLocation().toString());
        wherec.put("type", 15);
        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
        if (!rsc.resultSet()) {
            return;
        }
        event.setCancelled(true);
        if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(p.getUniqueId())) {
            // update block if it's not a display item entity
            if (block.getType().equals(Material.JUKEBOX) || block.getType().equals(Material.MUSHROOM_STEM)) {
                block.setType(Material.BARRIER);
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ADVANCED_CONSOLE, block, rsc.getTardis_id());
            }
        }
        int id = rsc.getTardis_id();
        // determine key item
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        String key;
        if (rsp.resultSet()) {
            key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
        } else {
            key = plugin.getConfig().getString("preferences.key");
        }
        onlythese.add(Material.valueOf(key));
        ItemStack disk = event.getPlayer().getInventory().getItemInMainHand();
        if (onlythese.contains(disk.getType()) && disk.hasItemMeta() || key.equals("AIR")) {
            // only the time lord of this tardis
            ResultSetTardisPowered rs = new ResultSetTardisPowered(plugin);
            if (!rs.fromBoth(id, uuid.toString())) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "NOT_OWNER");
                return;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered()) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "POWER_DOWN");
                return;
            }
            // open gui
            p.openInventory(new TARDISAdvancedConsoleInventory(plugin, uuid.toString(), id).getInventory());
        } else if (disk.getType().equals(Material.MUSIC_DISC_FAR)) {
            ItemMeta im = disk.getItemMeta();
            if (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Authorised Control Disk")) {
                // get the UUID from the disk
                if (im.getPersistentDataContainer().has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID())) {
                    UUID diskUuid = im.getPersistentDataContainer().get(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID());
                    // is the disk uuid the same as the tardis uuid?
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetTardis rst = new ResultSetTardis(plugin, where, "", false);
                    if (rst.resultSet() && rst.getTardis().getUuid().equals(diskUuid)) {
                        if (uuid == rst.getTardis().getUuid()) {
                            // time lords can't use their own disks!
                            plugin.getMessenger().send(p, TardisModule.TARDIS, "SECURITY_TIMELORD");
                            return;
                        }
                        Tardis tardis = rst.getTardis();
                        // process disk
                        TARDISAuthorisedControlDisk tacd = new TARDISAuthorisedControlDisk(plugin, tardis.getUuid(), im.lore(), id, p, tardis.getEps(), tardis.getCreeper());
                        String processed = tacd.process();
                        if (processed.equals("success")) {
                            // success remove disk from hand
                            int amount = p.getInventory().getItemInMainHand().getAmount();
                            int adjusted = amount - 1;
                            if (adjusted > 0) {
                                p.getInventory().getItemInMainHand().setAmount(adjusted);
                            } else {
                                p.getInventory().setItemInMainHand(null);
                            }
                            p.updateInventory();
                            plugin.getMessenger().send(p, TardisModule.TARDIS, "SECURITY_SUCCESS");
                        } else {
                            // error message player
                            plugin.getMessenger().send(p, TardisModule.TARDIS, "SECURITY_ERROR", processed);
                        }
                    }
                }
            }
        } else {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "ADV_OPEN");
        }
    }
}
