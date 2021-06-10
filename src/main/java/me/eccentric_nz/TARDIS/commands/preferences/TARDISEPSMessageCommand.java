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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISEPSMessageCommand {

    boolean setMessage(Player player, String[] args) {
        String message;
        int count = args.length;
        ItemStack bq = player.getInventory().getItemInMainHand();
        if (bq.getType().equals(Material.WRITABLE_BOOK) || bq.getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta bm = (BookMeta) bq.getItemMeta();
            assert bm != null;
            List<String> pages = bm.getPages();
            StringBuilder sb = new StringBuilder();
            pages.forEach((s) -> sb.append(s).append(" "));
            message = sb.toString();
        } else {
            if (count < 2) {
                TARDISMessage.send(player, "EP1_NEED");
                return false;
            }
            StringBuilder buf = new StringBuilder();
            for (int i = 1; i < count; i++) {
                buf.append(args[i]).append(" ");
            }
            String tmp = buf.toString();
            message = tmp.substring(0, tmp.length() - 1);
        }
        HashMap<String, Object> sete = new HashMap<>();
        sete.put("eps_message", message);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        TARDISPlugin.plugin.getQueryFactory().doUpdate("player_prefs", sete, where);
        TARDISMessage.send(player, "EP1_SET");
        return true;
    }
}
