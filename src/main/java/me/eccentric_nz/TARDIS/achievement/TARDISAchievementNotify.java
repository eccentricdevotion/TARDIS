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
package me.eccentric_nz.TARDIS.achievement;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * A message pod was a device used by the Time Lords as, essentially, a secure
 * courier for delivery of messages or small objects to a specific recipient.
 * The pod would only open when touched by the person for whom the message was
 * intended. Much like the outer plasmic shell of a TARDIS, the exterior of the
 * pod was nearly invulnerable to outside forces.
 *
 * @author eccentric_nz
 */
public class TARDISAchievementNotify {

    private final TARDIS plugin;

    public TARDISAchievementNotify(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends a notification to Spout enabled clients/servers, or a plain message
     * when a TARDIS achievement is gained.
     *
     * @param player The player to send the notification to
     * @param msg The message
     * @param mat The icon to display in the notification
     */
    public void sendAchievement(Player player, String msg, Material mat) {
        if (plugin.pm.getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
            ((SpoutPlayer) player).sendNotification("Achievement Get!", msg, mat);
        } else {
            player.sendMessage(ChatColor.YELLOW + "Achievement Get!");
            player.sendMessage(ChatColor.WHITE + msg);
        }
    }
}
