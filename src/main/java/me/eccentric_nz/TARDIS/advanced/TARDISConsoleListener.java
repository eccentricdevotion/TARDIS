/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConsoleListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> onlythese = new ArrayList<Material>();

    public TARDISConsoleListener(TARDIS plugin) {
        this.plugin = plugin;
        for (DISK_CIRCUIT dc : DISK_CIRCUIT.values()) {
            if (!onlythese.contains(dc.getMaterial())) {
                onlythese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsoleInteract(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if (!p.hasPermission("tardis.advanced")) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
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
                    // determine key item
                    HashMap<String, Object> wherek = new HashMap<String, Object>();
                    wherek.put("uuid", p.getUniqueId().toString());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                    String key;
                    if (rsp.resultSet()) {
                        key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
                    } else {
                        key = plugin.getConfig().getString("preferences.key");
                    }
                    onlythese.add(Material.valueOf(key));
                    ItemStack disk_tmp = event.getPlayer().getInventory().getItemInMainHand();
                    ItemStack disk = (disk_tmp != null) ? disk_tmp : event.getPlayer().getInventory().getItemInOffHand();
                    if ((disk != null && onlythese.contains(disk.getType()) && disk.hasItemMeta()) || key.equals("AIR")) {
                        // only the time lord of this tardis
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        wheret.put("uuid", p.getUniqueId().toString());
                        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                        if (!rs.resultSet()) {
                            TARDISMessage.send(p, "NOT_OWNER");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                            TARDISMessage.send(p, "POWER_DOWN");
                            return;
                        }
                        Inventory inv = plugin.getServer().createInventory(p, 9, "§4TARDIS Console");
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", p.getUniqueId().toString());
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
                            setstore.put("uuid", p.getUniqueId().toString());
                            setstore.put("tardis_id", id);
                            new QueryFactory(plugin).doInsert("storage", setstore);
                        }
                        // open gui
                        p.openInventory(inv);
                    } else {
                        TARDISMessage.send(p, "ADV_OPEN");
                    }
                }
            }
        }
    }
}
