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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConsoleSwitchListener implements Listener {

    private final TARDIS plugin;
    private final List<Byte> gui_circuits = Arrays.asList((byte) 1966, (byte) 1973, (byte) 1974, (byte) 1975, (byte) 1976, (byte) 1977);

    public TARDISConsoleSwitchListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("Deprecation")
    @EventHandler
    public void onConsoleInventoryClick(final InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Inventory inv = event.getInventory();
        if (inv.getTitle().equals("§4TARDIS Console")) {
            final Player p = (Player) event.getWhoClicked();
            // check they're in the TARDIS
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("uuid", p.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                event.setCancelled(true);
                TARDISMessage.send(p, plugin.getPluginName() + MESSAGE.NOT_IN_TARDIS.getText());
            }
            if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                event.setCancelled(true);
                final ItemStack item = inv.getItem(event.getRawSlot());
                if (item != null && item.getType().equals(Material.MAP)) {
                    final byte map = item.getData().getData();
                    if (gui_circuits.contains(map)) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", p.getUniqueId().toString());
                        final ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (rs.resultSet()) {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    ItemStack[] stack = null;
                                    Inventory new_inv = null;
                                    switch (map) {
                                        case (byte) 1966: // Chameleon circuit
                                            new_inv = plugin.getServer().createInventory(p, 54, "§4Chameleon Circuit");
                                            stack = new TARDISChameleonInventory(rs.isChamele_on(), rs.isAdapti_on()).getTerminal();
                                            break;
                                        case (byte) 1973: // ARS circuit
                                            new_inv = plugin.getServer().createInventory(p, 54, "§4Architectural Reconfiguration");
                                            stack = new TARDISARSInventory().getTerminal();
                                            break;
                                        case (byte) 1974: // Temporal circuit
                                            new_inv = plugin.getServer().createInventory(p, 27, "§4Temporal Locator");
                                            stack = new TARDISTemporalLocatorInventory().getTerminal();
                                            break;
                                        case (byte) 1975: // Memory circuit (saves/areas)
                                            new_inv = plugin.getServer().createInventory(p, 54, "§4TARDIS saves");
                                            stack = new TARDISSaveSignInventory(plugin, rs.getTardis_id()).getTerminal();
                                            break;
                                        case (byte) 1976: // Input circuit (terminal)
                                            new_inv = plugin.getServer().createInventory(p, 54, "§4Destination Terminal");
                                            stack = new TARDISTerminalInventory().getTerminal();
                                            break;
                                        default: // scanner circuit
                                            plugin.getGeneralKeeper().getScannerListener().scan(p, rs.getTardis_id(), plugin.getServer().getScheduler());
                                            break;
                                    }
                                    // close inventory
                                    p.closeInventory();
                                    if (new_inv != null && stack != null) {
                                        // open new inventory
                                        new_inv.setContents(stack);
                                        p.openInventory(new_inv);
                                    }
                                }
                            }, 1L);
                        } else {
                            TARDISMessage.send(p, plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                        }
                    }
                }
            }
        }
    }
}
