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
package me.eccentric_nz.tardis.arch;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisChameleonArchEvent;
import me.eccentric_nz.tardis.api.event.TardisChameleonArchOffEvent;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.messaging.TardisMessage;
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

import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisFobWatchListener implements Listener {

    private final TardisPlugin plugin;

    public TardisFobWatchListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFobWatchClick(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack is = event.getItem();
            if (is == null || !is.getType().equals(Material.CLOCK) || !is.hasItemMeta()) {
                return;
            }
            ItemMeta im = is.getItemMeta();
            assert im != null;
            if (!im.hasDisplayName() || !im.getDisplayName().equals("Fob Watch")) {
                return;
            }
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            boolean inv = plugin.getConfig().getBoolean("arch.switch_inventory");
            if (!plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                // only check the permission when trying to 'fob'
                if (!TardisPermission.hasPermission(player, "tardis.chameleonarch")) {
                    TardisMessage.send(player, "NO_PERM_CHAM_ARCH");
                    return;
                }
                String name = TardisRandomName.name();
                long time = System.currentTimeMillis() + plugin.getConfig().getLong("arch.min_time") * 60000L;
                TardisWatchData twd = new TardisWatchData(name, time);
                plugin.getTrackerKeeper().getJohnSmith().put(uuid, twd);
                if (plugin.isDisguisesOnServer()) {
                    TardisArchLibsDisguise.undisguise(player);
                } else {
                    TardisArchDisguise.undisguise(player);
                }
                player.getWorld().strikeLightningEffect(player.getLocation());
                double mh = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
                player.setHealth(mh / 10.0d);
                if (inv) {
                    new TardisArchInventory().switchInventories(player, 0);
                }
                if (plugin.isDisguisesOnServer()) {
                    TardisArchLibsDisguise.disguise(player, name);
                } else {
                    TardisArchDisguise.disguise(player, name);
                }
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.setDisplayName(name);
                    player.setPlayerListName(name);
                }, 5L);
                plugin.getPluginManager().callEvent(new TardisChameleonArchEvent(player, twd));
            } else if (plugin.getTrackerKeeper().getJohnSmith().get(uuid).getTime() <= System.currentTimeMillis()) {
                // no permission check, always allow 'de-fobbing'
                if (plugin.isDisguisesOnServer()) {
                    TardisArchLibsDisguise.undisguise(player);
                } else {
                    TardisArchDisguise.undisguise(player);
                }
                if (inv) {
                    new TardisArchInventory().switchInventories(player, 1);
                }
                player.getWorld().strikeLightningEffect(player.getLocation());
                plugin.getTrackerKeeper().getJohnSmith().remove(uuid);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.setDisplayName(player.getName());
                    player.setPlayerListName(player.getName());
                }, 5L);
                // remove player from arched table
                new TardisArchPersister(plugin).removeArch(uuid);
                plugin.getPluginManager().callEvent(new TardisChameleonArchOffEvent(player));
            }
        }
    }
}
