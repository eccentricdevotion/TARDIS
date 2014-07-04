/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISFobWatchListener implements Listener {

    private final TARDIS plugin;

    public TARDISFobWatchListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (!plugin.getConfig().getBoolean("allow.chameleon_arch")) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        ItemStack is = event.getItem();
        if (!is.getType().equals(Material.WATCH) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.getDisplayName().equals("Fob Watch")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!player.hasPermission("tardis.chameleonarch")) {
            TARDISMessage.send(player, "NO_PERM_CHAM_ARCH");
            return;
        }
        final UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getJohnSmith().contains(uuid)) {
            return;
        }
        plugin.getTrackerKeeper().getJohnSmith().add(uuid);
        if (DisguiseAPI.isDisguised(player)) {
            DisguiseAPI.undisguiseToAll(player);
        }
        player.getWorld().strikeLightningEffect(player.getLocation());
        PlayerDisguise playerDisguise = new PlayerDisguise("§§§§");
        DisguiseAPI.disguiseToAll(player, playerDisguise);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (DisguiseAPI.isDisguised(player)) {
                    DisguiseAPI.undisguiseToAll(player);
                }
                plugin.getTrackerKeeper().getJohnSmith().remove(uuid);
            }
        }, 300L);
    }
}
