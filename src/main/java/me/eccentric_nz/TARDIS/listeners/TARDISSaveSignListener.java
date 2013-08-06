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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSaveSignListener implements Listener {

    private final TARDIS plugin;

    public TARDISSaveSignListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSaveTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS saves")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                final Player player = (Player) event.getWhoClicked();
                String playerNameStr = player.getName();
                // get the TARDIS the player is in
                HashMap<String, Object> wheres = new HashMap<String, Object>();
                wheres.put("player", playerNameStr);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                if (rst.resultSet()) {
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("tardis_id", rst.getTardis_id());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        int id = rs.getTardis_id();
                        String d = rs.getDirection().toString();
                        ItemStack is = inv.getItem(slot);
                        if (is != null) {
                            if (plugin.trackSubmarine.contains(Integer.valueOf(id))) {
                                plugin.trackSubmarine.remove(Integer.valueOf(id));
                            }
                            ItemMeta im = is.getItemMeta();
                            List<String> lore = im.getLore();
                            String save = getDestination(lore, d);
                            // check the player is allowed!
                            Location save_dest = getLocation(lore);
                            if (save_dest != null) {
                                TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                                if (!respect.getRespect(player, save_dest, true)) {
                                    close(player);
                                    return;
                                }
                                if (!plugin.ta.areaCheckInExisting(save_dest)) {
                                    // save is in a TARDIS area, so check that the spot is not occupied
                                    HashMap<String, Object> wherea = new HashMap<String, Object>();
                                    wherea.put("save", save);
                                    ResultSetTardis rsz = new ResultSetTardis(plugin, wherea, "", true);
                                    if (rsz.resultSet()) {
                                        ArrayList<HashMap<String, String>> data = rsz.getData();
                                        if (data.size() > 0) {
                                            player.sendMessage(plugin.pluginName + "A TARDIS already occupies this parking spot! Try using the " + ChatColor.AQUA + "/tardistravel area [name]" + ChatColor.RESET + " command instead.");
                                            close(player);
                                            return;
                                        }
                                    }
                                }
                                if (!save.equals(rs.getCurrent())) {
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    int l_size = lore.size();
                                    if (l_size >= 5) {
                                        if (!lore.get(4).isEmpty() && !lore.get(4).equals("§6Current location")) {
                                            set.put("direction", lore.get(4));
                                        }
                                        if (l_size > 5 && !lore.get(5).isEmpty() && lore.get(5).equals("true") && !lore.get(5).equals("§6Current location")) {
                                            plugin.trackSubmarine.add(id);
                                        }
                                    }
                                    set.put("save", save);
                                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                                    wheret.put("tardis_id", id);
                                    new QueryFactory(plugin).doUpdate("tardis", set, wheret);
                                    plugin.tardisHasDestination.put(id, plugin.getArtronConfig().getInt("random"));
                                    if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                        plugin.trackRescue.remove(Integer.valueOf(id));
                                    }
                                    close(player);
                                    player.sendMessage(plugin.pluginName + im.getDisplayName() + " destination set. Please release the handbrake!");
                                } else if (!lore.contains("§6Current location")) {
                                    lore.add("§6Current location");
                                    im.setLore(lore);
                                    is.setItemMeta(im);
                                }
                            } else {
                                close(player);
                                player.sendMessage(plugin.pluginName + im.getDisplayName() + " is not a valid destination!");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Converts an Item Stacks lore to a destination string in the correct
     * format for entry into the database.
     *
     * @param lore the lore to read
     * @return the destination string
     */
    private String getDestination(List<String> lore, String d) {
        int size = lore.size();
        switch (size) {
            case 5:
                return lore.get(0) + ":" + lore.get(1) + ":" + lore.get(2) + ":" + lore.get(3) + ":" + lore.get(4) + ":false";
            case 6:
                return lore.get(0) + ":" + lore.get(1) + ":" + lore.get(2) + ":" + lore.get(3) + ":" + lore.get(4) + ":" + lore.get(5);
            default:
                return lore.get(0) + ":" + lore.get(1) + ":" + lore.get(2) + ":" + lore.get(3) + ":" + d + ":false";
        }
    }

    /**
     * Converts an Item Stacks lore to a Location.
     *
     * @param lore the lore to read
     * @return a Location
     */
    private Location getLocation(List<String> lore) {
        World w = plugin.getServer().getWorld(lore.get(0));
        int x = plugin.utils.parseNum(lore.get(1));
        int y = plugin.utils.parseNum(lore.get(2));
        int z = plugin.utils.parseNum(lore.get(3));
        return new Location(w, x, y, z);
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(final Player p) {
        final String n = p.getName();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }
}
