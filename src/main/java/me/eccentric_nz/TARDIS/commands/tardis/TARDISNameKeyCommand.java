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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.utility.TARDISItemRenamer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISNameKeyCommand {

    private final TARDIS plugin;

    public TARDISNameKeyCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean nameKey(Player player, String[] args) {
        // determine key item
        String key;
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player.getName());
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
        if (rsp.resultSet()) {
            key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("key");
        } else {
            key = plugin.getConfig().getString("key");
        }
        Material m = Material.getMaterial(key);
        if (m.equals(Material.AIR)) {
            player.sendMessage(plugin.pluginName + "You cannot rename AIR!");
            return true;
        }
        ItemStack is = player.getItemInHand();
        if (!is.getType().equals(m)) {
            player.sendMessage(plugin.pluginName + "You can only rename the TARDIS key!");
            return true;
        }
        int count = args.length;
        if (count < 2) {
            return false;
        }
        StringBuilder buf = new StringBuilder(args[1]);
        for (int i = 2; i < count; i++) {
            buf.append(" ").append(args[i]);
        }
        String tmp = buf.toString();
        if (!tmp.isEmpty()) {
            TARDISItemRenamer ir = new TARDISItemRenamer(is);
            ir.setName(tmp, false);
            player.sendMessage(plugin.pluginName + "TARDIS key renamed to '" + tmp + "'");
            return true;
        } else {
            return false;
        }
    }
}
