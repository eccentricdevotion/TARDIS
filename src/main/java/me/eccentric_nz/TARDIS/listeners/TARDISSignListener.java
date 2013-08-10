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
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import org.bukkit.ChatColor;
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

    private TARDIS plugin;

    public TARDISSignListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with the TARDIS chameleon or save-sign
     * Signs. If the signs are clicked, they trigger the appropriate actions,
     * for example turning the Chameleon Circuit on and off.
     *
     * @param event the player clicking a sign
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                // only proceed if they are clicking a sign!
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // get clicked block location
                    Location b = block.getLocation();
                    Sign s = (Sign) block.getState();
                    String line1 = s.getLine(0);
                    String bw = b.getWorld().getName();
                    int bx = b.getBlockX();
                    int by = b.getBlockY();
                    int bz = b.getBlockZ();
                    String signloc = bw + ":" + bx + ":" + by + ":" + bz;
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    if (line1.equals("Chameleon")) {
                        where.put("chameleon", signloc);
                    } else {
                        where.put("save_sign", signloc);
                    }
                    // get tardis from saved sign location
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        event.setCancelled(true);
                        if (rs.isIso_on() && !player.getName().equals(rs.getOwner()) && event.isCancelled()) {
                            player.sendMessage(plugin.pluginName + "The isomorphic security lockout has been engaged... Hands off the controls!");
                            return;
                        }
                        QueryFactory qf = new QueryFactory(plugin);
                        int id = rs.getTardis_id();
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        if (line1.equals("Chameleon")) {
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            if (rs.isChamele_on()) {
                                set.put("chamele_on", 0);
                                s.setLine(3, ChatColor.RED + "OFF");
                                s.update();
                            } else {
                                set.put("chamele_on", 1);
                                s.setLine(3, ChatColor.GREEN + "ON");
                                s.update();
                            }
                            qf.doUpdate("tardis", set, tid);
                        } else {
                            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && plugin.bukkitversion.compareTo(plugin.preIMversion) >= 0) {
                                TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, id, rs.getHome());
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

    /**
     * Reorders a list so the the first item is moved to the end.
     *
     * @param list
     * @param current
     */
    public void reOrder(List<String> list, String current) {
        int i = list.size();
        while (i-- > 0 && !list.get(0).equals(current)) {
            list.add(list.remove(0));
        }
    }
}
