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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.TardisInteriorPositioning;
import me.eccentric_nz.tardis.builders.TardisTipsData;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.desktop.TardisThemeInventory;
import me.eccentric_nz.tardis.desktop.TardisUpgradeData;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
class TardisUpgradeCommand {

    private final TardisPlugin plugin;

    TardisUpgradeCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean openUpgradeGui(Player player) {
        if (!TardisPermission.hasPermission(player, "tardis.upgrade")) {
            TardisMessage.send(player, "NO_PERM_UPGRADE");
            return true;
        }
        // they must have an existing TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TardisMessage.send(player, "NO_TARDIS");
            return false;
        }
        Tardis tardis = rs.getTardis();
        // console must in a TARDIS world
        if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
            TardisMessage.send(player, "UPGRADE_ABORT_WORLD");
            return true;
        }
        // it must be their own TARDIS
        boolean own;
        Location pl = player.getLocation();
        String current_world = Objects.requireNonNull(pl.getWorld()).getName();
        String[] split = tardis.getChunk().split(":");
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && TardisPermission.hasPermission(player, "tardis.create_world")) {
                own = (current_world.equals(split[0]));
            } else {
                // get if player is in TIPS area for their TARDIS
                TardisInteriorPositioning tintpos = new TardisInteriorPositioning(plugin);
                TardisTipsData pos = tintpos.getTipsData(tardis.getTips());
                own = (pl.getBlockX() > pos.getMinX() && pl.getBlockZ() > pos.getMinZ() && pl.getBlockX() < pos.getMaxX() && pl.getBlockZ() < pos.getMaxZ());
            }
        } else {
            own = (current_world.equals(split[0]));
        }
        if (!own) {
            TardisMessage.send(player, "NOT_OWNER");
            return true;
        }
        // get player's current console
        Schematic current_console = tardis.getSchematic();
        int level = tardis.getArtronLevel();
        TardisUpgradeData tud = new TardisUpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
        // open the upgrade menu
        ItemStack[] consoles = new TardisThemeInventory(plugin, player, current_console.getPermission(), level).getMenu();
        Inventory upg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "tardis Upgrade Menu");
        upg.setContents(consoles);
        player.openInventory(upg);
        return true;
    }
}
