/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISSavesPlanetListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISSavesPlanetListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onSavesPlanetClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().startsWith(ChatColor.DARK_RED + "TARDIS Dimension Map")) {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            // get the TARDIS the player is in
            int id = -1;
            if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(uuid)) {
                // junk mode
                id = plugin.getTrackerKeeper().getJunkPlayers().get(uuid);
            } else if (plugin.getTrackerKeeper().getSavesIds().containsKey(uuid)) {
                // player wants own saves
                id = plugin.getTrackerKeeper().getSavesIds().get(uuid);
            } else {
                // saves for the tardis the player is in
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("uuid", uuid.toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                if (rst.resultSet()) {
                    id = rst.getTardis_id();
                }
            }
            event.setCancelled(true);
            int slot = event.getRawSlot();
            event.setCancelled(true);
            if (slot == 0) {
                // home location
                ItemStack is = view.getItem(slot);
                ItemMeta im = is.getItemMeta();
                List<String> lore = im.getLore();
                World w = TARDISAliasResolver.getWorldFromAlias(lore.get(0));
                if (w == null) {
                    close(player);
                    return;
                }
                int x = TARDISNumberParsers.parseInt(lore.get(1));
                int y = TARDISNumberParsers.parseInt(lore.get(2));
                int z = TARDISNumberParsers.parseInt(lore.get(3));
                Location save_dest = new Location(w, x, y, z);
                // get tardis artron level
                ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                if (!rs.fromID(id)) {
                    close(player);
                    return;
                }
                int level = rs.getArtronLevel();
                int travel = plugin.getArtronConfig().getInt("travel");
                if (level < travel) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                    close(player);
                    return;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
                Location current = null;
                if (rsc.resultSet()) {
                    current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                }
                if (!save_dest.equals(current) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("world", lore.get(0));
                    set.put("x", TARDISNumberParsers.parseInt(lore.get(1)));
                    set.put("y", TARDISNumberParsers.parseInt(lore.get(2)));
                    set.put("z", TARDISNumberParsers.parseInt(lore.get(3)));
                    int l_size = lore.size();
                    if (l_size >= 5) {
                        if (!lore.get(4).isEmpty() && !lore.get(4).equals(ChatColor.GOLD + "Current location")) {
                            set.put("direction", lore.get(4));
                        }
                        if (l_size > 5 && !lore.get(5).isEmpty() && lore.get(5).equals("true")) {
                            set.put("submarine", 1);
                        } else {
                            set.put("submarine", 0);
                        }
                    }
                    if (l_size >= 7 && !lore.get(6).equals(ChatColor.GOLD + "Current location")) {
                        HashMap<String, Object> sett = new HashMap<>();
                        sett.put("chameleon_preset", lore.get(6));
                        // set chameleon adaption to OFF
                        sett.put("adapti_on", 0);
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        plugin.getQueryFactory().doSyncUpdate("tardis", sett, wheret);
                    }
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set, wheret);
                    TravelType travelType = (is.getItemMeta().getDisplayName().equals("Home")) ? TravelType.HOME : TravelType.SAVE;
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, travelType));
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    close(player);
                    plugin.getMessenger().sendJoined(player, "DEST_SET_TERMINAL", im.getDisplayName(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TARDISLand(plugin, id, player).exitVortex();
                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, id));
                    }
                } else if (!lore.contains(ChatColor.GOLD + "Current location")) {
                    lore.add(ChatColor.GOLD + "Current location");
                    im.setLore(lore);
                    is.setItemMeta(im);
                }
            }
            if (slot >= 2 && slot < 45) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    ItemMeta im = is.getItemMeta();
                    String alias = im.getDisplayName();
                    String world = TARDISAliasResolver.getWorldNameFromAlias(alias);
                    TARDISSavesInventory sst = new TARDISSavesInventory(plugin, id, world);
                    Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
                    ItemStack[] items = sst.getSaves();
                    inv.setContents(items);
                    player.openInventory(inv);
                }
            }
        }
    }
}
