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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicSound;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISSonicEntityListener implements Listener {

    private final TARDIS plugin;

    public TARDISSonicEntityListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        ItemStack is = player.getInventory().getItemInMainHand();
        Entity ent = event.getRightClicked();
        if (is.getType().equals(Material.BLAZE_ROD) && is.hasItemMeta()) {
            ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
            if (ComponentUtils.endsWith(im.displayName(), "Sonic Screwdriver")) {
                List<Component> lore = im.lore();
                if (ent instanceof Player scanned) {
                    TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                    if (TARDISPermission.hasPermission(player, "tardis.sonic.admin") && lore != null && lore.contains(Component.text("Admin Upgrade")) && player.isSneaking()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_INV");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(new TARDISViewPlayerInventory(plugin, scanned).getInventory()), 40L);
                    } else if (TARDISPermission.hasPermission(player, "tardis.sonic.bio") && lore != null && lore.contains(Component.text("Bio-scanner Upgrade"))) {
                        // save scanned player to sonic table
                        new TARDISSonicData().saveOrUpdate(plugin, scanned.getUniqueId().toString(), 1, is, player);
                        // message player
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_PLAYER");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            // getHealth() / getMaxHealth() * getHealthScale()
                            double mh = scanned.getAttribute(Attribute.MAX_HEALTH).getValue();
                            double health = scanned.getHealth() / mh * scanned.getHealthScale();
                            float hunger = (scanned.getFoodLevel() / 20F) * 100;
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_NAME", scanned.getName());
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_AGE", convertTicksToTime(scanned.getTicksLived()));
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_HEALTH", String.format("%f", health));
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_HUNGER", String.format("%.2f", hunger));
                        }, 40L);
                    }
                } else if (ent instanceof ItemFrame frame) {
                    // get TARDIS id?
                    int id = isDock(frame);
                    if (id == -1) {
                        return;
                    }
                    new TARDISSonicDock(plugin).dock(id, frame, player, is);
                }
            }
        } else if (ent instanceof ItemFrame frame && is.getType() == Material.AIR) {
            // get TARDIS id?
            int id = isDock(frame);
            if (id == -1) {
                return;
            }
            new TARDISSonicDock(plugin).undock(frame, player);
        }
    }

    private int isDock(ItemFrame frame) {
        ItemStack dock = frame.getItem();
        if (dock.getType() != Material.FLOWER_POT || !dock.hasItemMeta()) {
            return -1;
        }
        ItemMeta im = dock.getItemMeta();
        if (!im.hasItemModel() || !im.getItemModel().getKey().contains("sonic_dock")) {
            return -1;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", 48);
        where.put("location", frame.getLocation().toString());
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        return rsc.resultSet() ? rsc.getTardis_id() : -1;
    }

    private String convertTicksToTime(int time) {
        // convert to seconds
        int seconds = time / 20;
        int h = seconds / 3600;
        int remainder = seconds - (h * 3600);
        int m = remainder / 60;
        int s = remainder - (m * 60);
        String gh = (h > 1 || h == 0) ? " hours " : " hour ";
        String gm = (m > 1 || m == 0) ? " minutes " : " minute ";
        String gs = (s > 1 || s == 0) ? " seconds" : " second";
        return h + gh + m + gm + s + gs;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInventoryViewClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof TARDISViewPlayerInventory) {
            event.setCancelled(true);
        }
    }
}
