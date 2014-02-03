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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetKeyCommand {

    private final TARDIS plugin;

    public TARDISSetKeyCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setKeyPref(Player player, String[] args, QueryFactory qf) {
        if (args.length < 2) {
            player.sendMessage(plugin.getPluginName() + "You need to specify a key item!");
            return false;
        }
        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
        try {
            Material go = Material.valueOf(setMaterial);
        } catch (IllegalArgumentException e) {
            player.sendMessage(plugin.getPluginName() + ChatColor.RED + MESSAGE.NOT_VALID_MATERIAL.getText());
            return false;
        }
        String field = (plugin.getConfig().getString("storage.database").equals("sqlite")) ? "key" : "key_item";
        HashMap<String, Object> setk = new HashMap<String, Object>();
        setk.put(field, setMaterial);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player.getName());
        qf.doUpdate("player_prefs", setk, where);
        player.sendMessage(plugin.getPluginName() + "Key preference saved.");
        return true;
    }
}
