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
package me.eccentric_nz.TARDIS.desktop;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCount;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A control room's look could be changed over time. The process by which an
 * operator could transform a control room was fairly simple, once compared by
 * the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISThemeMenuListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISThemeMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS Upgrade Menu")) {
            Player p = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                event.setCancelled(true);
                switch (slot) {
                    case 18:
                        // archive
                        archive(p);
                        break;
                    case 19:
                        // repair
                        repair(p);
                        break;
                    case 20:
                        // clean
                        clean(p);
                        break;
                    case 26:
                        // close
                        close(p);
                        break;
                    default:
                        // get Display name of selected console
                        ItemStack choice = inv.getItem(slot);
                        if (choice != null) {
                            String perm = CONSOLES.SCHEMATICFor(choice.getType()).getPermission();
                            if (p.hasPermission("tardis." + perm)) {
                                // remember the upgrade choice
                                SCHEMATIC schm = CONSOLES.SCHEMATICFor(perm);
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
                        break;
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
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
    public void close(final Player p) {
        plugin.getTrackerKeeper().getUpgrades().remove(p.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }

    /**
     * Closes the inventory and opens the Wall block selector menu.
     *
     * @param p the player using the GUI
     */
    private void wall(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
                ItemStack[] wall_blocks = new TARDISWallsInventory(plugin).getMenu();
                Inventory wall = plugin.getServer().createInventory(p, 54, "§4TARDIS Wall Menu");
                wall.setContents(wall_blocks);
                p.openInventory(wall);
            }
        }, 1L);
    }

    /**
     * Closes the inventory and opens the archive consoles menu.
     *
     * @param p the player using the GUI
     */
    private void archive(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
                ItemStack[] archive = new TARDISArchiveInventory(plugin, p).getArchives();
                Inventory menu = plugin.getServer().createInventory(p, 27, "§4TARDIS Archive");
                menu.setContents(archive);
                p.openInventory(menu);
            }
        }, 1L);
    }

    /**
     * Initiates a TARDIS repair. Resets the console back to the original
     * console schematic, Players must condense all missing blocks - unless the
     * /tardisadmin repair [player] [amount] command has been run, assigning the
     * player a 'free' repair(s).
     */
    private void repair(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
                String uuid = p.getUniqueId().toString();
                boolean repair;
                TARDISRepair tr = new TARDISRepair(plugin, p);
                // is it a free repair?
                ResultSetCount rsc = new ResultSetCount(plugin, uuid);
                if (rsc.resultSet() && rsc.getRepair() > 0) {
                    // decrement repair
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("uuid", uuid);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("repair", rsc.getRepair() - 1);
                    new QueryFactory(plugin).doUpdate("t_count", set, where);
                    repair = true;
                } else {
                    // scan console and check condensed blocks
                    repair = tr.hasCondensedMissingBlocks();
                }
                if (repair) {
                    tr.restore(false);
                }
            }
        }, 1L);
    }

    /**
     * Initiates a TARDIS clean. Removes any blocks that are not part of the
     * original console schematic (missing blocks will not be restored).
     */
    private void clean(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
                new TARDISRepair(plugin, p).restore(true);
            }
        }, 1L);
    }
}
