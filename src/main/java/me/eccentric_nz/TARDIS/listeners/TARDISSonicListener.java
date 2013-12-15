/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminMenuInventory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicListener implements Listener {

    private final TARDIS plugin;
    private final Material sonic;
    private final HashMap<String, Long> timeout = new HashMap<String, Long>();
    private final List<Material> doors = new ArrayList<Material>();

    public TARDISSonicListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        this.sonic = Material.valueOf(split[0]);
        doors.add(Material.WOODEN_DOOR);
        doors.add(Material.IRON_DOOR_BLOCK);
        doors.add(Material.TRAP_DOOR);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        final ItemStack is = player.getItemInHand();
        if (is.getType().equals(sonic) && is.hasItemMeta()) {
            ItemMeta im = player.getItemInHand().getItemMeta();
            if (im.getDisplayName().equals("Sonic Screwdriver")) {
                List<String> lore = im.getLore();
                Action action = event.getAction();
                if (action.equals(Action.RIGHT_CLICK_AIR)) {
                    playSonicSound(player, now);
                    if (player.hasPermission("tardis.admin") && lore.contains("Admin Upgrade")) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                ItemStack[] items = new TARDISAdminMenuInventory(plugin).getMenu();
                                Inventory menu = plugin.getServer().createInventory(player, 54, "§4Admin Menu");
                                menu.setContents(items);
                                player.openInventory(menu);
                            }
                        }, 40L);
                    }
                }
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    final Block b = event.getClickedBlock();
                    if (doors.contains(b.getType()) && player.hasPermission("tardis.admin") && lore.contains("Admin Upgrade")) {
                        playSonicSound(player, now);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                HashMap<String, Object> wheredoor = new HashMap<String, Object>();
                                Location loc = b.getLocation();
                                String bw = loc.getWorld().getName();
                                int bx = loc.getBlockX();
                                int by = loc.getBlockY();
                                int bz = loc.getBlockZ();
                                if (b.getData() >= 8 && !b.getType().equals(Material.TRAP_DOOR)) {
                                    by = (by - 1);
                                }
                                String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                                wheredoor.put("door_location", doorloc);
                                wheredoor.put("door_type", 0);
                                ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
                                if (rsd.resultSet()) {
                                    int id = rsd.getTardis_id();
                                    HashMap<String, Object> whereid = new HashMap<String, Object>();
                                    whereid.put("tardis_id", id);
                                    ResultSetTravellers rst = new ResultSetTravellers(plugin, whereid, true);
                                    if (rst.resultSet()) {
                                        List<String> data = rst.getData();
                                        player.sendMessage(plugin.pluginName + "The players inside this TARDIS are:");
                                        for (String s : data) {
                                            player.sendMessage(s);
                                        }
                                    } else {
                                        player.sendMessage(plugin.pluginName + "The TARDIS is unoccupied.");
                                    }
                                }
                            }
                        }, 60L);
                    }
                }
            }
        }
    }

    private void playSonicSound(final Player player, long now) {
        if ((!timeout.containsKey(player.getName()) || timeout.get(player.getName()) < now)) {
            ItemMeta im = player.getItemInHand().getItemMeta();
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            player.getItemInHand().setItemMeta(im);
            timeout.put(player.getName(), now + 3050);
            plugin.utils.playTARDISSound(player.getLocation(), player, "sonic_screwdriver");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (Enchantment e : player.getItemInHand().getEnchantments().keySet()) {
                        player.getItemInHand().removeEnchantment(e);
                    }
                }
            }, 60L);
        }
    }
}
