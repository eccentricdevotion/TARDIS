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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.database.ResultSetTardisSign;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A TARDIS with a functioning chameleon circuit can appear as almost anything
 * desired. The owner can program the circuit to make it assume a specific
 * shape.
 *
 * @author eccentric_nz
 */
public class TARDISSignListener implements Listener {

    private final TARDIS plugin;
    List<Material> validSigns = new ArrayList<Material>();

    public TARDISSignListener(TARDIS plugin) {
        this.plugin = plugin;
        validSigns.add(Material.REDSTONE_COMPARATOR_OFF);
        validSigns.add(Material.REDSTONE_COMPARATOR_ON);
        validSigns.add(Material.WALL_SIGN);
        validSigns.add(Material.SIGN_POST);
    }

    /**
     * Listens for player interaction with the TARDIS chameleon or save-sign
     * Signs. If the signs are clicked, they trigger the appropriate actions,
     * for example turning the Chameleon Circuit on and off.
     *
     * @param event the player clicking a sign
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSignInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            // only proceed if they are right-clicking a valid sign block!
            if (action == Action.RIGHT_CLICK_BLOCK && validSigns.contains(blockType)) {
                // check they are in the TARDIS
                // get the TARDIS the player is in
                HashMap<String, Object> wheres = new HashMap<String, Object>();
                wheres.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                if (!rst.resultSet()) {
                    return;
                }
                // get clicked block location
                Location b = block.getLocation();
                String bw = b.getWorld().getName();
                int bx = b.getBlockX();
                int by = b.getBlockY();
                int bz = b.getBlockZ();
                String signloc = bw + ":" + bx + ":" + by + ":" + bz;
                // get tardis from saved sign location
                ResultSetTardisSign rs = new ResultSetTardisSign(plugin, signloc);
                if (rs.resultSet()) {
                    event.setCancelled(true);
                    if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                        TARDISMessage.send(player, "POWER_DOWN");
                        return;
                    }
                    if ((rs.isIso_on() && !player.getUniqueId().equals(rs.getUuid()) && event.isCancelled() && !player.hasPermission("tardis.skeletonkey")) || plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId())) {
                        TARDISMessage.send(player, "ISO_HANDS_OFF");
                        return;
                    }
                    String line1;
                    if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                        Sign s = (Sign) block.getState();
                        line1 = s.getLine(0);
                    } else {
                        line1 = (signloc.equals(rs.getChameleon())) ? "Chameleon" : "Save Sign";
                    }
                    TARDISCircuitChecker tcc = null;
                    if (plugin.getConfig().getString("preferences.difficulty").equals("hard") && !plugin.getUtils().inGracePeriod(player)) {
                        tcc = new TARDISCircuitChecker(plugin, rs.getTardis_id());
                        tcc.getCircuits();
                    }
                    if (line1.equals("Chameleon")) {
                        if (tcc != null && !tcc.hasChameleon()) {
                            TARDISMessage.send(player, "CHAM_MISSING");
                            return;
                        }
                        // open Chameleon Circuit GUI
                        ItemStack[] cc = new TARDISChameleonInventory(plugin, rs.isChamele_on(), rs.isAdapti_on()).getTerminal();
                        Inventory cc_gui = plugin.getServer().createInventory(player, 54, "§4Chameleon Circuit");
                        cc_gui.setContents(cc);
                        player.openInventory(cc_gui);
                    } else {
                        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            if (tcc != null && !tcc.hasMemory()) {
                                TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                                return;
                            }
                            TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, rs.getTardis_id());
                            ItemStack[] items = sst.getTerminal();
                            Inventory inv = plugin.getServer().createInventory(player, 54, "§4TARDIS saves");
                            inv.setContents(items);
                            player.openInventory(inv);
                        }
                    }
                }
            }
        }
    }
}
