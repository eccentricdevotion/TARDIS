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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISThemeButton;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A control room's look could be changed over time. The process by which an
 * operator could transform a control room was fairly simple, once compared by
 * the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISArchiveMenuListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISArchiveMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS Archive")) {
            final Player p = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                event.setCancelled(true);
                switch (slot) {
                    case 17:
                        // back
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", p.getUniqueId().toString());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                        rs.resultSet();
                        final Tardis tardis = rs.getTardis();
                        // return to Desktop Theme GUI
                        close(p);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                new TARDISThemeButton(plugin, p, tardis.getSchematic(), tardis.getArtron_level(), tardis.getTardis_id()).clickButton();
                            }
                        }, 2L);
                        break;
                    case 18:
                        // size
                        ItemStack iss = inv.getItem(18);
                        ItemMeta ims = iss.getItemMeta();
                        List<String> lores = ims.getLore();
                        String t;
                        String b;
                        int s;
                        int o = ConsoleSize.valueOf(lores.get(0)).ordinal();
                        s = (o < 2) ? o + 1 : 0;
                        t = ConsoleSize.values()[s].toString();
                        b = ConsoleSize.values()[s].getBlocks();
                        if (t != null) {
                            ims.setLore(Arrays.asList(t, b, ChatColor.AQUA + "Click to change"));
                            iss.setItemMeta(ims);
                        }
                        break;
                    case 19:
                        // scan
                        scan(p, inv);
                        break;
                    case 20:
                        // archive
                        archive(p, inv);
                        break;
                    case 22:
                    case 23:
                    case 24:
                        ItemStack template = inv.getItem(slot);
                        if (template != null) {
                            final UUID uuid = p.getUniqueId();
                            final TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                            ItemMeta im = template.getItemMeta();
                            String size = im.getDisplayName().toLowerCase(Locale.ENGLISH);
                            int upgrade = plugin.getArtronConfig().getInt("upgrades." + size);
                            if (tud.getLevel() >= upgrade) {
                                new ArchiveUpdate(plugin, uuid.toString(), im.getDisplayName()).setInUse();
                                tud.setSchematic(CONSOLES.SCHEMATICFor(size));
                                tud.setWall("WOOL:1");
                                tud.setFloor("WOOL:8");
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                                        // process upgrade
                                        new TARDISThemeProcessor(plugin, uuid).changeDesktop();
                                    }
                                }, 10L);
                                close(p);
                            }
                        }
                        break;
                    case 26:
                        // close
                        close(p);
                        break;
                    default:
                        // get Display name of selected archive
                        ItemStack choice = inv.getItem(slot);
                        if (choice != null) {
                            // remember the upgrade choice
                            SCHEMATIC schm = CONSOLES.SCHEMATICFor("archive");
                            final UUID uuid = p.getUniqueId();
                            final TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                            ItemMeta im = choice.getItemMeta();
                            List<String> lore = im.getLore();
                            if (lore.contains(ChatColor.GREEN + plugin.getLanguage().getString("CURRENT_CONSOLE"))) {
                                TARDISMessage.send(p, "ARCHIVE_NOT_CURRENT");
                                return;
                            }
                            int upgrade = plugin.getArtronConfig().getInt("upgrades.archive.tall");
                            for (String l : lore) {
                                if (l.startsWith("Cost")) {
                                    upgrade = TARDISNumberParsers.parseInt(l.replace("Cost: ", ""));
                                }
                            }
                            if (tud.getLevel() >= upgrade) {
                                new ArchiveUpdate(plugin, uuid.toString(), im.getDisplayName()).setInUse();
                                tud.setSchematic(schm);
                                tud.setWall("WOOL:1");
                                tud.setFloor("WOOL:8");
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                                        // process upgrade
                                        new TARDISThemeProcessor(plugin, uuid).changeDesktop();
                                    }
                                }, 10L);
                                close(p);
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
     * Closes the inventory and scans the current console.
     *
     * @param p the player using the GUI
     */
    private void scan(final Player p, final Inventory inv) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                List<String> lore = getSizeLore(inv);
                String size = lore.get(0);
                p.closeInventory();
                p.performCommand("tardis archive scan " + size);
            }
        }, 1L);
    }

    /**
     * Closes the inventory and archives the current console. A random name will
     * be generated.
     *
     * @param p the player using the GUI
     */
    private void archive(final Player p, final Inventory inv) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                List<String> lore = getSizeLore(inv);
                String size = lore.get(0);
                p.closeInventory();
                // generate random name
                String name = TARDISRandomArchiveName.getRandomName();
                p.performCommand("tardis archive add " + name + " " + size);
            }
        }, 1L);
    }

    private List<String> getSizeLore(Inventory inv) {
        ItemStack is = inv.getItem(18);
        ItemMeta im = is.getItemMeta();
        return im.getLore();
    }
}
