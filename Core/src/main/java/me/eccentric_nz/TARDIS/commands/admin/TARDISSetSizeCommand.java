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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISSetSizeCommand {

    private final TARDIS plugin;

    TARDISSetSizeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean overwrite(CommandSender sender, String[] args) {
        // /tadmin set_size [player] [size]
        // get the player
        Player p = plugin.getServer().getPlayer(args[1]);
        if (p == null) { // player must be online
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return true;
        }
        String type = args[2].toUpperCase(Locale.ROOT);
        // check size
        if (!Consoles.getBY_NAMES().containsKey(type)) {
            plugin.getMessenger().message(sender, "Not a valid console size! Try using tab completion.");
            return true;
        }
        UUID uuid = p.getUniqueId();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        HashMap<String, Object> set = new HashMap<>();
        set.put("size", type);
        plugin.getQueryFactory().doUpdate("tardis", set, where);
        plugin.getMessenger().message(sender, "Successfully set " + args[1] + "'s console size to " + type);
        return true;
    }
}
