/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.advanced;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConsoleListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> onlythese = new ArrayList<Material>();
    private final List<String> metanames = new ArrayList<String>();

    public TARDISConsoleListener(TARDIS plugin) {
        this.plugin = plugin;
        for (DISK_CIRCUIT dc : DISK_CIRCUIT.values()) {
            metanames.add(dc.getName());
            if (!onlythese.contains(dc.getMaterial())) {
                onlythese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        final Inventory inv = event.getInventory();
        String inv_name = inv.getTitle();
        if (inv_name.equals("§4TARDIS Console")) {
            Player p = ((Player) event.getPlayer());
            // loop through inventory contents and remove any items that are not disks or circuits
            for (int i = 0; i < 9; i++) {
                ItemStack is = inv.getItem(i);
                if (is != null) {
                    if (!onlythese.contains(is.getType())) {
                        p.getLocation().getWorld().dropItemNaturally(p.getLocation(), is);
                        inv.setItem(i, new ItemStack(Material.AIR));
                    } else {
                        // process any disks
                        switch (is.getType()) {
                            case RECORD_3: // area
                                break;
                            case GREEN_RECORD: // biome
                                break;
                            case RECORD_12: // player
                                break;
                            case RECORD_6: // preset
                                break;
                            case RECORD_4: // save
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            // remember what was placed in the console
            saveCurrentConsole(inv, p.getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onConsoleInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("tardis.advanced")) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack disk = event.getPlayer().getItemInHand();
            if (onlythese.contains(disk.getType()) && disk.hasItemMeta()) {
                ItemMeta im = disk.getItemMeta();
                if (im.hasDisplayName() && metanames.contains(im.getDisplayName())) {
                    Block b = event.getClickedBlock();
                    if (b != null && b.getType().equals(Material.JUKEBOX)) {
                        // is it a TARDIS console?
                        HashMap<String, Object> wherec = new HashMap<String, Object>();
                        wherec.put("location", b.getLocation().toString());
                        wherec.put("type", 15);
                        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                        if (rsc.resultSet()) {
                            event.setCancelled(true);
                            int id = rsc.getTardis_id();
                            // only the time lord of this tardis
                            HashMap<String, Object> wheret = new HashMap<String, Object>();
                            wheret.put("tardis_id", id);
                            wheret.put("owner", player.getName());
                            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                            if (!rs.resultSet()) {
                                player.sendMessage(plugin.pluginName + MESSAGE.NOT_OWNER);
                                return;
                            }
                            Inventory inv = plugin.getServer().createInventory(player, 9, "§4TARDIS Console");
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("owner", player.getName());
                            ResultSetDiskStorage rsds = new ResultSetDiskStorage(plugin, where);
                            if (rsds.resultSet()) {
                                String console = rsds.getConsole();
                                if (!console.isEmpty()) {
                                    try {
                                        ItemStack[] stack = TARDISSerializeInventory.itemStacksFromString(console);
                                        inv.setContents(stack);
                                    } catch (IOException ex) {
                                        plugin.debug("Could not read console from database!");
                                    }
                                }
                            } else {
                                // create new storage record
                                HashMap<String, Object> setstore = new HashMap<String, Object>();
                                setstore.put("owner", player.getName());
                                setstore.put("tardis_id", id);
                                new QueryFactory(plugin).doInsert("storage", setstore);
                            }
                            // open gui
                            player.openInventory(inv);
                        }
                    }
                }
            }
        }
    }

    private void saveCurrentConsole(Inventory inv, String p) {
        String serialized = TARDISSerializeInventory.itemStacksToString(inv.getContents());
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("console", serialized);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", p);
        new QueryFactory(plugin).doUpdate("storage", set, where);
    }
}
