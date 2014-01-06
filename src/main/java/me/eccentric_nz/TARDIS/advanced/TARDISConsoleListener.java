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
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
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
                // loop through inventory contents and remove any items that are not disks or circuits
                for (int i = 0; i < 9; i++) {
                    ItemStack is = inv.getItem(i);
                    if (is != null) {
                        if (!onlythese.contains(is.getType())) {
                            p.getLocation().getWorld().dropItemNaturally(p.getLocation(), is);
                            inv.setItem(i, new ItemStack(Material.AIR));
                        } else {
                            boolean ignore = false;
                            HashMap<String, Object> set_next = new HashMap<String, Object>();
                            HashMap<String, Object> set_tardis = new HashMap<String, Object>();
                            HashMap<String, Object> where_next = new HashMap<String, Object>();
                            HashMap<String, Object> where_tardis = new HashMap<String, Object>();

                            // process any disks
                            List<String> lore = is.getItemMeta().getLore();
                            String first = lore.get(0);
                            TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                            switch (is.getType()) {
                                case RECORD_3: // area
                                    // check the current location is not in this area already
                                    if (!plugin.ta.areaCheckInExile(first, current)) {
                                        return;
                                    }
                                    // get a parking spot in this area
                                    HashMap<String, Object> wherea = new HashMap<String, Object>();
                                    wherea.put("area_name", first);
                                    ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
                                    if (!rsa.resultSet()) {
                                        p.sendMessage(plugin.pluginName + "Could not find an area with that name! try using " + ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET + " first.");
                                        return;
                                    }
                                    if ((!p.hasPermission("tardis.area." + first) && !p.hasPermission("tardis.area.*")) || (!p.isPermissionSet("tardis.area." + first) && !p.isPermissionSet("tardis.area.*"))) {
                                        p.sendMessage(plugin.pluginName + "You do not have permission [tardis.area." + first + "] to send the TARDIS to this location!");
                                        return;
                                    }
                                    Location l = plugin.ta.getNextSpot(rsa.getArea_name());
                                    if (l == null) {
                                        p.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                                        return;
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
                                        return;
                                    }
                                    if (current.getBlock().getBiome().toString().equals(first)) {
                                        return;
                                    }
                                    try {
                                        Biome biome = Biome.valueOf(first);
                                        p.sendMessage(plugin.pluginName + "Searching for biome, this may take some time!");
                                        Location nsob = plugin.tardisTravelCommand.searchBiome(p, id, biome, rsc.getWorld(), rsc.getX(), rsc.getZ());
                                        if (nsob == null) {
                                            p.sendMessage(plugin.pluginName + "Could not find biome!");
                                            return;
                                        } else {
                                            TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                                            if (!respect.getRespect(p, nsob, true)) {
                                                return;
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
                                        return;
                                    }
                                    break;
                                case RECORD_12: // player
                                    // get the player's location
                                    if (p.hasPermission("tardis.timetravel.player")) {
                                        if (p.getName().equalsIgnoreCase(first)) {
                                            p.sendMessage(plugin.pluginName + "You cannot travel to yourself!");
                                            return;
                                        }
                                        // check the to player's DND status
                                        HashMap<String, Object> wherednd = new HashMap<String, Object>();
                                        wherednd.put("player", first.toLowerCase());
                                        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, wherednd);
                                        if (rspp.resultSet() && rspp.isDND()) {
                                            p.sendMessage(plugin.pluginName + first + " does not want to be disturbed right now! Try again later.");
                                            return;
                                        }
                                        TARDISRescue to_player = new TARDISRescue(plugin);
                                        to_player.rescue(p, first, id, tt, rsc.getDirection(), false);
                                    } else {
                                        p.sendMessage(plugin.pluginName + "You do not have permission to time travel to a player!");
                                        return;
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
                                        return;
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
                // remember what was placed in the console
                saveCurrentConsole(inv, p.getName());
            } else {
                p.sendMessage(plugin.pluginName + MESSAGE.NOT_IN_TARDIS.getText());
            }
        }
    }

    @EventHandler
    public void onConsoleInteract(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if (!p.hasPermission("tardis.advanced")) {
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
                            wheret.put("owner", p.getName());
                            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                            if (!rs.resultSet()) {
                                p.sendMessage(plugin.pluginName + MESSAGE.NOT_OWNER);
                                return;
                            }
                            Inventory inv = plugin.getServer().createInventory(p, 9, "§4TARDIS Console");
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("owner", p.getName());
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
                                setstore.put("owner", p.getName());
                                setstore.put("tardis_id", id);
                                new QueryFactory(plugin).doInsert("storage", setstore);
                            }
                            // open gui
                            p.openInventory(inv);
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
