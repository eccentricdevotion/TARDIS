/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author eccentric_nz
 */
public class TARDISGlassesListener implements Listener {

    private final TARDIS plugin;

    public TARDISGlassesListener(TARDIS plugin) {
        this.plugin = plugin;
        checkGlasses(this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on3dGlassesEquip(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getType().equals(InventoryType.CRAFTING)) {
            Player player = (Player) event.getPlayer();
            PlayerInventory pi = player.getInventory();
            ItemStack is = pi.getHelmet();
            if (is != null) {
                if (is3DGlasses(is)) {
                    if (!plugin.getTrackerKeeper().getSpectacleWearers().contains(player.getUniqueId())) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3600, 10));
                        plugin.getTrackerKeeper().getSpectacleWearers().add(player.getUniqueId());
                    }
                }
            } else {
                if (plugin.getTrackerKeeper().getSpectacleWearers().contains(player.getUniqueId())) {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    plugin.getTrackerKeeper().getSpectacleWearers().remove(player.getUniqueId());
                }
            }
        }
    }

    private void checkGlasses(TARDIS plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> plugin.getTrackerKeeper().getSpectacleWearers().forEach((uuid) -> {
            Player p = plugin.getServer().getPlayer(uuid);
            if (p != null && p.isOnline()) {
                PlayerInventory pi = p.getInventory();
                ItemStack is = pi.getHelmet();
                boolean g = is3DGlasses(is);
                if ((is == null || !g) && p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new removeFromMap(uuid), 20L);
                } else if (is != null && g) {
                    // damage the glasses so they run out
                    Damageable damageable = (Damageable) is.getItemMeta();
                    int d = damageable.getDamage() + 1;
                    if (d >= 56) {
                        // if run out then remove them and the potion effect
                        pi.setHelmet(null);
                        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "GLASSES_DONE");
                        p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.PAPER, 1));
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new removeFromMap(uuid), 20L);
                    } else {
                        damageable.setDamage(d);
                    }
                    p.updateInventory();
                }
            }
        }), 3600L, 1200L);
    }

    private boolean is3DGlasses(ItemStack is) {
        if (is != null && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            return im.hasDisplayName() && im.getDisplayName().equals("3-D Glasses");
        }
        return false;
    }

    class removeFromMap implements Runnable {

        final UUID uuid;

        removeFromMap(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void run() {
            plugin.getTrackerKeeper().getSpectacleWearers().remove(uuid);
        }
    }
}
