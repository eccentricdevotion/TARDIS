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
package me.eccentric_nz.TARDIS.arch;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISChameleonArchEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISChameleonArchOffEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISFobWatchListener implements Listener {

    private final TARDIS plugin;

    public TARDISFobWatchListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFobWatchClick(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        ItemStack is = event.getItem();
        if (is == null || !is.getType().equals(Material.CLOCK) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.getDisplayName().endsWith("Fob Watch")) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        boolean inv = plugin.getConfig().getBoolean("arch.switch_inventory");
        if (!plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
            // only check the permission when trying to 'fob'
            if (!TARDISPermission.hasPermission(player, "tardis.chameleonarch")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_CHAM_ARCH");
                return;
            }
            String name = TARDISRandomName.name();
            long time = System.currentTimeMillis() + plugin.getConfig().getLong("arch.min_time") * 60000L;
            TARDISWatchData twd = new TARDISWatchData(name, time);
            plugin.getTrackerKeeper().getJohnSmith().put(uuid, twd);
            if (plugin.isDisguisesOnServer()) {
                TARDISArchLibsDisguise.undisguise(player);
            } else {
                TARDISArchDisguise.undisguise(player);
            }
            player.getWorld().strikeLightningEffect(player.getLocation());
            double mh = player.getAttribute(Attribute.MAX_HEALTH).getValue();
            player.setHealth(mh / 10.0d);
            if (inv) {
                new TARDISArchInventory().switchInventories(player, 0);
            }
            if (plugin.isDisguisesOnServer()) {
                TARDISArchLibsDisguise.disguise(player, name);
            } else {
                TARDISArchDisguise.disguise(player, name);
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.setDisplayName(name);
                player.setPlayerListName(name);
            }, 5L);
            plugin.getPM().callEvent(new TARDISChameleonArchEvent(player, twd));
        } else if (plugin.getTrackerKeeper().getJohnSmith().get(uuid).getTime() <= System.currentTimeMillis()) {
            // no permission check, always allow 'de-fobbing'
            if (plugin.isDisguisesOnServer()) {
                TARDISArchLibsDisguise.undisguise(player);
            } else {
                TARDISArchDisguise.undisguise(player);
            }
            if (inv) {
                new TARDISArchInventory().switchInventories(player, 1);
            }
            player.getWorld().strikeLightningEffect(player.getLocation());
            plugin.getTrackerKeeper().getJohnSmith().remove(uuid);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.setDisplayName(player.getName());
                player.setPlayerListName(player.getName());
            }, 5L);
            // remove player from arched table
            new TARDISArchPersister(plugin).removeArch(uuid);
            plugin.getPM().callEvent(new TARDISChameleonArchOffEvent(player));
        }
    }
}
