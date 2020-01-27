/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A TARDIS with a functioning chameleon circuit can appear as almost anything desired. The owner can program the
 * circuit to make it assume a specific shape.
 *
 * @author eccentric_nz
 */
public class TARDISSignListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validSigns = new ArrayList<>();

    public TARDISSignListener(TARDIS plugin) {
        this.plugin = plugin;
        validSigns.add(Material.COMPARATOR);
        validSigns.addAll(Tag.SIGNS.getValues());
    }

    /**
     * Listens for player interaction with the TARDIS chameleon or save-sign Signs. If the signs are clicked, they
     * trigger the appropriate actions, for example turning the Chameleon Circuit on and off.
     *
     * @param event the player clicking a sign
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            // only proceed if they are right-clicking a valid sign block!
            if (action == Action.RIGHT_CLICK_BLOCK && validSigns.contains(blockType)) {
                UUID uuid = player.getUniqueId();
                // check they are in the TARDIS
                HashMap<String, Object> wheres = new HashMap<>();
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
                    HashMap<String, Object> where = new HashMap<>();
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
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
                    rs.resultSet();
                    Tardis tardis = rs.getTardis();
                    int tid = tardis.getTardis_id();
                    if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                        TARDISMessage.send(player, "POWER_DOWN");
                        return;
                    }
                    if ((tardis.isIso_on() && !uuid.equals(tardis.getUuid()) && event.isCancelled() && !player.hasPermission("tardis.skeletonkey")) || plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                        TARDISMessage.send(player, "ISO_HANDS_OFF");
                        return;
                    }
                    String line1;
                    if (Tag.SIGNS.isTagged(blockType)) {
                        Sign s = (Sign) block.getState();
                        line1 = s.getLine(0);
                    } else {
                        line1 = (signloc.equals(tardis.getChameleon())) ? plugin.getSigns().getStringList("chameleon").get(0) : "TARDIS";
                    }
                    TARDISCircuitChecker tcc = null;
                    if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                        tcc = new TARDISCircuitChecker(plugin, tid);
                        tcc.getCircuits();
                    }
                    if (which == 0 && line1.contains(plugin.getSigns().getStringList("chameleon").get(0))) {
                        if (tcc != null && !tcc.hasChameleon()) {
                            TARDISMessage.send(player, "CHAM_MISSING");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(tid)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getDispersedTARDII().contains(tid)) {
                            TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            TARDISMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
                            return;
                        }
                        // open Chameleon Circuit GUI
                        ItemStack[] cc = new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
                        Inventory cc_gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                        cc_gui.setContents(cc);
                        player.openInventory(cc_gui);
                    } else if (which == 1 && line1.contains("TARDIS")) {
                        if (tcc != null && !tcc.hasMemory()) {
                            TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(uuid) && plugin.getDifficulty().equals(DIFFICULTY.HARD)) {
                            ItemStack disk = player.getInventory().getItemInMainHand();
                            if (disk.hasItemMeta() && disk.getItemMeta().hasDisplayName() && disk.getItemMeta().getDisplayName().equals("Save Storage Disk")) {
                                List<String> lore = disk.getItemMeta().getLore();
                                if (!lore.get(0).equals("Blank")) {
                                    // read the lore from the disk
                                    String world = lore.get(1);
                                    int x = TARDISNumberParsers.parseInt(lore.get(2));
                                    int y = TARDISNumberParsers.parseInt(lore.get(3));
                                    int z = TARDISNumberParsers.parseInt(lore.get(4));
                                    HashMap<String, Object> set_next = new HashMap<>();
                                    set_next.put("world", world);
                                    set_next.put("x", x);
                                    set_next.put("y", y);
                                    set_next.put("z", z);
                                    set_next.put("direction", lore.get(6));
                                    boolean sub = Boolean.valueOf(lore.get(7));
                                    set_next.put("submarine", (sub) ? 1 : 0);
                                    TARDISMessage.send(player, "LOC_SET", true);
                                    // update next
                                    HashMap<String, Object> where_next = new HashMap<>();
                                    where_next.put("tardis_id", id);
                                    plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                                    plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
                                }
                            } else {
                                TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, tid);
                                ItemStack[] items = sst.getTerminal();
                                Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
                                inv.setContents(items);
                                player.openInventory(inv);
                            }
                        } else {
                            TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, tid);
                            ItemStack[] items = sst.getTerminal();
                            Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
                            inv.setContents(items);
                            player.openInventory(inv);
                        }
                    }
                }
            }
        }
    }
}
