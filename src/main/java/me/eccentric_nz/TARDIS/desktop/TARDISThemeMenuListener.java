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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCount;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISThemeMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISThemeMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Upgrade Menu")) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        ItemStack choice = view.getItem(slot);
        event.setCancelled(true);
        switch (slot) {
            case 46 -> {
                // archive
                if (choice != null) {
                    archive(p);
                }
            }
            case 47 -> {
                // repair
                if (choice != null && plugin.getConfig().getBoolean("allow.repair")) {
                    repair(p);
                }
            }
            case 48 -> {
                // clean
                if (choice != null && plugin.getConfig().getBoolean("allow.repair")) {
                    clean(p);
                }
            }
            case 51 -> {
                // get player upgrade data
                TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(p.getUniqueId());
                ItemStack[] consoles;
                // switch page
                if (choice.getItemMeta().getCustomModelData() == GUIChameleonPresets.GO_TO_PAGE_2.getCustomModelData()) {
                    // page 2
                    consoles = new TARDISCustomThemeInventory(plugin, p, tud.getPrevious().getPermission(), tud.getLevel()).getMenu();
                } else {
                    // page 1
                    consoles = new TARDISPluginThemeInventory(plugin, p, tud.getPrevious().getPermission(), tud.getLevel()).getMenu();
                }
                Inventory upg = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "TARDIS Upgrade Menu");
                upg.setContents(consoles);
                p.openInventory(upg);
            }
            case 53 -> close(p); // close
            default -> {
                // get Display name of selected console
                if (choice == null) {
                    return;
                }
                String perm = Consoles.schematicFor(choice.getType()).getPermission();
                if (!TARDISPermission.hasPermission(p, "tardis." + perm)) {
                    return;
                }
                // remember the upgrade choice
                Schematic schm = Consoles.schematicFor(perm);
                TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(p.getUniqueId());
                int upgrade = plugin.getArtronConfig().getInt("upgrades." + perm);
                int needed = (tud.getPrevious().getPermission().equals(schm.getPermission())) ? upgrade / 2 : upgrade;
                if (tud.getLevel() >= needed) {
                    tud.setSchematic(schm);
                    plugin.getTrackerKeeper().getUpgrades().put(p.getUniqueId(), tud);
                    // open the wall block GUI
                    wall(p);
                    if (tud.getPrevious().getPermission().equals("archive")) {
                        new ArchiveUpdate(plugin, p.getUniqueId().toString(), "ª°º").setInUse();
                    }
                }
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    @Override
    public void close(Player p) {
        plugin.getTrackerKeeper().getUpgrades().remove(p.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }

    /**
     * Closes the inventory and opens the Wall block selector menu.
     *
     * @param p the player using the GUI
     */
    private void wall(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            ItemStack[] wall_blocks = new TARDISWallsInventory(plugin).getMenu();
            Inventory wall = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "TARDIS Wall Menu");
            wall.setContents(wall_blocks);
            p.openInventory(wall);
        }, 1L);
    }

    /**
     * Closes the inventory and opens the archive consoles menu.
     *
     * @param p the player using the GUI
     */
    private void archive(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            ItemStack[] archive = new TARDISArchiveInventory(plugin, p).getArchives();
            Inventory menu = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "TARDIS Archive");
            menu.setContents(archive);
            p.openInventory(menu);
        }, 1L);
    }

    /**
     * Initiates a TARDIS repair. Resets the console back to the original console schematic, Players must condense all
     * missing blocks - unless the /tardisadmin repair [player] [amount] command has been run, assigning the player a
     * 'free' repair(s).
     */
    private void repair(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            String uuid = p.getUniqueId().toString();
            boolean repair;
            TARDISRepair tr = new TARDISRepair(plugin, p);
            // is it a free repair?
            ResultSetCount rsc = new ResultSetCount(plugin, uuid);
            if (rsc.resultSet() && rsc.getRepair() > 0) {
                // decrement repair
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                HashMap<String, Object> set = new HashMap<>();
                set.put("repair", rsc.getRepair() - 1);
                plugin.getQueryFactory().doUpdate("t_count", set, where);
                repair = true;
            } else {
                // scan console and check condensed blocks
                repair = tr.hasCondensedMissingBlocks();
            }
            if (repair) {
                tr.restore(false);
            }
        }, 1L);
    }

    /**
     * Initiates a TARDIS clean. Removes any blocks that are not part of the original console schematic (missing blocks
     * will not be restored).
     */
    private void clean(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
            new TARDISRepair(plugin, p).restore(true);
        }, 1L);
    }
}
