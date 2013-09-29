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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISAreasInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
public class TARDISChameleonListener implements Listener {

    private final TARDIS plugin;

    public TARDISChameleonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChameleonTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Chameleon Circuit")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 27) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    String playerNameStr = player.getName();
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<String, Object>();
                    wheres.put("player", playerNameStr);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("tardis_id", id);
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        QueryFactory qf = new QueryFactory(plugin);
                        switch (slot) {
                            case 0:
                                // toggle chameleon circuit
                                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                                if (rs.resultSet()) {
                                    String onoff;
                                    int oo;
                                    if (rs.isChamele_on()) {
                                        onoff = ChatColor.RED + "OFF";
                                        oo = 0;
                                    } else {
                                        onoff = ChatColor.GREEN + "ON";
                                        oo = 1;
                                    }
                                    ItemMeta im = is.getItemMeta();
                                    im.setLore(Arrays.asList(new String[]{onoff}));
                                    is.setItemMeta(im);
                                    // get sign block so we can update it
                                    Block cc = plugin.utils.getLocationFromDB(rs.getChameleon(), 0, 0).getBlock();
                                    if (cc.getType() == Material.WALL_SIGN || cc.getType() == Material.SIGN_POST) {
                                        Sign sign = (Sign) cc.getState();
                                        sign.setLine(3, onoff);
                                        sign.update();
                                    } else {
                                        player.sendMessage(plugin.pluginName + "Chameleon Circuit " + onoff);
                                    }
                                    HashMap<String, Object> wherec = new HashMap<String, Object>();
                                    wherec.put("tardis_id", id);
                                    set.put("chamele_on", oo);
                                    qf.doUpdate("tardis", set, wherec);
                                }
                                break;
                            case 3:
                                // new Police Box
                                break;
                            case 5:
                                // disengaged
                                break;
                            case 10:
                                // stone column
                                break;
                            case 12:
                                // desert temple
                                break;
                            case 14:
                                // jungle temple
                                break;
                            case 16:
                                // nether fortress
                                break;
                            case 18:
                                // old police box
                                break;
                            case 20:
                                // swamp hut
                                break;
                            case 22:
                                // party tent
                                break;
                            case 24:
                                // village house
                                break;
                            case 26:
                                // yellow submarine
                                break;
                            default:
                                close(player);
                        }
                    }
                }
            }
            if (slot == 49) {
                // load TARDIS areas
                close(player);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        TARDISAreasInventory sst = new TARDISAreasInventory(plugin, player);
                        ItemStack[] items = sst.getTerminal();
                        Inventory areainv = plugin.getServer().createInventory(player, 54, "§4TARDIS areas");
                        areainv.setContents(items);
                        player.openInventory(areainv);
                    }
                }, 2L);
            }
        }
    }

    /**
     * Converts an Item Stacks lore to a destination string.
     *
     * @param lore the lore to read
     * @return the destination string
     */
    private String getDestination(List<String> lore) {
        return lore.get(0) + ":" + lore.get(1) + ":" + lore.get(2) + ":" + lore.get(3);
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }
}
