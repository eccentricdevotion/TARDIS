/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetKeyCommand {

    private final TARDIS plugin;
    private final List<Material> keys = new ArrayList<Material>();

    public TARDISSetKeyCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (String m : plugin.getBlocksConfig().getStringList("keys")) {
            try {
                keys.add(Material.valueOf(m));
            } catch (IllegalArgumentException e) {
            }
        }
    }

    public boolean setKeyPref(Player player, String[] args, QueryFactory qf) {
        if (args.length < 2) {
            TARDISMessage.send(player, "KEY_NEED");
            return false;
        }
        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
        Material go;
        try {
            go = Material.valueOf(setMaterial);
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(player, "MATERIAL_NOT_VALID");
            return false;
        }
        if (go.isBlock()) {
            TARDISMessage.send(player, "KEY_NO_BLOCK");
            return true;
        }
        if (plugin.getConfig().getBoolean("travel.give_key") && !plugin.getConfig().getBoolean("allow.all_blocks") && !keys.contains(go)) {
            TARDISMessage.send(player, "MATERIAL_NOT_VALID");
            return true;
        }
        String field = (plugin.getConfig().getString("storage.database").equals("sqlite")) ? "key" : "key_item";
        HashMap<String, Object> setk = new HashMap<String, Object>();
        setk.put(field, setMaterial);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", player.getUniqueId().toString());
        qf.doUpdate("player_prefs", setk, where);
        TARDISMessage.send(player, "KEY_SAVED");
        return true;
    }
}
