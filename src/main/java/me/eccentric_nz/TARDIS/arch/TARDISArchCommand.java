/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISArchCommand {

    private final TARDIS plugin;

    public TARDISArchCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean getTime(Player player) {
        UUID uuid = player.getUniqueId();
        if (!plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCH_NOT_VALID");
            return true;
        }
        long time = plugin.getTrackerKeeper().getJohnSmith().get(uuid).getTime();
        long now = System.currentTimeMillis();
        long diff = (time - now);
        if (diff > 0) {
            String sub0 = String.format("%d", (diff / (1000 * 60)) % 60);
            String sub1 = String.format("%d", (diff / 1000) % 60);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCH_TIME", sub0, sub1);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCH_FREE");
        }
        return true;
    }

    public boolean whois(CommandSender sender, String[] args) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (ChatColor.stripColor(p.getPlayerListName()).equalsIgnoreCase(args[1])) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARCH_PLAYER", p.getName());
                return true;
            }
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
        return true;
    }

    public boolean force(CommandSender sender, String[] args) {
        if (args[2].length() < 2) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        Player player = plugin.getServer().getPlayer(args[1]);
        if (player == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return true;
        }
        UUID uuid = player.getUniqueId();
        boolean inv = plugin.getConfig().getBoolean("arch.switch_inventory");
        if (!plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
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
            double mh = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
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
        } else {
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
            }, 5L);            // remove player from arched table
            new TARDISArchPersister(plugin).removeArch(uuid);
        }
        return true;
    }
}
