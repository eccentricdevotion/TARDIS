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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * The Silurians, also known as Earth Reptiles, Eocenes, Homo reptilia and
 * Psionosauropodomorpha, are a species of Earth reptile. Technologically
 * advanced, they live alongside their aquatic cousins, the Sea Devils.
 *
 * @author eccentric_nz
 */
public class TARDISBlockBreakListener implements Listener {

    private final TARDIS plugin;

    public TARDISBlockBreakListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for the TARDIS Police Box sign being broken. If the sign is
     * broken, then the TARDIS is destroyed, the database records removed and
     * the TARDIS world deleted.
     *
     * @param event a player breaking a block
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onSignBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.WALL_SIGN) {
            // check the text on the sign
            Sign sign = (Sign) block.getState();
            String line1 = sign.getLine(1);
            String line2 = sign.getLine(2);
            if (line1.equals(ChatColor.WHITE + "POLICE") && line2.equals(ChatColor.WHITE + "BOX")) {
                event.setCancelled(true);
                sign.update();
                Player player = event.getPlayer();
                if (player.hasPermission("tardis.exterminate")) {
                    final String playerNameStr = player.getName();
                    // check it is their TARDIS
                    plugin.trackExterminate.put(playerNameStr, block);
                    long timeout = plugin.getConfig().getLong("police_box.confirm_timeout");
                    player.sendMessage(plugin.pluginName + "Are you sure you want to delete the TARDIS? Type " + ChatColor.AQUA + "/tardis exterminate" + ChatColor.RESET + " within " + timeout + " seconds to proceed.");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.trackExterminate.remove(playerNameStr);
                        }
                    }, timeout * 20);
                } else {
                    player.sendMessage(plugin.pluginName + "You do not have permission to delete a TARDIS!");
                }
            }
        }
    }
}
