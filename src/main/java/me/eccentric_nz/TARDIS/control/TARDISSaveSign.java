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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetInventory;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

class TARDISSaveSign {

    private final TARDIS plugin;

    TARDISSaveSign(TARDIS plugin) {
        this.plugin = plugin;
    }

    void openGUI(Player player, int id) {
        UUID uuid = player.getUniqueId();
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.SAVES)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
            return;
        }
        TARDISCircuitChecker tcc = null;
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, false)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasMemory()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MEM_CIRCUIT");
            return;
        }
        if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(uuid) && plugin.getConfig().getBoolean("difficulty.disks")) {
            ItemStack disk = player.getInventory().getItemInMainHand();
            if (disk.hasItemMeta() && disk.getItemMeta().hasDisplayName() && ComponentUtils.endsWith(disk.getItemMeta().displayName(), "Save Storage Disk")) {
                List<Component> lore = disk.getItemMeta().lore();
                if (!ComponentUtils.stripColour(lore.getFirst()).equals("Blank")) {
                    // read the lore from the disk
                    String world = ComponentUtils.stripColour(lore.get(1));
                    int x = ComponentUtils.parseInt(lore.get(2));
                    int y = ComponentUtils.parseInt(lore.get(3));
                    int z = ComponentUtils.parseInt(lore.get(4));
                    HashMap<String, Object> set_next = new HashMap<>();
                    set_next.put("world", world);
                    set_next.put("x", x);
                    set_next.put("y", y);
                    set_next.put("z", z);
                    set_next.put("direction", lore.get(6));
                    boolean sub = ComponentUtils.parseBoolean(lore.get(7));
                    set_next.put("submarine", (sub) ? 1 : 0);
                    plugin.getMessenger().send(player, "LOC_SET", true);
                    // update next
                    HashMap<String, Object> where_next = new HashMap<>();
                    where_next.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.SAVE));
                }
            } else {
                player.openInventory(new TARDISSavesPlanetInventory(plugin, id, player).getInventory());
            }
        } else {
            player.openInventory(new TARDISSavesPlanetInventory(plugin, id, player).getInventory());
        }
    }
}
