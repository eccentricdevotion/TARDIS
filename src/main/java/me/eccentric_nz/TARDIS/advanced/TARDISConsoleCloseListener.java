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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConsoleCloseListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> onlythese = new ArrayList<Material>();

    public TARDISConsoleCloseListener(TARDIS plugin) {
        this.plugin = plugin;
        for (DISK_CIRCUIT dc : DISK_CIRCUIT.values()) {
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
            // get the TARDIS the player is in
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("player", p.getName());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                // get TARDIS's current location
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    p.sendMessage(plugin.pluginName + "Could not get the current TARDIS location!");
                    return;
                }
                Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                // loop through inventory contents and remove any items that are not disks or circuits
                for (int i = 0; i < 9; i++) {
                    ItemStack is = inv.getItem(i);
                    if (is != null) {
                        Material mat = is.getType();
                        if (!onlythese.contains(mat)) {
                            p.getLocation().getWorld().dropItemNaturally(p.getLocation(), is);
                            inv.setItem(i, new ItemStack(Material.AIR));
                        }
                    }
                }
                // remember what was placed in the console
                saveCurrentConsole(inv, p.getName());
                if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                    // check circuits
                    TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                    // if no materialisation circuit exit
                    if (!tcc.hasMaterialisation()) {
                        p.sendMessage(plugin.pluginName + "The Materialisation Circuit is missing from the console! No destination will be set.");
                        return;
                    }
                }
                // loop through remaining inventory items and process the disks
                for (int i = 0; i < 9; i++) {
                    ItemStack is = inv.getItem(i);
                    if (is != null) {
                        Material mat = is.getType();
                        if (!mat.equals(Material.MAP)) {
                            boolean ignore = false;
                            HashMap<String, Object> set_next = new HashMap<String, Object>();
                            HashMap<String, Object> set_tardis = new HashMap<String, Object>();
                            HashMap<String, Object> where_next = new HashMap<String, Object>();
                            HashMap<String, Object> where_tardis = new HashMap<String, Object>();

                            // process any disks
                            List<String> lore = is.getItemMeta().getLore();
                            String first = lore.get(0);
                            switch (mat) {
                                case RECORD_3: // area
                                    // check the current location is not in this area already
                                    if (!plugin.ta.areaCheckInExile(first, current)) {
                                        continue;
                                    }
                                    // get a parking spot in this area
                                    HashMap<String, Object> wherea = new HashMap<String, Object>();
                                    wherea.put("area_name", first);
                                    ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
                                    if (!rsa.resultSet()) {
                                        p.sendMessage(plugin.pluginName + "Could not find an area with that name! try using " + ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET + " first.");
                                        continue;
                                    }
                                    if ((!p.hasPermission("tardis.area." + first) && !p.hasPermission("tardis.area.*")) || (!p.isPermissionSet("tardis.area." + first) && !p.isPermissionSet("tardis.area.*"))) {
                                        p.sendMessage(plugin.pluginName + "You do not have permission [tardis.area." + first + "] to send the TARDIS to this location!");
                                        continue;
                                    }
                                    Location l = plugin.ta.getNextSpot(rsa.getArea_name());
                                    if (l == null) {
                                        p.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                                        continue;
                                    }
                                    set_next.put("world", l.getWorld().getName());
                                    set_next.put("x", l.getBlockX());
                                    set_next.put("y", l.getBlockY());
                                    set_next.put("z", l.getBlockZ());
                                    set_next.put("submarine", 0);
                                    p.sendMessage(plugin.pluginName + "Your TARDIS was approved for parking in [" + first + "]!");
                                    plugin.tardisHasDestination.put(id, plugin.getArtronConfig().getInt("travel"));
                                    break;
                                case GREEN_RECORD: // biome
                                    // find a biome location
                                    if (!p.hasPermission("tardis.timetravel.biome")) {
                                        p.sendMessage(plugin.pluginName + "You do not have permission to time travel to a biome!");
                                        continue;
                                    }
                                    if (current.getBlock().getBiome().toString().equals(first)) {
                                        continue;
                                    }
                                    try {
                                        Biome biome = Biome.valueOf(first);
                                        p.sendMessage(plugin.pluginName + "Searching for biome, this may take some time!");
                                        Location nsob = plugin.tardisTravelCommand.searchBiome(p, id, biome, rsc.getWorld(), rsc.getX(), rsc.getZ());
                                        if (nsob == null) {
                                            p.sendMessage(plugin.pluginName + "Could not find biome!");
                                            continue;
                                        } else {
                                            TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                                            if (!respect.getRespect(p, nsob, true)) {
                                                continue;
                                            }
                                            World bw = nsob.getWorld();
                                            // check location
                                            while (!bw.getChunkAt(nsob).isLoaded()) {
                                                bw.getChunkAt(nsob).load();
                                            }
                                            int[] start_loc = tt.getStartLocation(nsob, rsc.getDirection());
                                            int tmp_y = nsob.getBlockY();
                                            for (int up = 0; up < 10; up++) {
                                                int count = tt.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], nsob.getWorld(), rsc.getDirection());
                                                if (count == 0) {
                                                    nsob.setY(tmp_y + up);
                                                    break;
                                                }
                                            }
                                            set_next.put("world", nsob.getWorld().getName());
                                            set_next.put("x", nsob.getBlockX());
                                            set_next.put("y", nsob.getBlockY());
                                            set_next.put("z", nsob.getBlockZ());
                                            set_next.put("direction", rsc.getDirection().toString());
                                            set_next.put("submarine", 0);
                                            p.sendMessage(plugin.pluginName + "The biome was set succesfully. Please release the handbrake!");
                                        }
                                    } catch (IllegalArgumentException iae) {
                                        p.sendMessage(plugin.pluginName + "Biome type not valid!");
                                        continue;
                                    }
                                    break;
                                case RECORD_12: // player
                                    // get the player's location
                                    if (p.hasPermission("tardis.timetravel.player")) {
                                        if (p.getName().equalsIgnoreCase(first)) {
                                            p.sendMessage(plugin.pluginName + "You cannot travel to yourself!");
                                            continue;
                                        }
                                        // check the to player's DND status
                                        HashMap<String, Object> wherednd = new HashMap<String, Object>();
                                        wherednd.put("player", first.toLowerCase());
                                        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, wherednd);
                                        if (rspp.resultSet() && rspp.isDND()) {
                                            p.sendMessage(plugin.pluginName + first + " does not want to be disturbed right now! Try again later.");
                                            continue;
                                        }
                                        TARDISRescue to_player = new TARDISRescue(plugin);
                                        to_player.rescue(p, first, id, tt, rsc.getDirection(), false);
                                    } else {
                                        p.sendMessage(plugin.pluginName + "You do not have permission to time travel to a player!");
                                        continue;
                                    }
                                    break;
                                case RECORD_6: // preset
                                    if (!ignore) {
                                        // apply the preset
                                        set_tardis.put("chameleon_preset", first);
                                    }
                                    break;
                                case RECORD_4: // save
                                    ignore = true;
                                    String world = lore.get(1);
                                    int x = plugin.utils.parseNum(lore.get(2));
                                    int y = plugin.utils.parseNum(lore.get(3));
                                    int z = plugin.utils.parseNum(lore.get(4));
                                    if (current.getWorld().toString().equals(world) && current.getBlockX() == x && current.getBlockZ() == z) {
                                        continue;
                                    }
                                    // read the lore from the disk
                                    set_next.put("world", world);
                                    set_next.put("x", x);
                                    set_next.put("y", y);
                                    set_next.put("z", z);
                                    set_next.put("direction", lore.get(6));
                                    boolean sub = Boolean.valueOf(lore.get(7));
                                    set_next.put("submarine", (sub) ? 1 : 0);
                                    set_tardis.put("chameleon_preset", lore.get(5));
                                    p.sendMessage(plugin.pluginName + "The specified location was set succesfully. Please release the handbrake!");
                                    break;
                                default:
                                    break;
                            }
                            QueryFactory qf = new QueryFactory(plugin);
                            if (set_next.size() > 0) {
                                // update next
                                where_next.put("tardis_id", id);
                                qf.doUpdate("next", set_next, where_next);
                            }
                            if (set_tardis.size() > 0) {
                                // update tardis
                                where_tardis.put("tardis_id", id);
                                qf.doUpdate("tardis", set_tardis, where_tardis);
                            }
                            if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                plugin.trackRescue.remove(Integer.valueOf(id));
                            }
                        }
                    }
                }
            } else {
                p.sendMessage(plugin.pluginName + MESSAGE.NOT_IN_TARDIS.getText());
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
