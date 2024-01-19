/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetInventory;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

class TARDISSaveSign {

    private final TARDIS plugin;

    TARDISSaveSign(TARDIS plugin) {
        this.plugin = plugin;
    }

    void openGUI(Player player, int id) {
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasMemory()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MEM_CIRCUIT");
            return;
        }
        if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(player.getUniqueId()) && plugin.getDifficulty().equals(Difficulty.HARD)) {
            ItemStack disk = player.getInventory().getItemInMainHand();
            if (disk.hasItemMeta() && disk.getItemMeta().hasDisplayName() && disk.getItemMeta().getDisplayName().equals("Save Storage Disk")) {
                List<String> lore = disk.getItemMeta().getLore();
                if (!lore.get(0).equals("Blank")) {
                    // read the lore from the disk
                    String world = lore.get(1);
                    int x = TARDISNumberParsers.parseInt(lore.get(2));
                    int y = TARDISNumberParsers.parseInt(lore.get(3));
                    int z = TARDISNumberParsers.parseInt(lore.get(4));
                    HashMap<String, Object> set_next = new HashMap<>();
                    set_next.put("world", world);
                    set_next.put("x", x);
                    set_next.put("y", y);
                    set_next.put("z", z);
                    set_next.put("direction", lore.get(6));
                    boolean sub = Boolean.parseBoolean(lore.get(7));
                    set_next.put("submarine", (sub) ? 1 : 0);
                    plugin.getMessenger().send(player, "LOC_SET", true);
                    // update next
                    HashMap<String, Object> where_next = new HashMap<>();
                    where_next.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.SAVE));
                }
            } else {
                TARDISSavesPlanetInventory sst = new TARDISSavesPlanetInventory(plugin, id);
                ItemStack[] items = sst.getPlanets();
                Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Dimension Map");
                inv.setContents(items);
                player.openInventory(inv);
            }
        } else {
            TARDISSavesPlanetInventory sst = new TARDISSavesPlanetInventory(plugin, id);
            ItemStack[] items = sst.getPlanets();
            Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Dimension Map");
            inv.setContents(items);
            player.openInventory(inv);
        }
    }
}
