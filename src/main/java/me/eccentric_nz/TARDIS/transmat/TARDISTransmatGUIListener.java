/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.transmat;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.desktop.PreviewData;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISTransmatGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, String> selectedLocation = new HashMap<>();

    public TARDISTransmatGUIListener(TARDIS plugin) {
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
    public void onTransmatMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISTransmatInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot > 53) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        switch (slot) {
            case 17:
                if (!selectedLocation.containsKey(player.getUniqueId())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_SELECT");
                } else {
                    // transmat to selected location
                    ResultSetTransmat rsm;
                    boolean isRoomsWorld;
                    if (selectedLocation.get(player.getUniqueId()).equals("Rooms World")) {
                        rsm = new ResultSetTransmat(plugin, -1, "rooms");
                        isRoomsWorld = true;
                    } else {
                        rsm = new ResultSetTransmat(plugin, id, selectedLocation.get(player.getUniqueId()));
                        isRoomsWorld = false;
                    }
                    if (rsm.resultSet()) {
                        if (isRoomsWorld) {
                            plugin.getTrackerKeeper().getPreviewers().put(player.getUniqueId(), new PreviewData(player.getLocation().clone(), player.getGameMode(), id));
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
                        Location tp_loc = rsm.getLocation();
                        tp_loc.setYaw(rsm.getYaw());
                        tp_loc.setPitch(player.getLocation().getPitch());
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            player.teleport(tp_loc);
                            if (isRoomsWorld) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "PREVIEW_DONE");
                            }
                        }, 10L);
                        close(player);
                    }
                }
                break;
            case 35:
                if (!selectedLocation.containsKey(player.getUniqueId())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_SELECT");
                } else {
                    // delete
                    ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, selectedLocation.get(player.getUniqueId()));
                    if (rsm.resultSet()) {
                        HashMap<String, Object> wherer = new HashMap<>();
                        wherer.put("transmat_id", rsm.getTransmat_id());
                        plugin.getQueryFactory().doDelete("transmats", wherer);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_REMOVED");
                        close(player);
                    }
                }
                break;
            case 53:
                selectedLocation.remove(player.getUniqueId());
                //close
                close(player);
                break;
            default:
                // select
                ItemMeta im = is.getItemMeta();
                selectedLocation.put(player.getUniqueId(), ComponentUtils.stripColour(im.displayName()));
                break;
        }
    }
}
