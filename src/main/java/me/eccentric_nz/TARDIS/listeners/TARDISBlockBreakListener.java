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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
    private final HashMap<String, String> sign_lookup = new HashMap<String, String>();

    public TARDISBlockBreakListener(TARDIS plugin) {
        this.plugin = plugin;
        int i = 0;
        for (PRESET p : PRESET.values()) {
            if (!p.getFirstLine().isEmpty() && !sign_lookup.containsKey(p.getFirstLine())) {
                sign_lookup.put(p.getFirstLine(), p.getSecondLine());
            }
        }
    }

    /**
     * Listens for the TARDIS Police Box sign being broken. If the sign is
     * broken, then the TARDIS is destroyed, the database records removed and
     * the TARDIS world deleted.
     *
     * @param event a player breaking a block
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSignBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NOT_IN_ZERO");
            return;
        }
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.WALL_SIGN) {
            // check the text on the sign
            Sign sign = (Sign) block.getState();
            String line1 = ChatColor.stripColor(sign.getLine(1));
            String line2 = ChatColor.stripColor(sign.getLine(2));
            if (sign_lookup.containsKey(line1) && line2.equals(sign_lookup.get(line1))) {
                event.setCancelled(true);
                sign.update();
                if (player.hasPermission("tardis.exterminate")) {
                    final UUID uuid = player.getUniqueId();
                    // check it is their TARDIS
                    plugin.getTrackerKeeper().getExterminate().put(uuid, block);
                    long timeout = plugin.getConfig().getLong("police_box.confirm_timeout");
                    TARDISMessage.send(player, "Q_DELETE", ChatColor.AQUA + "/tardis exterminate" + ChatColor.RESET, String.format("%d", timeout));
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.getTrackerKeeper().getExterminate().remove(uuid);
                        }
                    }, timeout * 20);
                } else {
                    TARDISMessage.send(player, "NO_PERM_DELETE");
                }
            }
        }
    }
}
