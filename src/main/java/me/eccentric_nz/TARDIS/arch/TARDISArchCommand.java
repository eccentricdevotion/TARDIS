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

import java.util.ArrayList;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArchCommand {

    private final TARDIS plugin;

    public TARDISArchCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean getTime(Player player) {
        UUID uuid = player.getUniqueId();
        long time = plugin.getTrackerKeeper().getJohnSmith().get(uuid).getTime();
        long now = System.currentTimeMillis();
        long diff = (time - now);
        String sub0 = String.format("%d", (diff / (1000 * 60)) % 60);
        String sub1 = String.format("%d", (diff / 1000) % 60);
        TARDISMessage.send(player, "ARCH_TIME", sub0, sub1);
        return true;
    }

    public boolean whois(CommandSender sender, String[] args) {
        for (Player p : new ArrayList<Player>(plugin.getServer().getOnlinePlayers())) {
            if (ChatColor.stripColor(p.getPlayerListName()).equalsIgnoreCase(args[1])) {
                TARDISMessage.send(sender, "ARCH_PLAYER", p.getName());
                return true;
            }
        }
        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
        return true;
    }
}
