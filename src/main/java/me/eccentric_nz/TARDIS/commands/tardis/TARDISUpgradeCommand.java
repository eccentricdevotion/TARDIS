/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.desktop.TARDISThemeInventory;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISUpgradeCommand {

    private final TARDIS plugin;

    public TARDISUpgradeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean openUpgradeGUI(Player player) {
        if (!player.hasPermission("tardis.upgrade")) {
            TARDISMessage.send(player, "NO_PERM_UPGRADE");
            return true;
        }
        // they must have an existing TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, "NO_TARDIS");
            return false;
        }
        Tardis tardis = rs.getTardis();
        // console must in a TARDIS world
        if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
            TARDISMessage.send(player, "UPGRADE_ABORT_WORLD");
            return true;
        }
        // it must be their own TARDIS
        boolean own;
        Location pl = player.getLocation();
        String current_world = pl.getWorld().getName();
        String[] split = tardis.getChunk().split(":");
        String db_world = split[0];
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && player.hasPermission("tardis.create_world")) {
                own = (current_world.equals(db_world));
            } else {
                // get if player is in TIPS area for their TARDIS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(tardis.getTIPS());
                own = (pl.getBlockX() > pos.getMinX() && pl.getBlockZ() > pos.getMinZ() && pl.getBlockX() < pos.getMaxX() && pl.getBlockZ() < pos.getMaxZ());
            }
        } else {
            own = (current_world.equals(db_world));
        }
        if (!own) {
            TARDISMessage.send(player, "NOT_OWNER");
            return true;
        }
        // get player's current console
        SCHEMATIC current_console = tardis.getSchematic();
        int level = tardis.getArtron_level();
        TARDISUpgradeData tud = new TARDISUpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
        // open the upgrade menu
        ItemStack[] consoles = new TARDISThemeInventory(plugin, player, current_console.getPermission(), level).getMenu();
        Inventory upg = plugin.getServer().createInventory(player, 27, "§4TARDIS Upgrade Menu");
        upg.setContents(consoles);
        player.openInventory(upg);
        return true;
    }
}
