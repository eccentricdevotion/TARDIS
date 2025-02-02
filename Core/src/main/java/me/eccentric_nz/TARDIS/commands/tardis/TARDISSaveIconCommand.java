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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class TARDISSaveIconCommand {

    private final TARDIS plugin;

    public TARDISSaveIconCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean changeIcon(CommandSender sender, String[] args) {
        if (args.length < 3) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return false;
        }
        Material material;
        try {
            material = Material.valueOf(args[2].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
            return false;
        }
        String m = material.toString();
        if (args[0].equalsIgnoreCase("dimensionicon")) {
            if (!TARDISPermission.hasPermission(sender, "tardis.admin")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                return true;
            }
            World world = TARDISAliasResolver.getWorldFromAlias(args[1]);
            if (world == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_WORLD");
                return false;
            }
            plugin.getPlanetsConfig().set("planets." + world.getName() + ".icon", m);
            try {
                plugin.getPlanetsConfig().save(new File(plugin.getDataFolder(), "planets.yml"));
            } catch (IOException ex) {
                plugin.debug("Could not save planets.yml, " + ex.getMessage());
            }
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "DIMENSION_ICON", m);
        } else {
            if (sender instanceof Player player && TARDISPermission.hasPermission(player, "tardis.save")) {
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (!rs.fromUUID(player.getUniqueId().toString())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                    return false;
                }
                int id = rs.getTardisId();
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("dest_name", args[1]);
                whered.put("tardis_id", id);
                ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                if (!rsd.resultSet()) {
                    plugin.getMessenger().sendColouredCommand(player, "SAVE_NOT_FOUND", "/tardis list saves", plugin);
                    return false;
                }
                int destID = rsd.getDest_id();
                HashMap<String, Object> did = new HashMap<>();
                did.put("dest_id", destID);
                HashMap<String, Object> set = new HashMap<>();
                set.put("icon", m);
                plugin.getQueryFactory().doUpdate("destinations", set, did);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_ICON", m);
            }
        }
        return true;
    }
}
