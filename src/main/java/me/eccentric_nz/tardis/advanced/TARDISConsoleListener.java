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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.custommodeldata.TardisMushroomBlockData;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.enumeration.GlowstoneCircuit;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

/**
 * @author eccentric_nz
 */
public class TardisConsoleListener implements Listener {

    private final TardisPlugin plugin;
    private final List<Material> onlyThese = new ArrayList<>();

    public TardisConsoleListener(TardisPlugin plugin) {
        this.plugin = plugin;
        for (DiskCircuit diskCircuit : DiskCircuit.values()) {
            if (!onlyThese.contains(diskCircuit.getMaterial())) {
                onlyThese.add(diskCircuit.getMaterial());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsoleInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!TardisPermission.hasPermission(player, "tardis.advanced")) {
            return;
        }
        if (plugin.getTrackerKeeper().getPlayers().containsKey(uuid)) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if (block != null && (block.getType().equals(Material.JUKEBOX) || block.getType().equals(Material.MUSHROOM_STEM))) {
                // is it a tardis console?
                HashMap<String, Object> whereConsole = new HashMap<>();
                whereConsole.put("location", block.getLocation().toString());
                whereConsole.put("type", 15);
                ResultSetControls resultSetControls = new ResultSetControls(plugin, whereConsole, false);
                if (resultSetControls.resultSet()) {
                    event.setCancelled(true);
                    // update block if it's not MUSHROOM_STEM
                    if (block.getType().equals(Material.JUKEBOX)) {
                        BlockData mushroom = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(50));
                        block.setBlockData(mushroom);
                    }
                    int id = resultSetControls.getTardisId();
                    // determine key item
                    ResultSetPlayerPrefs resultSetPlayerPrefs = new ResultSetPlayerPrefs(plugin, uuid.toString());
                    String key;
                    if (resultSetPlayerPrefs.resultSet()) {
                        key = (!resultSetPlayerPrefs.getKey().isEmpty()) ? resultSetPlayerPrefs.getKey() : plugin.getConfig().getString("preferences.key");
                    } else {
                        key = plugin.getConfig().getString("preferences.key");
                    }
                    onlyThese.add(Material.valueOf(key));
                    ItemStack disk = event.getPlayer().getInventory().getItemInMainHand();
                    if (onlyThese.contains(disk.getType()) && disk.hasItemMeta() || Objects.equals(key, "AIR")) {
                        // only the time lord of this tardis
                        ResultSetTardisPowered resultSetTardisPowered = new ResultSetTardisPowered(plugin);
                        if (!resultSetTardisPowered.fromBoth(id, uuid.toString())) {
                            TardisMessage.send(player, "NOT_OWNER");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !resultSetTardisPowered.isPowered()) {
                            TardisMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        Inventory inventory = plugin.getServer().createInventory(player, 9, ChatColor.DARK_RED + "tardis Console");
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", uuid.toString());
                        ResultSetDiskStorage resultSetDiskStorage = new ResultSetDiskStorage(plugin, where);
                        if (resultSetDiskStorage.resultSet()) {
                            String console = resultSetDiskStorage.getConsole();
                            if (!console.isEmpty()) {
                                try {
                                    ItemStack[] itemStacks = TardisInventorySerializer.itemStacksFromString(console);
                                    for (ItemStack circuit : itemStacks) {
                                        if (circuit != null && circuit.hasItemMeta()) {
                                            ItemMeta itemMeta = circuit.getItemMeta();
                                            if (circuit.getType().equals(Material.FILLED_MAP)) {
                                                assert itemMeta != null;
                                                if (itemMeta.hasDisplayName()) {
                                                    GlowstoneCircuit glowstoneCircuit = GlowstoneCircuit.getByName().get(itemMeta.getDisplayName());
                                                    if (glowstoneCircuit != null) {
                                                        circuit.setType(Material.GLOWSTONE_DUST);
                                                    }
                                                }
                                            } else if (TardisStaticUtils.isMusicDisk(circuit)) {
                                                assert itemMeta != null;
                                                itemMeta.setCustomModelData(10000001);
                                                circuit.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
                                    inventory.setContents(itemStacks);
                                } catch (IOException ioException) {
                                    plugin.debug("Could not read console from database!");
                                }
                            }
                        } else {
                            // create new storage record
                            HashMap<String, Object> setStorage = new HashMap<>();
                            setStorage.put("uuid", uuid.toString());
                            setStorage.put("tardis_id", id);
                            plugin.getQueryFactory().doInsert("storage", setStorage);
                        }
                        // open gui
                        player.openInventory(inventory);
                    } else if (disk.getType().equals(Material.MUSIC_DISC_FAR)) {
                        ItemMeta itemMeta = disk.getItemMeta();
                        assert itemMeta != null;
                        if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Authorised Control Disk")) {
                            // get the UUID from the disk
                            if (itemMeta.getPersistentDataContainer().has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID())) {
                                UUID diskUuid = itemMeta.getPersistentDataContainer().get(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID());
                                // is the disk uuid the same as the tardis uuid?
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("tardis_id", id);
                                ResultSetTardis resultSetTardis = new ResultSetTardis(plugin, where, "", false, 2);
                                if (resultSetTardis.resultSet() && resultSetTardis.getTardis().getUuid() == diskUuid) {
                                    if (uuid == resultSetTardis.getTardis().getUuid()) {
                                        // time lords can't use their own disks!
                                        TardisMessage.send(player, "SECURITY_TIMELORD");
                                        return;
                                    }
                                    // process disk
                                    TardisAuthorisedControlDisk tardisAuthorisedControlDisk = new TardisAuthorisedControlDisk(plugin, resultSetTardis.getTardis().getUuid(), itemMeta.getLore(), id, player, resultSetTardis.getTardis().getEps(), resultSetTardis.getTardis().getCreeper());
                                    String processed = tardisAuthorisedControlDisk.process();
                                    if (processed.equals("success")) {
                                        // success remove disk from hand
                                        int amount = player.getInventory().getItemInMainHand().getAmount();
                                        int adjusted = amount - 1;
                                        if (adjusted > 0) {
                                            player.getInventory().getItemInMainHand().setAmount(adjusted);
                                        } else {
                                            player.getInventory().setItemInMainHand(null);
                                        }
                                        player.updateInventory();
                                        TardisMessage.send(player, "SECURITY_SUCCESS");
                                    } else {
                                        // error message player
                                        TardisMessage.send(player, "SECURITY_ERROR", processed);
                                    }
                                }
                            }
                        }
                    } else {
                        TardisMessage.send(player, "ADV_OPEN");
                    }
                }
            }
        }
    }
}
