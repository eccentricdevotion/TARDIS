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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISAreaSignListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISAreaSignListener(TARDIS plugin) {
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
    public void onAreaTerminalClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(ChatColor.DARK_RED + "TARDIS areas")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 45) {
                // get the TARDIS the player is in
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                if (rst.resultSet()) {
                    ItemStack is = view.getItem(slot);
                    if (is != null) {
                        ItemMeta im = is.getItemMeta();
                        String area = im.getDisplayName();
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("area_name", area);
                        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                        rsa.resultSet();
                        Location l;
                        if (rsa.getArea().isGrid()) {
                            l = plugin.getTardisArea().getNextSpot(area);
                        } else {
                            l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().getAreaId());
                        }
                        if (l == null) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MORE_SPOTS");
                            close(player);
                            return;
                        }
                        // check the player is not already in the area!
                        if (plugin.getTardisArea().isInExistingArea(rst.getTardis_id(), rsa.getArea().getAreaId())) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_AREA");
                            close(player);
                            return;
                        }
                        player.performCommand("tardistravel area " + area);
                        close(player);
                        return;
                    }
                }
            }
            if (slot == 49) {
                // load TARDIS saves
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rs = new ResultSetTravellers(plugin, wheres, false);
                    if (rs.resultSet()) {
                        TARDISSavesPlanetInventory sst = new TARDISSavesPlanetInventory(plugin, rs.getTardis_id());
                        ItemStack[] items = sst.getPlanets();
                        Inventory saveinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Dimension Map");
                        saveinv.setContents(items);
                        player.openInventory(saveinv);
                    }
                }, 2L);
            }
        }
    }
}
