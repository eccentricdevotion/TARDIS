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

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSaveTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS saves")) {
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
                    int slot = event.getRawSlot();
                    ItemStack is = inv.getItem(slot);
                    ItemMeta im = is.getItemMeta();
                    List<String> lore = im.getLore();
                    String save = getDestination(lore);
                    if (!save.equals(rs.getCurrent())) {
                        HashMap<String, Object> set = new HashMap<String, Object>();
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
                    } else {
                        lore.add("§6Current location");
                        im.setLore(lore);
                        is.setItemMeta(im);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    private String getDestination(List<String> lore) {
        return lore.get(0) + ":" + lore.get(1) + ":" + lore.get(2) + ":" + lore.get(3);
    }

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
