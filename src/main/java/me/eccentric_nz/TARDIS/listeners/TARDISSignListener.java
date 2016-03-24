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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.database.ResultSetJunk;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTardisSign;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
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
                UUID uuid = player.getUniqueId();
                // check they are in the TARDIS
                HashMap<String, Object> wheres = new HashMap<String, Object>();
                wheres.put("uuid", uuid.toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                boolean inside = rst.resultSet();
                boolean found = false;
                int which = 1;
                int id = -1;
                // get clicked block location
                Location b = block.getLocation();
                String signloc = b.toString();
                if (inside) {
                    String bw = b.getWorld().getName();
                    int bx = b.getBlockX();
                    int by = b.getBlockY();
                    int bz = b.getBlockZ();
                    signloc = bw + ":" + bx + ":" + by + ":" + bz;
                    // get tardis from saved sign location
                    ResultSetTardisSign rsts = new ResultSetTardisSign(plugin, signloc);
                    if (rsts.resultSet()) {
                        found = true;
                        id = rsts.getTardis_id();
                        which = rsts.getWhich();
                    }
                } else {
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("uuid", uuid.toString());
                    where.put("save_sign", signloc);
                    ResultSetJunk rsj = new ResultSetJunk(plugin, where);
                    if (rsj.resultSet()) {
                        found = true;
                        id = rsj.getTardis_id();
                        // track player for save sign GUI
                        plugin.getTrackerKeeper().getJunkPlayers().put(uuid, id);
                    }
                }
                if (found) {
                    event.setCancelled(true);
                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                    wheret.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                    rs.resultSet();
                    if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                        TARDISMessage.send(player, "POWER_DOWN");
                        return;
                    }
                    if ((rs.isIso_on() && !uuid.equals(rs.getUuid()) && event.isCancelled() && !player.hasPermission("tardis.skeletonkey")) || plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                        TARDISMessage.send(player, "ISO_HANDS_OFF");
                        return;
                    }
                    String line1;
                    if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                        Sign s = (Sign) block.getState();
                        line1 = s.getLine(0);
                    } else {
                        line1 = (signloc.equals(rs.getChameleon())) ? plugin.getSigns().getStringList("chameleon").get(0) : "TARDIS";
                    }
                    TARDISCircuitChecker tcc = null;
                    if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                        tcc = new TARDISCircuitChecker(plugin, rs.getTardis_id());
                        tcc.getCircuits();
                    }
                    if (which == 0 && line1.contains(plugin.getSigns().getStringList("chameleon").get(0))) {
                        if (tcc != null && !tcc.hasChameleon()) {
                            TARDISMessage.send(player, "CHAM_MISSING");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardis_id())) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        // open Chameleon Circuit GUI
                        ItemStack[] cc = new TARDISChameleonInventory(plugin, rs.isChamele_on(), rs.isAdapti_on()).getTerminal();
                        Inventory cc_gui = plugin.getServer().createInventory(player, 54, "§4Chameleon Circuit");
                        cc_gui.setContents(cc);
                        player.openInventory(cc_gui);
                    } else if (which == 1 && line1.contains("TARDIS")) {
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
