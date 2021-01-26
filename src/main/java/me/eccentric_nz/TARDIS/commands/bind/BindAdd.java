/*
 * Copyright (C) 2020 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class BindAdd {

    private final TARDIS plugin;

    public BindAdd(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setClick(Bind bind, Player player, int id, String[] args) {
        String which = (args.length > 2) ? args[2] : "";
        int bind_id = 0;
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        switch (bind) {
            case SAVE: // type 0
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("tardis_id", id);
                whered.put("name", which);
                ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                if (!rsd.resultSet()) {
                    TARDISMessage.send(player, "SAVE_NOT_FOUND", ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET);
                    return true;
                } else {
                    set.put("type", 0);
                    set.put("name", which);
                    bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                }
                break;
            case CAVE: // type 1
            case HIDE:
            case HOME:
            case MAKE_HER_BLUE:
            case OCCUPY:
            case REBUILD:
                set.put("type", 1);
                set.put("name", bind.toString().toLowerCase());
                bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                break;
            case PLAYER: // type 2
                // get player online or offline
                Player p = plugin.getServer().getPlayer(which);
                if (p == null) {
                    OfflinePlayer offp = plugin.getServer().getOfflinePlayer(which);
                    if (offp == null) {
                        TARDISMessage.send(player, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                }
                set.put("name", which);
                set.put("type", 2);
                bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                break;
            case AREA: // type 3
                // check area name
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("area_name", which);
                ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                if (!rsa.resultSet()) {
                    TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                    return true;
                }
                if (!TARDISPermission.hasPermission(player, "tardis.area." + which) || !player.isPermissionSet("tardis.area." + which)) {
                    TARDISMessage.send(player, "BIND_NO_AREA_PERM", which);
                    return true;
                }
                set.put("name", which.toLowerCase(Locale.ENGLISH));
                set.put("type", 3);
                bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                break;
            case BIOME:  // type 4
                // check valid biome
                Biome biome;
                try {
                    biome = Biome.valueOf(which.toUpperCase(Locale.ENGLISH));
                    if (!biome.equals(Biome.THE_VOID)) {
                        set.put("type", 4);
                        set.put("name", biome.toString());
                        bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                    }
                } catch (IllegalArgumentException iae) {
                    TARDISMessage.send(player, "BIOME_NOT_VALID");
                    return true;
                }
                break;
            case CHAMELEON: // type 5
                if (which.equalsIgnoreCase("OFF") || which.equalsIgnoreCase("ADAPT")) {
                    set.put("name", which.toUpperCase(Locale.ENGLISH));
                } else {
                    // check valid preset
                    PRESET preset;
                    try {
                        preset = PRESET.valueOf(which.toUpperCase(Locale.ENGLISH));
                    } catch (IllegalArgumentException e) {
                        // abort
                        TARDISMessage.send(player, "ARG_PRESET");
                        return true;
                    }
                    set.put("name", preset.toString());
                }
                set.put("type", 5);
                bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                break;
            case TRANSMAT: // type 6
                // check transmat location exists
                if (args.length > 2) {
                    ResultSetTransmat rst = new ResultSetTransmat(plugin, id, which);
                    if (rst.resultSet()) {
                        set.put("name", which);
                    } else {
                        // abort
                        TARDISMessage.send(player, "TRANSMAT_NOT_FOUND");
                        return true;
                    }
                } else {
                    set.put("name", "console");
                }
                set.put("type", 6);
                bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                break;
            default:
                break;
        }
        if (bind_id != 0) {
            plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
            TARDISMessage.send(player, "BIND_CLICK");
            return true;
        }
        return false;
    }
}
