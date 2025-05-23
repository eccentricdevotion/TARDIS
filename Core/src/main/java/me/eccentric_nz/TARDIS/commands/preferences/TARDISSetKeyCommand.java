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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISSetKeyCommand {

    private final TARDIS plugin;
    private final List<Material> keys = new ArrayList<>();

    TARDISSetKeyCommand(TARDIS plugin) {
        this.plugin = plugin;
        plugin.getBlocksConfig().getStringList("keys").forEach((m) -> {
            try {
                keys.add(Material.valueOf(m));
            } catch (IllegalArgumentException e) {
                plugin.debug("Illegal Key value!");
            }
        });
    }

    boolean setKeyPref(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "KEY_NEED");
            return false;
        }
        String setMaterial = args[1].toUpperCase(Locale.ROOT);
        Material go;
        try {
            go = Material.valueOf(setMaterial);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
            return false;
        }
        if (go.isBlock()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "KEY_NO_BLOCK");
            return true;
        }
        if (plugin.getConfig().getBoolean("travel.give_key") && !plugin.getConfig().getBoolean("allow.all_blocks") && !keys.contains(go)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
            return true;
        }
        String field = (plugin.getConfig().getString("storage.database").equals("sqlite")) ? "key" : "key_item";
        HashMap<String, Object> setk = new HashMap<>();
        setk.put(field, setMaterial);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().doUpdate("player_prefs", setk, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "KEY_SAVED");
        return true;
    }
}
