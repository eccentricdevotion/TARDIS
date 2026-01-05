/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class OccupyCommand {

    private final TARDIS plugin;

    OccupyCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean toggleOccupancy(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            Component occupied;
            if (rst.resultSet()) {
                // only if they're not in the TARDIS world
                if (!plugin.getUtils().inTARDISWorld(player)) {
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("uuid", player.getUniqueId().toString());
                    plugin.getQueryFactory().doDelete("travellers", whered);
                    occupied = Component.text(plugin.getLanguage().getString("OCCUPY_OUT", "UNOCCUPIED"), NamedTextColor.RED);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "OCCUPY_MUST_BE_OUT");
                    return true;
                }
            } else if (plugin.getUtils().inTARDISWorld(player)) {
                ResultSetTardisID rsid = new ResultSetTardisID(plugin);
                // if TIPS determine tardis_id from player location
                if (plugin.getConfig().getBoolean("creation.default_world") && !player.hasPermission("tardis.create_world")) {
                    int slot = TARDISInteriorPostioning.getTIPSSlot(player.getLocation());
                    if (!rsid.fromTIPSSlot(slot)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "OCCUPY_MUST_BE_IN");
                        return false;
                    }
                } else if (!rsid.fromUUID(player.getUniqueId().toString())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                    return false;
                }
                int id = rsid.getTardisId();
                HashMap<String, Object> wherei = new HashMap<>();
                wherei.put("tardis_id", id);
                wherei.put("uuid", player.getUniqueId().toString());
                plugin.getQueryFactory().doInsert("travellers", wherei);
                occupied = Component.text(plugin.getLanguage().getString("OCCUPY_IN", "OCCUPIED"), NamedTextColor.GREEN);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "OCCUPY_MUST_BE_IN");
                return true;
            }
            if (args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "OCCUPY_SET", occupied);
            }
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
