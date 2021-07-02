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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISItemRenamer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author eccentric_nz
 */
class TARDISNameKeyCommand {

    private final TARDIS plugin;

    TARDISNameKeyCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean nameKey(Player player, String[] args) {
        // determine key item
        String key;
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        if (rsp.resultSet()) {
            key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
        } else {
            key = plugin.getConfig().getString("preferences.key");
        }
        assert key != null;
        Material m = Material.getMaterial(key);
        assert m != null;
        if (m.isAir()) {
            TARDISMessage.send(player, "KEY_NO_RENAME");
            return true;
        }
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!is.getType().equals(m)) {
            TARDISMessage.send(player, "KEY_ONLY");
            return true;
        }
        int count = args.length;
        if (count < 2) {
            return false;
        }
        String newName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if (!newName.isEmpty()) {
            TARDISItemRenamer ir = new TARDISItemRenamer(plugin, player, is);
            ir.setName(newName, false);
            TARDISMessage.send(player, "KEY_RENAMED", newName);
            return true;
        } else {
            return false;
        }
    }
}
